package ni.edu.uam.innovacion.modules.user.dto;

import java.time.LocalDateTime;
import java.util.List;
import ni.edu.uam.innovacion.modules.catalog.dto.RolResponse;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;

public record UsuarioResponse(
    Long idUsuario,
    String nombreCompleto,
    String documento,
    String telefono,
    String correo,
    String sexo,
    String tallaCamisa,
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
