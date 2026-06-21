package ni.edu.uam.innovacion.modules.participation.mapper;

import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.catalog.entity.RolParticipacion;
import ni.edu.uam.innovacion.modules.enrollment.entity.Inscripcion;
import ni.edu.uam.innovacion.modules.participation.dto.ActualizarParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.dto.CrearParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.dto.ParticipacionResponse;
import ni.edu.uam.innovacion.modules.participation.entity.Participacion;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

public class ParticipacionMapper {

    private ParticipacionMapper() {
    }

    public static Participacion toEntity(
            CrearParticipacionRequest request,
            Inscripcion inscripcion,
            RolParticipacion rolParticipacion
    ) {
        Participacion participacion = new Participacion();
        participacion.setInscripcion(inscripcion);
        participacion.setRolParticipacion(rolParticipacion);
        participacion.setObservaciones(request.observaciones());

        return participacion;
    }

    public static void updateEntity(
            Participacion participacion,
            ActualizarParticipacionRequest request,
            RolParticipacion rolParticipacion
    ) {
        participacion.setRolParticipacion(rolParticipacion);
        participacion.setObservaciones(request.observaciones());
    }

    public static ParticipacionResponse toResponse(Participacion participacion) {
        Inscripcion inscripcion = participacion.getInscripcion();
        Usuario usuario = inscripcion.getUsuario();
        Actividad actividad = inscripcion.getActividad();
        RolParticipacion rolParticipacion = participacion.getRolParticipacion();
        PerfilAdministrador administrador = participacion.getValidadoPorAdmin();

        Usuario usuarioAdministrador = administrador == null ? null : administrador.getUsuario();

        return new ParticipacionResponse(
                participacion.getIdParticipacion(),

                inscripcion.getIdInscripcion(),

                usuario.getIdUsuario(),
                usuario.getNombreCompleto(),
                usuario.getCorreo(),

                actividad.getIdActividad(),
                actividad.getNombre(),

                rolParticipacion.getId(),
                rolParticipacion.getNombre(),

                participacion.getEstado(),

                participacion.getFechaValidacion(),

                administrador == null ? null : administrador.getIdUsuario(),
                usuarioAdministrador == null ? null : usuarioAdministrador.getNombreCompleto(),

                participacion.getObservaciones(),

                participacion.getCreadoEn(),
                participacion.getActualizadoEn()
        );
    }
}