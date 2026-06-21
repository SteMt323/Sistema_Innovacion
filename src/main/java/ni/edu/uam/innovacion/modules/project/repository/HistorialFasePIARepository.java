package ni.edu.uam.innovacion.modules.project.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.modules.project.entity.HistorialFasePIA;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialFasePIARepository extends JpaRepository<HistorialFasePIA, Long> {

    List<HistorialFasePIA> findAllByOrderByFechaInicioDescCreadoEnDesc();

    List<HistorialFasePIA> findByProyectoPIA_IdProyectoPIAOrderByFechaInicioDescCreadoEnDesc(
            Long idProyectoPIA
    );

    List<HistorialFasePIA> findByProyectoPIA_Proyecto_IdProyectoOrderByFechaInicioDescCreadoEnDesc(
            Long idProyecto
    );

    List<HistorialFasePIA> findByFaseOrderByFechaInicioDescCreadoEnDesc(
            FasePIA fase
    );

    List<HistorialFasePIA> findByRegistradoPorAdmin_IdUsuarioOrderByFechaInicioDescCreadoEnDesc(
            Long idAdministrador
    );

    Optional<HistorialFasePIA> findFirstByProyectoPIA_IdProyectoPIAAndFechaFinIsNullOrderByFechaInicioDescCreadoEnDesc(
            Long idProyectoPIA
    );

    boolean existsByProyectoPIA_IdProyectoPIAAndFechaFinIsNull(
            Long idProyectoPIA
    );

    boolean existsByProyectoPIA_IdProyectoPIAAndFaseAndFechaInicio(
            Long idProyectoPIA,
            FasePIA fase,
            LocalDate fechaInicio
    );
}