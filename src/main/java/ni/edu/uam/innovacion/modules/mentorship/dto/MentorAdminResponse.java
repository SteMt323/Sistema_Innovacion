package ni.edu.uam.innovacion.modules.mentorship.dto;

import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;

public record MentorAdminResponse(
    Long idUsuario,
    String nombreCompleto,
    String documento,
    String telefono,
    String correo,
    EstadoUsuario estadoUsuario,
    String areaExperiencia,
    String especialidad,
    String institucion,
    String tipoAcompanamiento,
    GradoAcademico gradoAcademico,
    String tituloUniversitario,
    long mentoriasActivas
) {
}
