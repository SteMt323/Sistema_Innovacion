package ni.edu.uam.innovacion.modules.project.dto;

import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.project.enums.TipoVinculoProyectoActividad;

/**
 * DTO utilizado para devolver la información de la relación
 * entre un proyecto y una actividad.
 */
public record ProyectoActividadResponse(
        Long idProyectoActividad,

        Long idProyecto,
        String nombreProyecto,

        Long idActividad,
        String nombreActividad,

        TipoVinculoProyectoActividad tipoVinculo,
        Boolean esActividadOrigen,

        LocalDateTime fechaVinculacion,

        Long idRegistradoPorAdmin,
        String nombreRegistradoPorAdmin,

        String observaciones
) {
}