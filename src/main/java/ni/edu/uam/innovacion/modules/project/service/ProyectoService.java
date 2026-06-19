package ni.edu.uam.innovacion.modules.project.service;

import java.time.LocalDate;
import java.util.List;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;
import ni.edu.uam.innovacion.modules.catalog.repository.FuenteProyectoRepository;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoResponse;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;
import ni.edu.uam.innovacion.modules.project.mapper.ProyectoMapper;
import ni.edu.uam.innovacion.modules.project.repository.ProyectoRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de la lógica de negocio del módulo de proyectos.
 *
 * Permite registrar, consultar, actualizar y cambiar el estado
 * de proyectos de innovación o emprendimiento.
 *
 * Un proyecto debe tener:
 * - una fuente de proyecto activa
 * - un administrador que lo registra
 * - un nombre obligatorio
 * - fechas coherentes, si se registran
 */
@Service
@Transactional
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final FuenteProyectoRepository fuenteProyectoRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;

    public ProyectoService(
            ProyectoRepository proyectoRepository,
            FuenteProyectoRepository fuenteProyectoRepository,
            PerfilAdministradorRepository perfilAdministradorRepository
    ) {
        this.proyectoRepository = proyectoRepository;
        this.fuenteProyectoRepository = fuenteProyectoRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
    }

    /**
     * Registra un nuevo proyecto.
     *
     * El administrador se obtiene desde el usuario autenticado mediante JWT,
     * por eso el idAdministradorAutenticado se recibe desde el controller.
     */
    public ProyectoResponse crear(
            CrearProyectoRequest request,
            Long idAdministradorAutenticado
    ) {
        validarDatosBasicos(
                request.nombre(),
                request.fechaInicio(),
                request.fechaFin()
        );

        validarNombreDisponible(request.nombre());

        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoActiva(request.idFuenteProyecto());
        PerfilAdministrador administradorRegistro = obtenerAdministrador(idAdministradorAutenticado);

        Proyecto proyecto = ProyectoMapper.toEntity(
                request,
                fuenteProyecto,
                administradorRegistro
        );

        return ProyectoMapper.toResponse(proyectoRepository.save(proyecto));
    }

    /**
     * Lista todos los proyectos registrados, ordenados por fecha de registro descendente.
     */
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarTodos() {
        return proyectoRepository.findAllByOrderByFechaRegistroDesc()
                .stream()
                .map(ProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista los proyectos activos.
     */
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarActivos() {
        return proyectoRepository.findByEstadoOrderByFechaRegistroDesc(EstadoProyecto.ACTIVO)
                .stream()
                .map(ProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista proyectos por estado.
     */
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarPorEstado(EstadoProyecto estado) {
        if (estado == null) {
            throw new BadRequestException("El estado del proyecto es obligatorio");
        }

        return proyectoRepository.findByEstadoOrderByFechaRegistroDesc(estado)
                .stream()
                .map(ProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista proyectos según su fuente de proyecto.
     */
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarPorFuente(Long idFuenteProyecto) {
        obtenerFuenteProyectoPorId(idFuenteProyecto);

        return proyectoRepository.findByFuenteProyecto_IdOrderByFechaRegistroDesc(idFuenteProyecto)
                .stream()
                .map(ProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista proyectos activos según su fuente de proyecto.
     */
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarActivosPorFuente(Long idFuenteProyecto) {
        obtenerFuenteProyectoPorId(idFuenteProyecto);

        return proyectoRepository
                .findByEstadoAndFuenteProyecto_IdOrderByFechaRegistroDesc(
                        EstadoProyecto.ACTIVO,
                        idFuenteProyecto
                )
                .stream()
                .map(ProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista los proyectos registrados por un administrador específico.
     */
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarPorAdministrador(Long idAdministradorRegistro) {
        obtenerAdministrador(idAdministradorRegistro);

        return proyectoRepository
                .findByAdministradorRegistro_IdUsuarioOrderByFechaRegistroDesc(idAdministradorRegistro)
                .stream()
                .map(ProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Busca un proyecto por id.
     */
    @Transactional(readOnly = true)
    public ProyectoResponse buscarPorId(Long idProyecto) {
        return ProyectoMapper.toResponse(obtenerProyectoPorId(idProyecto));
    }

    /**
     * Actualiza los datos generales de un proyecto.
     *
     * No permite actualizar proyectos archivados.
     * Los cambios de estado se manejan mediante métodos específicos.
     */
    public ProyectoResponse actualizar(
            Long idProyecto,
            ActualizarProyectoRequest request
    ) {
        validarDatosBasicos(
                request.nombre(),
                request.fechaInicio(),
                request.fechaFin()
        );

        Proyecto proyecto = obtenerProyectoPorId(idProyecto);

        if (proyecto.estaArchivado()) {
            throw new BadRequestException("No se puede actualizar un proyecto archivado");
        }

        validarNombreDisponibleParaActualizar(request.nombre(), idProyecto);

        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoActiva(request.idFuenteProyecto());

        ProyectoMapper.updateEntity(
                proyecto,
                request,
                fuenteProyecto
        );

        return ProyectoMapper.toResponse(proyectoRepository.save(proyecto));
    }

    /**
     * Pausa un proyecto activo.
     */
    public ProyectoResponse pausar(Long idProyecto) {
        Proyecto proyecto = obtenerProyectoPorId(idProyecto);

        validarEstadoActual(proyecto, EstadoProyecto.ACTIVO);

        proyecto.setEstado(EstadoProyecto.PAUSADO);

        return ProyectoMapper.toResponse(proyectoRepository.save(proyecto));
    }

    /**
     * Reactiva un proyecto pausado.
     */
    public ProyectoResponse reactivar(Long idProyecto) {
        Proyecto proyecto = obtenerProyectoPorId(idProyecto);

        validarEstadoActual(proyecto, EstadoProyecto.PAUSADO);

        proyecto.setEstado(EstadoProyecto.ACTIVO);

        return ProyectoMapper.toResponse(proyectoRepository.save(proyecto));
    }

    /**
     * Finaliza un proyecto activo o pausado.
     */
    public ProyectoResponse finalizar(Long idProyecto) {
        Proyecto proyecto = obtenerProyectoPorId(idProyecto);

        validarEstadoActual(
                proyecto,
                EstadoProyecto.ACTIVO,
                EstadoProyecto.PAUSADO
        );

        proyecto.setEstado(EstadoProyecto.FINALIZADO);

        if (proyecto.getFechaFin() == null) {
            proyecto.setFechaFin(LocalDate.now());
        }

        return ProyectoMapper.toResponse(proyectoRepository.save(proyecto));
    }

    /**
     * Cancela un proyecto activo o pausado.
     */
    public ProyectoResponse cancelar(Long idProyecto) {
        Proyecto proyecto = obtenerProyectoPorId(idProyecto);

        validarEstadoActual(
                proyecto,
                EstadoProyecto.ACTIVO,
                EstadoProyecto.PAUSADO
        );

        proyecto.setEstado(EstadoProyecto.CANCELADO);

        return ProyectoMapper.toResponse(proyectoRepository.save(proyecto));
    }

    /**
     * Archiva un proyecto finalizado o cancelado.
     *
     * Se conserva como historial y ya no debería modificarse.
     */
    public ProyectoResponse archivar(Long idProyecto) {
        Proyecto proyecto = obtenerProyectoPorId(idProyecto);

        validarEstadoActual(
                proyecto,
                EstadoProyecto.FINALIZADO,
                EstadoProyecto.CANCELADO
        );

        proyecto.setEstado(EstadoProyecto.ARCHIVADO);

        return ProyectoMapper.toResponse(proyectoRepository.save(proyecto));
    }

    private Proyecto obtenerProyectoPorId(Long idProyecto) {
        return proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el proyecto con id " + idProyecto
                ));
    }

    private FuenteProyecto obtenerFuenteProyectoPorId(Long idFuenteProyecto) {
        return fuenteProyectoRepository.findById(idFuenteProyecto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la fuente de proyecto con id " + idFuenteProyecto
                ));
    }

    private FuenteProyecto obtenerFuenteProyectoActiva(Long idFuenteProyecto) {
        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoPorId(idFuenteProyecto);

        if (!fuenteProyecto.estaActivo()) {
            throw new BadRequestException("La fuente de proyecto debe estar activa");
        }

        return fuenteProyecto;
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministradorRegistro) {
        return perfilAdministradorRepository.findById(idAdministradorRegistro)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el perfil administrador con id " + idAdministradorRegistro
                ));
    }

    private void validarDatosBasicos(
            String nombre,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre del proyecto es obligatorio");
        }

        if (fechaFin != null && fechaInicio != null && fechaFin.isBefore(fechaInicio)) {
            throw new BadRequestException("La fecha de finalizacion no puede ser anterior a la fecha de inicio");
        }
    }

    private void validarNombreDisponible(String nombre) {
        String nombreLimpio = limpiar(nombre);

        if (proyectoRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new DuplicateResourceException(
                    "Ya existe un proyecto con el nombre " + nombreLimpio
            );
        }
    }

    private void validarNombreDisponibleParaActualizar(
            String nombre,
            Long idProyecto
    ) {
        String nombreLimpio = limpiar(nombre);

        proyectoRepository.findByNombreIgnoreCase(nombreLimpio)
                .ifPresent(proyectoExistente -> {
                    if (!proyectoExistente.getIdProyecto().equals(idProyecto)) {
                        throw new DuplicateResourceException(
                                "Ya existe otro proyecto con el nombre " + nombreLimpio
                        );
                    }
                });
    }

    private void validarEstadoActual(
            Proyecto proyecto,
            EstadoProyecto... estadosPermitidos
    ) {
        for (EstadoProyecto estadoPermitido : estadosPermitidos) {
            if (estadoPermitido.equals(proyecto.getEstado())) {
                return;
            }
        }

        throw new BadRequestException(
                "El estado actual del proyecto no permite esta transicion: "
                        + proyecto.getEstado().getValor()
        );
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}