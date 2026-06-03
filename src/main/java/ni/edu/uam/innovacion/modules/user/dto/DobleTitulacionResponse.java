package ni.edu.uam.innovacion.modules.user.dto;

import java.time.LocalDate;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

public record DobleTitulacionResponse(
    Long idDobleTitulacion,
    Long idEstudiante,
    Long idCarreraSecundaria,
    String nombreCarreraSecundaria,
    String codigoCarreraSecundaria,
    Long idFacultadCarreraSecundaria,
    String nombreFacultadCarreraSecundaria,
    LocalDate fechaRegistro,
    EstadoRegistro estado
) {
}
