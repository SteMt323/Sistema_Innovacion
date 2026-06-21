package ni.edu.uam.innovacion.modules.participation.dto;

import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;

public record ParticipacionResponse(

        Long idParticipacion,

        Long idInscripcion,

        Long idUsuario,
        String nombreUsuario,
        String correoUsuario,

        Long idActividad,
        String nombreActividad,

        Long idRolParticipacion,
        String nombreRolParticipacion,

        EstadoParticipacion estado,

        LocalDateTime fechaValidacion,

        Long idAdministradorValidador,
        String nombreAdministradorValidador,

        String observaciones,

        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}