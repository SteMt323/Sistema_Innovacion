package ni.edu.uam.innovacion.modules.auth.controller;

import jakarta.validation.Valid;
import java.util.List;
import ni.edu.uam.innovacion.modules.auth.dto.AuthenticatedUserResponse;
import ni.edu.uam.innovacion.modules.auth.dto.LoginRequest;
import ni.edu.uam.innovacion.modules.auth.dto.LoginResponse;
import ni.edu.uam.innovacion.modules.auth.service.AuthService;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public AuthenticatedUserResponse me(@AuthenticationPrincipal Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        String estado = jwt.getClaimAsString("estado");
        List<String> roles = jwt.getClaimAsStringList("roles");

        return authService.toAuthenticatedUserResponseFromClaims(
            idUsuario.longValue(),
            jwt.getClaimAsString("nombreCompleto"),
            jwt.getClaimAsString("correo"),
            EstadoUsuario.fromValue(estado),
            roles == null ? List.of() : roles
        );
    }
}
