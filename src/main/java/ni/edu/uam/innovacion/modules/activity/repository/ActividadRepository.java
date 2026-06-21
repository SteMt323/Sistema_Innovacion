package ni.edu.uam.innovacion.modules.activity.repository;

import java.util.List;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findAllByOrderByFechaInicioDesc();

    List<Actividad> findByEstadoOrderByFechaInicioAsc(EstadoActividad estado);

    long countByEstado(EstadoActividad estado);
}
