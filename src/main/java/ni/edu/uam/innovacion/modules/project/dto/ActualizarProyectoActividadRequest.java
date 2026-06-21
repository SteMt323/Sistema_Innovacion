package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.modules.project.enums.TipoVinculoProyectoActividad;

/**
 * DTO utilizado para actualizar la información de una relación
 * entre un proyecto y una actividad.
 *
 * No permite cambiar el proyecto ni la actividad vinculada,
 * porque esa relación identifica el vínculo principal.
 */
public record ActualizarProyectoActividadRequest(

        @NotNull(message = "El tipo de vinculo es obligatorio")
        TipoVinculoProyectoActividad tipoVinculo,

        Boolean esActividadOrigen,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}