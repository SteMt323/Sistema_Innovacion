package ni.edu.uam.innovacion.modules.points.dto;

import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.points.enums.EstadoPuntos;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;

public record PuntoInnovacionResponse(
    Long idPunto,
    Long idUsuario,
    String nombreUsuario,
    Long idParticipacion,
    Long idAdminAjuste,
    String nombreAdmin,
    Long idActividad,
    String nombreActividad,
    Integer cantidad,
    TipoMovimientoPuntos tipoMovimiento,
    String motivo,
    String origen,
    EstadoPuntos estado,
    LocalDateTime fechaAsignacion,
    LocalDateTime creadoEn
) {
}
