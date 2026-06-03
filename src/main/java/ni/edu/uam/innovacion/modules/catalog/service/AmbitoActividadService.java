package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.AmbitoActividadRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.AmbitoActividadResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.mapper.AmbitoActividadMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.AmbitoActividadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio del catálogo de ámbitos de actividad.
 *
 * Este catálogo permite clasificar las actividades según su origen o contexto general.
 *

 */
@Service
@Transactional
public class AmbitoActividadService {

    private final AmbitoActividadRepository ambitoActividadRepository;

    /**
     * Constructor para inyectar el repository.
     */
    public AmbitoActividadService(AmbitoActividadRepository ambitoActividadRepository) {
        this.ambitoActividadRepository = ambitoActividadRepository;
    }

    @Transactional(readOnly = true)
    public List<AmbitoActividadResponse> listarTodos() {
        return ambitoActividadRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(AmbitoActividadMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AmbitoActividadResponse> listarActivos() {
        return ambitoActividadRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(AmbitoActividadMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AmbitoActividadResponse> listarQueRequierenCategoria() {
        return ambitoActividadRepository.findByEstadoAndRequiereCategoriaOrderByNombreAsc(
                        EstadoRegistro.ACTIVO,
                        Boolean.TRUE
                )
                .stream()
                .map(AmbitoActividadMapper::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<AmbitoActividadResponse> listarQueNoRequierenCategoria() {
        return ambitoActividadRepository.findByEstadoAndRequiereCategoriaOrderByNombreAsc(
                        EstadoRegistro.ACTIVO,
                        Boolean.FALSE
                )
                .stream()
                .map(AmbitoActividadMapper::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public AmbitoActividadResponse buscarPorId(Long id) {
        AmbitoActividad ambitoActividad = obtenerAmbitoActividadPorId(id);
        return AmbitoActividadMapper.toResponse(ambitoActividad);
    }

    /**
     * Crea un nuevo ámbito de actividad.
     *
     * Antes de guardar:
     * - normaliza los datos recibidos.
     * - valida que no exista otro ámbito con el mismo nombre.
     *
     * Esto evita duplicados como:
     * - DIEM
     * - diem
     * - Diem
     */
    public AmbitoActividadResponse crear(AmbitoActividadRequest request) {
        normalizarRequest(request);

        if (ambitoActividadRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un ámbito de actividad con ese nombre"
            );
        }

        AmbitoActividad ambitoActividad = AmbitoActividadMapper.toEntity(request);
        AmbitoActividad ambitoGuardado = ambitoActividadRepository.save(ambitoActividad);

        return AmbitoActividadMapper.toResponse(ambitoGuardado);
    }


    public AmbitoActividadResponse actualizar(Long id, AmbitoActividadRequest request) {
        normalizarRequest(request);

        AmbitoActividad ambitoActividad = obtenerAmbitoActividadPorId(id);

        ambitoActividadRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(ambitoExistente -> {
                    if (!ambitoExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otro ámbito de actividad con ese nombre"
                        );
                    }
                });

        AmbitoActividadMapper.updateEntity(ambitoActividad, request);

        AmbitoActividad ambitoActualizado = ambitoActividadRepository.save(ambitoActividad);
        return AmbitoActividadMapper.toResponse(ambitoActualizado);
    }

    /**
     * Activa un ámbito de actividad.
     *
     * No se crea un nuevo registro, solo se cambia su estado a ACTIVO.
     */
    public AmbitoActividadResponse activar(Long id) {
        AmbitoActividad ambitoActividad = obtenerAmbitoActividadPorId(id);
        ambitoActividad.activar();

        return AmbitoActividadMapper.toResponse(
                ambitoActividadRepository.save(ambitoActividad)
        );
    }


    public AmbitoActividadResponse inactivar(Long id) {
        AmbitoActividad ambitoActividad = obtenerAmbitoActividadPorId(id);
        ambitoActividad.inactivar();

        return AmbitoActividadMapper.toResponse(
                ambitoActividadRepository.save(ambitoActividad)
        );
    }


    public AmbitoActividadResponse archivar(Long id) {
        AmbitoActividad ambitoActividad = obtenerAmbitoActividadPorId(id);
        ambitoActividad.archivar();

        return AmbitoActividadMapper.toResponse(
                ambitoActividadRepository.save(ambitoActividad)
        );
    }

    private AmbitoActividad obtenerAmbitoActividadPorId(Long id) {
        return ambitoActividadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el ámbito de actividad con id: " + id
                ));
    }

    /**
     * Normaliza los datos recibidos desde el request.
     *
     * Esto ayuda a mantener datos limpios en la base.
     *
     * Ejemplo:
     * Si el administrador escribe " diem ", se guarda como "DIEM".
     *
     * Se pasa el nombre a mayúsculas porque los ámbitos funcionan mejor
     * como valores administrativos claros:
     * - DIEM
     * - EXTERNA
     */
    private void normalizarRequest(AmbitoActividadRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim().toUpperCase());
        }

        if (request.getDescripcion() != null) {
            request.setDescripcion(request.getDescripcion().trim());
        }
    }
}