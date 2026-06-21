package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;

/**
 * DTO utilizado para actualizar los datos generales
 * de un proyecto registrado en el Programa PIA.
 *
 * Los cambios de estado se deben manejar mediante métodos
 * específicos del service.
 */
public record ActualizarProyectoPIARequest(

        @NotNull(message = "La fase actual del proyecto PIA es obligatoria")
        FasePIA faseActual,

        @NotNull(message = "La fecha de ingreso es obligatoria")
        LocalDate fechaIngreso,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}