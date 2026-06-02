package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AsignarRolRequest(
    @NotBlank @Size(max = 50) String nombreRol
) {
}
