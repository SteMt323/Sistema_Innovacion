package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.RolRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.RolResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.Rol;
import ni.edu.uam.innovacion.modules.catalog.mapper.RolMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.RolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio del catálogo de roles.
 *
 * Aquí se valida que no existan roles duplicados y se controla
 * el cambio de estado de cada rol.
 */
@Service
@Transactional
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<RolResponse> listarTodos() {
        return rolRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(RolMapper::toResponse)
                .toList();
    }

    /**
     * Lista solamente los roles activos.
     *
     * Esto sirve para formularios donde solo deben aparecer
     * roles disponibles para ser asignados.
     */
    @Transactional(readOnly = true)
    public List<RolResponse> listarActivos() {
        return rolRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(RolMapper::toResponse)
                .toList();
    }

    /**
     * Busca un rol por su id.
     */
    @Transactional(readOnly = true)
    public RolResponse buscarPorId(Long id) {
        Rol rol = obtenerRolPorId(id);
        return RolMapper.toResponse(rol);
    }

    /**
     * Crea un nuevo rol.
     *
     * Antes de guardar, valida que no exista otro rol
     * con el mismo nombre.
     */
    public RolResponse crear(RolRequest request) {
        normalizarRequest(request);

        if (rolRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un rol con ese nombre"
            );
        }

        Rol rol = RolMapper.toEntity(request);
        Rol rolGuardado = rolRepository.save(rol);

        return RolMapper.toResponse(rolGuardado);
    }

    /**
     * Actualiza un rol existente.
     *
     * También valida que el nuevo nombre no esté usado por otro rol.
     */
    public RolResponse actualizar(Long id, RolRequest request) {
        normalizarRequest(request);

        Rol rol = obtenerRolPorId(id);

        rolRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(rolExistente -> {
                    if (!rolExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otro rol con ese nombre"
                        );
                    }
                });

        RolMapper.updateEntity(rol, request);

        Rol rolActualizado = rolRepository.save(rol);
        return RolMapper.toResponse(rolActualizado);
    }

    /**
     * Cambia el estado del rol a ACTIVO.
     */
    public RolResponse activar(Long id) {
        Rol rol = obtenerRolPorId(id);
        rol.activar();

        return RolMapper.toResponse(rolRepository.save(rol));
    }

    /**
     * Cambia el estado del rol a INACTIVO.
     *
     * No se elimina físicamente porque puede estar relacionado
     * con usuarios existentes o históricos.
     */
    public RolResponse inactivar(Long id) {
        Rol rol = obtenerRolPorId(id);
        rol.inactivar();

        return RolMapper.toResponse(rolRepository.save(rol));
    }

    /**
     * Cambia el estado del rol a ARCHIVADO.
     *
     * Se usa cuando el rol ya no forma parte del uso normal
     * del sistema, pero se quiere conservar por historial.
     */
    public RolResponse archivar(Long id) {
        Rol rol = obtenerRolPorId(id);
        rol.archivar();

        return RolMapper.toResponse(rolRepository.save(rol));
    }

    /**
     * Busca la entidad Rol por id.
     *
     * Si no existe, lanza un error 404.
     */
    private Rol obtenerRolPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el rol con id: " + id
                ));
    }

    /**
     * Limpia espacios innecesarios antes de validar o guardar.
     *
     * Ejemplo:
     * "  ADMINISTRADOR  " se convierte en "ADMINISTRADOR".
     */
    private void normalizarRequest(RolRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim());
        }

        if (request.getDescripcion() != null) {
            request.setDescripcion(request.getDescripcion().trim());
        }
    }
}