package ni.edu.uam.innovacion.modules.project.service;

import java.time.LocalDate;
import java.util.List;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarProyectoPIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CambiarFaseProyectoPIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearProyectoPIARequest;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoPIAResponse;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.project.entity.ProyectoPIA;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyectoPIA;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;
import ni.edu.uam.innovacion.modules.project.mapper.ProyectoPIAMapper;
import ni.edu.uam.innovacion.modules.project.repository.ProyectoPIARepository;
import ni.edu.uam.innovacion.modules.project.repository.ProyectoRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de la lógica de negocio de ProyectoPIA.
 *
 * Permite registrar proyectos dentro del Programa PIA,
 * consultar proyectos PIA, actualizar información general,
 * cambiar fase y modificar el estado del proceso.
 *
 * Reglas principales:
 * - Un proyecto solo puede tener un registro principal en ProyectoPIA.
 * - No se permite registrar proyectos archivados.
 * - Solo un administrador puede registrar proyectos en PIA.
 * - No se deben modificar proyectos PIA finalizados o retirados.
 * - Los cambios de fase deben ser coherentes con el ciclo del Programa PIA.
 */
@Service
@Transactional
public class ProyectoPIAService {

    private final ProyectoPIARepository proyectoPIARepository;
    private final ProyectoRepository proyectoRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;

    public ProyectoPIAService(
            ProyectoPIARepository proyectoPIARepository,
            ProyectoRepository proyectoRepository,
            PerfilAdministradorRepository perfilAdministradorRepository
    ) {
        this.proyectoPIARepository = proyectoPIARepository;
        this.proyectoRepository = proyectoRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
    }

    /**
     * Registra un proyecto dentro del Programa PIA.
     *
     * El administrador se obtiene desde el JWT en el controller.
     */
    public ProyectoPIAResponse crear(
            CrearProyectoPIARequest request,
            Long idAdministradorAutenticado
    ) {
        validarDatosBasicosCreacion(request);

        Proyecto proyecto = obtenerProyectoPorId(request.idProyecto());
        validarProyectoDisponibleParaPIA(proyecto);

        validarProyectoNoRegistradoEnPIA(request.idProyecto());

        PerfilAdministrador administrador = obtenerAdministrador(idAdministradorAutenticado);

        ProyectoPIA proyectoPIA = ProyectoPIAMapper.toEntity(
                request,
                proyecto,
                administrador
        );

        if (proyectoPIA.getFaseActual() == null) {
            proyectoPIA.setFaseActual(FasePIA.PROSPECTO);
        }

        if (proyectoPIA.getFechaIngreso() == null) {
            proyectoPIA.setFechaIngreso(LocalDate.now());
        }

        proyectoPIA.setEstado(EstadoProyectoPIA.ACTIVO);

        return ProyectoPIAMapper.toResponse(
                proyectoPIARepository.save(proyectoPIA)
        );
    }

    /**
     * Lista todos los proyectos registrados en PIA.
     */
    @Transactional(readOnly = true)
    public List<ProyectoPIAResponse> listarTodos() {
        return proyectoPIARepository.findAllByOrderByFechaIngresoDesc()
                .stream()
                .map(ProyectoPIAMapper::toResponse)
                .toList();
    }

    /**
     * Lista únicamente los proyectos PIA activos.
     */
    @Transactional(readOnly = true)
    public List<ProyectoPIAResponse> listarActivos() {
        return proyectoPIARepository
                .findByEstadoOrderByFechaIngresoDesc(EstadoProyectoPIA.ACTIVO)
                .stream()
                .map(ProyectoPIAMapper::toResponse)
                .toList();
    }

    /**
     * Lista proyectos PIA por estado.
     */
    @Transactional(readOnly = true)
    public List<ProyectoPIAResponse> listarPorEstado(EstadoProyectoPIA estado) {
        if (estado == null) {
            throw new BadRequestException("El estado del proyecto PIA es obligatorio");
        }

        return proyectoPIARepository
                .findByEstadoOrderByFechaIngresoDesc(estado)
                .stream()
                .map(ProyectoPIAMapper::toResponse)
                .toList();
    }

    /**
     * Lista proyectos PIA por fase actual.
     */
    @Transactional(readOnly = true)
    public List<ProyectoPIAResponse> listarPorFase(FasePIA faseActual) {
        if (faseActual == null) {
            throw new BadRequestException("La fase del proyecto PIA es obligatoria");
        }

        return proyectoPIARepository
                .findByFaseActualOrderByFechaIngresoDesc(faseActual)
                .stream()
                .map(ProyectoPIAMapper::toResponse)
                .toList();
    }

    /**
     * Lista proyectos PIA por estado y fase.
     */
    @Transactional(readOnly = true)
    public List<ProyectoPIAResponse> listarPorEstadoYFase(
            EstadoProyectoPIA estado,
            FasePIA faseActual
    ) {
        if (estado == null) {
            throw new BadRequestException("El estado del proyecto PIA es obligatorio");
        }

        if (faseActual == null) {
            throw new BadRequestException("La fase del proyecto PIA es obligatoria");
        }

        return proyectoPIARepository
                .findByEstadoAndFaseActualOrderByFechaIngresoDesc(
                        estado,
                        faseActual
                )
                .stream()
                .map(ProyectoPIAMapper::toResponse)
                .toList();
    }

    /**
     * Lista proyectos PIA registrados por un administrador específico.
     */
    @Transactional(readOnly = true)
    public List<ProyectoPIAResponse> listarPorAdministrador(Long idAdministrador) {
        obtenerAdministrador(idAdministrador);

        return proyectoPIARepository
                .findByRegistradoPorAdmin_IdUsuarioOrderByFechaIngresoDesc(idAdministrador)
                .stream()
                .map(ProyectoPIAMapper::toResponse)
                .toList();
    }

    /**
     * Busca un registro ProyectoPIA por su id.
     */
    @Transactional(readOnly = true)
    public ProyectoPIAResponse buscarPorId(Long idProyectoPIA) {
        return ProyectoPIAMapper.toResponse(
                obtenerProyectoPIAPorId(idProyectoPIA)
        );
    }

    /**
     * Busca el registro PIA asociado a un proyecto base.
     */
    @Transactional(readOnly = true)
    public ProyectoPIAResponse buscarPorProyecto(Long idProyecto) {
        return ProyectoPIAMapper.toResponse(
                proyectoPIARepository.findByProyecto_IdProyecto(idProyecto)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "El proyecto con id " + idProyecto + " no está registrado en PIA"
                        ))
        );
    }

    /**
     * Actualiza datos generales del registro PIA.
     *
     * No cambia el estado. Para estado se usan métodos específicos.
     */
    public ProyectoPIAResponse actualizar(
            Long idProyectoPIA,
            ActualizarProyectoPIARequest request
    ) {
        validarDatosBasicosActualizacion(request);

        ProyectoPIA proyectoPIA = obtenerProyectoPIAPorId(idProyectoPIA);

        validarPuedeModificarse(proyectoPIA);

        ProyectoPIAMapper.updateEntity(proyectoPIA, request);

        return ProyectoPIAMapper.toResponse(
                proyectoPIARepository.save(proyectoPIA)
        );
    }

    /**
     * Cambia la fase actual del proyecto PIA.
     *
     * Cuando se implemente HistorialFasePIA, aquí se debe registrar
     * el cambio de fase anterior a la nueva fase.
     */
    public ProyectoPIAResponse cambiarFase(
            Long idProyectoPIA,
            CambiarFaseProyectoPIARequest request
    ) {
        if (request.nuevaFase() == null) {
            throw new BadRequestException("La nueva fase del proyecto PIA es obligatoria");
        }

        ProyectoPIA proyectoPIA = obtenerProyectoPIAPorId(idProyectoPIA);

        validarPuedeModificarse(proyectoPIA);
        validarEstadoActual(proyectoPIA, EstadoProyectoPIA.ACTIVO, EstadoProyectoPIA.PAUSADO);
        validarCambioFase(proyectoPIA.getFaseActual(), request.nuevaFase());

        proyectoPIA.setFaseActual(request.nuevaFase());

        if (request.observaciones() != null && !request.observaciones().isBlank()) {
            proyectoPIA.setObservaciones(request.observaciones());
        }

        return ProyectoPIAMapper.toResponse(
                proyectoPIARepository.save(proyectoPIA)
        );
    }

    /**
     * Pausa un proyecto PIA activo.
     */
    public ProyectoPIAResponse pausar(Long idProyectoPIA) {
        ProyectoPIA proyectoPIA = obtenerProyectoPIAPorId(idProyectoPIA);

        validarEstadoActual(proyectoPIA, EstadoProyectoPIA.ACTIVO);

        proyectoPIA.setEstado(EstadoProyectoPIA.PAUSADO);

        return ProyectoPIAMapper.toResponse(
                proyectoPIARepository.save(proyectoPIA)
        );
    }

    /**
     * Reactiva un proyecto PIA pausado.
     */
    public ProyectoPIAResponse reactivar(Long idProyectoPIA) {
        ProyectoPIA proyectoPIA = obtenerProyectoPIAPorId(idProyectoPIA);

        validarEstadoActual(proyectoPIA, EstadoProyectoPIA.PAUSADO);

        proyectoPIA.setEstado(EstadoProyectoPIA.ACTIVO);

        return ProyectoPIAMapper.toResponse(
                proyectoPIARepository.save(proyectoPIA)
        );
    }

    /**
     * Finaliza un proyecto PIA activo o pausado.
     */
    public ProyectoPIAResponse finalizar(Long idProyectoPIA) {
        ProyectoPIA proyectoPIA = obtenerProyectoPIAPorId(idProyectoPIA);

        validarEstadoActual(
                proyectoPIA,
                EstadoProyectoPIA.ACTIVO,
                EstadoProyectoPIA.PAUSADO
        );

        proyectoPIA.setEstado(EstadoProyectoPIA.FINALIZADO);

        return ProyectoPIAMapper.toResponse(
                proyectoPIARepository.save(proyectoPIA)
        );
    }

    /**
     * Retira un proyecto del Programa PIA.
     */
    public ProyectoPIAResponse retirar(Long idProyectoPIA) {
        ProyectoPIA proyectoPIA = obtenerProyectoPIAPorId(idProyectoPIA);

        validarEstadoActual(
                proyectoPIA,
                EstadoProyectoPIA.ACTIVO,
                EstadoProyectoPIA.PAUSADO
        );

        proyectoPIA.setEstado(EstadoProyectoPIA.RETIRADO);

        return ProyectoPIAMapper.toResponse(
                proyectoPIARepository.save(proyectoPIA)
        );
    }

    private ProyectoPIA obtenerProyectoPIAPorId(Long idProyectoPIA) {
        return proyectoPIARepository.findById(idProyectoPIA)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el proyecto PIA con id " + idProyectoPIA
                ));
    }

    private Proyecto obtenerProyectoPorId(Long idProyecto) {
        return proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el proyecto con id " + idProyecto
                ));
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministrador) {
        return perfilAdministradorRepository.findById(idAdministrador)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el perfil administrador con id " + idAdministrador
                ));
    }

    private void validarProyectoDisponibleParaPIA(Proyecto proyecto) {
        if (EstadoProyecto.ARCHIVADO.equals(proyecto.getEstado())) {
            throw new BadRequestException(
                    "No se puede registrar un proyecto archivado dentro del Programa PIA"
            );
        }

        if (EstadoProyecto.CANCELADO.equals(proyecto.getEstado())) {
            throw new BadRequestException(
                    "No se puede registrar un proyecto cancelado dentro del Programa PIA"
            );
        }
    }

    private void validarProyectoNoRegistradoEnPIA(Long idProyecto) {
        if (proyectoPIARepository.existsByProyecto_IdProyecto(idProyecto)) {
            throw new DuplicateResourceException(
                    "El proyecto ya está registrado dentro del Programa PIA"
            );
        }
    }

    private void validarDatosBasicosCreacion(CrearProyectoPIARequest request) {
        if (request.idProyecto() == null) {
            throw new BadRequestException("El proyecto es obligatorio");
        }

        if (request.idProyecto() <= 0) {
            throw new BadRequestException("El id del proyecto debe ser positivo");
        }

        if (request.fechaIngreso() != null && request.fechaIngreso().isAfter(LocalDate.now())) {
            throw new BadRequestException("La fecha de ingreso no puede ser futura");
        }
    }

    private void validarDatosBasicosActualizacion(ActualizarProyectoPIARequest request) {
        if (request.faseActual() == null) {
            throw new BadRequestException("La fase actual del proyecto PIA es obligatoria");
        }

        if (request.fechaIngreso() == null) {
            throw new BadRequestException("La fecha de ingreso es obligatoria");
        }

        if (request.fechaIngreso().isAfter(LocalDate.now())) {
            throw new BadRequestException("La fecha de ingreso no puede ser futura");
        }
    }

    private void validarPuedeModificarse(ProyectoPIA proyectoPIA) {
        if (!proyectoPIA.puedeModificarse()) {
            throw new BadRequestException(
                    "No se puede modificar un proyecto PIA finalizado o retirado"
            );
        }

        if (proyectoPIA.getProyecto() != null && proyectoPIA.getProyecto().estaArchivado()) {
            throw new BadRequestException(
                    "No se puede modificar el registro PIA de un proyecto archivado"
            );
        }
    }

    private void validarEstadoActual(
            ProyectoPIA proyectoPIA,
            EstadoProyectoPIA... estadosPermitidos
    ) {
        for (EstadoProyectoPIA estadoPermitido : estadosPermitidos) {
            if (estadoPermitido.equals(proyectoPIA.getEstado())) {
                return;
            }
        }

        throw new BadRequestException(
                "El estado actual del proyecto PIA no permite esta transición: "
                        + proyectoPIA.getEstado().getValor()
        );
    }

    /**
     * Valida una secuencia básica de fases del Programa PIA.
     *
     * Secuencia esperada:
     * PROSPECTO -> PREINCUBACION -> INCUBACION -> ACELERACION -> SEGUIMIENTO -> GRADUADO
     */
    private void validarCambioFase(FasePIA faseActual, FasePIA nuevaFase) {
        if (faseActual == null) {
            return;
        }

        if (faseActual.equals(nuevaFase)) {
            throw new BadRequestException(
                    "La nueva fase no puede ser igual a la fase actual"
            );
        }

        if (nuevaFase.ordinal() < faseActual.ordinal()) {
            throw new BadRequestException(
                    "No se puede retroceder a una fase anterior del Programa PIA"
            );
        }
    }
}