package ni.edu.uam.innovacion.modules.activity.controller;

import java.util.List;
import ni.edu.uam.innovacion.modules.activity.dto.ActividadResponse;
import ni.edu.uam.innovacion.modules.activity.service.ActividadService;
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
    public List<ActividadResponse> listarDisponibles() {
        return actividadService.listarDisponibles();
    }

    @GetMapping("/{idActividad}")
    public ActividadResponse buscarDisponiblePorId(@PathVariable Long idActividad) {
        return actividadService.buscarDisponiblePorId(idActividad);
    }
}
