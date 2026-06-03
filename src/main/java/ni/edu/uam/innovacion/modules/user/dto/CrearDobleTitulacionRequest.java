package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrearDobleTitulacionRequest(
    @NotNull @Positive Long idCarreraSecundaria
) {
}
