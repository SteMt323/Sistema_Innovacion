package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;

public record CrearPerfilMentorRequest(
    @Size(max = 120) String areaExperiencia,
    @Size(max = 120) String especialidad,
    @Size(max = 150) String institucion,
    @Size(max = 100) String tipoAcompanamiento,
    GradoAcademico gradoAcademico,
    @Size(max = 150) String tituloUniversitario
) {
}
