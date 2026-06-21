package ni.edu.uam.innovacion.modules.enrollment.dto;

import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.enrollment.enums.EstadoInscripcion;

/**
 * DTO de respuesta para mostrar la información de una inscripción.
 *
 * Incluye datos resumidos del usuario y de la actividad para evitar
 * devolver entidades completas.
 */
public record InscripcionResponse(

        Long idInscripcion,

        Long idUsuario,
        String nombreUsuario,
        String correoUsuario,

        Long idActividad,
        String nombreActividad,

        LocalDateTime fechaInscripcion,
        EstadoInscripcion estado,

        String observaciones,

        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}