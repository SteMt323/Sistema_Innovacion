package ni.edu.uam.innovacion.modules.auth.config;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.security.jwt")
@Validated
public class JwtProperties {

    public static final String LOCAL_DEV_SECRET =
        "dev-secret-for-local-development-only-change-me-1234567890";

    private String secret;

    @Min(value = 1, message = "app.security.jwt.expiration-minutes debe ser mayor que cero")
    private long expirationMinutes = 480;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMinutes() {
        return expirationMinutes;
    }

    public void setExpirationMinutes(long expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
    }
}
