package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.FacultadRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.FacultadResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.Facultad;
import ni.edu.uam.innovacion.modules.catalog.mapper.FacultadMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.FacultadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 Servicio encargado de la lógica de negocio del catálogo de facultades.
 */
@Service
@Transactional
public class FacultadService {

    private final FacultadRepository facultadRepository;

    /**
     * Constructor para inyectar el repository.
     *
     * Spring se encarga de enviar automáticamente una instancia
     * de FacultadRepository cuando crea este servicio.
     */
    public FacultadService(FacultadRepository facultadRepository) {
        this.facultadRepository = facultadRepository;
    }

    /**
     * Lista todas las facultades registradas
     */
    @Transactional(readOnly = true)
    public List<FacultadResponse> listarTodas() {
        return facultadRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(FacultadMapper::toResponse)
                .toList();
    }

    /**
     * Lista solamente las facultades activas.
     */
    @Transactional(readOnly = true)
    public List<FacultadResponse> listarActivas() {
        return facultadRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(FacultadMapper::toResponse)
                .toList();
    }

    /**
     * Busca una facultad por su id.
     */
    @Transactional(readOnly = true)
    public FacultadResponse buscarPorId(Long id) {
        Facultad facultad = obtenerFacultadPorId(id);
        return FacultadMapper.toResponse(facultad);
    }

    /**
     * Crea una nueva facultad.
     */
    public FacultadResponse crear(FacultadRequest request) {
        normalizarRequest(request);

        if (facultadRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una facultad con ese nombre"
            );
        }

        if (facultadRepository.existsByCodigoIgnoreCase(request.getCodigo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una facultad con ese código"
            );
        }

        Facultad facultad = FacultadMapper.toEntity(request);
        Facultad facultadGuardada = facultadRepository.save(facultad);

        return FacultadMapper.toResponse(facultadGuardada);
    }

    /**
     * Actualiza una facultad existente.
     */
    public FacultadResponse actualizar(Long id, FacultadRequest request) {
        normalizarRequest(request);

        Facultad facultad = obtenerFacultadPorId(id);

        facultadRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(facultadExistente -> {
                    if (!facultadExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otra facultad con ese nombre"
                        );
                    }
                });

        facultadRepository.findByCodigoIgnoreCase(request.getCodigo())
                .ifPresent(facultadExistente -> {
                    if (!facultadExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otra facultad con ese código"
                        );
                    }
                });

        FacultadMapper.updateEntity(facultad, request);

        Facultad facultadActualizada = facultadRepository.save(facultad);
        return FacultadMapper.toResponse(facultadActualizada);
    }

    /**
     * Cambia el estado de la facultad a ACTIVO.
     */
    public FacultadResponse activar(Long id) {
        Facultad facultad = obtenerFacultadPorId(id);
        facultad.activar();

        return FacultadMapper.toResponse(facultadRepository.save(facultad));
    }

    /**
     * Cambia el estado de la facultad a INACTIVO.
     */
    public FacultadResponse inactivar(Long id) {
        Facultad facultad = obtenerFacultadPorId(id);
        facultad.inactivar();

        return FacultadMapper.toResponse(facultadRepository.save(facultad));
    }

    /**
     * Cambia el estado de la facultad a ARCHIVADO.
     */
    public FacultadResponse archivar(Long id) {
        Facultad facultad = obtenerFacultadPorId(id);
        facultad.archivar();

        return FacultadMapper.toResponse(facultadRepository.save(facultad));
    }

    /**
     * Método privado reutilizable para buscar una facultad por id.
     */
    private Facultad obtenerFacultadPorId(Long id) {
        return facultadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la facultad con id: " + id
                ));
    }

    /**
     * Limpia los datos recibidos antes de validar o guardar..
     */
    private void normalizarRequest(FacultadRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim());
        }

        if (request.getDescripcion() != null) {
            request.setDescripcion(request.getDescripcion().trim());
        }

        if (request.getCodigo() != null) {
            request.setCodigo(request.getCodigo().trim().toUpperCase());
        }
    }
}