package ni.edu.uam.innovacion;

import static org.assertj.core.api.Assertions.assertThat;

import ni.edu.uam.innovacion.modules.auth.config.JwtConfigurationValidator;
import ni.edu.uam.innovacion.modules.auth.config.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class JwtConfigurationValidationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withUserConfiguration(TestConfiguration.class, JwtConfigurationValidator.class);

    @Test
    void bindsJwtProperties() {
        contextRunner
            .withPropertyValues(
                "app.security.jwt.secret=12345678901234567890123456789012",
                "app.security.jwt.expiration-minutes=120"
            )
            .run(context -> {
                assertThat(context).hasNotFailed();
                JwtProperties properties = context.getBean(JwtProperties.class);
                assertThat(properties.getSecret()).isEqualTo("12345678901234567890123456789012");
                assertThat(properties.getExpirationMinutes()).isEqualTo(120);
            });
    }

    @Test
    void localProfileAceptaElSecretoDeDesarrollo() {
        contextRunner
            .withPropertyValues(
                "spring.profiles.active=local",
                "app.security.jwt.secret=" + JwtProperties.LOCAL_DEV_SECRET,
                "app.security.jwt.expiration-minutes=480"
            )
            .run(context -> assertThat(context).hasNotFailed());
    }

    @Test
    void prodFallaSiNoExisteJwtSecretComoVariableDeEntorno() {
        contextRunner
            .withPropertyValues(
                "spring.profiles.active=prod",
                "app.security.jwt.secret=12345678901234567890123456789012",
                "app.security.jwt.expiration-minutes=480"
            )
            .run(context -> {
                assertThat(context).hasFailed();
                assertThat(context.getStartupFailure())
                    .hasMessageContaining("El perfil 'prod' requiere la variable de entorno JWT_SECRET");
            });
    }

    @Test
    void prodFallaSiUsaElSecretoLocalPorDefecto() {
        contextRunner
            .withPropertyValues(
                "spring.profiles.active=prod",
                "JWT_SECRET=" + JwtProperties.LOCAL_DEV_SECRET,
                "app.security.jwt.secret=" + JwtProperties.LOCAL_DEV_SECRET,
                "app.security.jwt.expiration-minutes=480"
            )
            .run(context -> {
                assertThat(context).hasFailed();
                assertThat(context.getStartupFailure())
                    .hasMessageContaining("no puede usar el secreto local por defecto");
            });
    }

    @Test
    void fallaSiElSecretoEsMuyCorto() {
        contextRunner
            .withPropertyValues(
                "spring.profiles.active=local",
                "app.security.jwt.secret=secreto-corto",
                "app.security.jwt.expiration-minutes=480"
            )
            .run(context -> {
                assertThat(context).hasFailed();
                assertThat(context.getStartupFailure())
                    .hasMessageContaining("JWT_SECRET debe tener al menos 32 caracteres");
            });
    }

    @Test
    void fallaSiLaExpiracionNoEsValida() {
        contextRunner
            .withPropertyValues(
                "app.security.jwt.secret=12345678901234567890123456789012",
                "app.security.jwt.expiration-minutes=0"
            )
            .run(context -> {
                assertThat(context).hasFailed();
                assertThat(context.getStartupFailure())
                    .hasStackTraceContaining("app.security.jwt.expiration-minutes debe ser mayor que cero");
            });
    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(JwtProperties.class)
    static class TestConfiguration {
    }
}
