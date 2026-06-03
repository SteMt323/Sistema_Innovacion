package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.RolParticipacionRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.RolParticipacionResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.RolParticipacion;
import ni.edu.uam.innovacion.modules.catalog.mapper.RolParticipacionMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.RolParticipacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Transactional
public class RolParticipacionService {

    private final RolParticipacionRepository rolParticipacionRepository;

    /**
     * Constructor para inyectar el repository.
     */
    public RolParticipacionService(RolParticipacionRepository rolParticipacionRepository) {
        this.rolParticipacionRepository = rolParticipacionRepository;
    }

    /**
     * Lista todos los roles de participación registrados.
     */
    @Transactional(readOnly = true)
    public List<RolParticipacionResponse> listarTodos() {
        return rolParticipacionRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(RolParticipacionMapper::toResponse)
                .toList();
    }

    /**
     * Lista únicamente los roles de participación activos.
     */
    @Transactional(readOnly = true)
    public List<RolParticipacionResponse> listarActivos() {
        return rolParticipacionRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(RolParticipacionMapper::toResponse)
                .toList();
    }

    /**
     * Busca un rol de participación por su id.
     */
    @Transactional(readOnly = true)
    public RolParticipacionResponse buscarPorId(Long id) {
        RolParticipacion rolParticipacion = obtenerRolParticipacionPorId(id);
        return RolParticipacionMapper.toResponse(rolParticipacion);
    }

    /**
     * Crea un nuevo rol de participación.
     */
    public RolParticipacionResponse crear(RolParticipacionRequest request) {
        normalizarRequest(request);

        if (rolParticipacionRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un rol de participación con ese nombre"
            );
        }

        RolParticipacion rolParticipacion = RolParticipacionMapper.toEntity(request);
        RolParticipacion rolGuardado = rolParticipacionRepository.save(rolParticipacion);

        return RolParticipacionMapper.toResponse(rolGuardado);
    }

    /**
     * Actualiza un rol de participación existente.
     */
    public RolParticipacionResponse actualizar(Long id, RolParticipacionRequest request) {
        normalizarRequest(request);

        RolParticipacion rolParticipacion = obtenerRolParticipacionPorId(id);

        rolParticipacionRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(rolExistente -> {
                    if (!rolExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otro rol de participación con ese nombre"
                        );
                    }
                });

        RolParticipacionMapper.updateEntity(rolParticipacion, request);

        RolParticipacion rolActualizado = rolParticipacionRepository.save(rolParticipacion);
        return RolParticipacionMapper.toResponse(rolActualizado);
    }

    /**
     * Activa un rol de participación.
     */
    public RolParticipacionResponse activar(Long id) {
        RolParticipacion rolParticipacion = obtenerRolParticipacionPorId(id);
        rolParticipacion.activar();

        return RolParticipacionMapper.toResponse(
                rolParticipacionRepository.save(rolParticipacion)
        );
    }

    /**
     * Inactiva un rol de participación.
     */
    public RolParticipacionResponse inactivar(Long id) {
        RolParticipacion rolParticipacion = obtenerRolParticipacionPorId(id);
        rolParticipacion.inactivar();

        return RolParticipacionMapper.toResponse(
                rolParticipacionRepository.save(rolParticipacion)
        );
    }

    /**
     * Archiva un rol de participación.
     */
    public RolParticipacionResponse archivar(Long id) {
        RolParticipacion rolParticipacion = obtenerRolParticipacionPorId(id);
        rolParticipacion.archivar();

        return RolParticipacionMapper.toResponse(
                rolParticipacionRepository.save(rolParticipacion)
        );
    }

    /**
     * Busca un rol de participación por id.
     */
    private RolParticipacion obtenerRolParticipacionPorId(Long id) {
        return rolParticipacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el rol de participación con id: " + id
                ));
    }

    /**
     * Normaliza los datos recibidos desde el request.
     */
    private void normalizarRequest(RolParticipacionRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim());
        }

        if (request.getDescripcion() != null) {
            request.setDescripcion(request.getDescripcion().trim());
        }
    }
}