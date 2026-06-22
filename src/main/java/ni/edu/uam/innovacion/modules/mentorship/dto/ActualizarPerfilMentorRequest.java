package ni.edu.uam.innovacion.modules.mentorship.dto;

import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;

public record ActualizarPerfilMentorRequest(
    @Size(max = 120, message = "El area de experiencia no puede superar los 120 caracteres")
    String areaExperiencia,
    @Size(max = 120, message = "La especialidad no puede superar los 120 caracteres")
    String especialidad,
    @Size(max = 150, message = "La institucion no puede superar los 150 caracteres")
    String institucion,
    @Size(max = 100, message = "El tipo de acompanamiento no puede superar los 100 caracteres")
    String tipoAcompanamiento,
    GradoAcademico gradoAcademico,
    @Size(max = 150, message = "El titulo universitario no puede superar los 150 caracteres")
    String tituloUniversitario
) {
}
