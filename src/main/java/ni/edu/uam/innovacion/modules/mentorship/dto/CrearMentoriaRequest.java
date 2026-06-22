package ni.edu.uam.innovacion.modules.mentorship.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record CrearMentoriaRequest(
    @NotNull(message = "El id de la actividad es obligatorio")
    @Positive(message = "El id de la actividad debe ser positivo")
    Long idActividad,
    @NotNull(message = "El id del mentor es obligatorio")
    @Positive(message = "El id del mentor debe ser positivo")
    Long idMentor,
    LocalDateTime fechaAsignacion,
    @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
    String observaciones
) {
}
