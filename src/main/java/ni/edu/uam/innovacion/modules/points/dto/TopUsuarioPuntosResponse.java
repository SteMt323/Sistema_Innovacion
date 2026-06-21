package ni.edu.uam.innovacion.modules.points.dto;

public record TopUsuarioPuntosResponse(
    Long idUsuario,
    String nombreUsuario,
    long totalPuntos,
    InsigniaResponse insignia
) {
}
