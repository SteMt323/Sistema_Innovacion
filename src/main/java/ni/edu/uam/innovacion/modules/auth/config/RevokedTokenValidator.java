package ni.edu.uam.innovacion.modules.auth.config;

import ni.edu.uam.innovacion.modules.auth.service.TokenRevocationService;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class RevokedTokenValidator implements OAuth2TokenValidator<Jwt> {

    private static final OAuth2Error TOKEN_REVOCADO = new OAuth2Error(
        "invalid_token",
        "El token fue revocado",
        null
    );

    private final TokenRevocationService tokenRevocationService;

    public RevokedTokenValidator(TokenRevocationService tokenRevocationService) {
        this.tokenRevocationService = tokenRevocationService;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (tokenRevocationService.estaRevocado(jwt.getTokenValue())) {
            return OAuth2TokenValidatorResult.failure(TOKEN_REVOCADO);
        }
        return OAuth2TokenValidatorResult.success();
    }
}
