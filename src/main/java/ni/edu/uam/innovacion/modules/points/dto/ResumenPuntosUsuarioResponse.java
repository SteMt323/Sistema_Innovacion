package ni.edu.uam.innovacion.modules.points.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record ResumenPuntosUsuarioResponse(
    Long idUsuario,
    String nombreUsuario,
    @JsonProperty("totalActivo") long totalPuntosActivos,
    @JsonProperty("totalHistorico") long totalPuntosHistoricos,
    long totalMovimientos,
    @JsonProperty("movimientosActivos") long totalMovimientosActivos,
    long totalOtorgado,
    long totalDebitado,
    @JsonProperty("insignia") InsigniaResponse insigniaActual,
    LocalDateTime ultimaActualizacion,
    PuntoInnovacionResponse ultimoMovimiento
) {
}
