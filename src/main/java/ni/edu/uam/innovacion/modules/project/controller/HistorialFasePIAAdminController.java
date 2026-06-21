package ni.edu.uam.innovacion.modules.project.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarHistorialFasePIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CerrarHistorialFasePIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearHistorialFasePIARequest;
import ni.edu.uam.innovacion.modules.project.dto.HistorialFasePIAResponse;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;
import ni.edu.uam.innovacion.modules.project.service.HistorialFasePIAService;
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
@RequestMapping("/api/admin/historial-fases-pia")
public class HistorialFasePIAAdminController {

    private final HistorialFasePIAService historialFasePIAService;

    public HistorialFasePIAAdminController(
            HistorialFasePIAService historialFasePIAService
    ) {
        this.historialFasePIAService = historialFasePIAService;
    }

    @PostMapping
    public ResponseEntity<HistorialFasePIAResponse> crear(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CrearHistorialFasePIARequest request
    ) {
        HistorialFasePIAResponse response = historialFasePIAService.crear(
                request,
                obtenerIdUsuario(jwt)
        );

        return ResponseEntity
                .created(URI.create("/api/admin/historial-fases-pia/" + response.idHistorialFase()))
                .body(response);
    }

    @GetMapping
    public List<HistorialFasePIAResponse> listarTodos() {
        return historialFasePIAService.listarTodos();
    }

    @GetMapping("/{idHistorialFase}")
    public HistorialFasePIAResponse buscarPorId(
            @PathVariable Long idHistorialFase
    ) {
        return historialFasePIAService.buscarPorId(idHistorialFase);
    }

    @GetMapping("/proyecto-pia/{idProyectoPIA}")
    public List<HistorialFasePIAResponse> listarPorProyectoPIA(
            @PathVariable Long idProyectoPIA
    ) {
        return historialFasePIAService.listarPorProyectoPIA(idProyectoPIA);
    }

    @GetMapping("/proyecto/{idProyecto}")
    public List<HistorialFasePIAResponse> listarPorProyecto(
            @PathVariable Long idProyecto
    ) {
        return historialFasePIAService.listarPorProyecto(idProyecto);
    }

    @GetMapping("/fase/{fase}")
    public List<HistorialFasePIAResponse> listarPorFase(
            @PathVariable String fase
    ) {
        return historialFasePIAService.listarPorFase(FasePIA.fromValue(fase));
    }

    @GetMapping("/administrador/{idAdministrador}")
    public List<HistorialFasePIAResponse> listarPorAdministrador(
            @PathVariable Long idAdministrador
    ) {
        return historialFasePIAService.listarPorAdministrador(idAdministrador);
    }

    @GetMapping("/proyecto-pia/{idProyectoPIA}/vigente")
    public HistorialFasePIAResponse buscarFaseVigentePorProyectoPIA(
            @PathVariable Long idProyectoPIA
    ) {
        return historialFasePIAService.buscarFaseVigentePorProyectoPIA(idProyectoPIA);
    }

    @PutMapping("/{idHistorialFase}")
    public HistorialFasePIAResponse actualizar(
            @PathVariable Long idHistorialFase,
            @Valid @RequestBody ActualizarHistorialFasePIARequest request
    ) {
        return historialFasePIAService.actualizar(idHistorialFase, request);
    }

    @PatchMapping("/{idHistorialFase}/cerrar")
    public HistorialFasePIAResponse cerrarPorId(
            @PathVariable Long idHistorialFase,
            @Valid @RequestBody CerrarHistorialFasePIARequest request
    ) {
        return historialFasePIAService.cerrarPorId(idHistorialFase, request);
    }

    @PatchMapping("/proyecto-pia/{idProyectoPIA}/vigente/cerrar")
    public HistorialFasePIAResponse cerrarFaseVigentePorProyectoPIA(
            @PathVariable Long idProyectoPIA,
            @Valid @RequestBody CerrarHistorialFasePIARequest request
    ) {
        return historialFasePIAService.cerrarFaseVigentePorProyectoPIA(
                idProyectoPIA,
                request
        );
    }

    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}