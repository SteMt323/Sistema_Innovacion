package ni.edu.uam.innovacion.modules.project.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.catalog.entity.RolProyecto;
import ni.edu.uam.innovacion.modules.catalog.repository.RolProyectoRepository;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarIntegranteProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearIntegranteProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.IntegranteProyectoResponse;
import ni.edu.uam.innovacion.modules.project.entity.IntegranteProyecto;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;
import ni.edu.uam.innovacion.modules.project.mapper.IntegranteProyectoMapper;
import ni.edu.uam.innovacion.modules.project.repository.IntegranteProyectoRepository;
import ni.edu.uam.innovacion.modules.project.repository.ProyectoRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio encargado de la lógica de negocio de integrantes de proyecto.
 *
 * Permite registrar, consultar, actualizar y cambiar el estado de los usuarios
 * vinculados a un proyecto.
 *
 * Reglas principales:
 * - Un usuario no puede agregarse dos veces al mismo proyecto.
 * - El proyecto debe permitir modificaciones.
 * - El usuario debe existir y estar activo.
 * - El rol de proyecto debe existir y estar activo.
 * - El registro debe ser realizado por un administrador existente.
 */
@Service
@Transactional
public class IntegranteProyectoService {

    private final IntegranteProyectoRepository integranteProyectoRepository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolProyectoRepository rolProyectoRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;

    public IntegranteProyectoService(
            IntegranteProyectoRepository integranteProyectoRepository,
            ProyectoRepository proyectoRepository,
            UsuarioRepository usuarioRepository,
            RolProyectoRepository rolProyectoRepository,
            PerfilAdministradorRepository perfilAdministradorRepository
    ) {
        this.integranteProyectoRepository = integranteProyectoRepository;
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolProyectoRepository = rolProyectoRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
    }

    /**
     * Registra un nuevo integrante dentro de un proyecto.
     *
     * El id del administrador se recibe desde el controller,
     * normalmente obtenido del JWT autenticado.
     */
    public IntegranteProyectoResponse crear(
            CrearIntegranteProyectoRequest request,
            Long idAdministradorAutenticado
    ) {
        validarDatosCrear(request);

        Proyecto proyecto = obtenerProyectoModificable(request.idProyecto());
        Usuario usuario = obtenerUsuarioActivo(request.idUsuario());
        RolProyecto rolProyecto = obtenerRolProyectoActivo(request.idRolProyecto());
        PerfilAdministrador administrador = obtenerAdministrador(idAdministradorAutenticado);

        validarUsuarioNoDuplicadoEnProyecto(
                proyecto.getIdProyecto(),
                usuario.getIdUsuario()
        );

        IntegranteProyecto integranteProyecto = IntegranteProyectoMapper.toEntity(
                request,
                proyecto,
                usuario,
                rolProyecto,
                administrador
        );

        return IntegranteProyectoMapper.toResponse(
                integranteProyectoRepository.save(integranteProyecto)
        );
    }

    /**
     * Lista todos los integrantes registrados.
     */
    @Transactional(readOnly = true)
    public List<IntegranteProyectoResponse> listarTodos() {
        return integranteProyectoRepository.findAllByOrderByFechaVinculacionDesc()
                .stream()
                .map(IntegranteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista los integrantes de un proyecto.
     */
    @Transactional(readOnly = true)
    public List<IntegranteProyectoResponse> listarPorProyecto(Long idProyecto) {
        obtenerProyectoPorId(idProyecto);

        return integranteProyectoRepository
                .findByProyecto_IdProyectoOrderByFechaVinculacionDesc(idProyecto)
                .stream()
                .map(IntegranteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista solamente los integrantes activos de un proyecto.
     */
    @Transactional(readOnly = true)
    public List<IntegranteProyectoResponse> listarActivosPorProyecto(Long idProyecto) {
        obtenerProyectoPorId(idProyecto);

        return integranteProyectoRepository
                .findByProyecto_IdProyectoAndEstadoOrderByFechaVinculacionDesc(
                        idProyecto,
                        EstadoRegistro.ACTIVO
                )
                .stream()
                .map(IntegranteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista los proyectos en los que participa un usuario.
     */
    @Transactional(readOnly = true)
    public List<IntegranteProyectoResponse> listarPorUsuario(Long idUsuario) {
        obtenerUsuarioPorId(idUsuario);

        return integranteProyectoRepository
                .findByUsuario_IdUsuarioOrderByFechaVinculacionDesc(idUsuario)
                .stream()
                .map(IntegranteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista los proyectos activos en los que participa un usuario.
     */
    @Transactional(readOnly = true)
    public List<IntegranteProyectoResponse> listarActivosPorUsuario(Long idUsuario) {
        obtenerUsuarioPorId(idUsuario);

        return integranteProyectoRepository
                .findByUsuario_IdUsuarioAndEstadoOrderByFechaVinculacionDesc(
                        idUsuario,
                        EstadoRegistro.ACTIVO
                )
                .stream()
                .map(IntegranteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista integrantes según el rol de proyecto.
     */
    @Transactional(readOnly = true)
    public List<IntegranteProyectoResponse> listarPorRolProyecto(Long idRolProyecto) {
        obtenerRolProyectoPorId(idRolProyecto);

        return integranteProyectoRepository
                .findByRolProyecto_IdOrderByFechaVinculacionDesc(idRolProyecto)
                .stream()
                .map(IntegranteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista integrantes registrados por un administrador.
     */
    @Transactional(readOnly = true)
    public List<IntegranteProyectoResponse> listarPorAdministrador(Long idAdministrador) {
        obtenerAdministrador(idAdministrador);

        return integranteProyectoRepository
                .findByRegistradoPorAdmin_IdUsuarioOrderByFechaVinculacionDesc(idAdministrador)
                .stream()
                .map(IntegranteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Busca un integrante de proyecto por id.
     */
    @Transactional(readOnly = true)
    public IntegranteProyectoResponse buscarPorId(Long idIntegranteProyecto) {
        return IntegranteProyectoMapper.toResponse(
                obtenerIntegranteProyectoPorId(idIntegranteProyecto)
        );
    }

    /**
     * Actualiza los datos editables de un integrante.
     *
     * No cambia el proyecto ni el usuario.
     */
    public IntegranteProyectoResponse actualizar(
            Long idIntegranteProyecto,
            ActualizarIntegranteProyectoRequest request
    ) {
        validarDatosActualizar(request);

        IntegranteProyecto integranteProyecto = obtenerIntegranteProyectoPorId(idIntegranteProyecto);

        validarIntegranteModificable(integranteProyecto);
        validarProyectoPermiteIntegrantes(integranteProyecto.getProyecto());

        RolProyecto rolProyecto = obtenerRolProyectoActivo(request.idRolProyecto());

        IntegranteProyectoMapper.updateEntity(
                integranteProyecto,
                request,
                rolProyecto
        );

        return IntegranteProyectoMapper.toResponse(
                integranteProyectoRepository.save(integranteProyecto)
        );
    }

    /**
     * Activa un integrante de proyecto.
     */
    public IntegranteProyectoResponse activar(Long idIntegranteProyecto) {
        IntegranteProyecto integranteProyecto = obtenerIntegranteProyectoPorId(idIntegranteProyecto);

        validarProyectoPermiteIntegrantes(integranteProyecto.getProyecto());

        integranteProyecto.activar();

        return IntegranteProyectoMapper.toResponse(
                integranteProyectoRepository.save(integranteProyecto)
        );
    }

    /**
     * Inactiva un integrante de proyecto.
     *
     * No elimina físicamente el registro para mantener trazabilidad.
     */
    public IntegranteProyectoResponse inactivar(Long idIntegranteProyecto) {
        IntegranteProyecto integranteProyecto = obtenerIntegranteProyectoPorId(idIntegranteProyecto);

        if (integranteProyecto.estaArchivado()) {
            throw new BadRequestException("No se puede inactivar un integrante archivado");
        }

        integranteProyecto.inactivar();

        return IntegranteProyectoMapper.toResponse(
                integranteProyectoRepository.save(integranteProyecto)
        );
    }

    /**
     * Archiva un integrante de proyecto.
     *
     * Se usa cuando el registro debe conservarse por historial,
     * pero ya no forma parte de la operación normal del proyecto.
     */
    public IntegranteProyectoResponse archivar(Long idIntegranteProyecto) {
        IntegranteProyecto integranteProyecto = obtenerIntegranteProyectoPorId(idIntegranteProyecto);

        integranteProyecto.archivar();

        return IntegranteProyectoMapper.toResponse(
                integranteProyectoRepository.save(integranteProyecto)
        );
    }

    private void validarDatosCrear(CrearIntegranteProyectoRequest request) {
        if (request.idProyecto() == null) {
            throw new BadRequestException("El id del proyecto es obligatorio");
        }

        if (request.idUsuario() == null) {
            throw new BadRequestException("El id del usuario es obligatorio");
        }

        if (request.idRolProyecto() == null) {
            throw new BadRequestException("El id del rol de proyecto es obligatorio");
        }

        validarFechaVinculacion(request.fechaVinculacion());
    }

    private void validarDatosActualizar(ActualizarIntegranteProyectoRequest request) {
        if (request.idRolProyecto() == null) {
            throw new BadRequestException("El id del rol de proyecto es obligatorio");
        }

        validarFechaVinculacion(request.fechaVinculacion());
    }

    private void validarFechaVinculacion(LocalDate fechaVinculacion) {
        if (fechaVinculacion != null && fechaVinculacion.isAfter(LocalDate.now())) {
            throw new BadRequestException("La fecha de vinculacion no puede ser futura");
        }
    }

    private void validarUsuarioNoDuplicadoEnProyecto(
            Long idProyecto,
            Long idUsuario
    ) {
        if (integranteProyectoRepository.existsByProyecto_IdProyectoAndUsuario_IdUsuario(
                idProyecto,
                idUsuario
        )) {
            throw new DuplicateResourceException(
                    "El usuario ya está vinculado a este proyecto"
            );
        }
    }

    private void validarIntegranteModificable(IntegranteProyecto integranteProyecto) {
        if (integranteProyecto.estaArchivado()) {
            throw new BadRequestException("No se puede modificar un integrante archivado");
        }
    }

    private Proyecto obtenerProyectoModificable(Long idProyecto) {
        Proyecto proyecto = obtenerProyectoPorId(idProyecto);
        validarProyectoPermiteIntegrantes(proyecto);
        return proyecto;
    }

    private Proyecto obtenerProyectoPorId(Long idProyecto) {
        return proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el proyecto con id " + idProyecto
                ));
    }

    private void validarProyectoPermiteIntegrantes(Proyecto proyecto) {
        EstadoProyecto estado = proyecto.getEstado();

        if (EstadoProyecto.ARCHIVADO.equals(estado)) {
            throw new BadRequestException("No se pueden modificar integrantes de un proyecto archivado");
        }

        if (EstadoProyecto.CANCELADO.equals(estado)) {
            throw new BadRequestException("No se pueden modificar integrantes de un proyecto cancelado");
        }

        if (EstadoProyecto.FINALIZADO.equals(estado)) {
            throw new BadRequestException("No se pueden modificar integrantes de un proyecto finalizado");
        }
    }

    private Usuario obtenerUsuarioActivo(Long idUsuario) {
        Usuario usuario = obtenerUsuarioPorId(idUsuario);

        if (!EstadoUsuario.ACTIVO.equals(usuario.getEstado())) {
            throw new BadRequestException("El usuario debe estar activo para vincularse a un proyecto");
        }

        return usuario;
    }

    private Usuario obtenerUsuarioPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con id " + idUsuario
                ));
    }

    private RolProyecto obtenerRolProyectoActivo(Long idRolProyecto) {
        RolProyecto rolProyecto = obtenerRolProyectoPorId(idRolProyecto);

        if (!rolProyecto.estaActivo()) {
            throw new BadRequestException("El rol de proyecto debe estar activo");
        }

        return rolProyecto;
    }

    private RolProyecto obtenerRolProyectoPorId(Long idRolProyecto) {
        return rolProyectoRepository.findById(idRolProyecto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el rol de proyecto con id " + idRolProyecto
                ));
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministrador) {
        return perfilAdministradorRepository.findById(idAdministrador)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el perfil administrador con id " + idAdministrador
                ));
    }

    private IntegranteProyecto obtenerIntegranteProyectoPorId(Long idIntegranteProyecto) {
        return integranteProyectoRepository.findById(idIntegranteProyecto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el integrante de proyecto con id " + idIntegranteProyecto
                ));
    }
}