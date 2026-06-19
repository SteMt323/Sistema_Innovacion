package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaFuenteProyecto;
import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;
import ni.edu.uam.innovacion.modules.catalog.mapper.FuenteProyectoMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.CategoriaFuenteProyectoRepository;
import ni.edu.uam.innovacion.modules.catalog.repository.FuenteProyectoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

/**
 * Servicio encargado de la lógica de negocio del catálogo de fuentes de proyecto.
 *
 * Este catálogo permite registrar el origen específico desde donde nace
 * o se vincula un proyecto.
 */
@Service
@Transactional
public class FuenteProyectoService {

    private final FuenteProyectoRepository fuenteProyectoRepository;
    private final CategoriaFuenteProyectoRepository categoriaFuenteProyectoRepository;

    /**
     * Constructor para inyectar los repositories.
     */
    public FuenteProyectoService(
            FuenteProyectoRepository fuenteProyectoRepository,
            CategoriaFuenteProyectoRepository categoriaFuenteProyectoRepository
    ) {
        this.fuenteProyectoRepository = fuenteProyectoRepository;
        this.categoriaFuenteProyectoRepository = categoriaFuenteProyectoRepository;
    }

    @Transactional(readOnly = true)
    public List<FuenteProyectoResponse> listarTodas() {
        return fuenteProyectoRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(FuenteProyectoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FuenteProyectoResponse> listarActivas() {
        return fuenteProyectoRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(FuenteProyectoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FuenteProyectoResponse> listarPorCategoria(Long idCategoriaFuenteProyecto) {
        obtenerCategoriaFuenteProyectoPorId(idCategoriaFuenteProyecto);

        return fuenteProyectoRepository
                .findByCategoriaFuenteProyecto_IdOrderByNombreAsc(idCategoriaFuenteProyecto)
                .stream()
                .map(FuenteProyectoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FuenteProyectoResponse> listarActivasPorCategoria(
            Long idCategoriaFuenteProyecto
    ) {
        obtenerCategoriaFuenteProyectoPorId(idCategoriaFuenteProyecto);

        return fuenteProyectoRepository
                .findByEstadoAndCategoriaFuenteProyecto_IdOrderByNombreAsc(
                        EstadoRegistro.ACTIVO,
                        idCategoriaFuenteProyecto
                )
                .stream()
                .map(FuenteProyectoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FuenteProyectoResponse buscarPorId(Long id) {
        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoPorId(id);
        return FuenteProyectoMapper.toResponse(fuenteProyecto);
    }

    /**
     * Crea una nueva fuente de proyecto.
     *
     * Antes de guardar:
     * - normaliza los datos recibidos.
     * - valida que no exista otra fuente con el mismo nombre.
     * - valida que la categoría indicada exista y esté activa.
     */
    public FuenteProyectoResponse crear(FuenteProyectoRequest request) {
        normalizarRequest(request);

        if (fuenteProyectoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una fuente de proyecto con ese nombre"
            );
        }

        CategoriaFuenteProyecto categoriaFuenteProyecto =
                obtenerCategoriaFuenteProyectoPorId(request.getIdCategoriaFuenteProyecto());

        validarCategoriaActiva(categoriaFuenteProyecto);

        FuenteProyecto fuenteProyecto = FuenteProyectoMapper.toEntity(
                request,
                categoriaFuenteProyecto
        );

        FuenteProyecto fuenteGuardada = fuenteProyectoRepository.save(fuenteProyecto);

        return FuenteProyectoMapper.toResponse(fuenteGuardada);
    }

    /**
     * Actualiza una fuente de proyecto existente.
     *
     * Valida que el nuevo nombre no pertenezca a otra fuente.
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

        CategoriaFuenteProyecto categoriaFuenteProyecto =
                obtenerCategoriaFuenteProyectoPorId(request.getIdCategoriaFuenteProyecto());

        validarCategoriaActiva(categoriaFuenteProyecto);

        FuenteProyectoMapper.updateEntity(
                fuenteProyecto,
                request,
                categoriaFuenteProyecto
        );

        FuenteProyecto fuenteActualizada = fuenteProyectoRepository.save(fuenteProyecto);

        return FuenteProyectoMapper.toResponse(fuenteActualizada);
    }

    /**
     * Activa una fuente de proyecto.
     *
     * Para activar una fuente, su categoría también debe estar activa.
     */
    public FuenteProyectoResponse activar(Long id) {
        FuenteProyecto fuenteProyecto = obtenerFuenteProyectoPorId(id);

        validarCategoriaActiva(fuenteProyecto.getCategoriaFuenteProyecto());

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
     * Obtiene una fuente de proyecto por id.
     *
     * Si no existe, retorna error 404.
     */
    private FuenteProyecto obtenerFuenteProyectoPorId(Long id) {
        return fuenteProyectoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la fuente de proyecto con id: " + id
                ));
    }

    /**
     * Obtiene una categoría de fuente de proyecto por id.
     *
     * Si no existe, retorna error 404.
     */
    private CategoriaFuenteProyecto obtenerCategoriaFuenteProyectoPorId(
            Long idCategoriaFuenteProyecto
    ) {
        return categoriaFuenteProyectoRepository.findById(idCategoriaFuenteProyecto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la categoría de fuente de proyecto con id: "
                                + idCategoriaFuenteProyecto
                ));
    }

    /**
     * Valida que la categoría de fuente de proyecto esté activa.
     */
    private void validarCategoriaActiva(CategoriaFuenteProyecto categoriaFuenteProyecto) {
        if (categoriaFuenteProyecto == null || !categoriaFuenteProyecto.estaActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría de fuente de proyecto no está activa"
            );
        }
    }

    /**
     * Normaliza los datos recibidos desde el request.
     */
    private void normalizarRequest(FuenteProyectoRequest request) {
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