package ni.edu.uam.innovacion.modules.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO para registrar una nueva inscripción.
 *
 * El usuario y la actividad son obligatorios porque la inscripción
 * representa la relación entre ambos.
 */
public record CrearInscripcionRequest(

        @NotNull(message = "El usuario es obligatorio")
        @Positive(message = "El id del usuario debe ser positivo")
        Long idUsuario,

        @NotNull(message = "La actividad es obligatoria")
        @Positive(message = "El id de la actividad debe ser positivo")
        Long idActividad,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}