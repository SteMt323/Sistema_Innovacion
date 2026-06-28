package ni.edu.uam.innovacion.modules.report.dto;

import java.util.List;

public record ReporteParticipantesUnicosResponse(
    long totalInscripciones,
    long totalParticipantesUnicos,
    List<ParticipanteUnicoResponse> participantes
) {
}
