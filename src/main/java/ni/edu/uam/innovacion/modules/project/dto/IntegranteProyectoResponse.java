package ni.edu.uam.innovacion.modules.project.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para devolver la información de un integrante
 * registrado dentro de un proyecto.
 *
 * Incluye datos resumidos del proyecto, usuario, rol de proyecto
 * y administrador que realizó el registro.
 */
public record IntegranteProyectoResponse(

        Long idIntegranteProyecto,

        Long idProyecto,
        String nombreProyecto,

        Long idUsuario,
        String nombreUsuario,
        String correoUsuario,
        String documentoUsuario,

        Long idRolProyecto,
        String nombreRolProyecto,

        LocalDate fechaVinculacion,

        EstadoRegistro estado,

        String observaciones,

        Long idAdministradorRegistro,
        String nombreAdministradorRegistro,

        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}