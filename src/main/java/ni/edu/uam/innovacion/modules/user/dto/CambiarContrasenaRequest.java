package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambiarContrasenaRequest(
    @NotBlank @Size(min = 6, max = 100) String contrasena
) {
}
