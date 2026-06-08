package ni.edu.uam.innovacion.modules.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "tokens_revocados")
public class TokenRevocado {

    @Id
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "revocado_en", nullable = false)
    private Instant revocadoEn;

    @Column(name = "expira_en", nullable = false)
    private Instant expiraEn;

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Instant getRevocadoEn() {
        return revocadoEn;
    }

    public void setRevocadoEn(Instant revocadoEn) {
        this.revocadoEn = revocadoEn;
    }

    public Instant getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(Instant expiraEn) {
        this.expiraEn = expiraEn;
    }
}
