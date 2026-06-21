package ni.edu.uam.innovacion.modules.dashboard.dto;

import java.util.List;
import ni.edu.uam.innovacion.modules.points.dto.PuntoInnovacionResponse;
import ni.edu.uam.innovacion.modules.points.dto.TopUsuarioPuntosResponse;

public record AdminDashboardResponse(
    long usuariosRegistrados,
    long usuariosActivos,
    long actividadesTotales,
    long actividadesFinalizadas,
    long inscripcionesTotales,
    long inscripcionesConfirmadas,
    long participacionesValidadas,
    long participacionesNoValidadas,
    long puntosActivosOtorgados,
    long movimientosPuntosAnulados,
    List<TopUsuarioPuntosResponse> topUsuariosPuntos,
    List<PuntoInnovacionResponse> movimientosRecientes
) {
}
