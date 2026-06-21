package ni.edu.uam.innovacion.modules.enrollment.mapper;

import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.enrollment.dto.ActualizarInscripcionRequest;
import ni.edu.uam.innovacion.modules.enrollment.dto.CrearInscripcionRequest;
import ni.edu.uam.innovacion.modules.enrollment.dto.InscripcionResponse;
import ni.edu.uam.innovacion.modules.enrollment.entity.Inscripcion;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

/**
 * Mapper para convertir entre la entidad Inscripcion y sus DTOs.
 *
 * Evita exponer entidades completas y devuelve únicamente
 * la información necesaria para consultar inscripciones.
 */
public class InscripcionMapper {

    private InscripcionMapper() {
    }

    public static Inscripcion toEntity(
            CrearInscripcionRequest request,
            Usuario usuario,
            Actividad actividad
    ) {
        Inscripcion inscripcion = new Inscripcion();

        inscripcion.setUsuario(usuario);
        inscripcion.setActividad(actividad);
        inscripcion.setObservaciones(request.observaciones());

        return inscripcion;
    }

    public static void updateEntity(
            Inscripcion inscripcion,
            ActualizarInscripcionRequest request
    ) {
        inscripcion.setObservaciones(request.observaciones());
    }

    public static InscripcionResponse toResponse(Inscripcion inscripcion) {
        Usuario usuario = inscripcion.getUsuario();
        Actividad actividad = inscripcion.getActividad();

        return new InscripcionResponse(
                inscripcion.getIdInscripcion(),

                usuario.getIdUsuario(),
                usuario.getNombreCompleto(),
                usuario.getCorreo(),

                actividad.getIdActividad(),
                actividad.getNombre(),

                inscripcion.getFechaInscripcion(),
                inscripcion.getEstado(),

                inscripcion.getObservaciones(),

                inscripcion.getCreadoEn(),
                inscripcion.getActualizadoEn()
        );
    }
}