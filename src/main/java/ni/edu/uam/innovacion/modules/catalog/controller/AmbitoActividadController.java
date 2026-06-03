package ni.edu.uam.innovacion.modules.catalog.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.catalog.dto.AmbitoActividadRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.AmbitoActividadResponse;
import ni.edu.uam.innovacion.modules.catalog.service.AmbitoActividadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para administrar el catálogo de ámbitos de actividad.

 * Permite:
 * - listar todos los ámbitos de actividad
 * - listar solo ámbitos activos
 * - listar ámbitos que requieren categoría
 * - listar ámbitos que no requieren categoría
 * - buscar ámbito por id
 * - crear ámbito
 * - actualizar ámbito
 * - activar, inactivar o archivar ámbito
 *
 * Ejemplos de ámbitos:
 * - DIEM
 * - EXTERNA
 */
@RestController
@RequestMapping("/api/admin/catalog/ambitos-actividad")
public class AmbitoActividadController {

    private final AmbitoActividadService ambitoActividadService;

    /**
     * Constructor para inyectar el Service.
     */
    public AmbitoActividadController(AmbitoActividadService ambitoActividadService) {
        this.ambitoActividadService = ambitoActividadService;
    }


    @GetMapping
    public ResponseEntity<List<AmbitoActividadResponse>> listarTodos() {
        return ResponseEntity.ok(ambitoActividadService.listarTodos());
    }


    @GetMapping("/activos")
    public ResponseEntity<List<AmbitoActividadResponse>> listarActivos() {
        return ResponseEntity.ok(ambitoActividadService.listarActivos());
    }


    @GetMapping("/requieren-categoria")
    public ResponseEntity<List<AmbitoActividadResponse>> listarQueRequierenCategoria() {
        return ResponseEntity.ok(ambitoActividadService.listarQueRequierenCategoria());
    }

    @GetMapping("/no-requieren-categoria")
    public ResponseEntity<List<AmbitoActividadResponse>> listarQueNoRequierenCategoria() {
        return ResponseEntity.ok(ambitoActividadService.listarQueNoRequierenCategoria());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmbitoActividadResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ambitoActividadService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AmbitoActividadResponse> crear(
            @Valid @RequestBody AmbitoActividadRequest request
    ) {
        AmbitoActividadResponse response = ambitoActividadService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmbitoActividadResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AmbitoActividadRequest request
    ) {
        return ResponseEntity.ok(ambitoActividadService.actualizar(id, request));
    }


    @PatchMapping("/{id}/activar")
    public ResponseEntity<AmbitoActividadResponse> activar(@PathVariable Long id) {
        return ResponseEntity.ok(ambitoActividadService.activar(id));
    }


    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<AmbitoActividadResponse> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(ambitoActividadService.inactivar(id));
    }


    @PatchMapping("/{id}/archivar")
    public ResponseEntity<AmbitoActividadResponse> archivar(@PathVariable Long id) {
        return ResponseEntity.ok(ambitoActividadService.archivar(id));
    }
}