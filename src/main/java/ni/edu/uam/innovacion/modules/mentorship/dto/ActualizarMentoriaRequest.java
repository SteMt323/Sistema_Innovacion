package ni.edu.uam.innovacion.modules.mentorship.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record ActualizarMentoriaRequest(
    LocalDateTime fechaAsignacion,
    @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
    String observaciones
) {
}
