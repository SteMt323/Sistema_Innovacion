package ni.edu.uam.innovacion.modules.user.dto;

public record PerfilParticipanteExternoResponse(
    Long idUsuario,
    String ocupacion,
    String institucionProcedencia
) {
}
