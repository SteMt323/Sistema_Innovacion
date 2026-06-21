package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO para registrar un nuevo integrante dentro de un proyecto.
 *
 * El administrador no se recibe desde el body, porque se obtiene
 * desde el token JWT en el controller.
 */
public record CrearIntegranteProyectoRequest(

        @NotNull(message = "El id del proyecto es obligatorio")
        Long idProyecto,

        @NotNull(message = "El id del usuario es obligatorio")
        Long idUsuario,

        @NotNull(message = "El id del rol de proyecto es obligatorio")
        Long idRolProyecto,

        LocalDate fechaVinculacion,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}