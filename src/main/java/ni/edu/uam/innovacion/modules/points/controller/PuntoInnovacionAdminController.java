package ni.edu.uam.innovacion.modules.points.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import ni.edu.uam.innovacion.modules.points.dto.CrearAjustePuntosRequest;
import ni.edu.uam.innovacion.modules.points.dto.PuntoInnovacionResponse;
import ni.edu.uam.innovacion.modules.points.dto.ResumenPuntosUsuarioResponse;
import ni.edu.uam.innovacion.modules.points.enums.EstadoPuntos;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;
import ni.edu.uam.innovacion.modules.points.service.PuntoInnovacionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/puntos")
public class PuntoInnovacionAdminController {

    private final PuntoInnovacionService puntoService;

    public PuntoInnovacionAdminController(PuntoInnovacionService puntoService) {
        this.puntoService = puntoService;
    }

    @GetMapping
    public List<PuntoInnovacionResponse> listar(
        @RequestParam(required = false) Long idUsuario,
        @RequestParam(required = false) Long idParticipacion,
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta
    ) {
        return puntoService.listar(
            idUsuario,
            idParticipacion,
            TipoMovimientoPuntos.fromValue(tipo),
            EstadoPuntos.fromValue(estado),
            fechaDesde,
            fechaHasta
        );
    }

    @GetMapping("/usuario/{idUsuario}/resumen")
    public ResumenPuntosUsuarioResponse obtenerResumenUsuario(@PathVariable Long idUsuario) {
        return puntoService.obtenerResumenUsuario(idUsuario);
    }

    @PostMapping("/ajustes")
    public ResponseEntity<PuntoInnovacionResponse> crearAjuste(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CrearAjustePuntosRequest request
    ) {
        PuntoInnovacionResponse response = puntoService.crearAjuste(
            request,
            obtenerIdUsuario(jwt)
        );
        return ResponseEntity
            .created(URI.create("/api/admin/puntos/" + response.idPunto()))
            .body(response);
    }

    @PatchMapping("/{idPunto}/anular")
    public PuntoInnovacionResponse anular(@PathVariable Long idPunto) {
        return puntoService.anular(idPunto);
    }

    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}
