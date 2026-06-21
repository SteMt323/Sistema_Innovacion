package ni.edu.uam.innovacion.modules.points.dto;

public record InsigniaResponse(
    String codigo,
    String nombre,
    String color,
    long totalPuntos
) {
}
