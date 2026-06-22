package ni.edu.uam.innovacion.modules.mentorship.mapper;

import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentoriaResponse;
import ni.edu.uam.innovacion.modules.mentorship.entity.MentoriaActividad;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;

public final class MentoriaMapper {

    private MentoriaMapper() {
    }

    public static MentoriaResponse toResponse(MentoriaActividad mentoria) {
        Actividad actividad = mentoria.getActividad();
        PerfilMentor mentor = mentoria.getMentor();
        PerfilAdministrador administrador = mentoria.getAgregadoPorAdmin();

        return new MentoriaResponse(
            mentoria.getIdColaborador(),
            actividad == null ? null : actividad.getIdActividad(),
            actividad == null ? null : actividad.getNombre(),
            mentor == null ? null : mentor.getIdUsuario(),
            mentor == null || mentor.getUsuario() == null ? null : mentor.getUsuario().getNombreCompleto(),
            mentor == null || mentor.getUsuario() == null ? null : mentor.getUsuario().getCorreo(),
            mentor == null ? null : mentor.getAreaExperiencia(),
            mentor == null ? null : mentor.getEspecialidad(),
            mentor == null ? null : mentor.getInstitucion(),
            mentor == null ? null : mentor.getTipoAcompanamiento(),
            administrador == null ? null : administrador.getIdUsuario(),
            administrador == null || administrador.getUsuario() == null
                ? null
                : administrador.getUsuario().getNombreCompleto(),
            mentoria.getFechaAsignacion(),
            mentoria.getEstado(),
            mentoria.getObservaciones(),
            mentoria.getCreadoEn(),
            mentoria.getActualizadoEn()
        );
    }
}
