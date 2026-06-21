package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;

/**
 * DTO utilizado para registrar un proyecto dentro del Programa PIA.
 *
 * El administrador no se recibe desde el request, porque se obtiene
 * desde el usuario autenticado mediante JWT en el controller.
 */
public record CrearProyectoPIARequest(

        @NotNull(message = "El proyecto es obligatorio")
        @Positive(message = "El id del proyecto debe ser positivo")
        Long idProyecto,

        /**
         * Fase inicial del proyecto dentro del Programa PIA.
         *
         * Si se envía null, el service puede asignar PROSPECTO
         * como valor por defecto.
         */
        FasePIA faseActual,

        /**
         * Fecha de ingreso al Programa PIA.
         *
         * Si se envía null, la entidad asigna la fecha actual.
         */
        LocalDate fechaIngreso,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}