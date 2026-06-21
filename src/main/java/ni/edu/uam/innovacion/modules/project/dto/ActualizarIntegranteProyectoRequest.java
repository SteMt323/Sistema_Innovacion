package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO para actualizar los datos editables de un integrante de proyecto.
 *
 * No se permite cambiar el proyecto ni el usuario desde este DTO,
 * porque eso alteraría la relación principal. Para corregir un integrante
 * mal registrado, lo recomendable sería inactivar o archivar el registro
 * y crear uno nuevo.
 */
public record ActualizarIntegranteProyectoRequest(

        @NotNull(message = "El id del rol de proyecto es obligatorio")
        Long idRolProyecto,

        LocalDate fechaVinculacion,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}