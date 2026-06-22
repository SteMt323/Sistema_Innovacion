package ni.edu.uam.innovacion.modules.points.controller;

import java.util.List;
import ni.edu.uam.innovacion.common.exception.ApiException;
import ni.edu.uam.innovacion.modules.points.dto.ResumenPuntosUsuarioResponse;
import ni.edu.uam.innovacion.modules.points.service.PuntoInnovacionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de puntos accesibles por el propio usuario (portal del participante).
 *
 * Replican la lectura del controller administrativo pero validando que el
 * solicitante consulte unicamente sus propios datos, salvo que sea administrador.
 */
@RestController
@RequestMapping("/api/puntos")
public class PuntoInnovacionController {

    private final PuntoInnovacionService puntoService;

    public PuntoInnovacionController(PuntoInnovacionService puntoService) {
        this.puntoService = puntoService;
    }

    @GetMapping("/usuario/{idUsuario}/resumen")
    public ResumenPuntosUsuarioResponse obtenerResumenPropio(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable Long idUsuario
    ) {
        validarAccesoPropioOAdmin(jwt, idUsuario);
        return puntoService.obtenerResumenUsuario(idUsuario);
    }

    private void validarAccesoPropioOAdmin(Jwt jwt, Long idUsuario) {
        Number idClaim = jwt.getClaim("idUsuario");
        Long idSolicitante = idClaim == null ? null : idClaim.longValue();
        List<String> roles = jwt.getClaimAsStringList("roles");
        boolean esAdmin = roles != null && roles.stream()
            .anyMatch(rol -> rol != null && rol.equalsIgnoreCase("administrador"));

        if (!esAdmin && !idUsuario.equals(idSolicitante)) {
            throw new ApiException(
                HttpStatus.FORBIDDEN,
                "No puede consultar los puntos de otro usuario"
            );
        }
    }
}
