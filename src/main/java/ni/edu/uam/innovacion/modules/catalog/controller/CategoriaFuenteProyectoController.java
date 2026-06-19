package ni.edu.uam.innovacion.modules.catalog.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaFuenteProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaFuenteProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.service.CategoriaFuenteProyectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para administrar el catálogo de categorías de fuente de proyecto.
 *
 * Permite:
 * - listar todas las categorías de fuente de proyecto
 * - listar solo categorías activas
 * - buscar categoría por id
 * - crear categoría
 * - actualizar categoría
 * - activar, inactivar o archivar categoría
 *
 * Ejemplos de categorías:
 * - PROGRAMA_PIA
 * - CONCURSO
 * - ACTIVIDAD_INNOVACION
 * - EXTERNO
 * - OTRO
 */
@RestController
@RequestMapping("/api/admin/catalog/categorias-fuente-proyecto")
public class CategoriaFuenteProyectoController {

    private final CategoriaFuenteProyectoService categoriaFuenteProyectoService;

    /**
     * Constructor para inyectar el Service.
     */
    public CategoriaFuenteProyectoController(
            CategoriaFuenteProyectoService categoriaFuenteProyectoService
    ) {
        this.categoriaFuenteProyectoService = categoriaFuenteProyectoService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaFuenteProyectoResponse>> listarTodos() {
        return ResponseEntity.ok(categoriaFuenteProyectoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CategoriaFuenteProyectoResponse>> listarActivos() {
        return ResponseEntity.ok(categoriaFuenteProyectoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaFuenteProyectoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoriaFuenteProyectoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaFuenteProyectoResponse> crear(
            @Valid @RequestBody CategoriaFuenteProyectoRequest request
    ) {
        CategoriaFuenteProyectoResponse response =
                categoriaFuenteProyectoService.crear(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaFuenteProyectoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaFuenteProyectoRequest request
    ) {
        return ResponseEntity.ok(
                categoriaFuenteProyectoService.actualizar(id, request)
        );
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<CategoriaFuenteProyectoResponse> activar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoriaFuenteProyectoService.activar(id));
    }

    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<CategoriaFuenteProyectoResponse> inactivar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoriaFuenteProyectoService.inactivar(id));
    }

    @PatchMapping("/{id}/archivar")
    public ResponseEntity<CategoriaFuenteProyectoResponse> archivar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoriaFuenteProyectoService.archivar(id));
    }
}