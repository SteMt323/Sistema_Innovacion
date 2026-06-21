package ni.edu.uam.innovacion.modules.participation.dto;

import jakarta.validation.constraints.Size;

public record ProcesarParticipacionRequest(

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}