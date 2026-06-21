package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.modules.project.enums.TipoVinculoProyectoActividad;

/**
 * DTO utilizado para vincular un proyecto con una actividad.
 *
 * Permite indicar si la actividad fue el origen del proyecto,
 * el tipo de vínculo y observaciones administrativas.
 */
public record CrearProyectoActividadRequest(

        @NotNull(message = "El proyecto es obligatorio")
        @Positive(message = "El id del proyecto debe ser positivo")
        Long idProyecto,

        @NotNull(message = "La actividad es obligatoria")
        @Positive(message = "El id de la actividad debe ser positivo")
        Long idActividad,

        @NotNull(message = "El tipo de vinculo es obligatorio")
        TipoVinculoProyectoActividad tipoVinculo,

        Boolean esActividadOrigen,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}