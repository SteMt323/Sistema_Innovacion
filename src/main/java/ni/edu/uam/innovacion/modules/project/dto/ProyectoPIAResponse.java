package ni.edu.uam.innovacion.modules.project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyectoPIA;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;

/**
 * DTO utilizado para devolver la información de un proyecto
 * registrado dentro del Programa PIA.
 */
public record ProyectoPIAResponse(

        Long idProyectoPIA,

        Long idProyecto,
        String nombreProyecto,
        EstadoProyecto estadoProyecto,

        Long idFuenteProyecto,
        String nombreFuenteProyecto,

        FasePIA faseActual,
        LocalDate fechaIngreso,
        EstadoProyectoPIA estado,

        Long idRegistradoPorAdmin,
        String nombreRegistradoPorAdmin,

        String observaciones,

        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}