package ni.edu.uam.innovacion.modules.auth.repository;

import java.time.Instant;
import ni.edu.uam.innovacion.modules.auth.entity.TokenRevocado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRevocadoRepository extends JpaRepository<TokenRevocado, String> {

    void deleteByExpiraEnBefore(Instant instante);
}
