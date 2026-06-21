package ni.edu.uam.innovacion.modules.project.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarAsignacionMentorProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.AsignacionMentorProyectoResponse;
import ni.edu.uam.innovacion.modules.project.dto.CrearAsignacionMentorProyectoRequest;
import ni.edu.uam.innovacion.modules.project.enums.EstadoAsignacion;
import ni.edu.uam.innovacion.modules.project.service.AsignacionMentorProyectoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller REST administrativo para gestionar asignaciones
 * de mentores a proyectos.
 *
 * Permite registrar mentorías, consultar asignaciones,
 * actualizar observaciones y cambiar el estado de la asignación.
 *
 * Todos los endpoints están bajo /api/admin/**, por lo que requieren
 * autenticación JWT y rol ADMINISTRADOR.
 */
@RestController
@RequestMapping("/api/admin/asignaciones-mentor-proyecto")
public class AsignacionMentorProyectoAdminController {

    private final AsignacionMentorProyectoService asignacionMentorProyectoService;

    public AsignacionMentorProyectoAdminController(
            AsignacionMentorProyectoService asignacionMentorProyectoService
    ) {
        this.asignacionMentorProyectoService = asignacionMentorProyectoService;
    }

    /**
     * Registra una nueva asignación de mentor a proyecto.
     *
     * POST /api/admin/asignaciones-mentor-proyecto
     */
    @PostMapping
    public ResponseEntity<AsignacionMentorProyectoResponse> crear(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CrearAsignacionMentorProyectoRequest request
    ) {
        AsignacionMentorProyectoResponse response = asignacionMentorProyectoService.crear(
                request,
                obtenerIdUsuario(jwt)
        );

        return ResponseEntity
                .created(URI.create("/api/admin/asignaciones-mentor-proyecto/" + response.idAsignacionMentor()))
                .body(response);
    }

    /**
     * Lista todas las asignaciones registradas.
     *
     * GET /api/admin/asignaciones-mentor-proyecto
     */
    @GetMapping
    public List<AsignacionMentorProyectoResponse> listarTodas() {
        return asignacionMentorProyectoService.listarTodas();
    }

    /**
     * Busca una asignación por id.
     *
     * GET /api/admin/asignaciones-mentor-proyecto/{idAsignacionMentor}
     */
    @GetMapping("/{idAsignacionMentor}")
    public AsignacionMentorProyectoResponse buscarPorId(
            @PathVariable Long idAsignacionMentor
    ) {
        return asignacionMentorProyectoService.buscarPorId(idAsignacionMentor);
    }

    /**
     * Lista asignaciones por proyecto.
     *
     * GET /api/admin/asignaciones-mentor-proyecto/proyecto/{idProyecto}
     */
    @GetMapping("/proyecto/{idProyecto}")
    public List<AsignacionMentorProyectoResponse> listarPorProyecto(
            @PathVariable Long idProyecto
    ) {
        return asignacionMentorProyectoService.listarPorProyecto(idProyecto);
    }

    /**
     * Lista asignaciones activas de un proyecto.
     *
     * GET /api/admin/asignaciones-mentor-proyecto/proyecto/{idProyecto}/activas
     */
    @GetMapping("/proyecto/{idProyecto}/activas")
    public List<AsignacionMentorProyectoResponse> listarActivasPorProyecto(
            @PathVariable Long idProyecto
    ) {
        return asignacionMentorProyectoService.listarActivasPorProyecto(idProyecto);
    }

    /**
     * Lista asignaciones por mentor.
     *
     * GET /api/admin/asignaciones-mentor-proyecto/mentor/{idMentor}
     */
    @GetMapping("/mentor/{idMentor}")
    public List<AsignacionMentorProyectoResponse> listarPorMentor(
            @PathVariable Long idMentor
    ) {
        return asignacionMentorProyectoService.listarPorMentor(idMentor);
    }

    /**
     * Lista asignaciones activas de un mentor.
     *
     * GET /api/admin/asignaciones-mentor-proyecto/mentor/{idMentor}/activas
     */
    @GetMapping("/mentor/{idMentor}/activas")
    public List<AsignacionMentorProyectoResponse> listarActivasPorMentor(
            @PathVariable Long idMentor
    ) {
        return asignacionMentorProyectoService.listarActivasPorMentor(idMentor);
    }

    /**
     * Lista asignaciones por estado.
     *
     * GET /api/admin/asignaciones-mentor-proyecto/estado/activa
     * GET /api/admin/asignaciones-mentor-proyecto/estado/finalizada
     * GET /api/admin/asignaciones-mentor-proyecto/estado/cancelada
     */
    @GetMapping("/estado/{estado}")
    public List<AsignacionMentorProyectoResponse> listarPorEstado(
            @PathVariable String estado
    ) {
        return asignacionMentorProyectoService.listarPorEstado(
                EstadoAsignacion.fromValue(estado)
        );
    }

    /**
     * Lista asignaciones registradas por un administrador.
     *
     * GET /api/admin/asignaciones-mentor-proyecto/administrador/{idAdministrador}
     */
    @GetMapping("/administrador/{idAdministrador}")
    public List<AsignacionMentorProyectoResponse> listarPorAdministrador(
            @PathVariable Long idAdministrador
    ) {
        return asignacionMentorProyectoService.listarPorAdministrador(idAdministrador);
    }

    /**
     * Actualiza datos editables de una asignación.
     *
     * PUT /api/admin/asignaciones-mentor-proyecto/{idAsignacionMentor}
     */
    @PutMapping("/{idAsignacionMentor}")
    public AsignacionMentorProyectoResponse actualizar(
            @PathVariable Long idAsignacionMentor,
            @Valid @RequestBody ActualizarAsignacionMentorProyectoRequest request
    ) {
        return asignacionMentorProyectoService.actualizar(idAsignacionMentor, request);
    }

    /**
     * Reactiva una asignación cancelada.
     *
     * PATCH /api/admin/asignaciones-mentor-proyecto/{idAsignacionMentor}/activar
     */
    @PatchMapping("/{idAsignacionMentor}/activar")
    public AsignacionMentorProyectoResponse activar(
            @PathVariable Long idAsignacionMentor
    ) {
        return asignacionMentorProyectoService.activar(idAsignacionMentor);
    }

    /**
     * Finaliza una asignación activa.
     *
     * PATCH /api/admin/asignaciones-mentor-proyecto/{idAsignacionMentor}/finalizar
     */
    @PatchMapping("/{idAsignacionMentor}/finalizar")
    public AsignacionMentorProyectoResponse finalizar(
            @PathVariable Long idAsignacionMentor
    ) {
        return asignacionMentorProyectoService.finalizar(idAsignacionMentor);
    }

    /**
     * Cancela una asignación activa.
     *
     * PATCH /api/admin/asignaciones-mentor-proyecto/{idAsignacionMentor}/cancelar
     */
    @PatchMapping("/{idAsignacionMentor}/cancelar")
    public AsignacionMentorProyectoResponse cancelar(
            @PathVariable Long idAsignacionMentor
    ) {
        return asignacionMentorProyectoService.cancelar(idAsignacionMentor);
    }

    /**
     * Obtiene el idUsuario desde el token JWT.
     *
     * Este usuario debe tener perfil administrador,
     * porque el service valida su existencia en perfiles_administrador.
     */
    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}   