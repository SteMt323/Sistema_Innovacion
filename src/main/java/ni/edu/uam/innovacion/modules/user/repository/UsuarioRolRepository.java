package ni.edu.uam.innovacion.modules.user.repository;

import java.util.Optional;
import ni.edu.uam.innovacion.modules.user.entity.UsuarioRol;
import ni.edu.uam.innovacion.modules.user.entity.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
    Optional<UsuarioRol> findByUsuarioIdUsuarioAndRolNombreIgnoreCase(Long idUsuario, String nombreRol);

    boolean existsByUsuarioIdUsuarioAndRolNombreIgnoreCaseAndActivoTrue(Long idUsuario, String nombreRol);
}
