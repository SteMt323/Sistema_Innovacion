package ni.edu.uam.innovacion.modules.points.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;

public record CrearAjustePuntosRequest(
    @NotNull @Positive Long idUsuario,
    @NotNull Integer cantidad,
    @NotNull TipoMovimientoPuntos tipoMovimiento,
    @NotBlank @Size(max = 255) String motivo,
    @Size(max = 100) String origen
) {
}
