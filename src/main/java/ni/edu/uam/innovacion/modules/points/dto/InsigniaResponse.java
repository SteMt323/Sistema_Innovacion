package ni.edu.uam.innovacion.modules.points.dto;

public record InsigniaResponse(
    String codigo,
    String nombre,
    String etiqueta,
    String color,
    long puntosMinimos,
    long totalPuntos
) {
}
