package ni.edu.uam.innovacion.modules.activity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.activity.enums.ModalidadActividad;

public record ActualizarActividadRequest(
    @NotNull(message = "El ambito de actividad es obligatorio")
    @Positive(message = "El id del ambito de actividad debe ser positivo")
    Long idAmbitoActividad,

    @Positive(message = "El id de la categoria DIEM debe ser positivo")
    Long idCategoriaDiem,

    @Positive(message = "El id del responsable debe ser positivo")
    Long idResponsableUsuario,

    @NotBlank(message = "El nombre de la actividad es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    String nombre,

    String descripcion,

    @NotNull(message = "La fecha de inicio es obligatoria")
    LocalDateTime fechaInicio,

    LocalDateTime fechaFin,

    @NotNull(message = "La modalidad es obligatoria")
    ModalidadActividad modalidad,

    @Positive(message = "El cupo maximo debe ser positivo")
    Integer cupoMaximo,

    @Size(max = 255, message = "La ubicacion no puede superar los 255 caracteres")
    String ubicacion,

    @Size(max = 150, message = "El nombre del responsable no puede superar los 150 caracteres")
    String responsableNombre,

    @Min(value = 0, message = "Los puntos base no pueden ser negativos")
    Integer puntosBase
) {
}
