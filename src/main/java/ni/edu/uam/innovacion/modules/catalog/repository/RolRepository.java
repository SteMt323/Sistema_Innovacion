package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.Rol;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a la tabla roles.
 *
 * El repository es la capa que se comunica directamente
 * con la base de datos mediante Spring Data JPA.
 */
public interface RolRepository extends JpaRepository<Rol, Long> {


    Optional<Rol> findByNombreIgnoreCase(String nombre);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select rol from Rol rol where lower(rol.nombre) = lower(:nombre)")
    Optional<Rol> findByNombreIgnoreCaseForUpdate(@Param("nombre") String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    List<Rol> findAllByOrderByNombreAsc();

    List<Rol> findByEstadoOrderByNombreAsc(EstadoRegistro estado);
}