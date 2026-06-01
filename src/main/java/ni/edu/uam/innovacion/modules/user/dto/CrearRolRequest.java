package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearRolRequest(
    @NotBlank @Size(max = 50) String nombre,
    @Size(max = 255) String descripcion
) {
}
