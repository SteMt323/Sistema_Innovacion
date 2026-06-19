package ni.edu.uam.innovacion.modules.project.controller;

import java.util.List;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoResponse;
import ni.edu.uam.innovacion.modules.project.service.ProyectoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping("/activos")
    public List<ProyectoResponse> listarActivos() {
        return proyectoService.listarActivos();
    }

    @GetMapping("/fuente/{idFuenteProyecto}/activos")
    public List<ProyectoResponse> listarActivosPorFuente(
            @PathVariable Long idFuenteProyecto
    ) {
        return proyectoService.listarActivosPorFuente(idFuenteProyecto);
    }
}