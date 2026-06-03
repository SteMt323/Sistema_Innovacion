package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacultadRepository extends JpaRepository<Facultad, Long> {

    Optional<Facultad> findByNombreIgnoreCase(String nombre);
    Optional<Facultad> findByCodigoIgnoreCase(String codigo);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByCodigoIgnoreCase(String codigo);
    List<Facultad> findAllByOrderByNombreAsc();
    List<Facultad> findByEstadoOrderByNombreAsc(EstadoRegistro estado);

}
