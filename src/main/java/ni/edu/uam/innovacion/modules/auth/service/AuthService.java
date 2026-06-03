package ni.edu.uam.innovacion.modules.auth.service;

import java.time.Instant;
import java.util.List;
import ni.edu.uam.innovacion.common.exception.UnauthorizedException;
import ni.edu.uam.innovacion.modules.auth.dto.AuthenticatedUserResponse;
import ni.edu.uam.innovacion.modules.auth.dto.LoginRequest;
import ni.edu.uam.innovacion.modules.auth.dto.LoginResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.Rol;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.entity.UsuarioRol;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;

    public AuthService(
        UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder,
        JwtEncoder jwtEncoder,
        @Value("${app.security.jwt.expiration-minutes}") long expirationMinutes
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.expirationMinutes = expirationMinutes;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String correo = normalizarCorreo(request.correo());
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
            .orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));

        if (!passwordEncoder.matches(request.contrasena(), usuario.getContrasenaHash())) {
            throw new UnauthorizedException("Credenciales invalidas");
        }

        if (!EstadoUsuario.ACTIVO.equals(usuario.getEstado())) {
            throw new UnauthorizedException("El usuario no esta activo");
        }

        List<String> roles = rolesActivos(usuario);
        Instant emitidoEn = Instant.now();
        Instant expiraEn = emitidoEn.plusSeconds(expirationMinutes * 60);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("sistema-innovacion-uam")
            .issuedAt(emitidoEn)
            .expiresAt(expiraEn)
            .subject(usuario.getIdUsuario().toString())
            .claim("idUsuario", usuario.getIdUsuario())
            .claim("correo", usuario.getCorreo())
            .claim("nombreCompleto", usuario.getNombreCompleto())
            .claim("estado", usuario.getEstado().getValor())
            .claim("roles", roles)
            .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new LoginResponse(
            token,
            TOKEN_TYPE,
            expirationMinutes * 60,
            toAuthenticatedUserResponse(usuario, roles)
        );
    }

    public AuthenticatedUserResponse toAuthenticatedUserResponse(Usuario usuario) {
        return toAuthenticatedUserResponse(usuario, rolesActivos(usuario));
    }

    public AuthenticatedUserResponse toAuthenticatedUserResponse(Usuario usuario, List<String> roles) {
        return new AuthenticatedUserResponse(
            usuario.getIdUsuario(),
            usuario.getNombreCompleto(),
            usuario.getCorreo(),
            usuario.getEstado(),
            roles
        );
    }

    public AuthenticatedUserResponse toAuthenticatedUserResponseFromClaims(
        Long idUsuario,
        String nombreCompleto,
        String correo,
        EstadoUsuario estado,
        List<String> roles
    ) {
        return new AuthenticatedUserResponse(idUsuario, nombreCompleto, correo, estado, roles);
    }

    private List<String> rolesActivos(Usuario usuario) {
        return usuario.getUsuarioRoles().stream()
            .filter(usuarioRol -> Boolean.TRUE.equals(usuarioRol.getActivo()))
            .map(UsuarioRol::getRol)
            .filter(Rol::estaActivo)
            .map(Rol::getNombre)
            .map(this::normalizarRol)
            .sorted()
            .toList();
    }

    private String normalizarCorreo(String correo) {
        return correo == null ? null : correo.trim().toLowerCase();
    }

    private String normalizarRol(String rol) {
        return rol == null ? null : rol.trim().toLowerCase();
    }
}
