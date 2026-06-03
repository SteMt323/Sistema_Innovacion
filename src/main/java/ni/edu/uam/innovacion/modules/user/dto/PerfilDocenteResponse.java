package ni.edu.uam.innovacion.modules.user.dto;

import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;

public record PerfilDocenteResponse(
    Long idUsuario,
    String areaAcademica,
    String cargo,
    GradoAcademico gradoAcademico,
    String tituloUniversitario,
    Long idFacultad
) {
}
