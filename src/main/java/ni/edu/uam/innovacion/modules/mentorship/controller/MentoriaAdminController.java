package ni.edu.uam.innovacion.modules.mentorship.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.mentorship.dto.ActualizarMentoriaRequest;
import ni.edu.uam.innovacion.modules.mentorship.dto.CrearMentoriaRequest;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentoriaResponse;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentoriaResumenResponse;
import ni.edu.uam.innovacion.modules.mentorship.service.MentoriaAdminService;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/mentorias")
public class MentoriaAdminController {

    private final MentoriaAdminService mentoriaAdminService;

    public MentoriaAdminController(MentoriaAdminService mentoriaAdminService) {
        this.mentoriaAdminService = mentoriaAdminService;
    }

    @PostMapping
    public ResponseEntity<MentoriaResponse> crear(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CrearMentoriaRequest request
    ) {
        MentoriaResponse response = mentoriaAdminService.crear(request, obtenerIdUsuario(jwt));
        return ResponseEntity
            .created(URI.create("/api/admin/mentorias/" + response.idMentoria()))
            .body(response);
    }

    @GetMapping
    public List<MentoriaResponse> listar(
        @RequestParam(required = false) Long idActividad,
        @RequestParam(required = false) Long idMentor,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta
    ) {
        return mentoriaAdminService.listar(
            idActividad,
            idMentor,
            parseEstado(estado),
            fechaDesde,
            fechaHasta
        );
    }

    @GetMapping("/resumen")
    public MentoriaResumenResponse obtenerResumen() {
        return mentoriaAdminService.obtenerResumen();
    }

    @GetMapping("/{idMentoria}")
    public MentoriaResponse obtenerPorId(@PathVariable Long idMentoria) {
        return mentoriaAdminService.obtenerPorId(idMentoria);
    }

    @PutMapping("/{idMentoria}")
    public MentoriaResponse actualizar(
        @PathVariable Long idMentoria,
        @Valid @RequestBody ActualizarMentoriaRequest request
    ) {
        return mentoriaAdminService.actualizar(idMentoria, request);
    }

    @PatchMapping("/{idMentoria}/activar")
    public MentoriaResponse activar(@PathVariable Long idMentoria) {
        return mentoriaAdminService.activar(idMentoria);
    }

    @PatchMapping("/{idMentoria}/inactivar")
    public MentoriaResponse inactivar(@PathVariable Long idMentoria) {
        return mentoriaAdminService.inactivar(idMentoria);
    }

    @PatchMapping("/{idMentoria}/archivar")
    public MentoriaResponse archivar(@PathVariable Long idMentoria) {
        return mentoriaAdminService.archivar(idMentoria);
    }

    private EstadoRegistro parseEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return null;
        }
        for (EstadoRegistro value : EstadoRegistro.values()) {
            if (value.name().equalsIgnoreCase(estado.trim())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Estado de registro no valido: " + estado);
    }

    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}
