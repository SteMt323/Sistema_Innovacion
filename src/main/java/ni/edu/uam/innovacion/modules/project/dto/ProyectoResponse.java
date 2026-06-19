package ni.edu.uam.innovacion.modules.project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;

/**
 * DTO utilizado para devolver al cliente la información
 * de un proyecto registrado.
 *
 * Incluye datos de la fuente del proyecto y del administrador
 * que realizó el registro.
 */
public record ProyectoResponse(

        Long idProyecto,

        Long idFuenteProyecto,
        String nombreFuenteProyecto,

        Long idCategoriaFuenteProyecto,
        String nombreCategoriaFuenteProyecto,

        Long idAdministradorRegistro,
        String nombreAdministradorRegistro,

        String nombre,
        String descripcion,

        LocalDate fechaRegistro,
        LocalDate fechaInicio,
        LocalDate fechaFin,

        EstadoProyecto estado,

        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn

) {
}