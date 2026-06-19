package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaFuenteProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaFuenteProyectoRepository extends JpaRepository<CategoriaFuenteProyecto, Long> {

    Optional<CategoriaFuenteProyecto> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    List<CategoriaFuenteProyecto> findAllByOrderByNombreAsc();

    List<CategoriaFuenteProyecto> findByEstadoOrderByNombreAsc(EstadoRegistro estado);
}