package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;

public record CrearPerfilDocenteRequest(
    @Size(max = 120) String areaAcademica,
    @Size(max = 100) String cargo,
    GradoAcademico gradoAcademico,
    @Size(max = 150) String tituloUniversitario,
    Long idFacultad
) {
}
