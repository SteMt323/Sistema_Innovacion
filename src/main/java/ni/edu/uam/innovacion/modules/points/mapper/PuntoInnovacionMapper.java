package ni.edu.uam.innovacion.modules.points.mapper;

import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.participation.entity.Participacion;
import ni.edu.uam.innovacion.modules.points.dto.PuntoInnovacionResponse;
import ni.edu.uam.innovacion.modules.points.entity.PuntoInnovacion;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;

public final class PuntoInnovacionMapper {

    private PuntoInnovacionMapper() {
    }

    public static PuntoInnovacionResponse toResponse(PuntoInnovacion punto) {
        Participacion participacion = punto.getParticipacion();
        Actividad actividad = participacion == null
            ? null
            : participacion.getInscripcion().getActividad();
        PerfilAdministrador admin = punto.getAdminAjuste();

        return new PuntoInnovacionResponse(
            punto.getIdPunto(),
            punto.getUsuario().getIdUsuario(),
            punto.getUsuario().getNombreCompleto(),
            participacion == null ? null : participacion.getIdParticipacion(),
            admin == null ? null : admin.getIdUsuario(),
            admin == null ? null : admin.getUsuario().getNombreCompleto(),
            actividad == null ? null : actividad.getIdActividad(),
            actividad == null ? null : actividad.getNombre(),
            punto.getCantidad(),
            punto.getTipoMovimiento(),
            punto.getMotivo(),
            punto.getOrigen(),
            punto.getEstado(),
            punto.getFechaAsignacion(),
            punto.getCreadoEn()
        );
    }
}
