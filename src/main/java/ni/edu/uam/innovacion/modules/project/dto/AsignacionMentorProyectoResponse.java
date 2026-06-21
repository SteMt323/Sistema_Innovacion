package ni.edu.uam.innovacion.modules.project.dto;

import ni.edu.uam.innovacion.modules.project.enums.EstadoAsignacion;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para devolver la información de una asignación
 * de mentor a proyecto.
 *
 * Incluye datos resumidos del proyecto, mentor, administrador
 * y estado actual de la asignación.
 */
public record AsignacionMentorProyectoResponse(

        Long idAsignacionMentor,

        Long idProyecto,
        String nombreProyecto,

        Long idMentor,
        String nombreMentor,
        String correoMentor,
        String areaExperienciaMentor,
        String especialidadMentor,
        String institucionMentor,
        String tipoAcompanamientoMentor,

        Long idAdministradorRegistro,
        String nombreAdministradorRegistro,

        LocalDateTime fechaAsignacion,

        EstadoAsignacion estado,

        String observaciones,

        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}