package ni.edu.uam.innovacion.modules.mentorship.dto;

import java.time.LocalDateTime;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

public record MentoriaResponse(
    Long idMentoria,
    Long idActividad,
    String nombreActividad,
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
    EstadoRegistro estado,
    String observaciones,
    LocalDateTime creadoEn,
    LocalDateTime actualizadoEn
) {
}
