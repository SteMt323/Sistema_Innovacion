package ni.edu.uam.innovacion.modules.auth.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtProperties jwtProperties;

    public SecurityConfig(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter
    ) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/actividades/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/usuarios/*/perfiles/administrador").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/usuarios/*/perfiles/mentor").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/puntos/usuario/*/resumen").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/participaciones/usuario/*").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/inscripciones").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) ->
                    writeError(
                        response,
                        HttpStatus.UNAUTHORIZED,
                        "Autenticacion requerida o token invalido"
                    )
                )
                .accessDeniedHandler((request, response, accessDeniedException) ->
                    writeError(
                        response,
                        HttpStatus.FORBIDDEN,
                        "No tiene permisos para acceder a este recurso"
                    )
                )
            )
            .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey()));
    }

    @Bean
    public JwtDecoder jwtDecoder(RevokedTokenValidator revokedTokenValidator) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey())
            .macAlgorithm(MacAlgorithm.HS256)
            .build();
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
            JwtValidators.createDefault(),
            revokedTokenValidator
        ));
        return decoder;
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) {
                return List.<GrantedAuthority>of();
            }

            return roles.stream()
                .map(rol -> rol == null ? "" : rol.trim().toUpperCase())
                .filter(rol -> !rol.isBlank())
                .map(rol -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + rol))
                .toList();
        });
        return converter;
    }

    private SecretKey secretKey() {
        return new SecretKeySpec(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    private void writeError(
        HttpServletResponse response,
        HttpStatus status,
        String message
    ) throws java.io.IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
            {"timestamp":"%s","status":%d,"error":"%s","message":"%s","details":{}}
            """.formatted(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message
        ));
    }
}
