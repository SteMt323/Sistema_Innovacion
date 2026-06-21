package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para actualizar los datos editables de una asignación de mentor.
 *
 * No se permite cambiar el proyecto ni el mentor desde este DTO,
 * porque esa es la relación principal del registro.
 *
 * Si se asignó un mentor incorrecto, lo más recomendable es cancelar
 * la asignación y crear una nueva.
 */
public record ActualizarAsignacionMentorProyectoRequest(

        LocalDateTime fechaAsignacion,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}