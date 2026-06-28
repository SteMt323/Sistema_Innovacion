package ni.edu.uam.innovacion.modules.report.dto;

import java.util.List;

public record ParticipanteUnicoResponse(
    Long idUsuario,
    String nombreCompleto,
    String documento,
    String correo,
    List<String> roles,
    long totalParticipaciones,
    List<String> actividades
) {
}
