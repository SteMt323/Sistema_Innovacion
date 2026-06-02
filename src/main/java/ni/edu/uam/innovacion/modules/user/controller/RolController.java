package ni.edu.uam.innovacion.modules.user.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import ni.edu.uam.innovacion.modules.user.dto.CrearRolRequest;
import ni.edu.uam.innovacion.modules.user.dto.RolResponse;
import ni.edu.uam.innovacion.modules.user.service.RolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<RolResponse> listarRoles() {
        return rolService.listarRoles();
    }

    @PostMapping
    public ResponseEntity<RolResponse> crearRol(@Valid @RequestBody CrearRolRequest request) {
        RolResponse response = rolService.crearRol(request);
        return ResponseEntity
            .created(URI.create("/api/roles/" + response.idRol()))
            .body(response);
    }
}
