package ni.edu.uam.innovacion.modules.catalog.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.catalog.dto.FacultadRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.FacultadResponse;
import ni.edu.uam.innovacion.modules.catalog.service.FacultadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para administrar el catálogo de facultades.
 */
@RestController
@RequestMapping("/api/admin/catalog/facultades")
public class FacultadController {

    private final FacultadService facultadService;

    /**
     * Constructor para inyectar el service.
     */
    public FacultadController(FacultadService facultadService) {
        this.facultadService = facultadService;
    }

    /**
     * Lista todas las facultades registradas.

     */
    @GetMapping
    public ResponseEntity<List<FacultadResponse>> listarTodas() {
        return ResponseEntity.ok(facultadService.listarTodas());
    }

    /**
     * Lista únicamente las facultades activas.

     */
    @GetMapping("/activas")
    public ResponseEntity<List<FacultadResponse>> listarActivas() {
        return ResponseEntity.ok(facultadService.listarActivas());
    }

    /**
     * Busca una facultad específica por su id.

     */
    @GetMapping("/{id}")
    public ResponseEntity<FacultadResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facultadService.buscarPorId(id));
    }

    /**
     * Crea una nueva facultad.

     */
    @PostMapping
    public ResponseEntity<FacultadResponse> crear(
            @Valid @RequestBody FacultadRequest request
    ) {
        FacultadResponse response = facultadService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza una facultad existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacultadResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FacultadRequest request
    ) {
        return ResponseEntity.ok(facultadService.actualizar(id, request));
    }

    /**
     * Cambia el estado de la facultad a ACTIVO.
     *
     * PATCH /api/admin/catalog/facultades/{id}/activar
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<FacultadResponse> activar(@PathVariable Long id) {
        return ResponseEntity.ok(facultadService.activar(id));
    }

    /**
     * Cambia el estado de la facultad a INACTIVO.

     */
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<FacultadResponse> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(facultadService.inactivar(id));
    }

    /**
     * Cambia el estado de la facultad a ARCHIVADO.

     */
    @PatchMapping("/{id}/archivar")
    public ResponseEntity<FacultadResponse> archivar(@PathVariable Long id) {
        return ResponseEntity.ok(facultadService.archivar(id));
    }
}