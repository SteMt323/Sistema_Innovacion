package ni.edu.uam.innovacion.modules.user.repository;

import ni.edu.uam.innovacion.modules.user.entity.PerfilEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilEstudianteRepository extends JpaRepository<PerfilEstudiante, Long> {
    boolean existsByCifIgnoreCase(String cif);

    boolean existsByCorreoInstitucionalIgnoreCase(String correoInstitucional);
}
