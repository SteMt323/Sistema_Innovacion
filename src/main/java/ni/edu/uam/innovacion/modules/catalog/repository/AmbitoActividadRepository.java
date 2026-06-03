package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface AmbitoActividadRepository extends JpaRepository<AmbitoActividad, Long> {

    Optional<AmbitoActividad> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
    List<AmbitoActividad> findAllByOrderByNombreAsc();

    List<AmbitoActividad> findByEstadoOrderByNombreAsc(EstadoRegistro estado);
    List<AmbitoActividad> findByRequiereCategoriaOrderByNombreAsc(Boolean requiereCategoria);
    List<AmbitoActividad> findByEstadoAndRequiereCategoriaOrderByNombreAsc(
            EstadoRegistro estado,
            Boolean requiereCategoria
    );
}