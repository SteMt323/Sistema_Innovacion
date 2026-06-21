package ni.edu.uam.innovacion.modules.project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;

public record HistorialFasePIAResponse(

        Long idHistorialFase,

        Long idProyectoPIA,

        Long idProyecto,

        String nombreProyecto,

        FasePIA fase,

        LocalDate fechaInicio,

        LocalDate fechaFin,

        Boolean faseVigente,

        Long idAdministradorRegistro,

        String nombreAdministradorRegistro,

        String observaciones,

        LocalDateTime creadoEn
) {
}