package ni.edu.uam.innovacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.enums.ModalidadActividad;
import ni.edu.uam.innovacion.modules.activity.repository.ActividadRepository;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.RolParticipacion;
import ni.edu.uam.innovacion.modules.catalog.repository.AmbitoActividadRepository;
import ni.edu.uam.innovacion.modules.catalog.repository.RolParticipacionRepository;
import ni.edu.uam.innovacion.modules.certificate.enums.TipoDocumentoParticipacion;
import ni.edu.uam.innovacion.modules.certificate.service.CertificadoParticipacionService;
import ni.edu.uam.innovacion.modules.dashboard.dto.AdminDashboardResponse;
import ni.edu.uam.innovacion.modules.dashboard.service.DashboardAdminService;
import ni.edu.uam.innovacion.modules.enrollment.entity.Inscripcion;
import ni.edu.uam.innovacion.modules.enrollment.enums.EstadoInscripcion;
import ni.edu.uam.innovacion.modules.enrollment.repository.InscripcionRepository;
import ni.edu.uam.innovacion.modules.participation.dto.CrearParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.dto.ProcesarParticipacionRequest;
import ni.edu.uam.innovacion.modules.participation.entity.Participacion;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;
import ni.edu.uam.innovacion.modules.participation.repository.ParticipacionRepository;
import ni.edu.uam.innovacion.modules.participation.service.ParticipacionService;
import ni.edu.uam.innovacion.modules.points.dto.CrearAjustePuntosRequest;
import ni.edu.uam.innovacion.modules.points.dto.ResumenPuntosUsuarioResponse;
import ni.edu.uam.innovacion.modules.points.entity.PuntoInnovacion;
import ni.edu.uam.innovacion.modules.points.enums.EstadoPuntos;
import ni.edu.uam.innovacion.modules.points.enums.InsigniaPuntos;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;
import ni.edu.uam.innovacion.modules.points.repository.PuntoInnovacionRepository;
import ni.edu.uam.innovacion.modules.points.service.PuntoInnovacionService;
import ni.edu.uam.innovacion.modules.report.dto.ArchivoDescarga;
import ni.edu.uam.innovacion.modules.report.enums.FormatoReporte;
import ni.edu.uam.innovacion.modules.report.service.ReporteAdminService;
import ni.edu.uam.innovacion.modules.user.dto.AsignarRolRequest;
import ni.edu.uam.innovacion.modules.user.dto.CambiarEstadoUsuarioRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilAdministradorRequest;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import ni.edu.uam.innovacion.modules.user.service.UsuarioService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AdminCompletionIntegrationTests {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilAdministradorRepository perfilAdministradorRepository;

    @Autowired
    private AmbitoActividadRepository ambitoActividadRepository;

    @Autowired
    private RolParticipacionRepository rolParticipacionRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private ParticipacionRepository participacionRepository;

    @Autowired
    private ParticipacionService participacionService;

    @Autowired
    private PuntoInnovacionRepository puntoRepository;

    @Autowired
    private PuntoInnovacionService puntoService;

    @Autowired
    private DashboardAdminService dashboardService;

    @Autowired
    private ReporteAdminService reporteService;

    @Autowired
    private CertificadoParticipacionService certificadoService;

    @Test
    void soloPermiteUnAdministradorActivoYControlaLaReactivacion() {
        Usuario primerAdmin = crearUsuario("admin-principal", EstadoUsuario.ACTIVO);
        asignarAdministrador(primerAdmin);

        Usuario segundoAdmin = crearUsuario("admin-secundario", EstadoUsuario.ACTIVO);
        assertThrows(
            DuplicateResourceException.class,
            () -> usuarioService.asignarRol(
                segundoAdmin.getIdUsuario(),
                new AsignarRolRequest("administrador")
            )
        );

        usuarioService.cambiarEstado(
            primerAdmin.getIdUsuario(),
            new CambiarEstadoUsuarioRequest(EstadoUsuario.INACTIVO)
        );
        asignarAdministrador(segundoAdmin);

        assertThrows(
            DuplicateResourceException.class,
            () -> usuarioService.cambiarEstado(
                primerAdmin.getIdUsuario(),
                new CambiarEstadoUsuarioRequest(EstadoUsuario.ACTIVO)
            )
        );
    }

    @Test
    void participacionMantieneUnSoloOtorgamientoYConservaHistorial() {
        Escenario escenario = crearEscenario(30);

        participacionService.validar(
            escenario.participacion().getIdParticipacion(),
            new ProcesarParticipacionRequest("Asistencia verificada"),
            escenario.admin().getIdUsuario()
        );

        List<PuntoInnovacion> movimientos = puntoRepository.findAll();
        assertEquals(1, movimientos.size());
        Long idPunto = movimientos.getFirst().getIdPunto();
        assertEquals(30, movimientos.getFirst().getCantidad());
        assertEquals(EstadoPuntos.ACTIVO, movimientos.getFirst().getEstado());

        puntoService.otorgarPorParticipacion(
            participacionRepository.findById(escenario.participacion().getIdParticipacion()).orElseThrow()
        );
        assertEquals(1, puntoRepository.count());

        participacionService.noValidar(
            escenario.participacion().getIdParticipacion(),
            new ProcesarParticipacionRequest("Evidencia insuficiente"),
            escenario.admin().getIdUsuario()
        );
        assertEquals(
            EstadoPuntos.ANULADO,
            puntoRepository.findById(idPunto).orElseThrow().getEstado()
        );

        participacionService.validar(
            escenario.participacion().getIdParticipacion(),
            new ProcesarParticipacionRequest("Evidencia corregida"),
            escenario.admin().getIdUsuario()
        );
        PuntoInnovacion reactivado = puntoRepository.findAll().getFirst();
        assertEquals(idPunto, reactivado.getIdPunto());
        assertEquals(EstadoPuntos.ACTIVO, reactivado.getEstado());

        participacionService.dejarPendiente(escenario.participacion().getIdParticipacion());
        assertEquals(
            EstadoPuntos.ANULADO,
            puntoRepository.findById(idPunto).orElseThrow().getEstado()
        );

        participacionService.validar(
            escenario.participacion().getIdParticipacion(),
            null,
            escenario.admin().getIdUsuario()
        );
        participacionService.anular(
            escenario.participacion().getIdParticipacion(),
            new ProcesarParticipacionRequest("Participacion anulada"),
            escenario.admin().getIdUsuario()
        );
        assertEquals(
            EstadoPuntos.ANULADO,
            puntoRepository.findById(idPunto).orElseThrow().getEstado()
        );
    }

    @Test
    void ajustesResumenInsigniasRankingYDashboardSonConsistentes() {
        Escenario escenario = crearEscenario(30);
        participacionService.validar(
            escenario.participacion().getIdParticipacion(),
            null,
            escenario.admin().getIdUsuario()
        );

        puntoService.crearAjuste(
            new CrearAjustePuntosRequest(
                escenario.participante().getIdUsuario(),
                150,
                TipoMovimientoPuntos.AJUSTE_MANUAL,
                "Bono por proyecto destacado",
                "comite"
            ),
            escenario.admin().getIdUsuario()
        );
        puntoService.crearAjuste(
            new CrearAjustePuntosRequest(
                escenario.participante().getIdUsuario(),
                -10,
                TipoMovimientoPuntos.PENALIZACION,
                "Correccion disciplinaria",
                "comite"
            ),
            escenario.admin().getIdUsuario()
        );

        ResumenPuntosUsuarioResponse resumen = puntoService.obtenerResumenUsuario(
            escenario.participante().getIdUsuario()
        );
        assertEquals(170, resumen.totalPuntosActivos());
        assertEquals(180, resumen.totalOtorgado());
        assertEquals(10, resumen.totalDebitado());
        assertEquals("oro", resumen.insigniaActual().codigo());

        assertEquals("sin_insignia", InsigniaPuntos.desdeTotal(24).codigo());
        assertEquals("bronce", InsigniaPuntos.desdeTotal(25).codigo());
        assertEquals("plata", InsigniaPuntos.desdeTotal(75).codigo());
        assertEquals("oro", InsigniaPuntos.desdeTotal(150).codigo());
        assertEquals("platino", InsigniaPuntos.desdeTotal(300).codigo());

        assertThrows(
            BadRequestException.class,
            () -> puntoService.crearAjuste(
                new CrearAjustePuntosRequest(
                    escenario.participante().getIdUsuario(),
                    5,
                    TipoMovimientoPuntos.PENALIZACION,
                    "Penalizacion invalida",
                    null
                ),
                escenario.admin().getIdUsuario()
            )
        );

        AdminDashboardResponse dashboard = dashboardService.obtenerResumen();
        assertTrue(dashboard.usuariosRegistrados() >= 2);
        assertTrue(dashboard.participacionesValidadas() >= 1);
        assertEquals(170, dashboard.puntosActivosOtorgados());
        assertEquals(
            escenario.participante().getIdUsuario(),
            dashboard.topUsuariosPuntos().getFirst().idUsuario()
        );
    }

    @Test
    void reportesGeneranCsvYPdfConDatosFiltrados() throws Exception {
        Escenario escenario = crearEscenario(40);
        participacionService.validar(
            escenario.participacion().getIdParticipacion(),
            null,
            escenario.admin().getIdUsuario()
        );

        ArchivoDescarga csv = reporteService.generarPuntos(
            FormatoReporte.CSV,
            escenario.participante().getIdUsuario(),
            null,
            TipoMovimientoPuntos.OTORGAMIENTO,
            EstadoPuntos.ACTIVO,
            null,
            null
        );
        String contenidoCsv = new String(csv.contenido(), java.nio.charset.StandardCharsets.UTF_8);
        assertEquals("text/csv", csv.contentType());
        assertTrue(contenidoCsv.contains(escenario.participante().getNombreCompleto()));
        assertTrue(contenidoCsv.contains("\"40\""));

        ArchivoDescarga pdf = reporteService.generarParticipaciones(
            FormatoReporte.PDF,
            escenario.participante().getIdUsuario(),
            EstadoParticipacion.VALIDADA,
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(1)
        );
        assertTrue(new String(pdf.contenido(), 0, 4, java.nio.charset.StandardCharsets.US_ASCII)
            .equals("%PDF"));
        String textoPdf = extraerTexto(pdf.contenido());
        assertTrue(textoPdf.contains("Reporte de participaciones"));
        assertTrue(textoPdf.contains(
            escenario.participante().getNombreCompleto().substring(0, 20)
        ));
    }

    @Test
    void documentosSoloSalenParaParticipacionValidadaYVarianPorTipo() throws Exception {
        Escenario escenario = crearEscenario(25);

        assertThrows(
            BadRequestException.class,
            () -> certificadoService.generar(
                escenario.participacion().getIdParticipacion(),
                TipoDocumentoParticipacion.CONSTANCIA
            )
        );

        participacionService.validar(
            escenario.participacion().getIdParticipacion(),
            null,
            escenario.admin().getIdUsuario()
        );

        ArchivoDescarga constancia = certificadoService.generar(
            escenario.participacion().getIdParticipacion(),
            TipoDocumentoParticipacion.CONSTANCIA
        );
        ArchivoDescarga certificado = certificadoService.generar(
            escenario.participacion().getIdParticipacion(),
            TipoDocumentoParticipacion.CERTIFICADO
        );

        String textoConstancia = extraerTexto(constancia.contenido());
        String textoCertificado = extraerTexto(certificado.contenido());
        assertTrue(textoConstancia.contains("CONSTANCIA DE PARTICIPACION"));
        assertTrue(textoCertificado.contains("CERTIFICADO DE PARTICIPACION"));
        assertTrue(textoCertificado.contains("PUNTOS OTORGADOS"));
        assertTrue(textoCertificado.contains("25"));
        assertNotEquals(constancia.nombreArchivo(), certificado.nombreArchivo());

        Path salida = Path.of("target", "test-output", "certificado-participacion.pdf");
        Files.createDirectories(salida.getParent());
        Files.write(salida, certificado.contenido());
    }

    private Escenario crearEscenario(int puntosBase) {
        Usuario adminUsuario = crearUsuario("admin-" + UUID.randomUUID(), EstadoUsuario.ACTIVO);
        PerfilAdministrador admin = asignarAdministrador(adminUsuario);
        Usuario participante = crearUsuario(
            "participante-" + UUID.randomUUID(),
            EstadoUsuario.ACTIVO
        );

        AmbitoActividad ambito = ambitoActividadRepository.save(new AmbitoActividad(
            "Externa " + UUID.randomUUID(),
            "Ambito para pruebas administrativas",
            false
        ));

        Actividad actividad = new Actividad();
        actividad.setAmbitoActividad(ambito);
        actividad.setAdministradorCreador(admin);
        actividad.setNombre("Reto de innovacion " + UUID.randomUUID());
        actividad.setDescripcion("Actividad para validar el cierre administrativo");
        actividad.setFechaInicio(LocalDateTime.now().minusDays(2));
        actividad.setFechaFin(LocalDateTime.now().minusDays(1));
        actividad.setModalidad(ModalidadActividad.PRESENCIAL);
        actividad.setEstado(EstadoActividad.FINALIZADA);
        actividad.setPuntosBase(puntosBase);
        actividad = actividadRepository.save(actividad);

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setUsuario(participante);
        inscripcion.setActividad(actividad);
        inscripcion.setEstado(EstadoInscripcion.CONFIRMADA);
        inscripcion = inscripcionRepository.save(inscripcion);

        RolParticipacion rol = rolParticipacionRepository.save(new RolParticipacion(
            "Participante " + UUID.randomUUID(),
            "Rol para pruebas"
        ));
        Long idParticipacion = participacionService.crear(new CrearParticipacionRequest(
            inscripcion.getIdInscripcion(),
            rol.getId(),
            "Registro de prueba"
        )).idParticipacion();

        Participacion participacion = participacionRepository.findById(idParticipacion)
            .orElseThrow();
        return new Escenario(admin, participante, participacion);
    }

    private PerfilAdministrador asignarAdministrador(Usuario usuario) {
        usuarioService.asignarRol(
            usuario.getIdUsuario(),
            new AsignarRolRequest("administrador")
        );
        usuarioService.crearPerfilAdministrador(
            usuario.getIdUsuario(),
            new CrearPerfilAdministradorRequest("Coordinador", "total")
        );
        return perfilAdministradorRepository.findById(usuario.getIdUsuario()).orElseThrow();
    }

    private Usuario crearUsuario(String sufijo, EstadoUsuario estado) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Usuario " + sufijo);
        usuario.setDocumento("DOC-" + UUID.randomUUID());
        usuario.setTelefono("88880000");
        usuario.setCorreo("usuario-" + UUID.randomUUID() + "@uam.edu.ni");
        usuario.setContrasenaHash("hash-de-prueba");
        usuario.setSexo("N");
        usuario.setTallaCamisa("M");
        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }

    private String extraerTexto(byte[] pdf) throws Exception {
        try (PDDocument document = Loader.loadPDF(pdf)) {
            return new PDFTextStripper().getText(document);
        }
    }

    private record Escenario(
        PerfilAdministrador admin,
        Usuario participante,
        Participacion participacion
    ) {
    }
}
