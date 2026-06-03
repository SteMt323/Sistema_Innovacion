package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ActualizarPerfilEstudianteRequest(
    @NotBlank @Size(max = 30) String cif,
    @Email @Size(max = 150) String correoInstitucional,
    @NotNull @Positive Long idCarreraPrincipal
) {
}
