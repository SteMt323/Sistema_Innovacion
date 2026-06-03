package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.RolParticipacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolParticipacionRepository extends JpaRepository<RolParticipacion, Long> {


    Optional<RolParticipacion> findByNombreIgnoreCase(String nombre);


    boolean existsByNombreIgnoreCase(String nombre);

    List<RolParticipacion> findAllByOrderByNombreAsc();

    List<RolParticipacion> findByEstadoOrderByNombreAsc(EstadoRegistro estado);
}