package ni.edu.uam.innovacion.modules.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;

/**
 * DTO utilizado para cambiar la fase de un proyecto PIA.
 *
 * Este DTO será útil cuando se implemente el historial de fases,
 * ya que permitirá registrar la nueva fase y las observaciones
 * asociadas al cambio.
 */
public record CambiarFaseProyectoPIARequest(

        @NotNull(message = "La nueva fase del proyecto PIA es obligatoria")
        FasePIA nuevaFase,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones
) {
}