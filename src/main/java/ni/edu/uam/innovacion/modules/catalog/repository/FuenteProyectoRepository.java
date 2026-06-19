package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuenteProyectoRepository extends JpaRepository<FuenteProyecto, Long> {

    Optional<FuenteProyecto> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    List<FuenteProyecto> findAllByOrderByNombreAsc();

    List<FuenteProyecto> findByEstadoOrderByNombreAsc(EstadoRegistro estado);

    List<FuenteProyecto> findByCategoriaFuenteProyecto_IdOrderByNombreAsc(
            Long idCategoriaFuenteProyecto
    );

    List<FuenteProyecto> findByEstadoAndCategoriaFuenteProyecto_IdOrderByNombreAsc(
            EstadoRegistro estado,
            Long idCategoriaFuenteProyecto
    );
}