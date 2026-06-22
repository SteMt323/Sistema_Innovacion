package ni.edu.uam.innovacion.modules.enrollment.controller;

import jakarta.validation.Valid;
import java.net.URI;
import ni.edu.uam.innovacion.common.exception.UnauthorizedException;
import ni.edu.uam.innovacion.modules.enrollment.dto.CrearInscripcionRequest;
import ni.edu.uam.innovacion.modules.enrollment.dto.InscripcionResponse;
import ni.edu.uam.innovacion.modules.enrollment.service.InscripcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de inscripcion self-service para el portal del participante.
 *
 * A diferencia del controller administrativo, el usuario inscrito se toma
 * siempre del token: se ignora cualquier idUsuario enviado en el cuerpo para
 * impedir que un usuario inscriba a otro.
 */
@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping
    public ResponseEntity<InscripcionResponse> inscribirse(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CrearInscripcionRequest request
    ) {
        Long idUsuario = obtenerIdUsuario(jwt);
        CrearInscripcionRequest inscripcionPropia = new CrearInscripcionRequest(
            idUsuario,
            request.idActividad(),
            request.observaciones()
        );

        InscripcionResponse response = inscripcionService.crear(inscripcionPropia);

        return ResponseEntity
            .created(URI.create("/api/inscripciones/" + response.idInscripcion()))
            .body(response);
    }

    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt == null ? null : jwt.getClaim("idUsuario");
        if (idUsuario == null) {
            throw new UnauthorizedException("Token sin identificador de usuario");
        }
        return idUsuario.longValue();
    }
}
