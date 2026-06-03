package ni.edu.uam.innovacion.modules.user.dto;

import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;

public record PerfilMentorResponse(
    Long idUsuario,
    String areaExperiencia,
    String especialidad,
    String institucion,
    String tipoAcompanamiento,
    GradoAcademico gradoAcademico,
    String tituloUniversitario
) {
}
