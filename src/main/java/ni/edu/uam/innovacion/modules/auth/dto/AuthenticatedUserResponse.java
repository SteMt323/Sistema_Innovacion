package ni.edu.uam.innovacion.modules.auth.dto;

import java.util.List;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;

public record AuthenticatedUserResponse(
    Long idUsuario,
    String nombreCompleto,
    String correo,
    EstadoUsuario estado,
    List<String> roles
) {
}
