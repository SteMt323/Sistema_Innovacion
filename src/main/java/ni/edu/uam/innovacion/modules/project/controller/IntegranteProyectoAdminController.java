package ni.edu.uam.innovacion.modules.project.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarIntegranteProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearIntegranteProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.IntegranteProyectoResponse;
import ni.edu.uam.innovacion.modules.project.service.IntegranteProyectoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller REST administrativo para gestionar integrantes de proyecto.
 *
 * Permite registrar usuarios dentro de proyectos, consultar integrantes,
 * actualizar su rol dentro del proyecto y cambiar el estado del registro.
 *
 * Todos estos endpoints están bajo /api/admin/**, por lo que requieren
 * autenticación con JWT y rol ADMINISTRADOR según la configuración de seguridad.
 */
@RestController
@RequestMapping("/api/admin/integrantes-proyecto")
public class IntegranteProyectoAdminController {

    private final IntegranteProyectoService integranteProyectoService;

    public IntegranteProyectoAdminController(
            IntegranteProyectoService integranteProyectoService
    ) {
        this.integranteProyectoService = integranteProyectoService;
    }

    /**
     * Registra un nuevo integrante dentro de un proyecto.
     *
     * POST /api/admin/integrantes-proyecto
     */
    @PostMapping
    public ResponseEntity<IntegranteProyectoResponse> crear(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CrearIntegranteProyectoRequest request
    ) {
        IntegranteProyectoResponse response = integranteProyectoService.crear(
                request,
                obtenerIdUsuario(jwt)
        );

        return ResponseEntity
                .created(URI.create("/api/admin/integrantes-proyecto/" + response.idIntegranteProyecto()))
                .body(response);
    }

    /**
     * Lista todos los integrantes de proyecto registrados.
     *
     * GET /api/admin/integrantes-proyecto
     */
    @GetMapping
    public List<IntegranteProyectoResponse> listarTodos() {
        return integranteProyectoService.listarTodos();
    }

    /**
     * Busca un integrante de proyecto por id.
     *
     * GET /api/admin/integrantes-proyecto/{idIntegranteProyecto}
     */
    @GetMapping("/{idIntegranteProyecto}")
    public IntegranteProyectoResponse buscarPorId(
            @PathVariable Long idIntegranteProyecto
    ) {
        return integranteProyectoService.buscarPorId(idIntegranteProyecto);
    }

    /**
     * Lista todos los integrantes de un proyecto específico.
     *
     * GET /api/admin/integrantes-proyecto/proyecto/{idProyecto}
     */
    @GetMapping("/proyecto/{idProyecto}")
    public List<IntegranteProyectoResponse> listarPorProyecto(
            @PathVariable Long idProyecto
    ) {
        return integranteProyectoService.listarPorProyecto(idProyecto);
    }

    /**
     * Lista únicamente los integrantes activos de un proyecto.
     *
     * GET /api/admin/integrantes-proyecto/proyecto/{idProyecto}/activos
     */
    @GetMapping("/proyecto/{idProyecto}/activos")
    public List<IntegranteProyectoResponse> listarActivosPorProyecto(
            @PathVariable Long idProyecto
    ) {
        return integranteProyectoService.listarActivosPorProyecto(idProyecto);
    }

    /**
     * Lista los proyectos en los que participa un usuario.
     *
     * GET /api/admin/integrantes-proyecto/usuario/{idUsuario}
     */
    @GetMapping("/usuario/{idUsuario}")
    public List<IntegranteProyectoResponse> listarPorUsuario(
            @PathVariable Long idUsuario
    ) {
        return integranteProyectoService.listarPorUsuario(idUsuario);
    }

    /**
     * Lista los proyectos activos en los que participa un usuario.
     *
     * GET /api/admin/integrantes-proyecto/usuario/{idUsuario}/activos
     */
    @GetMapping("/usuario/{idUsuario}/activos")
    public List<IntegranteProyectoResponse> listarActivosPorUsuario(
            @PathVariable Long idUsuario
    ) {
        return integranteProyectoService.listarActivosPorUsuario(idUsuario);
    }

    /**
     * Lista integrantes según el rol de proyecto.
     *
     * GET /api/admin/integrantes-proyecto/rol-proyecto/{idRolProyecto}
     */
    @GetMapping("/rol-proyecto/{idRolProyecto}")
    public List<IntegranteProyectoResponse> listarPorRolProyecto(
            @PathVariable Long idRolProyecto
    ) {
        return integranteProyectoService.listarPorRolProyecto(idRolProyecto);
    }

    /**
     * Lista integrantes registrados por un administrador específico.
     *
     * GET /api/admin/integrantes-proyecto/administrador/{idAdministrador}
     */
    @GetMapping("/administrador/{idAdministrador}")
    public List<IntegranteProyectoResponse> listarPorAdministrador(
            @PathVariable Long idAdministrador
    ) {
        return integranteProyectoService.listarPorAdministrador(idAdministrador);
    }

    /**
     * Actualiza los datos editables de un integrante de proyecto.
     *
     * No cambia el proyecto ni el usuario, solamente rol, fecha de vinculación
     * y observaciones.
     *
     * PUT /api/admin/integrantes-proyecto/{idIntegranteProyecto}
     */
    @PutMapping("/{idIntegranteProyecto}")
    public IntegranteProyectoResponse actualizar(
            @PathVariable Long idIntegranteProyecto,
            @Valid @RequestBody ActualizarIntegranteProyectoRequest request
    ) {
        return integranteProyectoService.actualizar(idIntegranteProyecto, request);
    }

    /**
     * Activa un integrante de proyecto.
     *
     * PATCH /api/admin/integrantes-proyecto/{idIntegranteProyecto}/activar
     */
    @PatchMapping("/{idIntegranteProyecto}/activar")
    public IntegranteProyectoResponse activar(
            @PathVariable Long idIntegranteProyecto
    ) {
        return integranteProyectoService.activar(idIntegranteProyecto);
    }

    /**
     * Inactiva un integrante de proyecto.
     *
     * PATCH /api/admin/integrantes-proyecto/{idIntegranteProyecto}/inactivar
     */
    @PatchMapping("/{idIntegranteProyecto}/inactivar")
    public IntegranteProyectoResponse inactivar(
            @PathVariable Long idIntegranteProyecto
    ) {
        return integranteProyectoService.inactivar(idIntegranteProyecto);
    }

    /**
     * Archiva un integrante de proyecto.
     *
     * PATCH /api/admin/integrantes-proyecto/{idIntegranteProyecto}/archivar
     */
    @PatchMapping("/{idIntegranteProyecto}/archivar")
    public IntegranteProyectoResponse archivar(
            @PathVariable Long idIntegranteProyecto
    ) {
        return integranteProyectoService.archivar(idIntegranteProyecto);
    }

    /**
     * Obtiene el idUsuario desde el token JWT.
     *
     * Este id debe corresponder a un usuario que tenga perfil administrador,
     * porque el service valida que exista en perfiles_administrador.
     */
    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}