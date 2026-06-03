package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.CategoriaFuenteProyecto;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;
import ni.edu.uam.innovacion.modules.catalog.mapper.FuenteProyectoMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.FuenteProyectoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio del catálogo FuenteProyecto.
 *
 * Este catálogo permite registrar de dónde surge un proyecto dentro
 * del sistema de Innovación y Emprendimiento.
 *
 * Ejemplos:
 * - Programa PIA
 * - Hackathon Nicaragua
 * - Rally Nacional de Innovación
 * - Actividad externa
 * - Otro
 *
 * Este service aplica reglas básicas:
 *
 * - evitar fuentes de proyecto duplicadas.
 * - listar fuentes activas.
 * - filtrar fuentes por categoría.
 * - conservar historial usando estados: ACTIVO, INACTIVO y ARCHIVADO.
 */
@Service
@Transactional
public class FuenteProyectoService {

    private final FuenteProyectoRepository fuenteProyectoRepository;

    /**
     * Constructor para inyectar el repository.
     */
    public FuenteProyectoService(FuenteProyectoRepository fuenteProyectoRepository) {
        this.fuenteProyectoRepository = fuenteProyectoRepository;
    }

    /**
     * Lista todas las fuentes de proyecto registradas.
     */
    @Transactional(readOnly = true)
    public List<FuenteProyectoResponse> listarTodas() {
        return fuenteProyectoRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(FuenteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista únicamente las fuentes de proyecto activas.
     */
    @Transactional(readOnly = true)
    public List<FuenteProyectoResponse> listarActivas() {
        return fuenteProyectoRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(FuenteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista todas las fuentes de proyecto de una categoría específica.
     */
    @Transactional(readOnly = true)
    public List<FuenteProyectoResponse> listarPorCategoria(
            CategoriaFuenteProyecto categoria
    ) {
        validarCategoria(categoria);

        return fuenteProyectoRepository.findByCategoriaOrderByNombreAsc(categoria)
                .stream()
                .map(FuenteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Lista únicamente las fuentes activas de una categoría específica.
     */
    @Transactional(readOnly = true)
    public List<FuenteProyectoResponse> listarActivasPorCategoria(
            CategoriaFuenteProyecto categoria
    ) {
        validarCategoria(categoria);

        return fuenteProyectoRepository.findByCategoriaAndEstadoOrderByNombreAsc(
                        categoria,
                        EstadoRegistro.ACTIVO
                )
                .stream()
                .map(FuenteProyectoMapper::toResponse)
                .toList();
    }

    /**
     * Busca una fuente de proyecto por su id.
     */
    @Transactional(readOnly = true)
    public FuenteProyectoResponse buscarPorId(Long id) {
        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoPorId(id);
        return FuenteProyectoMapper.toResponse(fuenteProyecto);
    }

    /**
     * Crea una nueva fuente de proyecto.
     */
    public FuenteProyectoResponse crear(FuenteProyectoRequest request) {
        normalizarRequest(request);

        if (fuenteProyectoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una fuente de proyecto con ese nombre"
            );
        }

        FuenteProyecto fuenteProyecto = FuenteProyectoMapper.toEntity(request);
        FuenteProyecto fuenteGuardada = fuenteProyectoRepository.save(fuenteProyecto);

        return FuenteProyectoMapper.toResponse(fuenteGuardada);
    }

    /**
     * Actualiza una fuente de proyecto existente.
     */
    public FuenteProyectoResponse actualizar(Long id, FuenteProyectoRequest request) {
        normalizarRequest(request);

        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoPorId(id);

        fuenteProyectoRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(fuenteExistente -> {
                    if (!fuenteExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otra fuente de proyecto con ese nombre"
                        );
                    }
                });

        FuenteProyectoMapper.updateEntity(fuenteProyecto, request);

        FuenteProyecto fuenteActualizada = fuenteProyectoRepository.save(fuenteProyecto);
        return FuenteProyectoMapper.toResponse(fuenteActualizada);
    }

    /**
     * Activa una fuente de proyecto.
     */
    public FuenteProyectoResponse activar(Long id) {
        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoPorId(id);
        fuenteProyecto.activar();

        return FuenteProyectoMapper.toResponse(
                fuenteProyectoRepository.save(fuenteProyecto)
        );
    }

    /**
     * Inactiva una fuente de proyecto.
     */
    public FuenteProyectoResponse inactivar(Long id) {
        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoPorId(id);
        fuenteProyecto.inactivar();

        return FuenteProyectoMapper.toResponse(
                fuenteProyectoRepository.save(fuenteProyecto)
        );
    }

    /**
     * Archiva una fuente de proyecto.
     */
    public FuenteProyectoResponse archivar(Long id) {
        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoPorId(id);
        fuenteProyecto.archivar();

        return FuenteProyectoMapper.toResponse(
                fuenteProyectoRepository.save(fuenteProyecto)
        );
    }

    /**
     * Busca una fuente de proyecto por id.
     */
    private FuenteProyecto obtenerFuenteProyectoPorId(Long id) {
        return fuenteProyectoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la fuente de proyecto con id: " + id
                ));
    }

    /**
     * Valida que la categoría no sea nula.
     */
    private void validarCategoria(CategoriaFuenteProyecto categoria) {
        if (categoria == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría de la fuente del proyecto es obligatoria"
            );
        }
    }

    /**
     * Normaliza los datos recibidos desde el request.
     */
    private void normalizarRequest(FuenteProyectoRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim());
        }

        if (request.getDescripcion() != null) {
            request.setDescripcion(request.getDescripcion().trim());
        }
    }
}