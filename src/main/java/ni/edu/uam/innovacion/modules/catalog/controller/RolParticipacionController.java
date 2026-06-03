package ni.edu.uam.innovacion.modules.catalog.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.catalog.dto.RolParticipacionRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.RolParticipacionResponse;
import ni.edu.uam.innovacion.modules.catalog.service.RolParticipacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para administrar el catálogo de roles de participación.
 */
@RestController
@RequestMapping("/api/admin/catalog/roles-participacion")
public class RolParticipacionController {

    private final RolParticipacionService rolParticipacionService;

    /**
     * Constructor para inyectar el Service.
     */
    public RolParticipacionController(RolParticipacionService rolParticipacionService) {
        this.rolParticipacionService = rolParticipacionService;
    }

    /**
     * Lista todos los roles de participación registrados.
     */
    @GetMapping
    public ResponseEntity<List<RolParticipacionResponse>> listarTodos() {
        return ResponseEntity.ok(rolParticipacionService.listarTodos());
    }

    /**
     * Lista únicamente los roles de participación activos.
     */
    @GetMapping("/activos")
    public ResponseEntity<List<RolParticipacionResponse>> listarActivos() {
        return ResponseEntity.ok(rolParticipacionService.listarActivos());
    }

    /**
     * Busca un rol de participación por su id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RolParticipacionResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rolParticipacionService.buscarPorId(id));
    }

    /**
     * Crea un nuevo rol de participación.
     */
    @PostMapping
    public ResponseEntity<RolParticipacionResponse> crear(
            @Valid @RequestBody RolParticipacionRequest request
    ) {
        RolParticipacionResponse response = rolParticipacionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un rol de participación existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RolParticipacionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolParticipacionRequest request
    ) {
        return ResponseEntity.ok(rolParticipacionService.actualizar(id, request));
    }

    /**
     * Activa un rol de participación.
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<RolParticipacionResponse> activar(@PathVariable Long id) {
        return ResponseEntity.ok(rolParticipacionService.activar(id));
    }

    /**
     * Inactiva un rol de participación.
     */
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<RolParticipacionResponse> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(rolParticipacionService.inactivar(id));
    }

    /**
     * Archiva un rol de participación.
     *
     */
    @PatchMapping("/{id}/archivar")
    public ResponseEntity<RolParticipacionResponse> archivar(@PathVariable Long id) {
        return ResponseEntity.ok(rolParticipacionService.archivar(id));
    }
}