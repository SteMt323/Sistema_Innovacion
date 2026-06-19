package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * DTO utilizado para actualizar los datos generales
 * de un proyecto registrado.
 *
 * No incluye el estado del proyecto porque los cambios de estado
 * se manejarán mediante endpoints específicos.
 */
public record ActualizarProyectoRequest(

        @NotNull(message = "La fuente del proyecto es obligatoria")
        @Positive(message = "El id de la fuente del proyecto debe ser positivo")
        Long idFuenteProyecto,

        @NotBlank(message = "El nombre del proyecto es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String nombre,

        String descripcion,

        LocalDate fechaInicio,

        LocalDate fechaFin

) {
}