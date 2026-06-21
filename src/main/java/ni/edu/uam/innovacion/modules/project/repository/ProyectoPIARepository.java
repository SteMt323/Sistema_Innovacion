package ni.edu.uam.innovacion.modules.project.repository;

import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.modules.project.entity.ProyectoPIA;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyectoPIA;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoPIARepository extends JpaRepository<ProyectoPIA, Long> {

    Optional<ProyectoPIA> findByProyecto_IdProyecto(Long idProyecto);

    boolean existsByProyecto_IdProyecto(Long idProyecto);

    List<ProyectoPIA> findAllByOrderByFechaIngresoDesc();

    List<ProyectoPIA> findByEstadoOrderByFechaIngresoDesc(EstadoProyectoPIA estado);

    List<ProyectoPIA> findByFaseActualOrderByFechaIngresoDesc(FasePIA faseActual);

    List<ProyectoPIA> findByEstadoAndFaseActualOrderByFechaIngresoDesc(
            EstadoProyectoPIA estado,
            FasePIA faseActual
    );

    List<ProyectoPIA> findByRegistradoPorAdmin_IdUsuarioOrderByFechaIngresoDesc(
            Long idAdministrador
    );
}