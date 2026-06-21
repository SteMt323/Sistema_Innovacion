package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;

public record CrearHistorialFasePIARequest(

        @NotNull(message = "El proyecto PIA es obligatorio")
        @Positive(message = "El id del proyecto PIA debe ser positivo")
        Long idProyectoPIA,

        @NotNull(message = "La fase es obligatoria")
        FasePIA fase,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        LocalDate fechaFin,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}