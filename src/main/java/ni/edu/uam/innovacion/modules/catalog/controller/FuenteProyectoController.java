package ni.edu.uam.innovacion.modules.catalog.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.common.enums.CategoriaFuenteProyecto;
import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.service.FuenteProyectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para administrar el catálogo de fuentes de proyecto.
 */
@RestController
@RequestMapping("/api/admin/catalog/fuentes-proyecto")
public class FuenteProyectoController {

    private final FuenteProyectoService fuenteProyectoService;

    /**
     * Constructor para inyectar el Service.
     */
    public FuenteProyectoController(FuenteProyectoService fuenteProyectoService) {
        this.fuenteProyectoService = fuenteProyectoService;
    }

    /**
     * Lista todas las fuentes de proyecto registradas.
     */
    @GetMapping
    public ResponseEntity<List<FuenteProyectoResponse>> listarTodas() {
        return ResponseEntity.ok(fuenteProyectoService.listarTodas());
    }

    /**
     * Lista únicamente las fuentes de proyecto activas.
     */
    @GetMapping("/activas")
    public ResponseEntity<List<FuenteProyectoResponse>> listarActivas() {
        return ResponseEntity.ok(fuenteProyectoService.listarActivas());
    }

    /**
     * Lista las fuentes de proyecto por categoría.
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<FuenteProyectoResponse>> listarPorCategoria(
            @PathVariable CategoriaFuenteProyecto categoria
    ) {
        return ResponseEntity.ok(
                fuenteProyectoService.listarPorCategoria(categoria)
        );
    }

    /**
     * Lista únicamente las fuentes activas de una categoría específica.
     */
    @GetMapping("/categoria/{categoria}/activas")
    public ResponseEntity<List<FuenteProyectoResponse>> listarActivasPorCategoria(
            @PathVariable CategoriaFuenteProyecto categoria
    ) {
        return ResponseEntity.ok(
                fuenteProyectoService.listarActivasPorCategoria(categoria)
        );
    }

    /**
     * Busca una fuente de proyecto por su id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FuenteProyectoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fuenteProyectoService.buscarPorId(id));
    }

    /**
     * Crea una nueva fuente de proyecto.
     */
    @PostMapping
    public ResponseEntity<FuenteProyectoResponse> crear(
            @Valid @RequestBody FuenteProyectoRequest request
    ) {
        FuenteProyectoResponse response = fuenteProyectoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza una fuente de proyecto existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FuenteProyectoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FuenteProyectoRequest request
    ) {
        return ResponseEntity.ok(fuenteProyectoService.actualizar(id, request));
    }

    /**
     * Activa una fuente de proyecto.
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<FuenteProyectoResponse> activar(@PathVariable Long id) {
        return ResponseEntity.ok(fuenteProyectoService.activar(id));
    }

    /**
     * Inactiva una fuente de proyecto.
     */
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<FuenteProyectoResponse> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(fuenteProyectoService.inactivar(id));
    }

    /**
     * Archiva una fuente de proyecto.
     */
    @PatchMapping("/{id}/archivar")
    public ResponseEntity<FuenteProyectoResponse> archivar(@PathVariable Long id) {
        return ResponseEntity.ok(fuenteProyectoService.archivar(id));
    }
}