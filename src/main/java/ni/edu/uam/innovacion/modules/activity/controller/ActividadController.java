package ni.edu.uam.innovacion.modules.activity.controller;

import java.util.List;
import ni.edu.uam.innovacion.modules.activity.dto.ActividadResponse;
import ni.edu.uam.innovacion.modules.activity.service.ActividadService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @GetMapping("/disponibles")
    public List<ActividadResponse> listarDisponibles(@AuthenticationPrincipal Jwt jwt) {
        return actividadService.listarDisponibles(idUsuarioOpcional(jwt));
    }

    @GetMapping("/{idActividad}")
    public ActividadResponse buscarDisponiblePorId(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable Long idActividad
    ) {
        return actividadService.buscarDisponiblePorId(idActividad, idUsuarioOpcional(jwt));
    }

    private Long idUsuarioOpcional(Jwt jwt) {
        if (jwt == null) {
            return null;
        }
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario == null ? null : idUsuario.longValue();
    }
}
