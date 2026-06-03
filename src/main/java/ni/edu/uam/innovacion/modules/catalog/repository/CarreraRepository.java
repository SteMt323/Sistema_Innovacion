package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    Optional<Carrera> findByNombreIgnoreCase(String nombre);
    Optional<Carrera> findByCodigoIgnoreCase(String codigo);

    Optional<Carrera> findByNombreIgnoreCaseAndFacultad_Id(String nombre, Long idFacultad);
    /**
     * Verifica si ya existe una carrera con ese código.
     */
    boolean existsByNombreIgnoreCase(String nombre);
    /**
     * Verifica si ya existe una carrera con ese nombre dentro de una facultad.
     */
    boolean existsByNombreIgnoreCaseAndFacultad_Id(String nombre, Long idFacultad);

    boolean existsByCodigoIgnoreCase(String codigo);
    List<Carrera> findAllByOrderByNombreAsc();
    List<Carrera> findByEstadoOrderByNombreAsc(EstadoRegistro estado);
    List<Carrera> findByFacultad_IdOrderByNombreAsc(Long idFacultad);
    List<Carrera> findByFacultad_IdAndEstadoOrderByNombreAsc(Long idFacultad, EstadoRegistro estado);









}
