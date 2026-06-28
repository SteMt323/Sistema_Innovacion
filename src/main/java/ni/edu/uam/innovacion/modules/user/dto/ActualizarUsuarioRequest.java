package ni.edu.uam.innovacion.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ActualizarUsuarioRequest(
    @NotBlank @Size(max = 150) String nombreCompleto,
    @NotBlank @Size(max = 40) String documento,
    @Size(max = 40) String cedula,
    @Size(max = 30) String telefono,
    @NotBlank @Email @Size(max = 150) String correo,
    @Size(max = 30) String sexo,
    @Size(max = 20) String tallaCamisa,
    @Past LocalDate fechaNacimiento
) {
}
