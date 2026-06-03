package ni.edu.uam.innovacion.modules.activity.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import ni.edu.uam.innovacion.modules.activity.dto.ActividadResponse;
import ni.edu.uam.innovacion.modules.activity.dto.ActualizarActividadRequest;
import ni.edu.uam.innovacion.modules.activity.dto.CrearActividadRequest;
import ni.edu.uam.innovacion.modules.activity.service.ActividadService;
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
@RequestMapping("/api/admin/actividades")
public class ActividadAdminController {

    private final ActividadService actividadService;

    public ActividadAdminController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @PostMapping
    public ResponseEntity<ActividadResponse> crear(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CrearActividadRequest request
    ) {
        ActividadResponse response = actividadService.crear(request, obtenerIdUsuario(jwt));
        return ResponseEntity
            .created(URI.create("/api/admin/actividades/" + response.idActividad()))
            .body(response);
    }

    @GetMapping
    public List<ActividadResponse> listarTodas() {
        return actividadService.listarTodas();
    }

    @GetMapping("/{idActividad}")
    public ActividadResponse buscarPorId(@PathVariable Long idActividad) {
        return actividadService.buscarPorId(idActividad);
    }

    @PutMapping("/{idActividad}")
    public ActividadResponse actualizar(
        @PathVariable Long idActividad,
        @Valid @RequestBody ActualizarActividadRequest request
    ) {
        return actividadService.actualizar(idActividad, request);
    }

    @PatchMapping("/{idActividad}/publicar")
    public ActividadResponse publicar(@PathVariable Long idActividad) {
        return actividadService.publicar(idActividad);
    }

    @PatchMapping("/{idActividad}/iniciar")
    public ActividadResponse iniciar(@PathVariable Long idActividad) {
        return actividadService.iniciar(idActividad);
    }

    @PatchMapping("/{idActividad}/finalizar")
    public ActividadResponse finalizar(@PathVariable Long idActividad) {
        return actividadService.finalizar(idActividad);
    }

    @PatchMapping("/{idActividad}/cancelar")
    public ActividadResponse cancelar(@PathVariable Long idActividad) {
        return actividadService.cancelar(idActividad);
    }

    @PatchMapping("/{idActividad}/archivar")
    public ActividadResponse archivar(@PathVariable Long idActividad) {
        return actividadService.archivar(idActividad);
    }

    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}
