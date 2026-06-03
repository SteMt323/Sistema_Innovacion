package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaDIEM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CategoriaDIEMRepository extends JpaRepository<CategoriaDIEM, Long> {


    Optional<CategoriaDIEM> findByNombreIgnoreCase(String nombre);

    /**
     * Busca una categoría DIEM por nombre dentro de un ámbito específico.
     */
    Optional<CategoriaDIEM> findByNombreIgnoreCaseAndAmbitoActividad_Id(
            String nombre,
            Long idAmbitoActividad
    );

    /**
     * Verifica si ya existe una categoría con ese nombre.
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Verifica si ya existe una categoría con ese nombre dentro de un ámbito.
     */
    boolean existsByNombreIgnoreCaseAndAmbitoActividad_Id(
            String nombre,
            Long idAmbitoActividad
    );

    /**
     * Lista todas las categorías DIEM ordenadas alfabéticamente por nombre.
     */
    List<CategoriaDIEM> findAllByOrderByNombreAsc();

    /**
     * Lista las categorías DIEM filtradas por estado y ordenadas por nombre.
     */
    List<CategoriaDIEM> findByEstadoOrderByNombreAsc(EstadoRegistro estado);

    /**
     * Lista todas las categorías asociadas a un ámbito específico.
     */
    List<CategoriaDIEM> findByAmbitoActividad_IdOrderByNombreAsc(Long idAmbitoActividad);

    /**
     * Lista las categorías asociadas a un ámbito específico y filtradas por estado.
     */
    List<CategoriaDIEM> findByAmbitoActividad_IdAndEstadoOrderByNombreAsc(
            Long idAmbitoActividad,
            EstadoRegistro estado
    );

    /**
     * Lista categorías por el nombre del ámbito y por estado.
     */
    List<CategoriaDIEM> findByAmbitoActividad_NombreIgnoreCaseAndEstadoOrderByNombreAsc(
            String nombreAmbito,
            EstadoRegistro estado
    );
}