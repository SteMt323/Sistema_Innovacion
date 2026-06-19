package ni.edu.uam.innovacion.modules.project.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoResponse;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;
import ni.edu.uam.innovacion.modules.project.service.ProyectoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/proyectos")
public class ProyectoAdminController {

    private final ProyectoService proyectoService;

    public ProyectoAdminController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @PostMapping
    public ResponseEntity<ProyectoResponse> crear(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CrearProyectoRequest request
    ) {
        ProyectoResponse response = proyectoService.crear(request, obtenerIdUsuario(jwt));

        return ResponseEntity
                .created(URI.create("/api/admin/proyectos/" + response.idProyecto()))
                .body(response);
    }

    @GetMapping
    public List<ProyectoResponse> listarTodos() {
        return proyectoService.listarTodos();
    }

    @GetMapping("/activos")
    public List<ProyectoResponse> listarActivos() {
        return proyectoService.listarActivos();
    }

    @GetMapping("/{idProyecto}")
    public ProyectoResponse buscarPorId(@PathVariable Long idProyecto) {
        return proyectoService.buscarPorId(idProyecto);
    }

    @GetMapping("/estado/{estado}")
    public List<ProyectoResponse> listarPorEstado(@PathVariable String estado) {
        return proyectoService.listarPorEstado(EstadoProyecto.fromValue(estado));
    }

    @GetMapping("/fuente/{idFuenteProyecto}")
    public List<ProyectoResponse> listarPorFuente(
            @PathVariable Long idFuenteProyecto
    ) {
        return proyectoService.listarPorFuente(idFuenteProyecto);
    }

    @GetMapping("/fuente/{idFuenteProyecto}/activos")
    public List<ProyectoResponse> listarActivosPorFuente(
            @PathVariable Long idFuenteProyecto
    ) {
        return proyectoService.listarActivosPorFuente(idFuenteProyecto);
    }

    @GetMapping("/administrador/{idAdministradorRegistro}")
    public List<ProyectoResponse> listarPorAdministrador(
            @PathVariable Long idAdministradorRegistro
    ) {
        return proyectoService.listarPorAdministrador(idAdministradorRegistro);
    }

    @PutMapping("/{idProyecto}")
    public ProyectoResponse actualizar(
            @PathVariable Long idProyecto,
            @Valid @RequestBody ActualizarProyectoRequest request
    ) {
        return proyectoService.actualizar(idProyecto, request);
    }

    @PatchMapping("/{idProyecto}/pausar")
    public ProyectoResponse pausar(@PathVariable Long idProyecto) {
        return proyectoService.pausar(idProyecto);
    }

    @PatchMapping("/{idProyecto}/reactivar")
    public ProyectoResponse reactivar(@PathVariable Long idProyecto) {
        return proyectoService.reactivar(idProyecto);
    }

    @PatchMapping("/{idProyecto}/finalizar")
    public ProyectoResponse finalizar(@PathVariable Long idProyecto) {
        return proyectoService.finalizar(idProyecto);
    }

    @PatchMapping("/{idProyecto}/cancelar")
    public ProyectoResponse cancelar(@PathVariable Long idProyecto) {
        return proyectoService.cancelar(idProyecto);
    }

    @PatchMapping("/{idProyecto}/archivar")
    public ProyectoResponse archivar(@PathVariable Long idProyecto) {
        return proyectoService.archivar(idProyecto);
    }

    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}