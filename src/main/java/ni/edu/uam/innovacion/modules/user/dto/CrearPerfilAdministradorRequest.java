package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearPerfilAdministradorRequest(
    @NotBlank @Size(max = 100) String cargo,
    @NotBlank @Size(max = 50) String nivelAcceso
) {
}
