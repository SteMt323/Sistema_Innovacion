package ni.edu.uam.innovacion.modules.points.dto;

import java.time.LocalDateTime;

public record ResumenPuntosUsuarioResponse(
    Long idUsuario,
    String nombreUsuario,
    long totalPuntosActivos,
    long totalPuntosHistoricos,
    long totalMovimientos,
    long totalMovimientosActivos,
    long totalOtorgado,
    long totalDebitado,
    InsigniaResponse insigniaActual,
    LocalDateTime ultimaActualizacion,
    PuntoInnovacionResponse ultimoMovimiento
) {
}
