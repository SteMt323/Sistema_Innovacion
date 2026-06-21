package ni.edu.uam.innovacion.modules.project.mapper;

import ni.edu.uam.innovacion.modules.project.dto.ActualizarAsignacionMentorProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.AsignacionMentorProyectoResponse;
import ni.edu.uam.innovacion.modules.project.dto.CrearAsignacionMentorProyectoRequest;
import ni.edu.uam.innovacion.modules.project.entity.AsignacionMentorProyecto;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

/**
 * Mapper encargado de convertir entre la entidad AsignacionMentorProyecto
 * y sus DTOs de entrada y salida.
 */
public class AsignacionMentorProyectoMapper {

    private AsignacionMentorProyectoMapper() {
    }

    /**
     * Convierte un CrearAsignacionMentorProyectoRequest en una entidad.
     *
     * El administrador no viene desde el request, porque se obtiene
     * desde el JWT en el controller y se resuelve en el service.
     */
    public static AsignacionMentorProyecto toEntity(
            CrearAsignacionMentorProyectoRequest request,
            Proyecto proyecto,
            PerfilMentor mentor,
            PerfilAdministrador administradorRegistro
    ) {
        AsignacionMentorProyecto asignacion = new AsignacionMentorProyecto();

        asignacion.setProyecto(proyecto);
        asignacion.setMentor(mentor);
        asignacion.setAdministradorRegistro(administradorRegistro);
        asignacion.setFechaAsignacion(request.fechaAsignacion());
        asignacion.setObservaciones(request.observaciones());

        return asignacion;
    }

    /**
     * Convierte una entidad AsignacionMentorProyecto en su DTO de respuesta.
     */
    public static AsignacionMentorProyectoResponse toResponse(
            AsignacionMentorProyecto asignacion
    ) {
        Proyecto proyecto = asignacion.getProyecto();
        PerfilMentor mentor = asignacion.getMentor();
        PerfilAdministrador administrador = asignacion.getAdministradorRegistro();

        Usuario usuarioMentor = mentor.getUsuario();
        Usuario usuarioAdministrador = administrador.getUsuario();

        return new AsignacionMentorProyectoResponse(
                asignacion.getIdAsignacionMentor(),

                proyecto.getIdProyecto(),
                proyecto.getNombre(),

                mentor.getIdUsuario(),
                usuarioMentor == null ? null : usuarioMentor.getNombreCompleto(),
                usuarioMentor == null ? null : usuarioMentor.getCorreo(),
                mentor.getAreaExperiencia(),
                mentor.getEspecialidad(),
                mentor.getInstitucion(),
                mentor.getTipoAcompanamiento(),

                administrador.getIdUsuario(),
                usuarioAdministrador == null ? null : usuarioAdministrador.getNombreCompleto(),

                asignacion.getFechaAsignacion(),
                asignacion.getEstado(),
                asignacion.getObservaciones(),

                asignacion.getCreadoEn(),
                asignacion.getActualizadoEn()
        );
    }

    /**
     * Actualiza los datos editables de una asignación.
     *
     * No se cambia el proyecto ni el mentor, porque esos campos
     * representan la relación principal del registro.
     */
    public static void updateEntity(
            AsignacionMentorProyecto asignacion,
            ActualizarAsignacionMentorProyectoRequest request
    ) {
        if (request.fechaAsignacion() != null) {
            asignacion.setFechaAsignacion(request.fechaAsignacion());
        }

        asignacion.setObservaciones(request.observaciones());
    }
}