package ni.edu.uam.innovacion.modules.catalog.controller;

import java.util.List;
import ni.edu.uam.innovacion.modules.catalog.dto.CarreraResponse;
import ni.edu.uam.innovacion.modules.catalog.dto.FacultadResponse;
import ni.edu.uam.innovacion.modules.catalog.service.CarreraService;
import ni.edu.uam.innovacion.modules.catalog.service.FacultadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints publicos de catalogo usados durante el registro self-service
 * (estudiante y participante externo). No requieren token.
 */
@RestController
@RequestMapping("/api/catalogos")
public class CatalogoPublicoController {

    private final FacultadService facultadService;
    private final CarreraService carreraService;

    public CatalogoPublicoController(FacultadService facultadService, CarreraService carreraService) {
        this.facultadService = facultadService;
        this.carreraService = carreraService;
    }

    @GetMapping("/facultades/activas")
    public List<FacultadResponse> facultadesActivas() {
        return facultadService.listarActivas();
    }

    @GetMapping("/carreras/activas")
    public List<CarreraResponse> carrerasActivas() {
        return carreraService.listarActivas();
    }
}
