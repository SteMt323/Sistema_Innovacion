package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaFuenteProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaFuenteProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaFuenteProyecto;
import ni.edu.uam.innovacion.modules.catalog.mapper.CategoriaFuenteProyectoMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.CategoriaFuenteProyectoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

/**
 * Servicio encargado de la lógica de negocio del catálogo
 * de categorías de fuente de proyecto.
 *
 * Este catálogo permite clasificar el origen general de los proyectos,
 * por ejemplo:
 * - PROGRAMA_PIA
 * - CONCURSO
 * - ACTIVIDAD_INNOVACION
 * - EXTERNO
 * - OTRO
 */
@Service
@Transactional
public class CategoriaFuenteProyectoService {

    private final CategoriaFuenteProyectoRepository categoriaFuenteProyectoRepository;

    /**
     * Constructor para inyectar el repository.
     */
    public CategoriaFuenteProyectoService(
            CategoriaFuenteProyectoRepository categoriaFuenteProyectoRepository
    ) {
        this.categoriaFuenteProyectoRepository = categoriaFuenteProyectoRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaFuenteProyectoResponse> listarTodos() {
        return categoriaFuenteProyectoRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(CategoriaFuenteProyectoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaFuenteProyectoResponse> listarActivos() {
        return categoriaFuenteProyectoRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(CategoriaFuenteProyectoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaFuenteProyectoResponse buscarPorId(Long id) {
        CategoriaFuenteProyecto categoriaFuenteProyecto = obtenerCategoriaFuenteProyectoPorId(id);
        return CategoriaFuenteProyectoMapper.toResponse(categoriaFuenteProyecto);
    }

    /**
     * Crea una nueva categoría de fuente de proyecto.
     *
     * Antes de guardar:
     * - normaliza el nombre y la descripción.
     * - valida que no exista otra categoría con el mismo nombre.
     *
     * Esto evita duplicados como:
     * - concurso
     * - Concurso
     * - CONCURSO
     */
    public CategoriaFuenteProyectoResponse crear(CategoriaFuenteProyectoRequest request) {
        normalizarRequest(request);

        if (categoriaFuenteProyectoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una categoría de fuente de proyecto con ese nombre"
            );
        }

        CategoriaFuenteProyecto categoriaFuenteProyecto =
                CategoriaFuenteProyectoMapper.toEntity(request);

        CategoriaFuenteProyecto categoriaGuardada =
                categoriaFuenteProyectoRepository.save(categoriaFuenteProyecto);

        return CategoriaFuenteProyectoMapper.toResponse(categoriaGuardada);
    }

    /**
     * Actualiza una categoría de fuente de proyecto existente.
     *
     * Valida que el nuevo nombre no pertenezca a otro registro.
     */
    public CategoriaFuenteProyectoResponse actualizar(
            Long id,
            CategoriaFuenteProyectoRequest request
    ) {
        normalizarRequest(request);

        CategoriaFuenteProyecto categoriaFuenteProyecto =
                obtenerCategoriaFuenteProyectoPorId(id);

        categoriaFuenteProyectoRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(categoriaExistente -> {
                    if (!categoriaExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otra categoría de fuente de proyecto con ese nombre"
                        );
                    }
                });

        CategoriaFuenteProyectoMapper.updateEntity(categoriaFuenteProyecto, request);

        CategoriaFuenteProyecto categoriaActualizada =
                categoriaFuenteProyectoRepository.save(categoriaFuenteProyecto);

        return CategoriaFuenteProyectoMapper.toResponse(categoriaActualizada);
    }

    /**
     * Activa una categoría de fuente de proyecto.
     */
    public CategoriaFuenteProyectoResponse activar(Long id) {
        CategoriaFuenteProyecto categoriaFuenteProyecto =
                obtenerCategoriaFuenteProyectoPorId(id);

        categoriaFuenteProyecto.activar();

        return CategoriaFuenteProyectoMapper.toResponse(
                categoriaFuenteProyectoRepository.save(categoriaFuenteProyecto)
        );
    }

    /**
     * Inactiva una categoría de fuente de proyecto.
     */
    public CategoriaFuenteProyectoResponse inactivar(Long id) {
        CategoriaFuenteProyecto categoriaFuenteProyecto =
                obtenerCategoriaFuenteProyectoPorId(id);

        categoriaFuenteProyecto.inactivar();

        return CategoriaFuenteProyectoMapper.toResponse(
                categoriaFuenteProyectoRepository.save(categoriaFuenteProyecto)
        );
    }

    /**
     * Archiva una categoría de fuente de proyecto.
     */
    public CategoriaFuenteProyectoResponse archivar(Long id) {
        CategoriaFuenteProyecto categoriaFuenteProyecto =
                obtenerCategoriaFuenteProyectoPorId(id);

        categoriaFuenteProyecto.archivar();

        return CategoriaFuenteProyectoMapper.toResponse(
                categoriaFuenteProyectoRepository.save(categoriaFuenteProyecto)
        );
    }

    /**
     * Busca una categoría de fuente de proyecto por id.
     *
     * Si no existe, retorna error 404.
     */
    private CategoriaFuenteProyecto obtenerCategoriaFuenteProyectoPorId(Long id) {
        return categoriaFuenteProyectoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la categoría de fuente de proyecto con id: " + id
                ));
    }

    /**
     * Normaliza los datos recibidos desde el request.
     *
     * El nombre se guarda en mayúsculas para mantener valores administrativos
     * consistentes, como:
     * - PROGRAMA_PIA
     * - CONCURSO
     * - ACTIVIDAD_INNOVACION
     */
    private void normalizarRequest(CategoriaFuenteProyectoRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(
                    request.getNombre()
                            .trim()
                            .toUpperCase(Locale.ROOT)
            );
        }

        if (request.getDescripcion() != null) {
            request.setDescripcion(request.getDescripcion().trim());
        }
    }
}