package ni.edu.uam.innovacion.modules.participation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ActualizarParticipacionRequest(

        @NotNull(message = "El rol de participación es obligatorio")
        @Positive(message = "El id del rol de participación debe ser positivo")
        Long idRolParticipacion,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}