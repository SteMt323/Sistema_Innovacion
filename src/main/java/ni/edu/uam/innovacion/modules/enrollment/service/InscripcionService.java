package ni.edu.uam.innovacion.modules.enrollment.service;

import java.util.List;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.repository.ActividadRepository;
import ni.edu.uam.innovacion.modules.enrollment.dto.ActualizarInscripcionRequest;
import ni.edu.uam.innovacion.modules.enrollment.dto.CambiarEstadoInscripcionRequest;
import ni.edu.uam.innovacion.modules.enrollment.dto.CrearInscripcionRequest;
import ni.edu.uam.innovacion.modules.enrollment.dto.InscripcionResponse;
import ni.edu.uam.innovacion.modules.enrollment.entity.Inscripcion;
import ni.edu.uam.innovacion.modules.enrollment.enums.EstadoInscripcion;
import ni.edu.uam.innovacion.modules.enrollment.mapper.InscripcionMapper;
import ni.edu.uam.innovacion.modules.enrollment.repository.InscripcionRepository;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de la lógica de negocio de inscripciones.
 *
 * Este módulo registra qué usuarios se inscriben en actividades,
 * pero no valida participación real. La participación efectiva
 * se manejará posteriormente desde el módulo de participaciones.
 */
@Service
@Transactional
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ActividadRepository actividadRepository;

    public InscripcionService(
            InscripcionRepository inscripcionRepository,
            UsuarioRepository usuarioRepository,
            ActividadRepository actividadRepository
    ) {
        this.inscripcionRepository = inscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.actividadRepository = actividadRepository;
    }

    /**
     * Crea una inscripción de un usuario en una actividad.
     *
     * Reglas aplicadas:
     * - El usuario debe existir.
     * - El usuario debe estar activo.
     * - La actividad debe existir.
     * - La actividad debe estar publicada.
     * - El usuario no puede inscribirse dos veces en la misma actividad.
     * - Si la actividad tiene cupo máximo, se valida disponibilidad.
     */
    public InscripcionResponse crear(CrearInscripcionRequest request) {
        Usuario usuario = obtenerUsuarioActivo(request.idUsuario());
        Actividad actividad = obtenerActividadDisponible(request.idActividad());

        validarInscripcionNoDuplicada(
                usuario.getIdUsuario(),
                actividad.getIdActividad()
        );

        validarCupoDisponible(actividad);

        Inscripcion inscripcion = InscripcionMapper.toEntity(
                request,
                usuario,
                actividad
        );

        return InscripcionMapper.toResponse(
                inscripcionRepository.save(inscripcion)
        );
    }

    /**
     * Lista todas las inscripciones registradas.
     */
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarTodas() {
        return inscripcionRepository.findAllByOrderByFechaInscripcionDesc()
                .stream()
                .map(InscripcionMapper::toResponse)
                .toList();
    }

    /**
     * Lista las inscripciones de un usuario específico.
     */
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarPorUsuario(Long idUsuario) {
        obtenerUsuarioPorId(idUsuario);

        return inscripcionRepository
                .findByUsuario_IdUsuarioOrderByFechaInscripcionDesc(idUsuario)
                .stream()
                .map(InscripcionMapper::toResponse)
                .toList();
    }

    /**
     * Lista las inscripciones de una actividad específica.
     */
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarPorActividad(Long idActividad) {
        obtenerActividadPorId(idActividad);

        return inscripcionRepository
                .findByActividad_IdActividadOrderByFechaInscripcionDesc(idActividad)
                .stream()
                .map(InscripcionMapper::toResponse)
                .toList();
    }

    /**
     * Lista inscripciones por estado.
     */
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarPorEstado(EstadoInscripcion estado) {
        if (estado == null) {
            throw new BadRequestException("El estado de la inscripción es obligatorio");
        }

        return inscripcionRepository
                .findByEstadoOrderByFechaInscripcionDesc(estado)
                .stream()
                .map(InscripcionMapper::toResponse)
                .toList();
    }

    /**
     * Lista inscripciones de una actividad filtradas por estado.
     */
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarPorActividadYEstado(
            Long idActividad,
            EstadoInscripcion estado
    ) {
        obtenerActividadPorId(idActividad);

        if (estado == null) {
            throw new BadRequestException("El estado de la inscripción es obligatorio");
        }

        return inscripcionRepository
                .findByActividad_IdActividadAndEstadoOrderByFechaInscripcionDesc(
                        idActividad,
                        estado
                )
                .stream()
                .map(InscripcionMapper::toResponse)
                .toList();
    }

    /**
     * Lista inscripciones de un usuario filtradas por estado.
     */
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarPorUsuarioYEstado(
            Long idUsuario,
            EstadoInscripcion estado
    ) {
        obtenerUsuarioPorId(idUsuario);

        if (estado == null) {
            throw new BadRequestException("El estado de la inscripción es obligatorio");
        }

        return inscripcionRepository
                .findByUsuario_IdUsuarioAndEstadoOrderByFechaInscripcionDesc(
                        idUsuario,
                        estado
                )
                .stream()
                .map(InscripcionMapper::toResponse)
                .toList();
    }

    /**
     * Busca una inscripción por id.
     */
    @Transactional(readOnly = true)
    public InscripcionResponse buscarPorId(Long idInscripcion) {
        return InscripcionMapper.toResponse(
                obtenerInscripcionPorId(idInscripcion)
        );
    }

    /**
     * Actualiza únicamente las observaciones de una inscripción.
     *
     * No se permite cambiar usuario ni actividad para conservar
     * la trazabilidad del registro.
     */
    public InscripcionResponse actualizar(
            Long idInscripcion,
            ActualizarInscripcionRequest request
    ) {
        Inscripcion inscripcion = obtenerInscripcionPorId(idInscripcion);

        if (inscripcion.estaCancelada() || inscripcion.estaRechazada()) {
            throw new BadRequestException(
                    "No se puede actualizar una inscripción cancelada o rechazada"
            );
        }

        InscripcionMapper.updateEntity(inscripcion, request);

        return InscripcionMapper.toResponse(
                inscripcionRepository.save(inscripcion)
        );
    }

    /**
     * Cambia el estado de una inscripción usando un request genérico.
     */
    public InscripcionResponse cambiarEstado(
            Long idInscripcion,
            CambiarEstadoInscripcionRequest request
    ) {
        if (request.estado() == null) {
            throw new BadRequestException("El estado de la inscripción es obligatorio");
        }

        Inscripcion inscripcion = obtenerInscripcionPorId(idInscripcion);

        aplicarCambioEstado(inscripcion, request.estado());

        if (request.observaciones() != null) {
            inscripcion.setObservaciones(request.observaciones());
        }

        return InscripcionMapper.toResponse(
                inscripcionRepository.save(inscripcion)
        );
    }

    /**
     * Cambia una inscripción a estado PENDIENTE.
     */
    public InscripcionResponse dejarPendiente(Long idInscripcion) {
        Inscripcion inscripcion = obtenerInscripcionPorId(idInscripcion);

        validarEstadoActual(
                inscripcion,
                EstadoInscripcion.REGISTRADA
        );

        inscripcion.dejarPendiente();

        return InscripcionMapper.toResponse(
                inscripcionRepository.save(inscripcion)
        );
    }

    /**
     * Confirma una inscripción.
     *
     * Antes de confirmar, vuelve a validar cupo disponible.
     */
    public InscripcionResponse confirmar(Long idInscripcion) {
        Inscripcion inscripcion = obtenerInscripcionPorId(idInscripcion);

        validarEstadoActual(
                inscripcion,
                EstadoInscripcion.REGISTRADA,
                EstadoInscripcion.PENDIENTE
        );

        validarCupoDisponibleParaConfirmar(inscripcion);

        inscripcion.confirmar();

        return InscripcionMapper.toResponse(
                inscripcionRepository.save(inscripcion)
        );
    }

    /**
     * Cancela una inscripción.
     */
    public InscripcionResponse cancelar(Long idInscripcion) {
        Inscripcion inscripcion = obtenerInscripcionPorId(idInscripcion);

        validarEstadoActual(
                inscripcion,
                EstadoInscripcion.REGISTRADA,
                EstadoInscripcion.PENDIENTE,
                EstadoInscripcion.CONFIRMADA
        );

        inscripcion.cancelar();

        return InscripcionMapper.toResponse(
                inscripcionRepository.save(inscripcion)
        );
    }

    /**
     * Rechaza una inscripción.
     *
     * Normalmente esta acción la usaría el administrador.
     */
    public InscripcionResponse rechazar(Long idInscripcion) {
        Inscripcion inscripcion = obtenerInscripcionPorId(idInscripcion);

        validarEstadoActual(
                inscripcion,
                EstadoInscripcion.REGISTRADA,
                EstadoInscripcion.PENDIENTE
        );

        inscripcion.rechazar();

        return InscripcionMapper.toResponse(
                inscripcionRepository.save(inscripcion)
        );
    }

    private void aplicarCambioEstado(
            Inscripcion inscripcion,
            EstadoInscripcion nuevoEstado
    ) {
        switch (nuevoEstado) {
            case REGISTRADA -> {
                validarEstadoActual(
                        inscripcion,
                        EstadoInscripcion.PENDIENTE
                );
                inscripcion.registrar();
            }
            case PENDIENTE -> {
                validarEstadoActual(
                        inscripcion,
                        EstadoInscripcion.REGISTRADA
                );
                inscripcion.dejarPendiente();
            }
            case CONFIRMADA -> {
                validarEstadoActual(
                        inscripcion,
                        EstadoInscripcion.REGISTRADA,
                        EstadoInscripcion.PENDIENTE
                );
                validarCupoDisponibleParaConfirmar(inscripcion);
                inscripcion.confirmar();
            }
            case CANCELADA -> {
                validarEstadoActual(
                        inscripcion,
                        EstadoInscripcion.REGISTRADA,
                        EstadoInscripcion.PENDIENTE,
                        EstadoInscripcion.CONFIRMADA
                );
                inscripcion.cancelar();
            }
            case RECHAZADA -> {
                validarEstadoActual(
                        inscripcion,
                        EstadoInscripcion.REGISTRADA,
                        EstadoInscripcion.PENDIENTE
                );
                inscripcion.rechazar();
            }
        }
    }

    private Inscripcion obtenerInscripcionPorId(Long idInscripcion) {
        return inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la inscripción con id " + idInscripcion
                ));
    }

    private Usuario obtenerUsuarioPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con id " + idUsuario
                ));
    }

    private Usuario obtenerUsuarioActivo(Long idUsuario) {
        Usuario usuario = obtenerUsuarioPorId(idUsuario);

        if (!EstadoUsuario.ACTIVO.equals(usuario.getEstado())) {
            throw new BadRequestException("El usuario debe estar activo para inscribirse");
        }

        return usuario;
    }

    private Actividad obtenerActividadPorId(Long idActividad) {
        return actividadRepository.findById(idActividad)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la actividad con id " + idActividad
                ));
    }

    private Actividad obtenerActividadDisponible(Long idActividad) {
        Actividad actividad = obtenerActividadPorId(idActividad);

        if (!EstadoActividad.PUBLICADA.equals(actividad.getEstado())) {
            throw new BadRequestException(
                    "Solo se permiten inscripciones en actividades publicadas"
            );
        }

        return actividad;
    }

    private void validarInscripcionNoDuplicada(
            Long idUsuario,
            Long idActividad
    ) {
        if (inscripcionRepository.existsByUsuario_IdUsuarioAndActividad_IdActividad(
                idUsuario,
                idActividad
        )) {
            throw new DuplicateResourceException(
                    "El usuario ya tiene una inscripción registrada para esta actividad"
            );
        }
    }

    private void validarCupoDisponible(Actividad actividad) {
        Integer cupoMaximo = actividad.getCupoMaximo();

        if (cupoMaximo == null) {
            return;
        }

        long totalInscripcionesActivas = contarInscripcionesQueOcupanCupo(
                actividad.getIdActividad()
        );

        if (totalInscripcionesActivas >= cupoMaximo) {
            throw new BadRequestException("La actividad ya alcanzó el cupo máximo");
        }
    }

    private void validarCupoDisponibleParaConfirmar(Inscripcion inscripcion) {
        Actividad actividad = inscripcion.getActividad();
        Integer cupoMaximo = actividad.getCupoMaximo();

        if (cupoMaximo == null) {
            return;
        }

        long totalInscripcionesActivas = contarInscripcionesQueOcupanCupo(
                actividad.getIdActividad()
        );

        boolean yaOcupaCupo = EstadoInscripcion.REGISTRADA.equals(inscripcion.getEstado())
                || EstadoInscripcion.PENDIENTE.equals(inscripcion.getEstado())
                || EstadoInscripcion.CONFIRMADA.equals(inscripcion.getEstado());

        if (!yaOcupaCupo && totalInscripcionesActivas >= cupoMaximo) {
            throw new BadRequestException("La actividad ya alcanzó el cupo máximo");
        }
    }

    private long contarInscripcionesQueOcupanCupo(Long idActividad) {
        return inscripcionRepository.countByActividad_IdActividadAndEstadoIn(
                idActividad,
                List.of(
                        EstadoInscripcion.REGISTRADA,
                        EstadoInscripcion.PENDIENTE,
                        EstadoInscripcion.CONFIRMADA
                )
        );
    }

    private void validarEstadoActual(
            Inscripcion inscripcion,
            EstadoInscripcion... estadosPermitidos
    ) {
        for (EstadoInscripcion estadoPermitido : estadosPermitidos) {
            if (estadoPermitido.equals(inscripcion.getEstado())) {
                return;
            }
        }

        throw new BadRequestException(
                "El estado actual de la inscripción no permite esta transición: "
                        + inscripcion.getEstado().getValor()
        );
    }
}