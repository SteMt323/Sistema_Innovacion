package ni.edu.uam.innovacion.modules.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.modules.enrollment.enums.EstadoInscripcion;

/**
 * DTO para cambiar el estado de una inscripción.
 *
 * Puede usarse en endpoints administrativos cuando se necesite confirmar,
 * cancelar o rechazar una inscripción.
 */
public record CambiarEstadoInscripcionRequest(

        @NotNull(message = "El estado de la inscripción es obligatorio")
        EstadoInscripcion estado,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}