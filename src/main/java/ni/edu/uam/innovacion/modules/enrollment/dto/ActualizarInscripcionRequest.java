package ni.edu.uam.innovacion.modules.enrollment.dto;

import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar datos editables de una inscripción.
 *
 * No se permite cambiar usuario ni actividad desde este DTO,
 * porque eso alteraría la relación principal de la inscripción.
 */
public record ActualizarInscripcionRequest(

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}