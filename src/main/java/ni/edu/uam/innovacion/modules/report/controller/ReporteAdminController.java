package ni.edu.uam.innovacion.modules.report.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;
import ni.edu.uam.innovacion.modules.points.enums.EstadoPuntos;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;
import ni.edu.uam.innovacion.modules.report.dto.ArchivoDescarga;
import ni.edu.uam.innovacion.modules.report.dto.ReporteParticipantesUnicosResponse;
import ni.edu.uam.innovacion.modules.report.enums.FormatoReporte;
import ni.edu.uam.innovacion.modules.report.service.ReporteAdminService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reportes")
public class ReporteAdminController {

    private final ReporteAdminService reporteService;

    public ReporteAdminController(ReporteAdminService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/puntos")
    public ResponseEntity<byte[]> reportePuntos(
        @RequestParam(defaultValue = "pdf") String formato,
        @RequestParam(required = false) Long idUsuario,
        @RequestParam(required = false) Long idParticipacion,
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta
    ) {
        return descargar(reporteService.generarPuntos(
            FormatoReporte.fromValue(formato),
            idUsuario,
            idParticipacion,
            TipoMovimientoPuntos.fromValue(tipo),
            EstadoPuntos.fromValue(estado),
            fechaDesde,
            fechaHasta
        ));
    }

    @GetMapping("/participaciones")
    public ResponseEntity<byte[]> reporteParticipaciones(
        @RequestParam(defaultValue = "pdf") String formato,
        @RequestParam(required = false) Long idUsuario,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta
    ) {
        return descargar(reporteService.generarParticipaciones(
            FormatoReporte.fromValue(formato),
            idUsuario,
            EstadoParticipacion.fromValue(estado),
            fechaDesde,
            fechaHasta
        ));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<byte[]> reporteDashboard(
        @RequestParam(defaultValue = "pdf") String formato,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta
    ) {
        return descargar(reporteService.generarDashboard(
            FormatoReporte.fromValue(formato),
            fechaDesde,
            fechaHasta
        ));
    }

    /**
     * Devuelve la cantidad de inscripciones totales y la lista de participantes
     * únicos (RN-13). Filtros opcionales: anio, idCarrera, idFacultad, perfil.
     */
    @GetMapping("/participantes-unicos")
    public ReporteParticipantesUnicosResponse participantesUnicos(
        @RequestParam(required = false) Integer anio,
        @RequestParam(required = false) Long idCarrera,
        @RequestParam(required = false) Long idFacultad,
        @RequestParam(required = false) String perfil
    ) {
        return reporteService.participantesUnicos(anio, idCarrera, idFacultad, perfil);
    }

    private ResponseEntity<byte[]> descargar(ArchivoDescarga archivo) {
        ContentDisposition disposition = ContentDisposition.attachment()
            .filename(archivo.nombreArchivo(), StandardCharsets.UTF_8)
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
            .contentType(MediaType.parseMediaType(archivo.contentType()))
            .contentLength(archivo.contenido().length)
            .body(archivo.contenido());
    }
}
