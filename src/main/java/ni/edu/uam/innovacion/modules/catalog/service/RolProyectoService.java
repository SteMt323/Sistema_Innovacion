package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.RolProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.RolProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.RolProyecto;
import ni.edu.uam.innovacion.modules.catalog.mapper.RolProyectoMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.RolProyectoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio del catálogo de roles de proyecto.
 *
 * Aquí se valida que no existan roles de proyecto duplicados
 * y se controla el cambio de estado de cada registro.
 */
@Service
@Transactional
public class RolProyectoService {

    private final RolProyectoRepository rolProyectoRepository;

    public RolProyectoService(RolProyectoRepository rolProyectoRepository) {
        this.rolProyectoRepository = rolProyectoRepository;
    }

    /**
     * Lista todos los roles de proyecto registrados,
     * sin importar su estado.
     */
    @Transactional(readOnly = true)
    public List<RolProyectoResponse> listarTodos() {
        return rolProyectoRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(RolProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista únicamente los roles de proyecto activos.
     *
     * Este método sirve para formularios donde solo deben aparecer
     * roles disponibles para asignar integrantes a proyectos.
     */
    @Transactional(readOnly = true)
    public List<RolProyectoResponse> listarActivos() {
        return rolProyectoRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(RolProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Busca un rol de proyecto por su id.
     */
    @Transactional(readOnly = true)
    public RolProyectoResponse buscarPorId(Long id) {
        RolProyecto rolProyecto = obtenerRolProyectoPorId(id);
        return RolProyectoMapper.toResponse(rolProyecto);
    }

    /**
     * Crea un nuevo rol de proyecto.
     *
     * Antes de guardar, valida que no exista otro rol
     * con el mismo nombre.
     */
    public RolProyectoResponse crear(RolProyectoRequest request) {
        normalizarRequest(request);

        if (rolProyectoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un rol de proyecto con ese nombre"
            );
        }

        RolProyecto rolProyecto = RolProyectoMapper.toEntity(request);
        RolProyecto rolProyectoGuardado = rolProyectoRepository.save(rolProyecto);

        return RolProyectoMapper.toResponse(rolProyectoGuardado);
    }

    /**
     * Actualiza un rol de proyecto existente.
     *
     * También valida que el nuevo nombre no esté siendo usado
     * por otro rol de proyecto.
     */
    public RolProyectoResponse actualizar(Long id, RolProyectoRequest request) {
        normalizarRequest(request);

        RolProyecto rolProyecto = obtenerRolProyectoPorId(id);

        rolProyectoRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(rolProyectoExistente -> {
                    if (!rolProyectoExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otro rol de proyecto con ese nombre"
                        );
                    }
                });

        RolProyectoMapper.updateEntity(rolProyecto, request);

        RolProyecto rolProyectoActualizado = rolProyectoRepository.save(rolProyecto);
        return RolProyectoMapper.toResponse(rolProyectoActualizado);
    }

    /**
     * Cambia el estado del rol de proyecto a ACTIVO.
     */
    public RolProyectoResponse activar(Long id) {
        RolProyecto rolProyecto = obtenerRolProyectoPorId(id);
        rolProyecto.activar();

        return RolProyectoMapper.toResponse(
                rolProyectoRepository.save(rolProyecto)
        );
    }

    /**
     * Cambia el estado del rol de proyecto a INACTIVO.
     *
     * No se elimina físicamente porque puede estar relacionado
     * con integrantes de proyectos o registros históricos.
     */
    public RolProyectoResponse inactivar(Long id) {
        RolProyecto rolProyecto = obtenerRolProyectoPorId(id);
        rolProyecto.inactivar();

        return RolProyectoMapper.toResponse(
                rolProyectoRepository.save(rolProyecto)
        );
    }

    /**
     * Cambia el estado del rol de proyecto a ARCHIVADO.
     *
     * Se usa cuando el rol ya no forma parte del uso normal
     * del sistema, pero debe conservarse por historial.
     */
    public RolProyectoResponse archivar(Long id) {
        RolProyecto rolProyecto = obtenerRolProyectoPorId(id);
        rolProyecto.archivar();

        return RolProyectoMapper.toResponse(
                rolProyectoRepository.save(rolProyecto)
        );
    }

    /**
     * Busca la entidad RolProyecto por id.
     *
     * Si no existe, lanza un error 404.
     */
    private RolProyecto obtenerRolProyectoPorId(Long id) {
        return rolProyectoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el rol de proyecto con id: " + id
                ));
    }

    /**
     * Limpia espacios innecesarios antes de validar o guardar.
     *
     * Ejemplo:
     * "  Líder  " se convierte en "Líder".
     */
    private void normalizarRequest(RolProyectoRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim());
        }

        if (request.getDescripcion() != null) {
            request.setDescripcion(request.getDescripcion().trim());
        }
    }
}