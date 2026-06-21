package ni.edu.uam.innovacion.modules.participation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import ni.edu.uam.innovacion.modules.participation.dto.ActualizarParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.dto.CrearParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.dto.ParticipacionResponse;
import ni.edu.uam.innovacion.modules.participation.dto.ProcesarParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;
import ni.edu.uam.innovacion.modules.participation.service.ParticipacionService;
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
@RequestMapping("/api/admin/participaciones")
public class ParticipacionAdminController {

    private final ParticipacionService participacionService;

    public ParticipacionAdminController(ParticipacionService participacionService) {
        this.participacionService = participacionService;
    }

    @PostMapping
    public ResponseEntity<ParticipacionResponse> crear(
            @Valid @RequestBody CrearParticipacionRequest request
    ) {
        ParticipacionResponse response = participacionService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/admin/participaciones/" + response.idParticipacion()))
                .body(response);
    }

    @GetMapping
    public List<ParticipacionResponse> listarTodas() {
        return participacionService.listarTodas();
    }

    @GetMapping("/{idParticipacion}")
    public ParticipacionResponse buscarPorId(@PathVariable Long idParticipacion) {
        return participacionService.buscarPorId(idParticipacion);
    }

    @GetMapping("/inscripcion/{idInscripcion}")
    public ParticipacionResponse buscarPorInscripcion(@PathVariable Long idInscripcion) {
        return participacionService.buscarPorInscripcion(idInscripcion);
    }

    @GetMapping("/actividad/{idActividad}")
    public List<ParticipacionResponse> listarPorActividad(@PathVariable Long idActividad) {
        return participacionService.listarPorActividad(idActividad);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<ParticipacionResponse> listarPorUsuario(@PathVariable Long idUsuario) {
        return participacionService.listarPorUsuario(idUsuario);
    }

    @GetMapping("/estado/{estado}")
    public List<ParticipacionResponse> listarPorEstado(@PathVariable String estado) {
        return participacionService.listarPorEstado(EstadoParticipacion.fromValue(estado));
    }

    @GetMapping("/rol-participacion/{idRolParticipacion}")
    public List<ParticipacionResponse> listarPorRolParticipacion(
            @PathVariable Long idRolParticipacion
    ) {
        return participacionService.listarPorRolParticipacion(idRolParticipacion);
    }

    @GetMapping("/administrador-validador/{idAdministrador}")
    public List<ParticipacionResponse> listarPorAdministradorValidador(
            @PathVariable Long idAdministrador
    ) {
        return participacionService.listarPorAdministradorValidador(idAdministrador);
    }

    @PutMapping("/{idParticipacion}")
    public ParticipacionResponse actualizar(
            @PathVariable Long idParticipacion,
            @Valid @RequestBody ActualizarParticipacionRequest request
    ) {
        return participacionService.actualizar(idParticipacion, request);
    }

    @PatchMapping("/{idParticipacion}/validar")
    public ParticipacionResponse validar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long idParticipacion,
            @RequestBody(required = false) ProcesarParticipacionRequest request
    ) {
        return participacionService.validar(
                idParticipacion,
                request,
                obtenerIdUsuario(jwt)
        );
    }

    @PatchMapping("/{idParticipacion}/no-validar")
    public ParticipacionResponse noValidar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long idParticipacion,
            @RequestBody(required = false) ProcesarParticipacionRequest request
    ) {
        return participacionService.noValidar(
                idParticipacion,
                request,
                obtenerIdUsuario(jwt)
        );
    }

    @PatchMapping("/{idParticipacion}/anular")
    public ParticipacionResponse anular(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long idParticipacion,
            @RequestBody(required = false) ProcesarParticipacionRequest request
    ) {
        return participacionService.anular(
                idParticipacion,
                request,
                obtenerIdUsuario(jwt)
        );
    }

    @PatchMapping("/{idParticipacion}/pendiente")
    public ParticipacionResponse dejarPendiente(@PathVariable Long idParticipacion) {
        return participacionService.dejarPendiente(idParticipacion);
    }

    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}