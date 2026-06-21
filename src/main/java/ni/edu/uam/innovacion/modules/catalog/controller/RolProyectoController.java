package ni.edu.uam.innovacion.modules.catalog.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.catalog.dto.RolProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.RolProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.service.RolProyectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para administrar el catálogo de roles de proyecto.
 *
 * Este catálogo se utiliza para definir la función que cumple un usuario
 * dentro de un proyecto, por ejemplo:
 * líder, integrante, desarrollador, diseñador, investigador, entre otros.
 *
 * Estos roles serán usados posteriormente al registrar integrantes
 * en la entidad IntegranteProyecto.
 */
@RestController
@RequestMapping("/api/admin/catalog/roles-proyecto")
public class RolProyectoController {

    private final RolProyectoService rolProyectoService;

    public RolProyectoController(RolProyectoService rolProyectoService) {
        this.rolProyectoService = rolProyectoService;
    }

    /**
     * Lista todos los roles de proyecto registrados.
     *
     * GET /api/admin/catalog/roles-proyecto
     */
    @GetMapping
    public ResponseEntity<List<RolProyectoResponse>> listarTodos() {
        return ResponseEntity.ok(rolProyectoService.listarTodos());
    }

    /**
     * Lista únicamente los roles de proyecto activos.
     *
     * GET /api/admin/catalog/roles-proyecto/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<RolProyectoResponse>> listarActivos() {
        return ResponseEntity.ok(rolProyectoService.listarActivos());
    }

    /**
     * Busca un rol de proyecto por su id.
     *
     * GET /api/admin/catalog/roles-proyecto/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RolProyectoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rolProyectoService.buscarPorId(id));
    }

    /**
     * Crea un nuevo rol de proyecto.
     *
     * POST /api/admin/catalog/roles-proyecto
     */
    @PostMapping
    public ResponseEntity<RolProyectoResponse> crear(
            @Valid @RequestBody RolProyectoRequest request
    ) {
        RolProyectoResponse response = rolProyectoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un rol de proyecto existente.
     *
     * PUT /api/admin/catalog/roles-proyecto/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RolProyectoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolProyectoRequest request
    ) {
        return ResponseEntity.ok(rolProyectoService.actualizar(id, request));
    }

    /**
     * Activa un rol de proyecto.
     *
     * PATCH /api/admin/catalog/roles-proyecto/{id}/activar
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<RolProyectoResponse> activar(@PathVariable Long id) {
        return ResponseEntity.ok(rolProyectoService.activar(id));
    }

    /**
     * Inactiva un rol de proyecto.
     *
     * PATCH /api/admin/catalog/roles-proyecto/{id}/inactivar
     */
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<RolProyectoResponse> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(rolProyectoService.inactivar(id));
    }

    /**
     * Archiva un rol de proyecto.
     *
     * PATCH /api/admin/catalog/roles-proyecto/{id}/archivar
     */
    @PatchMapping("/{id}/archivar")
    public ResponseEntity<RolProyectoResponse> archivar(@PathVariable Long id) {
        return ResponseEntity.ok(rolProyectoService.archivar(id));
    }
}