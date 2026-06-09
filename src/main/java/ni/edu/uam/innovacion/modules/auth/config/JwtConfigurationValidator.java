package ni.edu.uam.innovacion.modules.auth.config;

import java.util.Arrays;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtConfigurationValidator implements InitializingBean {

    private static final int MIN_SECRET_LENGTH = 32;

    private final JwtProperties jwtProperties;
    private final Environment environment;

    public JwtConfigurationValidator(JwtProperties jwtProperties, Environment environment) {
        this.jwtProperties = jwtProperties;
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() {
        String secret = jwtProperties.getSecret();
        if (!StringUtils.hasText(secret)) {
            throw new IllegalStateException(
                "Falta configurar JWT_SECRET. Definelo en Backend/Sistema_Innovacion/.env "
                    + "para desarrollo local o como variable de entorno antes de iniciar el backend."
            );
        }

        String normalizedSecret = secret.trim();
        if (normalizedSecret.length() < MIN_SECRET_LENGTH) {
            throw new IllegalStateException(
                "JWT_SECRET debe tener al menos %d caracteres. Ajusta el valor antes de iniciar el backend."
                    .formatted(MIN_SECRET_LENGTH)
            );
        }

        if (!isProdProfileActive()) {
            return;
        }

        String jwtSecretFromEnv = environment.getProperty("JWT_SECRET");
        if (!StringUtils.hasText(jwtSecretFromEnv)) {
            throw new IllegalStateException(
                "El perfil 'prod' requiere la variable de entorno JWT_SECRET. "
                    + "Definela antes de iniciar el backend."
            );
        }

        if (JwtProperties.LOCAL_DEV_SECRET.equals(normalizedSecret)) {
            throw new IllegalStateException(
                "El perfil 'prod' no puede usar el secreto local por defecto. "
                    + "Configura JWT_SECRET con un valor unico y seguro."
            );
        }
    }

    private boolean isProdProfileActive() {
        return Arrays.stream(environment.getActiveProfiles())
            .anyMatch(profile -> "prod".equalsIgnoreCase(profile));
    }
}
