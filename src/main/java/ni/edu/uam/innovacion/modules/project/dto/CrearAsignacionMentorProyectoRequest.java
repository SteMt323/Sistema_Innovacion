package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para registrar una nueva asignación de mentor a proyecto.
 *
 * El administrador no se recibe desde el body, porque se obtiene
 * desde el token JWT en el controller.
 */
public record CrearAsignacionMentorProyectoRequest(

        @NotNull(message = "El id del proyecto es obligatorio")
        @Positive(message = "El id del proyecto debe ser positivo")
        Long idProyecto,

        @NotNull(message = "El id del mentor es obligatorio")
        @Positive(message = "El id del mentor debe ser positivo")
        Long idMentor,

        LocalDateTime fechaAsignacion,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}