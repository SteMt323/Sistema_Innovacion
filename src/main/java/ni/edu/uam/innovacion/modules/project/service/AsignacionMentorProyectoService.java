package ni.edu.uam.innovacion.modules.project.service;

import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarAsignacionMentorProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.AsignacionMentorProyectoResponse;
import ni.edu.uam.innovacion.modules.project.dto.CrearAsignacionMentorProyectoRequest;
import ni.edu.uam.innovacion.modules.project.entity.AsignacionMentorProyecto;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.project.enums.EstadoAsignacion;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;
import ni.edu.uam.innovacion.modules.project.mapper.AsignacionMentorProyectoMapper;
import ni.edu.uam.innovacion.modules.project.repository.AsignacionMentorProyectoRepository;
import ni.edu.uam.innovacion.modules.project.repository.ProyectoRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.PerfilMentorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio encargado de la lógica de negocio de asignaciones de mentor a proyecto.
 *
 * Permite registrar, consultar, actualizar y cambiar el estado de una mentoría
 * asociada a un proyecto.
 *
 * Reglas principales:
 * - Un mentor no puede asignarse dos veces al mismo proyecto.
 * - El proyecto debe permitir modificaciones.
 * - El mentor debe existir y su usuario debe estar activo.
 * - El administrador que registra se obtiene desde el JWT.
 * - Una asignación finalizada no debe reactivarse.
 */
@Service
@Transactional
public class AsignacionMentorProyectoService {

    private final AsignacionMentorProyectoRepository asignacionMentorProyectoRepository;
    private final ProyectoRepository proyectoRepository;
    private final PerfilMentorRepository perfilMentorRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;

    public AsignacionMentorProyectoService(
            AsignacionMentorProyectoRepository asignacionMentorProyectoRepository,
            ProyectoRepository proyectoRepository,
            PerfilMentorRepository perfilMentorRepository,
            PerfilAdministradorRepository perfilAdministradorRepository
    ) {
        this.asignacionMentorProyectoRepository = asignacionMentorProyectoRepository;
        this.proyectoRepository = proyectoRepository;
        this.perfilMentorRepository = perfilMentorRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
    }

    /**
     * Registra una nueva asignación de mentor a proyecto.
     */
    public AsignacionMentorProyectoResponse crear(
            CrearAsignacionMentorProyectoRequest request,
            Long idAdministradorAutenticado
    ) {
        validarDatosCrear(request);

        Proyecto proyecto = obtenerProyectoModificable(request.idProyecto());
        PerfilMentor mentor = obtenerMentorActivo(request.idMentor());
        PerfilAdministrador administrador = obtenerAdministrador(idAdministradorAutenticado);

        validarMentorNoDuplicadoEnProyecto(
                proyecto.getIdProyecto(),
                mentor.getIdUsuario()
        );

        AsignacionMentorProyecto asignacion = AsignacionMentorProyectoMapper.toEntity(
                request,
                proyecto,
                mentor,
                administrador
        );

        return AsignacionMentorProyectoMapper.toResponse(
                asignacionMentorProyectoRepository.save(asignacion)
        );
    }

    /**
     * Lista todas las asignaciones registradas.
     */
    @Transactional(readOnly = true)
    public List<AsignacionMentorProyectoResponse> listarTodas() {
        return asignacionMentorProyectoRepository.findAllByOrderByFechaAsignacionDesc()
                .stream()
                .map(AsignacionMentorProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista asignaciones por proyecto.
     */
    @Transactional(readOnly = true)
    public List<AsignacionMentorProyectoResponse> listarPorProyecto(Long idProyecto) {
        obtenerProyectoPorId(idProyecto);

        return asignacionMentorProyectoRepository
                .findByProyecto_IdProyectoOrderByFechaAsignacionDesc(idProyecto)
                .stream()
                .map(AsignacionMentorProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista asignaciones activas de un proyecto.
     */
    @Transactional(readOnly = true)
    public List<AsignacionMentorProyectoResponse> listarActivasPorProyecto(Long idProyecto) {
        obtenerProyectoPorId(idProyecto);

        return asignacionMentorProyectoRepository
                .findByProyecto_IdProyectoAndEstadoOrderByFechaAsignacionDesc(
                        idProyecto,
                        EstadoAsignacion.ACTIVA
                )
                .stream()
                .map(AsignacionMentorProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista proyectos asignados a un mentor.
     */
    @Transactional(readOnly = true)
    public List<AsignacionMentorProyectoResponse> listarPorMentor(Long idMentor) {
        obtenerMentorPorId(idMentor);

        return asignacionMentorProyectoRepository
                .findByMentor_IdUsuarioOrderByFechaAsignacionDesc(idMentor)
                .stream()
                .map(AsignacionMentorProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista proyectos activos asignados a un mentor.
     */
    @Transactional(readOnly = true)
    public List<AsignacionMentorProyectoResponse> listarActivasPorMentor(Long idMentor) {
        obtenerMentorPorId(idMentor);

        return asignacionMentorProyectoRepository
                .findByMentor_IdUsuarioAndEstadoOrderByFechaAsignacionDesc(
                        idMentor,
                        EstadoAsignacion.ACTIVA
                )
                .stream()
                .map(AsignacionMentorProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista asignaciones por estado.
     */
    @Transactional(readOnly = true)
    public List<AsignacionMentorProyectoResponse> listarPorEstado(EstadoAsignacion estado) {
        if (estado == null) {
            throw new BadRequestException("El estado de la asignación es obligatorio");
        }

        return asignacionMentorProyectoRepository
                .findByEstadoOrderByFechaAsignacionDesc(estado)
                .stream()
                .map(AsignacionMentorProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista asignaciones registradas por un administrador.
     */
    @Transactional(readOnly = true)
    public List<AsignacionMentorProyectoResponse> listarPorAdministrador(Long idAdministrador) {
        obtenerAdministrador(idAdministrador);

        return asignacionMentorProyectoRepository
                .findByAdministradorRegistro_IdUsuarioOrderByFechaAsignacionDesc(idAdministrador)
                .stream()
                .map(AsignacionMentorProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Busca una asignación por id.
     */
    @Transactional(readOnly = true)
    public AsignacionMentorProyectoResponse buscarPorId(Long idAsignacionMentor) {
        return AsignacionMentorProyectoMapper.toResponse(
                obtenerAsignacionPorId(idAsignacionMentor)
        );
    }

    /**
     * Actualiza datos editables de una asignación.
     *
     * No permite cambiar el proyecto ni el mentor.
     */
    public AsignacionMentorProyectoResponse actualizar(
            Long idAsignacionMentor,
            ActualizarAsignacionMentorProyectoRequest request
    ) {
        validarDatosActualizar(request);

        AsignacionMentorProyecto asignacion = obtenerAsignacionPorId(idAsignacionMentor);

        validarAsignacionModificable(asignacion);
        validarProyectoPermiteMentoria(asignacion.getProyecto());

        AsignacionMentorProyectoMapper.updateEntity(asignacion, request);

        return AsignacionMentorProyectoMapper.toResponse(
                asignacionMentorProyectoRepository.save(asignacion)
        );
    }

    /**
     * Reactiva una asignación cancelada.
     *
     * No se permite reactivar una asignación finalizada, porque una finalización
     * representa que el acompañamiento concluyó.
     */
    public AsignacionMentorProyectoResponse activar(Long idAsignacionMentor) {
        AsignacionMentorProyecto asignacion = obtenerAsignacionPorId(idAsignacionMentor);

        validarProyectoPermiteMentoria(asignacion.getProyecto());

        if (asignacion.estaFinalizada()) {
            throw new BadRequestException("No se puede reactivar una asignación finalizada");
        }

        asignacion.activar();

        return AsignacionMentorProyectoMapper.toResponse(
                asignacionMentorProyectoRepository.save(asignacion)
        );
    }

    /**
     * Finaliza una asignación activa.
     */
    public AsignacionMentorProyectoResponse finalizar(Long idAsignacionMentor) {
        AsignacionMentorProyecto asignacion = obtenerAsignacionPorId(idAsignacionMentor);

        validarEstadoActual(asignacion, EstadoAsignacion.ACTIVA);

        asignacion.finalizar();

        return AsignacionMentorProyectoMapper.toResponse(
                asignacionMentorProyectoRepository.save(asignacion)
        );
    }

    /**
     * Cancela una asignación activa.
     */
    public AsignacionMentorProyectoResponse cancelar(Long idAsignacionMentor) {
        AsignacionMentorProyecto asignacion = obtenerAsignacionPorId(idAsignacionMentor);

        validarEstadoActual(asignacion, EstadoAsignacion.ACTIVA);

        asignacion.cancelar();

        return AsignacionMentorProyectoMapper.toResponse(
                asignacionMentorProyectoRepository.save(asignacion)
        );
    }

    private void validarDatosCrear(CrearAsignacionMentorProyectoRequest request) {
        if (request.idProyecto() == null) {
            throw new BadRequestException("El id del proyecto es obligatorio");
        }

        if (request.idMentor() == null) {
            throw new BadRequestException("El id del mentor es obligatorio");
        }

        validarFechaAsignacion(request.fechaAsignacion());
    }

    private void validarDatosActualizar(ActualizarAsignacionMentorProyectoRequest request) {
        validarFechaAsignacion(request.fechaAsignacion());
    }

    private void validarFechaAsignacion(LocalDateTime fechaAsignacion) {
        if (fechaAsignacion != null && fechaAsignacion.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("La fecha de asignación no puede ser futura");
        }
    }

    private void validarMentorNoDuplicadoEnProyecto(Long idProyecto, Long idMentor) {
        if (asignacionMentorProyectoRepository.existsByProyecto_IdProyectoAndMentor_IdUsuario(
                idProyecto,
                idMentor
        )) {
            throw new DuplicateResourceException(
                    "El mentor ya tiene una asignación registrada en este proyecto"
            );
        }
    }

    private void validarAsignacionModificable(AsignacionMentorProyecto asignacion) {
        if (asignacion.estaFinalizada()) {
            throw new BadRequestException("No se puede modificar una asignación finalizada");
        }

        if (asignacion.estaCancelada()) {
            throw new BadRequestException("No se puede modificar una asignación cancelada");
        }
    }

    private Proyecto obtenerProyectoModificable(Long idProyecto) {
        Proyecto proyecto = obtenerProyectoPorId(idProyecto);
        validarProyectoPermiteMentoria(proyecto);
        return proyecto;
    }

    private Proyecto obtenerProyectoPorId(Long idProyecto) {
        return proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el proyecto con id " + idProyecto
                ));
    }

    private void validarProyectoPermiteMentoria(Proyecto proyecto) {
        EstadoProyecto estado = proyecto.getEstado();

        if (EstadoProyecto.ARCHIVADO.equals(estado)) {
            throw new BadRequestException("No se pueden modificar mentorías de un proyecto archivado");
        }

        if (EstadoProyecto.CANCELADO.equals(estado)) {
            throw new BadRequestException("No se pueden modificar mentorías de un proyecto cancelado");
        }

        if (EstadoProyecto.FINALIZADO.equals(estado)) {
            throw new BadRequestException("No se pueden modificar mentorías de un proyecto finalizado");
        }
    }

    private PerfilMentor obtenerMentorActivo(Long idMentor) {
        PerfilMentor mentor = obtenerMentorPorId(idMentor);

        if (mentor.getUsuario() == null) {
            throw new BadRequestException("El mentor no tiene un usuario asociado");
        }

        if (!EstadoUsuario.ACTIVO.equals(mentor.getUsuario().getEstado())) {
            throw new BadRequestException("El usuario mentor debe estar activo");
        }

        return mentor;
    }

    private PerfilMentor obtenerMentorPorId(Long idMentor) {
        return perfilMentorRepository.findById(idMentor)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el perfil mentor con id " + idMentor
                ));
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministrador) {
        return perfilAdministradorRepository.findById(idAdministrador)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el perfil administrador con id " + idAdministrador
                ));
    }

    private AsignacionMentorProyecto obtenerAsignacionPorId(Long idAsignacionMentor) {
        return asignacionMentorProyectoRepository.findById(idAsignacionMentor)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la asignación de mentor con id " + idAsignacionMentor
                ));
    }

    private void validarEstadoActual(
            AsignacionMentorProyecto asignacion,
            EstadoAsignacion... estadosPermitidos
    ) {
        for (EstadoAsignacion estadoPermitido : estadosPermitidos) {
            if (estadoPermitido.equals(asignacion.getEstado())) {
                return;
            }
        }

        throw new BadRequestException(
                "El estado actual de la asignación no permite esta transición: "
                        + asignacion.getEstado().getValor()
        );
    }
}