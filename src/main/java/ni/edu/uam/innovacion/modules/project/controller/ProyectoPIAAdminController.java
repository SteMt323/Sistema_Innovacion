package ni.edu.uam.innovacion.modules.project.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarProyectoPIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CambiarFaseProyectoPIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearProyectoPIARequest;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoPIAResponse;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyectoPIA;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;
import ni.edu.uam.innovacion.modules.project.service.ProyectoPIAService;
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
@RequestMapping("/api/admin/proyectos-pia")
public class ProyectoPIAAdminController {

    private final ProyectoPIAService proyectoPIAService;

    public ProyectoPIAAdminController(ProyectoPIAService proyectoPIAService) {
        this.proyectoPIAService = proyectoPIAService;
    }

    @PostMapping
    public ResponseEntity<ProyectoPIAResponse> crear(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CrearProyectoPIARequest request
    ) {
        ProyectoPIAResponse response = proyectoPIAService.crear(
                request,
                obtenerIdUsuario(jwt)
        );

        return ResponseEntity
                .created(URI.create("/api/admin/proyectos-pia/" + response.idProyectoPIA()))
                .body(response);
    }

    @GetMapping
    public List<ProyectoPIAResponse> listarTodos() {
        return proyectoPIAService.listarTodos();
    }

    @GetMapping("/activos")
    public List<ProyectoPIAResponse> listarActivos() {
        return proyectoPIAService.listarActivos();
    }

    @GetMapping("/estado/{estado}")
    public List<ProyectoPIAResponse> listarPorEstado(
            @PathVariable String estado
    ) {
        return proyectoPIAService.listarPorEstado(
                EstadoProyectoPIA.fromValue(estado)
        );
    }

    @GetMapping("/fase/{fase}")
    public List<ProyectoPIAResponse> listarPorFase(
            @PathVariable String fase
    ) {
        return proyectoPIAService.listarPorFase(
                FasePIA.fromValue(fase)
        );
    }

    @GetMapping("/estado/{estado}/fase/{fase}")
    public List<ProyectoPIAResponse> listarPorEstadoYFase(
            @PathVariable String estado,
            @PathVariable String fase
    ) {
        return proyectoPIAService.listarPorEstadoYFase(
                EstadoProyectoPIA.fromValue(estado),
                FasePIA.fromValue(fase)
        );
    }

    @GetMapping("/administrador/{idAdministrador}")
    public List<ProyectoPIAResponse> listarPorAdministrador(
            @PathVariable Long idAdministrador
    ) {
        return proyectoPIAService.listarPorAdministrador(idAdministrador);
    }

    @GetMapping("/proyecto/{idProyecto}")
    public ProyectoPIAResponse buscarPorProyecto(
            @PathVariable Long idProyecto
    ) {
        return proyectoPIAService.buscarPorProyecto(idProyecto);
    }

    @GetMapping("/{idProyectoPIA}")
    public ProyectoPIAResponse buscarPorId(
            @PathVariable Long idProyectoPIA
    ) {
        return proyectoPIAService.buscarPorId(idProyectoPIA);
    }

    @PutMapping("/{idProyectoPIA}")
    public ProyectoPIAResponse actualizar(
            @PathVariable Long idProyectoPIA,
            @Valid @RequestBody ActualizarProyectoPIARequest request
    ) {
        return proyectoPIAService.actualizar(idProyectoPIA, request);
    }

    @PatchMapping("/{idProyectoPIA}/fase")
    public ProyectoPIAResponse cambiarFase(
            @PathVariable Long idProyectoPIA,
            @Valid @RequestBody CambiarFaseProyectoPIARequest request
    ) {
        return proyectoPIAService.cambiarFase(idProyectoPIA, request);
    }

    @PatchMapping("/{idProyectoPIA}/pausar")
    public ProyectoPIAResponse pausar(
            @PathVariable Long idProyectoPIA
    ) {
        return proyectoPIAService.pausar(idProyectoPIA);
    }

    @PatchMapping("/{idProyectoPIA}/reactivar")
    public ProyectoPIAResponse reactivar(
            @PathVariable Long idProyectoPIA
    ) {
        return proyectoPIAService.reactivar(idProyectoPIA);
    }

    @PatchMapping("/{idProyectoPIA}/finalizar")
    public ProyectoPIAResponse finalizar(
            @PathVariable Long idProyectoPIA
    ) {
        return proyectoPIAService.finalizar(idProyectoPIA);
    }

    @PatchMapping("/{idProyectoPIA}/retirar")
    public ProyectoPIAResponse retirar(
            @PathVariable Long idProyectoPIA
    ) {
        return proyectoPIAService.retirar(idProyectoPIA);
    }

    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}