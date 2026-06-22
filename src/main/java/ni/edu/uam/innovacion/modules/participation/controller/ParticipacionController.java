package ni.edu.uam.innovacion.modules.participation.controller;

import java.util.List;
import ni.edu.uam.innovacion.common.exception.ApiException;
import ni.edu.uam.innovacion.modules.participation.dto.ParticipacionResponse;
import ni.edu.uam.innovacion.modules.participation.service.ParticipacionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de participaciones accesibles por el propio usuario (portal del participante).
 *
 * El historial solo puede consultarlo el dueño de las participaciones,
 * salvo que el solicitante sea administrador.
 */
@RestController
@RequestMapping("/api/participaciones")
public class ParticipacionController {

    private final ParticipacionService participacionService;

    public ParticipacionController(ParticipacionService participacionService) {
        this.participacionService = participacionService;
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<ParticipacionResponse> listarPropias(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable Long idUsuario
    ) {
        validarAccesoPropioOAdmin(jwt, idUsuario);
        return participacionService.listarPorUsuario(idUsuario);
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
                "No puede consultar las participaciones de otro usuario"
            );
        }
    }
}
