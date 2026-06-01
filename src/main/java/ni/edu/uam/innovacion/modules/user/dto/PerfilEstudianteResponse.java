package ni.edu.uam.innovacion.modules.user.dto;

public record PerfilEstudianteResponse(
    Long idUsuario,
    String cif,
    String correoInstitucional,
    Long idCarreraPrincipal,
    Boolean dobleTitular
) {
}
