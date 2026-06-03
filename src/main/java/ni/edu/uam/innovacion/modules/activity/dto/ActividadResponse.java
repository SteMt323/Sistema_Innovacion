package ni.edu.uam.innovacion.modules.activity.dto;

import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.enums.ModalidadActividad;

public record ActividadResponse(
    Long idActividad,
    Long idAmbitoActividad,
    String nombreAmbitoActividad,
    Boolean requiereCategoriaAmbito,
    Long idCategoriaDiem,
    String nombreCategoriaDiem,
    Long idAdministradorCreador,
    String nombreAdministradorCreador,
    Long idResponsableUsuario,
    String nombreResponsableUsuario,
    String nombre,
    String descripcion,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    ModalidadActividad modalidad,
    EstadoActividad estado,
    Integer cupoMaximo,
    String ubicacion,
    String responsableNombre,
    Integer puntosBase,
    LocalDateTime creadoEn,
    LocalDateTime actualizadoEn
) {
}
