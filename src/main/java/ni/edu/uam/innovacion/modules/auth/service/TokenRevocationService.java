package ni.edu.uam.innovacion.modules.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import ni.edu.uam.innovacion.modules.auth.entity.TokenRevocado;
import ni.edu.uam.innovacion.modules.auth.repository.TokenRevocadoRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenRevocationService {

    private final TokenRevocadoRepository tokenRevocadoRepository;

    public TokenRevocationService(TokenRevocadoRepository tokenRevocadoRepository) {
        this.tokenRevocadoRepository = tokenRevocadoRepository;
    }

    @Transactional
    public void revocar(Jwt jwt) {
        Instant ahora = Instant.now();
        tokenRevocadoRepository.deleteByExpiraEnBefore(ahora);

        Number idUsuario = jwt.getClaim("idUsuario");
        TokenRevocado tokenRevocado = new TokenRevocado();
        tokenRevocado.setTokenHash(calcularHash(jwt.getTokenValue()));
        tokenRevocado.setIdUsuario(idUsuario.longValue());
        tokenRevocado.setRevocadoEn(ahora);
        tokenRevocado.setExpiraEn(jwt.getExpiresAt());
        tokenRevocadoRepository.save(tokenRevocado);
    }

    @Transactional(readOnly = true)
    public boolean estaRevocado(String token) {
        return tokenRevocadoRepository.existsById(calcularHash(token));
    }

    private String calcularHash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 no esta disponible", exception);
        }
    }
}
