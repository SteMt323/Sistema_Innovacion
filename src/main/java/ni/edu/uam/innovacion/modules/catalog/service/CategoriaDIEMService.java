package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaDIEMRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaDIEMResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaDIEM;
import ni.edu.uam.innovacion.modules.catalog.mapper.CategoriaDIEMMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.AmbitoActividadRepository;
import ni.edu.uam.innovacion.modules.catalog.repository.CategoriaDIEMRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio del catálogo de categorías DIEM.
 *
 * Este catálogo permite clasificar las actividades internas de la Dirección
 * de Innovación y Emprendimiento.

 */
@Service
@Transactional
public class CategoriaDIEMService {

    private final CategoriaDIEMRepository categoriaDIEMRepository;
    private final AmbitoActividadRepository ambitoActividadRepository;

    public CategoriaDIEMService(
            CategoriaDIEMRepository categoriaDIEMRepository,
            AmbitoActividadRepository ambitoActividadRepository
    ) {
        this.categoriaDIEMRepository = categoriaDIEMRepository;
        this.ambitoActividadRepository = ambitoActividadRepository;
    }

    /**
     * Lista todas las categorías DIEM.

     */
    @Transactional(readOnly = true)
    public List<CategoriaDIEMResponse> listarTodas() {
        return categoriaDIEMRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(CategoriaDIEMMapper::toResponse)
                .toList();
    }

    /**
     * Lista únicamente las categorías DIEM activas.

     */
    @Transactional(readOnly = true)
    public List<CategoriaDIEMResponse> listarActivas() {
        return categoriaDIEMRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(CategoriaDIEMMapper::toResponse)
                .toList();
    }

    /**
     * Lista todas las categorías asociadas a un ámbito específico.
     */
    @Transactional(readOnly = true)
    public List<CategoriaDIEMResponse> listarPorAmbito(Long idAmbitoActividad) {
        verificarAmbitoActividadExiste(idAmbitoActividad);

        return categoriaDIEMRepository.findByAmbitoActividad_IdOrderByNombreAsc(idAmbitoActividad)
                .stream()
                .map(CategoriaDIEMMapper::toResponse)
                .toList();
    }

    /**
     * Lista las categorías activas asociadas a un ámbito específico.
     */
    @Transactional(readOnly = true)
    public List<CategoriaDIEMResponse> listarActivasPorAmbito(Long idAmbitoActividad) {
        verificarAmbitoActividadExiste(idAmbitoActividad);

        return categoriaDIEMRepository.findByAmbitoActividad_IdAndEstadoOrderByNombreAsc(
                        idAmbitoActividad,
                        EstadoRegistro.ACTIVO
                )
                .stream()
                .map(CategoriaDIEMMapper::toResponse)
                .toList();
    }

    /**
     * Lista las categorías activas del ámbito DIEM.
     */
    @Transactional(readOnly = true)
    public List<CategoriaDIEMResponse> listarActivasDelAmbitoDIEM() {
        return categoriaDIEMRepository
                .findByAmbitoActividad_NombreIgnoreCaseAndEstadoOrderByNombreAsc(
                        "DIEM",
                        EstadoRegistro.ACTIVO
                )
                .stream()
                .map(CategoriaDIEMMapper::toResponse)
                .toList();
    }

    /**
     * Busca una categoría DIEM por su id.
     */
    @Transactional(readOnly = true)
    public CategoriaDIEMResponse buscarPorId(Long id) {
        CategoriaDIEM categoriaDIEM = obtenerCategoriaDIEMPorId(id);
        return CategoriaDIEMMapper.toResponse(categoriaDIEM);
    }

    /**
     * Crea una nueva categoría DIEM.
     */
    public CategoriaDIEMResponse crear(CategoriaDIEMRequest request) {
        normalizarRequest(request);

        AmbitoActividad ambitoActividad = obtenerAmbitoActividadActivoPorId(
                request.getIdAmbitoActividad()
        );

        validarAmbitoRequiereCategoria(ambitoActividad);

        if (categoriaDIEMRepository.existsByNombreIgnoreCaseAndAmbitoActividad_Id(
                request.getNombre(),
                request.getIdAmbitoActividad()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una categoría DIEM con ese nombre en el ámbito seleccionado"
            );
        }

        CategoriaDIEM categoriaDIEM = CategoriaDIEMMapper.toEntity(request, ambitoActividad);
        CategoriaDIEM categoriaGuardada = categoriaDIEMRepository.save(categoriaDIEM);

        return CategoriaDIEMMapper.toResponse(categoriaGuardada);
    }

    /**
     * Actualiza una categoría DIEM existente.
     */
    public CategoriaDIEMResponse actualizar(Long id, CategoriaDIEMRequest request) {
        normalizarRequest(request);

        CategoriaDIEM categoriaDIEM = obtenerCategoriaDIEMPorId(id);

        AmbitoActividad ambitoActividad = obtenerAmbitoActividadActivoPorId(
                request.getIdAmbitoActividad()
        );

        validarAmbitoRequiereCategoria(ambitoActividad);

        categoriaDIEMRepository.findByNombreIgnoreCaseAndAmbitoActividad_Id(
                        request.getNombre(),
                        request.getIdAmbitoActividad()
                )
                .ifPresent(categoriaExistente -> {
                    if (!categoriaExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otra categoría DIEM con ese nombre en el ámbito seleccionado"
                        );
                    }
                });

        CategoriaDIEMMapper.updateEntity(categoriaDIEM, request, ambitoActividad);

        CategoriaDIEM categoriaActualizada = categoriaDIEMRepository.save(categoriaDIEM);
        return CategoriaDIEMMapper.toResponse(categoriaActualizada);
    }

    /**
     * Activa una categoría DIEM.
     */
    public CategoriaDIEMResponse activar(Long id) {
        CategoriaDIEM categoriaDIEM = obtenerCategoriaDIEMPorId(id);
        categoriaDIEM.activar();

        return CategoriaDIEMMapper.toResponse(
                categoriaDIEMRepository.save(categoriaDIEM)
        );
    }

    /**
     * Inactiva una categoría DIEM.
     */
    public CategoriaDIEMResponse inactivar(Long id) {
        CategoriaDIEM categoriaDIEM = obtenerCategoriaDIEMPorId(id);
        categoriaDIEM.inactivar();

        return CategoriaDIEMMapper.toResponse(
                categoriaDIEMRepository.save(categoriaDIEM)
        );
    }

    /**
     * Archiva una categoría DIEM.
     */
    public CategoriaDIEMResponse archivar(Long id) {
        CategoriaDIEM categoriaDIEM = obtenerCategoriaDIEMPorId(id);
        categoriaDIEM.archivar();

        return CategoriaDIEMMapper.toResponse(
                categoriaDIEMRepository.save(categoriaDIEM)
        );
    }

    /**
     * Busca una categoría DIEM por id.
     */
    private CategoriaDIEM obtenerCategoriaDIEMPorId(Long id) {
        return categoriaDIEMRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la categoría DIEM con id: " + id
                ));
    }

    /**
     * Busca un ámbito de actividad por id.
     */
    private AmbitoActividad obtenerAmbitoActividadPorId(Long idAmbitoActividad) {
        return ambitoActividadRepository.findById(idAmbitoActividad)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el ámbito de actividad con id: " + idAmbitoActividad
                ));
    }

    /**
     * Busca un ámbito y valida que esté activo.
     */
    private AmbitoActividad obtenerAmbitoActividadActivoPorId(Long idAmbitoActividad) {
        AmbitoActividad ambitoActividad = obtenerAmbitoActividadPorId(idAmbitoActividad);

        if (!ambitoActividad.estaActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ámbito de actividad seleccionado no está activo"
            );
        }

        return ambitoActividad;
    }

    /**
     * Verifica que el ámbito exista.
     */
    private void verificarAmbitoActividadExiste(Long idAmbitoActividad) {
        if (!ambitoActividadRepository.existsById(idAmbitoActividad)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontró el ámbito de actividad con id: " + idAmbitoActividad
            );
        }
    }

    /**
     * Valida que el ámbito seleccionado realmente permita categorías.
     */
    private void validarAmbitoRequiereCategoria(AmbitoActividad ambitoActividad) {
        if (!Boolean.TRUE.equals(ambitoActividad.getRequiereCategoria())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ámbito seleccionado no requiere categorías DIEM"
            );
        }
    }

    /**
     * Normaliza los datos recibidos desde el request.
     */
    private void normalizarRequest(CategoriaDIEMRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(capitalizarTexto(request.getNombre().trim()));
        }

        if (request.getDescripcion() != null) {
            request.setDescripcion(request.getDescripcion().trim());
        }

        if (request.getCriteriosPuntuacion() != null) {
            request.setCriteriosPuntuacion(request.getCriteriosPuntuacion().trim());
        }
    }

    /**
     * Convierte un texto a formato capitalizado simple.
     */
    private String capitalizarTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return texto;
        }

        String textoMinuscula = texto.toLowerCase();

        return textoMinuscula.substring(0, 1).toUpperCase()
                + textoMinuscula.substring(1);
    }
}