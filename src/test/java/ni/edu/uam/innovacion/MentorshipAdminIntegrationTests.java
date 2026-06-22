package ni.edu.uam.innovacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.enums.ModalidadActividad;
import ni.edu.uam.innovacion.modules.activity.repository.ActividadRepository;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.repository.AmbitoActividadRepository;
import ni.edu.uam.innovacion.modules.dashboard.dto.AdminDashboardResponse;
import ni.edu.uam.innovacion.modules.dashboard.service.DashboardAdminService;
import ni.edu.uam.innovacion.modules.mentorship.dto.ActualizarPerfilMentorRequest;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentorAdminResponse;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentoriaResponse;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentoriaResumenResponse;
import ni.edu.uam.innovacion.modules.mentorship.dto.CrearMentoriaRequest;
import ni.edu.uam.innovacion.modules.mentorship.repository.MentoriaActividadRepository;
import ni.edu.uam.innovacion.modules.mentorship.service.MentorAdminService;
import ni.edu.uam.innovacion.modules.mentorship.service.MentoriaAdminService;
import ni.edu.uam.innovacion.modules.report.dto.ArchivoDescarga;
import ni.edu.uam.innovacion.modules.report.enums.FormatoReporte;
import ni.edu.uam.innovacion.modules.report.service.ReporteAdminService;
import ni.edu.uam.innovacion.modules.user.dto.AsignarRolRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilAdministradorRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilMentorRequest;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import ni.edu.uam.innovacion.modules.user.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MentorshipAdminIntegrationTests {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilAdministradorRepository perfilAdministradorRepository;

    @Autowired
    private AmbitoActividadRepository ambitoActividadRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private MentoriaActividadRepository mentoriaActividadRepository;

    @Autowired
    private MentorAdminService mentorAdminService;

    @Autowired
    private MentoriaAdminService mentoriaAdminService;

    @Autowired
    private DashboardAdminService dashboardAdminService;

    @Autowired
    private ReporteAdminService reporteAdminService;

    @Test
    void creaReutilizaMentoriaYPropagaMetricasAlDashboardYReportes() {
        PerfilAdministrador admin = crearAdministrador("dashboard");
        Usuario mentor = crearUsuario("mentor-dashboard", EstadoUsuario.ACTIVO);
        crearPerfilMentor(mentor, "Diseno estrategico", "UX");
        Actividad actividad = crearActividad(admin, EstadoActividad.PUBLICADA, "Mentoria dashboard");

        MentoriaResponse primera = mentoriaAdminService.crear(
            new CrearMentoriaRequest(
                actividad.getIdActividad(),
                mentor.getIdUsuario(),
                LocalDateTime.now().minusDays(2),
                "Primera asignacion"
            ),
            admin.getIdUsuario()
        );

        assertEquals(1, mentoriaActividadRepository.count());
        assertEquals(EstadoRegistro.ACTIVO, primera.estado());

        MentoriaResponse inactiva = mentoriaAdminService.inactivar(primera.idMentoria());
        assertEquals(EstadoRegistro.INACTIVO, inactiva.estado());

        MentoriaResponse reactivada = mentoriaAdminService.crear(
            new CrearMentoriaRequest(
                actividad.getIdActividad(),
                mentor.getIdUsuario(),
                LocalDateTime.now().minusDays(1),
                "Mentoria reactivada"
            ),
            admin.getIdUsuario()
        );

        assertEquals(primera.idMentoria(), reactivada.idMentoria());
        assertEquals(1, mentoriaActividadRepository.count());
        assertEquals(EstadoRegistro.ACTIVO, reactivada.estado());
        assertEquals("Mentoria reactivada", reactivada.observaciones());

        assertThrows(DuplicateResourceException.class, () -> mentoriaAdminService.crear(
            new CrearMentoriaRequest(
                actividad.getIdActividad(),
                mentor.getIdUsuario(),
                null,
                "Intento duplicado"
            ),
            admin.getIdUsuario()
        ));

        MentoriaResumenResponse resumen = mentoriaAdminService.obtenerResumen();
        assertEquals(1, resumen.mentoresRegistrados());
        assertEquals(1, resumen.mentoresActivos());
        assertEquals(1, resumen.mentoriasActivas());
        assertEquals(0, resumen.mentoriasInactivas());
        assertEquals(0, resumen.mentoriasArchivadas());
        assertEquals(1, resumen.actividadesConMentoriasActivas());

        AdminDashboardResponse dashboard = dashboardAdminService.obtenerResumen();
        assertEquals(1, dashboard.mentoresRegistrados());
        assertEquals(1, dashboard.mentoresActivos());
        assertEquals(1, dashboard.mentoriasActivas());
        assertEquals(0, dashboard.mentoriasInactivas());
        assertEquals(0, dashboard.mentoriasArchivadas());
        assertEquals(1, dashboard.actividadesConMentoriasActivas());

        ArchivoDescarga reporte = reporteAdminService.generarDashboard(FormatoReporte.CSV, null, null);
        String contenido = new String(reporte.contenido(), StandardCharsets.UTF_8);
        assertTrue(contenido.contains("Mentorias activas"));
        assertTrue(contenido.contains("Actividades con mentorias activas"));
    }

    @Test
    void listaMentoresActivosYPermiteActualizarSuPerfil() {
        PerfilAdministrador admin = crearAdministrador("mentores");
        Usuario mentorConMentoria = crearUsuario("mentor-con-mentoria", EstadoUsuario.ACTIVO);
        crearPerfilMentor(mentorConMentoria, "Diseno estrategico", "UX");

        Usuario mentorSinMentoria = crearUsuario("mentor-sin-mentoria", EstadoUsuario.ACTIVO);
        crearPerfilMentor(mentorSinMentoria, "Finanzas", "Contabilidad");

        Actividad actividad = crearActividad(admin, EstadoActividad.BORRADOR, "Mentoria activa");
        mentoriaAdminService.crear(
            new CrearMentoriaRequest(
                actividad.getIdActividad(),
                mentorConMentoria.getIdUsuario(),
                null,
                "Mentor principal"
            ),
            admin.getIdUsuario()
        );

        List<MentorAdminResponse> soloActivos = mentorAdminService.listar(
            null,
            EstadoUsuario.ACTIVO,
            true
        );
        assertEquals(1, soloActivos.size());
        assertEquals(mentorConMentoria.getIdUsuario(), soloActivos.getFirst().idUsuario());
        assertTrue(soloActivos.stream().noneMatch(mentor ->
            mentorSinMentoria.getIdUsuario().equals(mentor.idUsuario())
        ));

        List<MentorAdminResponse> filtradosPorBusqueda = mentorAdminService.listar("ux", null, false);
        assertTrue(filtradosPorBusqueda.stream().anyMatch(mentor ->
            mentorConMentoria.getIdUsuario().equals(mentor.idUsuario())
        ));

        MentorAdminResponse actualizado = mentorAdminService.actualizar(
            mentorConMentoria.getIdUsuario(),
            new ActualizarPerfilMentorRequest(
                "Diseno de servicios",
                "Research",
                "UAM",
                "Mentoria individual",
                GradoAcademico.MAESTRIA,
                "Maestria en innovacion"
            )
        );

        assertEquals("Diseno de servicios", actualizado.areaExperiencia());
        assertEquals("Research", actualizado.especialidad());
        assertEquals("Mentoria individual", actualizado.tipoAcompanamiento());
        assertEquals(1, actualizado.mentoriasActivas());

        MentorAdminResponse consultado = mentorAdminService.obtenerPorId(mentorConMentoria.getIdUsuario());
        assertEquals("Diseno de servicios", consultado.areaExperiencia());
        assertEquals(1, consultado.mentoriasActivas());
    }

    @Test
    void rechazaMentoriaCuandoLaActividadNoEsEditableOElMentorNoEstaActivo() {
        PerfilAdministrador admin = crearAdministrador("reglas");
        Usuario mentor = crearUsuario("mentor-reglas", EstadoUsuario.ACTIVO);
        crearPerfilMentor(mentor, "Modelos de negocio", "Pitch");

        Actividad finalizada = crearActividad(admin, EstadoActividad.FINALIZADA, "Actividad cerrada");
        assertThrows(BadRequestException.class, () -> mentoriaAdminService.crear(
            new CrearMentoriaRequest(finalizada.getIdActividad(), mentor.getIdUsuario(), null, null),
            admin.getIdUsuario()
        ));

        Actividad borrador = crearActividad(admin, EstadoActividad.BORRADOR, "Actividad abierta");
        mentor.setEstado(EstadoUsuario.INACTIVO);
        usuarioRepository.saveAndFlush(mentor);

        assertThrows(BadRequestException.class, () -> mentoriaAdminService.crear(
            new CrearMentoriaRequest(borrador.getIdActividad(), mentor.getIdUsuario(), null, null),
            admin.getIdUsuario()
        ));
    }

    private PerfilAdministrador crearAdministrador(String sufijo) {
        Usuario usuario = crearUsuario("admin-" + sufijo, EstadoUsuario.ACTIVO);
        usuarioService.asignarRol(usuario.getIdUsuario(), new AsignarRolRequest("administrador"));
        usuarioService.crearPerfilAdministrador(
            usuario.getIdUsuario(),
            new CrearPerfilAdministradorRequest("Coordinador", "total")
        );
        return perfilAdministradorRepository.findById(usuario.getIdUsuario()).orElseThrow();
    }

    private void crearPerfilMentor(Usuario usuario, String areaExperiencia, String especialidad) {
        usuarioService.asignarRol(usuario.getIdUsuario(), new AsignarRolRequest("mentor"));
        usuarioService.crearPerfilMentor(
            usuario.getIdUsuario(),
            new CrearPerfilMentorRequest(
                areaExperiencia,
                especialidad,
                "UAM",
                "Mentoria grupal",
                GradoAcademico.DOCTORADO,
                "Doctorado en innovacion"
            )
        );
    }

    private Actividad crearActividad(
        PerfilAdministrador administrador,
        EstadoActividad estado,
        String nombre
    ) {
        AmbitoActividad ambito = ambitoActividadRepository.save(new AmbitoActividad(
            "Ambito mentorias " + UUID.randomUUID(),
            "Ambito para pruebas de mentorias",
            false
        ));

        Actividad actividad = new Actividad();
        actividad.setAmbitoActividad(ambito);
        actividad.setAdministradorCreador(administrador);
        actividad.setNombre(nombre + " " + UUID.randomUUID());
        actividad.setDescripcion("Actividad para pruebas administrativas de mentorias");
        actividad.setFechaInicio(LocalDateTime.now().minusDays(3));
        actividad.setFechaFin(LocalDateTime.now().plusDays(1));
        actividad.setModalidad(ModalidadActividad.PRESENCIAL);
        actividad.setEstado(estado);
        actividad.setUbicacion("Auditorio");
        actividad.setResponsableNombre("Equipo de innovacion");
        actividad.setPuntosBase(0);
        return actividadRepository.saveAndFlush(actividad);
    }

    private Usuario crearUsuario(String sufijo, EstadoUsuario estado) {
        String codigo = UUID.randomUUID().toString().substring(0, 8);
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Usuario " + sufijo);
        usuario.setDocumento("DOC-M-" + codigo);
        usuario.setTelefono("88880000");
        usuario.setCorreo("mentor-" + codigo + "@uam.edu.ni");
        usuario.setContrasenaHash("hash-prueba");
        usuario.setSexo("N");
        usuario.setTallaCamisa("M");
        usuario.setEstado(estado);
        return usuarioRepository.saveAndFlush(usuario);
    }
}