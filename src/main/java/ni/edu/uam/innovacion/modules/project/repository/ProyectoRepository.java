package ni.edu.uam.innovacion.modules.project.repository;

import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    Optional<Proyecto> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    List<Proyecto> findAllByOrderByFechaRegistroDesc();

    List<Proyecto> findByEstadoOrderByFechaRegistroDesc(EstadoProyecto estado);

    List<Proyecto> findByFuenteProyecto_IdOrderByFechaRegistroDesc(Long idFuenteProyecto);

    List<Proyecto> findByEstadoAndFuenteProyecto_IdOrderByFechaRegistroDesc(
            EstadoProyecto estado,
            Long idFuenteProyecto
    );

    List<Proyecto> findByAdministradorRegistro_IdUsuarioOrderByFechaRegistroDesc(
            Long idAdministradorRegistro
    );
}