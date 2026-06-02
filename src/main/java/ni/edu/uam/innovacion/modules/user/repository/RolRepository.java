package ni.edu.uam.innovacion.modules.user.repository;

import java.util.Optional;
import ni.edu.uam.innovacion.modules.user.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
