package ni.edu.uam.innovacion.modules.report.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import ni.edu.uam.innovacion.common.document.PdfDocumentService;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.modules.dashboard.dto.AdminDashboardResponse;
import ni.edu.uam.innovacion.modules.dashboard.service.DashboardAdminService;
import ni.edu.uam.innovacion.modules.participation.entity.Participacion;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;
import ni.edu.uam.innovacion.modules.participation.repository.ParticipacionRepository;
import ni.edu.uam.innovacion.modules.points.dto.PuntoInnovacionResponse;
import ni.edu.uam.innovacion.modules.points.enums.EstadoPuntos;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;
import ni.edu.uam.innovacion.modules.points.service.PuntoInnovacionService;
import ni.edu.uam.innovacion.modules.report.dto.ArchivoDescarga;
import ni.edu.uam.innovacion.modules.report.enums.FormatoReporte;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReporteAdminService {

    private static final DateTimeFormatter FECHA =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final PuntoInnovacionService puntoService;
    private final ParticipacionRepository participacionRepository;
    private final DashboardAdminService dashboardService;
    private final PdfDocumentService pdfService;

    public ReporteAdminService(
        PuntoInnovacionService puntoService,
        ParticipacionRepository participacionRepository,
        DashboardAdminService dashboardService,
        PdfDocumentService pdfService
    ) {
        this.puntoService = puntoService;
        this.participacionRepository = participacionRepository;
        this.dashboardService = dashboardService;
        this.pdfService = pdfService;
    }

    public ArchivoDescarga generarPuntos(
        FormatoReporte formato,
        Long idUsuario,
        Long idParticipacion,
        TipoMovimientoPuntos tipo,
        EstadoPuntos estado,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
    ) {
        List<PuntoInnovacionResponse> datos = puntoService.listar(
            idUsuario,
            idParticipacion,
            tipo,
            estado,
            fechaDesde,
            fechaHasta
        );
        List<String> encabezados = List.of(
            "ID", "Usuario", "Actividad", "Cantidad", "Tipo", "Estado", "Fecha", "Motivo"
        );
        List<List<String>> filas = datos.stream()
            .map(punto -> List.of(
                texto(punto.idPunto()),
                punto.nombreUsuario(),
                texto(punto.nombreActividad()),
                texto(punto.cantidad()),
                punto.tipoMovimiento().getValor(),
                punto.estado().getValor(),
                fecha(punto.fechaAsignacion()),
                texto(punto.motivo())
            ))
            .toList();

        return generarArchivo(
            formato,
            "Reporte de puntos de innovacion",
            descripcionPeriodo(fechaDesde, fechaHasta),
            encabezados,
            filas,
            "reporte-puntos"
        );
    }

    public ArchivoDescarga generarParticipaciones(
        FormatoReporte formato,
        Long idUsuario,
        EstadoParticipacion estado,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
    ) {
        validarRangoFechas(fechaDesde, fechaHasta);
        List<Participacion> datos = participacionRepository.findAllByOrderByCreadoEnDesc()
            .stream()
            .filter(participacion -> idUsuario == null
                || idUsuario.equals(participacion.getInscripcion().getUsuario().getIdUsuario()))
            .filter(participacion -> estado == null || estado.equals(participacion.getEstado()))
            .filter(participacion -> dentroDelPeriodo(
                participacion.getFechaValidacion(),
                fechaDesde,
                fechaHasta
            ))
            .toList();

        List<String> encabezados = List.of(
            "ID", "Usuario", "Actividad", "Rol", "Estado", "Fecha validacion", "Administrador"
        );
        List<List<String>> filas = datos.stream()
            .map(participacion -> List.of(
                texto(participacion.getIdParticipacion()),
                participacion.getInscripcion().getUsuario().getNombreCompleto(),
                participacion.getInscripcion().getActividad().getNombre(),
                participacion.getRolParticipacion().getNombre(),
                participacion.getEstado().getValor(),
                fecha(participacion.getFechaValidacion()),
                participacion.getValidadoPorAdmin() == null
                    ? ""
                    : participacion.getValidadoPorAdmin().getUsuario().getNombreCompleto()
            ))
            .toList();

        return generarArchivo(
            formato,
            "Reporte de participaciones",
            descripcionPeriodo(fechaDesde, fechaHasta),
            encabezados,
            filas,
            "reporte-participaciones"
        );
    }

    public ArchivoDescarga generarDashboard(
        FormatoReporte formato,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
    ) {
        validarRangoFechas(fechaDesde, fechaHasta);
        AdminDashboardResponse resumen = dashboardService.obtenerResumen();
        long movimientosPeriodo = puntoService.listar(
            null,
            null,
            null,
            null,
            fechaDesde,
            fechaHasta
        ).size();
        long participacionesPeriodo = participacionRepository.findAllByOrderByCreadoEnDesc()
            .stream()
            .filter(participacion -> dentroDelPeriodo(
                participacion.getFechaValidacion(),
                fechaDesde,
                fechaHasta
            ))
            .count();

        List<List<String>> filas = new ArrayList<>();
        filas.add(metrica("Usuarios registrados", resumen.usuariosRegistrados()));
        filas.add(metrica("Usuarios activos", resumen.usuariosActivos()));
        filas.add(metrica("Actividades totales", resumen.actividadesTotales()));
        filas.add(metrica("Actividades finalizadas", resumen.actividadesFinalizadas()));
        filas.add(metrica("Inscripciones totales", resumen.inscripcionesTotales()));
        filas.add(metrica("Inscripciones confirmadas", resumen.inscripcionesConfirmadas()));
        filas.add(metrica("Participaciones validadas", resumen.participacionesValidadas()));
        filas.add(metrica("Participaciones no validadas", resumen.participacionesNoValidadas()));
        filas.add(metrica("Puntos activos otorgados", resumen.puntosActivosOtorgados()));
        filas.add(metrica("Movimientos anulados", resumen.movimientosPuntosAnulados()));
        filas.add(metrica("Mentores registrados", resumen.mentoresRegistrados()));
        filas.add(metrica("Mentores activos", resumen.mentoresActivos()));
        filas.add(metrica("Mentorias activas", resumen.mentoriasActivas()));
        filas.add(metrica("Mentorias inactivas", resumen.mentoriasInactivas()));
        filas.add(metrica("Mentorias archivadas", resumen.mentoriasArchivadas()));
        filas.add(metrica("Actividades con mentorias activas", resumen.actividadesConMentoriasActivas()));
        filas.add(metrica("Movimientos en el periodo", movimientosPeriodo));
        filas.add(metrica("Participaciones procesadas en el periodo", participacionesPeriodo));

        return generarArchivo(
            formato,
            "Resumen administrativo",
            descripcionPeriodo(fechaDesde, fechaHasta),
            List.of("Metrica", "Valor"),
            filas,
            "reporte-dashboard"
        );
    }

    private ArchivoDescarga generarArchivo(
        FormatoReporte formato,
        String titulo,
        String subtitulo,
        List<String> encabezados,
        List<List<String>> filas,
        String nombreBase
    ) {
        String nombre = nombreBase + "-" + java.time.LocalDate.now()
            + "." + formato.getExtension();

        if (FormatoReporte.PDF.equals(formato)) {
            return new ArchivoDescarga(
                pdfService.crearReporte(titulo, subtitulo, encabezados, filas),
                formato.getContentType(),
                nombre
            );
        }
        return new ArchivoDescarga(
            crearCsv(encabezados, filas),
            formato.getContentType(),
            nombre
        );
    }

    private byte[] crearCsv(List<String> encabezados, List<List<String>> filas) {
        StringBuilder csv = new StringBuilder("\uFEFF");
        agregarFilaCsv(csv, encabezados);
        filas.forEach(fila -> agregarFilaCsv(csv, fila));
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void agregarFilaCsv(StringBuilder csv, List<String> valores) {
        for (int i = 0; i < valores.size(); i++) {
            if (i > 0) {
                csv.append(',');
            }
            String valor = texto(valores.get(i)).replace("\"", "\"\"");
            csv.append('"').append(valor).append('"');
        }
        csv.append("\r\n");
    }

    private boolean dentroDelPeriodo(
        LocalDateTime fechaValor,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
    ) {
        if (fechaDesde == null && fechaHasta == null) {
            return true;
        }
        if (fechaValor == null) {
            return false;
        }
        return (fechaDesde == null || !fechaValor.isBefore(fechaDesde))
            && (fechaHasta == null || !fechaValor.isAfter(fechaHasta));
    }

    private void validarRangoFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new BadRequestException("La fecha desde no puede ser posterior a la fecha hasta");
        }
    }

    private String descripcionPeriodo(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        if (fechaDesde == null && fechaHasta == null) {
            return "Todos los registros disponibles";
        }
        return "Periodo: " + fecha(fechaDesde) + " - " + fecha(fechaHasta);
    }

    private List<String> metrica(String nombre, long valor) {
        return List.of(nombre, String.valueOf(valor));
    }

    private String fecha(LocalDateTime valor) {
        return valor == null ? "" : FECHA.format(valor);
    }

    private String texto(Object valor) {
        return valor == null ? "" : String.valueOf(valor);
    }
}
