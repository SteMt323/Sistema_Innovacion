package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.CategoriaFuenteProyecto;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository encargado del acceso a datos del catálogo FuenteProyecto.
 */
public interface FuenteProyectoRepository extends JpaRepository<FuenteProyecto, Long> {

    /**
     * Busca una fuente de proyecto por su nombre
     */
    Optional<FuenteProyecto> findByNombreIgnoreCase(String nombre);

    /**
     * Verifica si ya existe una fuente de proyecto con ese nombre.
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Lista todas las fuentes de proyecto ordenadas alfabéticamente.
     */
    List<FuenteProyecto> findAllByOrderByNombreAsc();

    /**
     * Lista las fuentes de proyecto filtradas por estado.
     */
    List<FuenteProyecto> findByEstadoOrderByNombreAsc(EstadoRegistro estado);

    /**
     * Lista las fuentes de proyecto filtradas por categoría.
     */
    List<FuenteProyecto> findByCategoriaOrderByNombreAsc(CategoriaFuenteProyecto categoria);

    /**
     * Lista las fuentes de proyecto filtradas por categoría y estado.
     */
    List<FuenteProyecto> findByCategoriaAndEstadoOrderByNombreAsc(
            CategoriaFuenteProyecto categoria,
            EstadoRegistro estado
    );
}