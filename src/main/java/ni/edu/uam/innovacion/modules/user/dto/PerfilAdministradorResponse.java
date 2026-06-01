package ni.edu.uam.innovacion.modules.user.dto;

public record PerfilAdministradorResponse(
    Long idUsuario,
    String cargo,
    String nivelAcceso
) {
}
