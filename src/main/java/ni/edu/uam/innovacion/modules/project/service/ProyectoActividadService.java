package ni.edu.uam.innovacion.modules.project.service;

import java.util.List;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.repository.ActividadRepository;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarProyectoActividadRequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearProyectoActividadRequest;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoActividadResponse;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.project.entity.ProyectoActividad;
import ni.edu.uam.innovacion.modules.project.enums.TipoVinculoProyectoActividad;
import ni.edu.uam.innovacion.modules.project.mapper.ProyectoActividadMapper;
import ni.edu.uam.innovacion.modules.project.repository.ProyectoActividadRepository;
import ni.edu.uam.innovacion.modules.project.repository.ProyectoRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de la lógica de negocio de ProyectoActividad.
 *
 * Esta clase permite vincular proyectos con actividades, consultar sus vínculos
 * y actualizar el tipo de relación entre ambos elementos.
 *
 * Reglas principales:
 * - Un proyecto no puede vincularse dos veces con la misma actividad.
 * - Un proyecto no puede tener más de una actividad marcada como origen.
 * - No se pueden agregar ni modificar vínculos de proyectos archivados.
 * - La actividad debe existir y no estar cancelada ni archivada.
 * - La vinculación debe ser registrada por un administrador válido.
 */
@Service
@Transactional
public class ProyectoActividadService {

    private final ProyectoActividadRepository proyectoActividadRepository;
    private final ProyectoRepository proyectoRepository;
    private final ActividadRepository actividadRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;

    public ProyectoActividadService(
            ProyectoActividadRepository proyectoActividadRepository,
            ProyectoRepository proyectoRepository,
            ActividadRepository actividadRepository,
            PerfilAdministradorRepository perfilAdministradorRepository
    ) {
        this.proyectoActividadRepository = proyectoActividadRepository;
        this.proyectoRepository = proyectoRepository;
        this.actividadRepository = actividadRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
    }

    /**
     * Vincula un proyecto con una actividad.
     *
     * El administrador se obtiene desde el usuario autenticado por JWT.
     */
    public ProyectoActividadResponse crear(
            CrearProyectoActividadRequest request,
            Long idAdministradorAutenticado
    ) {
        validarDatosBasicos(
                request.idProyecto(),
                request.idActividad(),
                request.tipoVinculo(),
                request.esActividadOrigen()
        );

        Proyecto proyecto = obtenerProyectoPorId(request.idProyecto());
        validarProyectoEditable(proyecto);

        Actividad actividad = obtenerActividadPorId(request.idActividad());
        validarActividadVinculable(actividad);

        PerfilAdministrador administrador = obtenerAdministrador(idAdministradorAutenticado);

        validarVinculoNoDuplicado(
                proyecto.getIdProyecto(),
                actividad.getIdActividad()
        );

        validarActividadOrigenDisponible(
                proyecto.getIdProyecto(),
                null,
                request.tipoVinculo(),
                request.esActividadOrigen()
        );

        ProyectoActividad proyectoActividad = ProyectoActividadMapper.toEntity(
                request,
                proyecto,
                actividad,
                administrador
        );

        return ProyectoActividadMapper.toResponse(
                proyectoActividadRepository.save(proyectoActividad)
        );
    }

    /**
     * Lista todos los vínculos registrados para un proyecto.
     */
    @Transactional(readOnly = true)
    public List<ProyectoActividadResponse> listarPorProyecto(Long idProyecto) {
        obtenerProyectoPorId(idProyecto);

        return proyectoActividadRepository
                .findByProyecto_IdProyectoOrderByFechaVinculacionDesc(idProyecto)
                .stream()
                .map(ProyectoActividadMapper::toResponse)
                .toList();
    }

    /**
     * Lista todos los proyectos vinculados a una actividad.
     */
    @Transactional(readOnly = true)
    public List<ProyectoActividadResponse> listarPorActividad(Long idActividad) {
        obtenerActividadPorId(idActividad);

        return proyectoActividadRepository
                .findByActividad_IdActividadOrderByFechaVinculacionDesc(idActividad)
                .stream()
                .map(ProyectoActividadMapper::toResponse)
                .toList();
    }

    /**
     * Lista vínculos por tipo.
     *
     * Ejemplos:
     * - origen
     * - seguimiento
     * - presentacion
     * - concurso
     */
    @Transactional(readOnly = true)
    public List<ProyectoActividadResponse> listarPorTipoVinculo(
            TipoVinculoProyectoActividad tipoVinculo
    ) {
        if (tipoVinculo == null) {
            throw new BadRequestException("El tipo de vinculo es obligatorio");
        }

        return proyectoActividadRepository
                .findByTipoVinculoOrderByFechaVinculacionDesc(tipoVinculo)
                .stream()
                .map(ProyectoActividadMapper::toResponse)
                .toList();
    }

    /**
     * Lista los vínculos de un proyecto filtrados por tipo de vínculo.
     */
    @Transactional(readOnly = true)
    public List<ProyectoActividadResponse> listarPorProyectoYTipoVinculo(
            Long idProyecto,
            TipoVinculoProyectoActividad tipoVinculo
    ) {
        obtenerProyectoPorId(idProyecto);

        if (tipoVinculo == null) {
            throw new BadRequestException("El tipo de vinculo es obligatorio");
        }

        return proyectoActividadRepository
                .findByProyecto_IdProyectoAndTipoVinculoOrderByFechaVinculacionDesc(
                        idProyecto,
                        tipoVinculo
                )
                .stream()
                .map(ProyectoActividadMapper::toResponse)
                .toList();
    }

    /**
     * Busca el vínculo marcado como actividad origen de un proyecto.
     */
    @Transactional(readOnly = true)
    public List<ProyectoActividadResponse> listarActividadOrigen(Long idProyecto) {
        obtenerProyectoPorId(idProyecto);

        return proyectoActividadRepository
                .findByProyecto_IdProyectoAndEsActividadOrigenTrue(idProyecto)
                .stream()
                .map(ProyectoActividadMapper::toResponse)
                .toList();
    }

    /**
     * Lista los vínculos registrados por un administrador específico.
     */
    @Transactional(readOnly = true)
    public List<ProyectoActividadResponse> listarPorAdministrador(Long idAdministrador) {
        obtenerAdministrador(idAdministrador);

        return proyectoActividadRepository
                .findByRegistradoPorAdmin_IdUsuarioOrderByFechaVinculacionDesc(idAdministrador)
                .stream()
                .map(ProyectoActividadMapper::toResponse)
                .toList();
    }

    /**
     * Busca un vínculo por su id.
     */
    @Transactional(readOnly = true)
    public ProyectoActividadResponse buscarPorId(Long idProyectoActividad) {
        return ProyectoActividadMapper.toResponse(
                obtenerProyectoActividadPorId(idProyectoActividad)
        );
    }

    /**
     * Actualiza el tipo de vínculo, si es actividad origen y observaciones.
     *
     * No permite cambiar el proyecto ni la actividad vinculada.
     */
    public ProyectoActividadResponse actualizar(
            Long idProyectoActividad,
            ActualizarProyectoActividadRequest request
    ) {
        if (request.tipoVinculo() == null) {
            throw new BadRequestException("El tipo de vinculo es obligatorio");
        }

        ProyectoActividad proyectoActividad =
                obtenerProyectoActividadPorId(idProyectoActividad);

        Proyecto proyecto = proyectoActividad.getProyecto();
        validarProyectoEditable(proyecto);

        Actividad actividad = proyectoActividad.getActividad();
        validarActividadVinculable(actividad);

        validarActividadOrigenDisponible(
                proyecto.getIdProyecto(),
                idProyectoActividad,
                request.tipoVinculo(),
                request.esActividadOrigen()
        );

        ProyectoActividadMapper.updateEntity(proyectoActividad, request);

        return ProyectoActividadMapper.toResponse(
                proyectoActividadRepository.save(proyectoActividad)
        );
    }

    private ProyectoActividad obtenerProyectoActividadPorId(Long idProyectoActividad) {
        return proyectoActividadRepository.findById(idProyectoActividad)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el vinculo entre proyecto y actividad con id "
                                + idProyectoActividad
                ));
    }

    private Proyecto obtenerProyectoPorId(Long idProyecto) {
        if (idProyecto == null) {
            throw new BadRequestException("El id del proyecto es obligatorio");
        }

        return proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el proyecto con id " + idProyecto
                ));
    }

    private Actividad obtenerActividadPorId(Long idActividad) {
        if (idActividad == null) {
            throw new BadRequestException("El id de la actividad es obligatorio");
        }

        return actividadRepository.findById(idActividad)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la actividad con id " + idActividad
                ));
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministrador) {
        if (idAdministrador == null) {
            throw new BadRequestException("El id del administrador es obligatorio");
        }

        return perfilAdministradorRepository.findById(idAdministrador)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el perfil administrador con id " + idAdministrador
                ));
    }

    private void validarProyectoEditable(Proyecto proyecto) {
        if (proyecto.estaArchivado()) {
            throw new BadRequestException(
                    "No se pueden registrar ni modificar vinculos de un proyecto archivado"
            );
        }
    }

    private void validarActividadVinculable(Actividad actividad) {
        if (EstadoActividad.CANCELADA.equals(actividad.getEstado())) {
            throw new BadRequestException(
                    "No se puede vincular una actividad cancelada a un proyecto"
            );
        }

        if (EstadoActividad.ARCHIVADA.equals(actividad.getEstado())) {
            throw new BadRequestException(
                    "No se puede vincular una actividad archivada a un proyecto"
            );
        }
    }

    private void validarVinculoNoDuplicado(
            Long idProyecto,
            Long idActividad
    ) {
        if (
                proyectoActividadRepository
                        .existsByProyecto_IdProyectoAndActividad_IdActividad(
                                idProyecto,
                                idActividad
                        )
        ) {
            throw new DuplicateResourceException(
                    "El proyecto ya está vinculado con esta actividad"
            );
        }
    }

    private void validarActividadOrigenDisponible(
            Long idProyecto,
            Long idProyectoActividadActual,
            TipoVinculoProyectoActividad tipoVinculo,
            Boolean esActividadOrigen
    ) {
        boolean marcadoComoOrigen = Boolean.TRUE.equals(esActividadOrigen);
        boolean tipoOrigen = TipoVinculoProyectoActividad.ORIGEN.equals(tipoVinculo);

        if (tipoOrigen && !marcadoComoOrigen) {
            throw new BadRequestException(
                    "Si el tipo de vinculo es origen, debe marcarse como actividad origen"
            );
        }

        if (marcadoComoOrigen && !tipoOrigen) {
            throw new BadRequestException(
                    "Si la actividad se marca como origen, el tipo de vinculo debe ser origen"
            );
        }

        if (!marcadoComoOrigen) {
            return;
        }

        boolean yaTieneActividadOrigen;

        if (idProyectoActividadActual == null) {
            yaTieneActividadOrigen =
                    proyectoActividadRepository
                            .existsByProyecto_IdProyectoAndEsActividadOrigenTrue(idProyecto);
        } else {
            yaTieneActividadOrigen =
                    proyectoActividadRepository
                            .existsByProyecto_IdProyectoAndEsActividadOrigenTrueAndIdProyectoActividadNot(
                                    idProyecto,
                                    idProyectoActividadActual
                            );
        }

        if (yaTieneActividadOrigen) {
            throw new DuplicateResourceException(
                    "El proyecto ya tiene una actividad marcada como origen"
            );
        }
    }

    private void validarDatosBasicos(
            Long idProyecto,
            Long idActividad,
            TipoVinculoProyectoActividad tipoVinculo,
            Boolean esActividadOrigen
    ) {
        if (idProyecto == null) {
            throw new BadRequestException("El proyecto es obligatorio");
        }

        if (idProyecto <= 0) {
            throw new BadRequestException("El id del proyecto debe ser positivo");
        }

        if (idActividad == null) {
            throw new BadRequestException("La actividad es obligatoria");
        }

        if (idActividad <= 0) {
            throw new BadRequestException("El id de la actividad debe ser positivo");
        }

        if (tipoVinculo == null) {
            throw new BadRequestException("El tipo de vinculo es obligatorio");
        }

        validarCoherenciaTipoOrigen(tipoVinculo, esActividadOrigen);
    }

    private void validarCoherenciaTipoOrigen(
            TipoVinculoProyectoActividad tipoVinculo,
            Boolean esActividadOrigen
    ) {
        boolean marcadoComoOrigen = Boolean.TRUE.equals(esActividadOrigen);
        boolean tipoOrigen = TipoVinculoProyectoActividad.ORIGEN.equals(tipoVinculo);

        if (tipoOrigen && !marcadoComoOrigen) {
            throw new BadRequestException(
                    "Si el tipo de vinculo es origen, debe marcarse como actividad origen"
            );
        }

        if (marcadoComoOrigen && !tipoOrigen) {
            throw new BadRequestException(
                    "Si la actividad se marca como origen, el tipo de vinculo debe ser origen"
            );
        }
    }
}