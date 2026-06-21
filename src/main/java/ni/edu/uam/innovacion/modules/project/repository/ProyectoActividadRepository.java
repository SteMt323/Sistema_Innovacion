package ni.edu.uam.innovacion.modules.project.repository;

import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.modules.project.entity.ProyectoActividad;
import ni.edu.uam.innovacion.modules.project.enums.TipoVinculoProyectoActividad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoActividadRepository extends JpaRepository<ProyectoActividad, Long> {

    Optional<ProyectoActividad> findByProyecto_IdProyectoAndActividad_IdActividad(
            Long idProyecto,
            Long idActividad
    );

    boolean existsByProyecto_IdProyectoAndActividad_IdActividad(
            Long idProyecto,
            Long idActividad
    );

    boolean existsByProyecto_IdProyectoAndEsActividadOrigenTrue(Long idProyecto);

    boolean existsByProyecto_IdProyectoAndEsActividadOrigenTrueAndIdProyectoActividadNot(
            Long idProyecto,
            Long idProyectoActividad
    );

    List<ProyectoActividad> findByProyecto_IdProyectoOrderByFechaVinculacionDesc(
            Long idProyecto
    );

    List<ProyectoActividad> findByActividad_IdActividadOrderByFechaVinculacionDesc(
            Long idActividad
    );

    List<ProyectoActividad> findByTipoVinculoOrderByFechaVinculacionDesc(
            TipoVinculoProyectoActividad tipoVinculo
    );

    List<ProyectoActividad> findByProyecto_IdProyectoAndTipoVinculoOrderByFechaVinculacionDesc(
            Long idProyecto,
            TipoVinculoProyectoActividad tipoVinculo
    );

    List<ProyectoActividad> findByProyecto_IdProyectoAndEsActividadOrigenTrue(
            Long idProyecto
    );

    List<ProyectoActividad> findByRegistradoPorAdmin_IdUsuarioOrderByFechaVinculacionDesc(
            Long idAdministrador
    );
}