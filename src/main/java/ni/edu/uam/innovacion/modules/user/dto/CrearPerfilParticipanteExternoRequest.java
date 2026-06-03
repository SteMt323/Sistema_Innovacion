package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.Size;

public record CrearPerfilParticipanteExternoRequest(
    @Size(max = 100) String ocupacion,
    @Size(max = 150) String institucionProcedencia
) {
}
