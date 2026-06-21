package ni.edu.uam.innovacion.modules.dashboard.controller;

import java.util.List;
import ni.edu.uam.innovacion.modules.dashboard.dto.AdminDashboardResponse;
import ni.edu.uam.innovacion.modules.dashboard.service.DashboardAdminService;
import ni.edu.uam.innovacion.modules.points.dto.PuntoInnovacionResponse;
import ni.edu.uam.innovacion.modules.points.dto.TopUsuarioPuntosResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardAdminController {

    private final DashboardAdminService dashboardService;

    public DashboardAdminController(DashboardAdminService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumen")
    public AdminDashboardResponse obtenerResumen() {
        return dashboardService.obtenerResumen();
    }

    @GetMapping("/top-usuarios-puntos")
    public List<TopUsuarioPuntosResponse> obtenerTopUsuarios(
        @RequestParam(defaultValue = "10") int limite
    ) {
        return dashboardService.obtenerTopUsuarios(limite);
    }

    @GetMapping("/movimientos-recientes")
    public List<PuntoInnovacionResponse> obtenerMovimientosRecientes() {
        return dashboardService.obtenerMovimientosRecientes();
    }
}
