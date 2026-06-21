package ni.edu.uam.innovacion.modules.participation.service;

import java.util.List;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.RolParticipacion;
import ni.edu.uam.innovacion.modules.catalog.repository.RolParticipacionRepository;
import ni.edu.uam.innovacion.modules.enrollment.entity.Inscripcion;
import ni.edu.uam.innovacion.modules.enrollment.repository.InscripcionRepository;
import ni.edu.uam.innovacion.modules.participation.dto.ActualizarParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.dto.CrearParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.dto.ParticipacionResponse;
import ni.edu.uam.innovacion.modules.participation.dto.ProcesarParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.entity.Participacion;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;
import ni.edu.uam.innovacion.modules.participation.mapper.ParticipacionMapper;
import ni.edu.uam.innovacion.modules.participation.repository.ParticipacionRepository;
import ni.edu.uam.innovacion.modules.points.service.PuntoInnovacionService;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParticipacionService {

    private final ParticipacionRepository participacionRepository;
    private final InscripcionRepository inscripcionRepository;
    private final RolParticipacionRepository rolParticipacionRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;
    private final PuntoInnovacionService puntoService;

    public ParticipacionService(
            ParticipacionRepository participacionRepository,
            InscripcionRepository inscripcionRepository,
            RolParticipacionRepository rolParticipacionRepository,
            PerfilAdministradorRepository perfilAdministradorRepository,
            PuntoInnovacionService puntoService
    ) {
        this.participacionRepository = participacionRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.rolParticipacionRepository = rolParticipacionRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
        this.puntoService = puntoService;
    }

    public ParticipacionResponse crear(CrearParticipacionRequest request) {
        validarRequestCrear(request);

        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());

        validarInscripcionDisponibleParaParticipacion(inscripcion);
        validarParticipacionNoDuplicada(inscripcion.getIdInscripcion());

        RolParticipacion rolParticipacion = obtenerRolParticipacionActivo(request.idRolParticipacion());

        Participacion participacion = ParticipacionMapper.toEntity(
                request,
                inscripcion,
                rolParticipacion
        );

        return ParticipacionMapper.toResponse(
                participacionRepository.save(participacion)
        );
    }

    @Transactional(readOnly = true)
    public List<ParticipacionResponse> listarTodas() {
        return participacionRepository.findAllByOrderByCreadoEnDesc()
                .stream()
                .map(ParticipacionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ParticipacionResponse buscarPorId(Long idParticipacion) {
        return ParticipacionMapper.toResponse(
                obtenerParticipacion(idParticipacion)
        );
    }

    @Transactional(readOnly = true)
    public ParticipacionResponse buscarPorInscripcion(Long idInscripcion) {
        return ParticipacionMapper.toResponse(
                participacionRepository.findByInscripcion_IdInscripcion(idInscripcion)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "No existe participación registrada para la inscripción con id " + idInscripcion
                        ))
        );
    }

    @Transactional(readOnly = true)
    public List<ParticipacionResponse> listarPorActividad(Long idActividad) {
        return participacionRepository
                .findByInscripcion_Actividad_IdActividadOrderByCreadoEnDesc(idActividad)
                .stream()
                .map(ParticipacionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParticipacionResponse> listarPorUsuario(Long idUsuario) {
        return participacionRepository
                .findByInscripcion_Usuario_IdUsuarioOrderByCreadoEnDesc(idUsuario)
                .stream()
                .map(ParticipacionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParticipacionResponse> listarPorEstado(EstadoParticipacion estado) {
        if (estado == null) {
            throw new BadRequestException("El estado de participación es obligatorio");
        }

        return participacionRepository.findByEstadoOrderByCreadoEnDesc(estado)
                .stream()
                .map(ParticipacionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParticipacionResponse> listarPorRolParticipacion(Long idRolParticipacion) {
        obtenerRolParticipacion(idRolParticipacion);

        return participacionRepository
                .findByRolParticipacion_IdOrderByCreadoEnDesc(idRolParticipacion)
                .stream()
                .map(ParticipacionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParticipacionResponse> listarPorAdministradorValidador(Long idAdministrador) {
        obtenerAdministrador(idAdministrador);

        return participacionRepository
                .findByValidadoPorAdmin_IdUsuarioOrderByFechaValidacionDesc(idAdministrador)
                .stream()
                .map(ParticipacionMapper::toResponse)
                .toList();
    }

    public ParticipacionResponse actualizar(
            Long idParticipacion,
            ActualizarParticipacionRequest request
    ) {
        validarRequestActualizar(request);

        Participacion participacion = obtenerParticipacion(idParticipacion);

        if (!participacion.estaPendiente()) {
            throw new BadRequestException("Solo se puede actualizar una participación en estado pendiente");
        }

        RolParticipacion rolParticipacion = obtenerRolParticipacionActivo(request.idRolParticipacion());

        ParticipacionMapper.updateEntity(
                participacion,
                request,
                rolParticipacion
        );

        return ParticipacionMapper.toResponse(
                participacionRepository.save(participacion)
        );
    }

    public ParticipacionResponse validar(
            Long idParticipacion,
            ProcesarParticipacionRequest request,
            Long idAdministradorAutenticado
    ) {
        Participacion participacion = obtenerParticipacion(idParticipacion);
        PerfilAdministrador administrador = obtenerAdministrador(idAdministradorAutenticado);

        validarTransicion(
                participacion,
                EstadoParticipacion.PENDIENTE,
                EstadoParticipacion.NO_VALIDADA
        );

        participacion.setObservaciones(obtenerObservaciones(request));
        participacion.validar(administrador);

        Participacion guardada = participacionRepository.save(participacion);
        puntoService.otorgarPorParticipacion(guardada);
        return ParticipacionMapper.toResponse(guardada);
    }

    public ParticipacionResponse noValidar(
            Long idParticipacion,
            ProcesarParticipacionRequest request,
            Long idAdministradorAutenticado
    ) {
        Participacion participacion = obtenerParticipacion(idParticipacion);
        PerfilAdministrador administrador = obtenerAdministrador(idAdministradorAutenticado);

        validarTransicion(
                participacion,
                EstadoParticipacion.PENDIENTE,
                EstadoParticipacion.VALIDADA
        );

        participacion.setObservaciones(obtenerObservaciones(request));
        participacion.noValidar(administrador);

        Participacion guardada = participacionRepository.save(participacion);
        puntoService.anularOtorgamientoPorParticipacion(guardada);
        return ParticipacionMapper.toResponse(guardada);
    }

    public ParticipacionResponse anular(
            Long idParticipacion,
            ProcesarParticipacionRequest request,
            Long idAdministradorAutenticado
    ) {
        Participacion participacion = obtenerParticipacion(idParticipacion);
        PerfilAdministrador administrador = obtenerAdministrador(idAdministradorAutenticado);

        if (participacion.estaAnulada()) {
            throw new BadRequestException("La participación ya se encuentra anulada");
        }

        participacion.setObservaciones(obtenerObservaciones(request));
        participacion.anular(administrador);

        Participacion guardada = participacionRepository.save(participacion);
        puntoService.anularOtorgamientoPorParticipacion(guardada);
        return ParticipacionMapper.toResponse(guardada);
    }

    public ParticipacionResponse dejarPendiente(Long idParticipacion) {
        Participacion participacion = obtenerParticipacion(idParticipacion);

        if (participacion.estaAnulada()) {
            throw new BadRequestException("No se puede regresar a pendiente una participación anulada");
        }

        if (participacion.estaPendiente()) {
            throw new BadRequestException("La participación ya se encuentra pendiente");
        }

        participacion.dejarPendiente();

        Participacion guardada = participacionRepository.save(participacion);
        puntoService.anularOtorgamientoPorParticipacion(guardada);
        return ParticipacionMapper.toResponse(guardada);
    }

    private Participacion obtenerParticipacion(Long idParticipacion) {
        return participacionRepository.findById(idParticipacion)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la participación con id " + idParticipacion
                ));
    }

    private Inscripcion obtenerInscripcion(Long idInscripcion) {
        return inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la inscripción con id " + idInscripcion
                ));
    }

    private RolParticipacion obtenerRolParticipacion(Long idRolParticipacion) {
        return rolParticipacionRepository.findById(idRolParticipacion)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el rol de participación con id " + idRolParticipacion
                ));
    }

    private RolParticipacion obtenerRolParticipacionActivo(Long idRolParticipacion) {
        RolParticipacion rolParticipacion = obtenerRolParticipacion(idRolParticipacion);

        if (!rolParticipacion.estaActivo()) {
            throw new BadRequestException("El rol de participación debe estar activo");
        }

        return rolParticipacion;
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministrador) {
        return perfilAdministradorRepository.findById(idAdministrador)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el perfil administrador con id " + idAdministrador
                ));
    }

    private void validarParticipacionNoDuplicada(Long idInscripcion) {
        if (participacionRepository.existsByInscripcion_IdInscripcion(idInscripcion)) {
            throw new DuplicateResourceException(
                    "Ya existe una participación registrada para la inscripción con id " + idInscripcion
            );
        }
    }

    private void validarInscripcionDisponibleParaParticipacion(Inscripcion inscripcion) {
        if (inscripcion.estaCancelada()) {
            throw new BadRequestException("No se puede registrar participación desde una inscripción cancelada");
        }

        if (inscripcion.estaRechazada()) {
            throw new BadRequestException("No se puede registrar participación desde una inscripción rechazada");
        }

        EstadoActividad estadoActividad = inscripcion.getActividad().getEstado();

        if (EstadoActividad.BORRADOR.equals(estadoActividad)) {
            throw new BadRequestException("No se puede registrar participación en una actividad en borrador");
        }

        if (EstadoActividad.CANCELADA.equals(estadoActividad)) {
            throw new BadRequestException("No se puede registrar participación en una actividad cancelada");
        }

        if (EstadoActividad.ARCHIVADA.equals(estadoActividad)) {
            throw new BadRequestException("No se puede registrar participación en una actividad archivada");
        }
    }

    private void validarTransicion(
            Participacion participacion,
            EstadoParticipacion... estadosPermitidos
    ) {
        for (EstadoParticipacion estadoPermitido : estadosPermitidos) {
            if (estadoPermitido.equals(participacion.getEstado())) {
                return;
            }
        }

        throw new BadRequestException(
                "El estado actual de la participación no permite esta transición: "
                        + participacion.getEstado().getValor()
        );
    }

    private void validarRequestCrear(CrearParticipacionRequest request) {
        if (request.idInscripcion() == null) {
            throw new BadRequestException("La inscripción es obligatoria");
        }

        if (request.idInscripcion() <= 0) {
            throw new BadRequestException("El id de la inscripción debe ser positivo");
        }

        if (request.idRolParticipacion() == null) {
            throw new BadRequestException("El rol de participación es obligatorio");
        }

        if (request.idRolParticipacion() <= 0) {
            throw new BadRequestException("El id del rol de participación debe ser positivo");
        }
    }

    private void validarRequestActualizar(ActualizarParticipacionRequest request) {
        if (request.idRolParticipacion() == null) {
            throw new BadRequestException("El rol de participación es obligatorio");
        }

        if (request.idRolParticipacion() <= 0) {
            throw new BadRequestException("El id del rol de participación debe ser positivo");
        }
    }

    private String obtenerObservaciones(ProcesarParticipacionRequest request) {
        if (request == null) {
            return null;
        }

        return limpiar(request.observaciones());
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}