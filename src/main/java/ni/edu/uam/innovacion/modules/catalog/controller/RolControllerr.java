package ni.edu.uam.innovacion.modules.catalog.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.catalog.dto.RolRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.RolResponse;
import ni.edu.uam.innovacion.modules.catalog.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para administrar el catálogo de roles.
 *
 * El controller expone endpoints que pueden ser probados desde Postman
 * o consumidos desde el frontend.
 */
@RestController
@RequestMapping("/api/admin/catalog/roles")
public class RolControllerr {

    private final RolService rolService;

    public RolControllerr(RolService rolService) {
        this.rolService = rolService;
    }

    /**
     * Lista todos los roles.
     *
     * GET /api/admin/catalog/roles
     */
    @GetMapping
    public ResponseEntity<List<RolResponse>> listarTodos() {
        return ResponseEntity.ok(rolService.listarTodos());
    }

    /**
     * Lista solamente los roles activos.
     *
     * GET /api/admin/catalog/roles/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<RolResponse>> listarActivos() {
        return ResponseEntity.ok(rolService.listarActivos());
    }

    /**
     * Busca un rol por id.
     *
     * GET /api/admin/catalog/roles/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.buscarPorId(id));
    }

    /**
     * Crea un nuevo rol.
     *
     * POST /api/admin/catalog/roles
     */
    @PostMapping
    public ResponseEntity<RolResponse> crear(@Valid @RequestBody RolRequest request) {
        RolResponse response = rolService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un rol existente.
     *
     * PUT /api/admin/catalog/roles/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolRequest request
    ) {
        return ResponseEntity.ok(rolService.actualizar(id, request));
    }

    /**
     * Activa un rol.
     *
     * PATCH /api/admin/catalog/roles/{id}/activar
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<RolResponse> activar(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.activar(id));
    }

    /**
     * Inactiva un rol.
     *
     * PATCH /api/admin/catalog/roles/{id}/inactivar
     */
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<RolResponse> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.inactivar(id));
    }

    /**
     * Archiva un rol.
     *
     * PATCH /api/admin/catalog/roles/{id}/archivar
     */
    @PatchMapping("/{id}/archivar")
    public ResponseEntity<RolResponse> archivar(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.archivar(id));
    }
}   