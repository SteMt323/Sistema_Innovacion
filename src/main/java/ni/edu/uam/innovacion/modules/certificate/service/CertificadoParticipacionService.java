package ni.edu.uam.innovacion.modules.certificate.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ni.edu.uam.innovacion.common.document.PdfDocumentService;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.certificate.enums.TipoDocumentoParticipacion;
import ni.edu.uam.innovacion.modules.participation.entity.Participacion;
import ni.edu.uam.innovacion.modules.participation.repository.ParticipacionRepository;
import ni.edu.uam.innovacion.modules.points.service.PuntoInnovacionService;
import ni.edu.uam.innovacion.modules.report.dto.ArchivoDescarga;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CertificadoParticipacionService {

    private static final DateTimeFormatter ID_FECHA =
        DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private final ParticipacionRepository participacionRepository;
    private final PuntoInnovacionService puntoService;
    private final PdfDocumentService pdfService;

    public CertificadoParticipacionService(
        ParticipacionRepository participacionRepository,
        PuntoInnovacionService puntoService,
        PdfDocumentService pdfService
    ) {
        this.participacionRepository = participacionRepository;
        this.puntoService = puntoService;
        this.pdfService = pdfService;
    }

    public ArchivoDescarga generar(
        Long idParticipacion,
        TipoDocumentoParticipacion tipo
    ) {
        Participacion participacion = participacionRepository.findById(idParticipacion)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe la participacion con id " + idParticipacion
            ));
        if (!participacion.estaValidada()) {
            throw new BadRequestException(
                "Solo se pueden emitir documentos para participaciones validadas"
            );
        }

        int puntos = puntoService.obtenerPuntosActivosParticipacion(idParticipacion);
        LocalDate fechaActividad = obtenerFechaActividad(participacion);
        String identificador = crearIdentificador(participacion);
        boolean certificado = TipoDocumentoParticipacion.CERTIFICADO.equals(tipo);

        byte[] contenido = pdfService.crearConstancia(
            certificado,
            participacion.getInscripcion().getUsuario().getNombreCompleto(),
            participacion.getInscripcion().getActividad().getNombre(),
            participacion.getRolParticipacion().getNombre(),
            fechaActividad,
            puntos,
            identificador
        );

        return new ArchivoDescarga(
            contenido,
            "application/pdf",
            tipo.getValor() + "-participacion-" + idParticipacion + ".pdf"
        );
    }

    private LocalDate obtenerFechaActividad(Participacion participacion) {
        LocalDateTime fechaFin = participacion.getInscripcion().getActividad().getFechaFin();
        LocalDateTime fechaInicio = participacion.getInscripcion().getActividad().getFechaInicio();
        LocalDateTime fecha = fechaFin == null ? fechaInicio : fechaFin;
        return fecha == null ? null : fecha.toLocalDate();
    }

    private String crearIdentificador(Participacion participacion) {
        LocalDateTime fecha = participacion.getFechaValidacion();
        String sufijo = fecha == null ? "SINFECHA" : ID_FECHA.format(fecha);
        return "PART-" + participacion.getIdParticipacion() + "-" + sufijo;
    }
}
