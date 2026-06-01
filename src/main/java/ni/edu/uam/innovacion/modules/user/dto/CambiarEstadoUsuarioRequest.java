package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.NotNull;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;

public record CambiarEstadoUsuarioRequest(
    @NotNull EstadoUsuario estado
) {
}
