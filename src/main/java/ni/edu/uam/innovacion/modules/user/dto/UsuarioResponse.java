package ni.edu.uam.innovacion.modules.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import ni.edu.uam.innovacion.modules.catalog.dto.RolResponse;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;

public record UsuarioResponse(
    Long idUsuario,
    String nombreCompleto,
    String documento,
    String cedula,
    String telefono,
    String correo,
    String sexo,
    String tallaCamisa,
    LocalDate fechaNacimiento,
    EstadoUsuario estado,
    LocalDateTime fechaRegistro,
    LocalDateTime ultimoAcceso,
    List<RolResponse> roles,
    PerfilEstudianteResponse perfilEstudiante,
    PerfilAdministradorResponse perfilAdministrador,
    PerfilDocenteResponse perfilDocente,
    PerfilMentorResponse perfilMentor,
    PerfilParticipanteExternoResponse perfilParticipanteExterno
) {
}
