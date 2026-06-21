package ni.edu.uam.innovacion;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import jakarta.servlet.Filter;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaDIEM;
import ni.edu.uam.innovacion.modules.catalog.entity.Rol;
import ni.edu.uam.innovacion.modules.catalog.repository.AmbitoActividadRepository;
import ni.edu.uam.innovacion.modules.catalog.repository.CategoriaDIEMRepository;
import ni.edu.uam.innovacion.modules.catalog.repository.RolRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.entity.UsuarioRol;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@Transactional
class AuthIntegrationTests {

    private static final String CONTRASENA = "secreto123";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PerfilAdministradorRepository perfilAdministradorRepository;

    @Autowired
    private AmbitoActividadRepository ambitoActividadRepository;

    @Autowired
    private CategoriaDIEMRepository categoriaDIEMRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(springSecurityFilterChain)
            .build();
    }

    @Test
    void loginExitosoDevuelveTokenYUsuario() throws Exception {
        Usuario admin = crearUsuario("login-ok", EstadoUsuario.ACTIVO);
        asignarRol(admin, "administrador");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody(admin.getCorreo(), CONTRASENA)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isString())
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andExpect(jsonPath("$.expiresIn").value(28_800))
            .andExpect(jsonPath("$.usuario.idUsuario").value(admin.getIdUsuario()))
            .andExpect(jsonPath("$.usuario.roles[0]").value("administrador"));
    }

    @Test
    void loginFallaConCredencialesInvalidasOUsuarioNoActivo() throws Exception {
        Usuario activo = crearUsuario("login-invalidas", EstadoUsuario.ACTIVO);
        Usuario inactivo = crearUsuario("login-inactivo", EstadoUsuario.INACTIVO);
        Usuario suspendido = crearUsuario("login-suspendido", EstadoUsuario.SUSPENDIDO);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody(activo.getCorreo(), "incorrecta123")))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody("no-existe@uam.edu.ni", CONTRASENA)))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody(inactivo.getCorreo(), CONTRASENA)))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody(suspendido.getCorreo(), CONTRASENA)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void protegeRutasAdminYMantieneActividadesPublicas() throws Exception {
        Usuario estudiante = crearUsuario("solo-estudiante", EstadoUsuario.ACTIVO);
        asignarRol(estudiante, "estudiante");
        String tokenEstudiante = login(estudiante.getCorreo());

        Usuario admin = crearUsuario("admin-rutas", EstadoUsuario.ACTIVO);
        asignarRol(admin, "administrador");
        crearPerfilAdministrador(admin);
        String tokenAdmin = login(admin.getCorreo());

        mockMvc.perform(get("/api/admin/actividades")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/admin/actividades")
                .header("Authorization", "Bearer " + tokenEstudiante)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/admin/actividades")
                .header("Authorization", "Bearer " + tokenAdmin)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/actividades/disponibles")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void authMeDevuelveDatosDelToken() throws Exception {
        Usuario admin = crearUsuario("me", EstadoUsuario.ACTIVO);
        asignarRol(admin, "administrador");
        String token = login(admin.getCorreo());

        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idUsuario").value(admin.getIdUsuario()))
            .andExpect(jsonPath("$.correo").value(admin.getCorreo()))
            .andExpect(jsonPath("$.roles[0]").value("administrador"));
    }

    @Test
    void logoutRevocaTokenYRequiereAutenticacion() throws Exception {
        Usuario admin = crearUsuario("logout", EstadoUsuario.ACTIVO);
        asignarRol(admin, "administrador");
        String token = login(admin.getCorreo());

        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/admin/actividades")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/auth/logout"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void crearPerfilAdministradorRequiereRolAdministrador() throws Exception {
        Usuario objetivo = crearUsuario("perfil-admin-protegido", EstadoUsuario.ACTIVO);
        Usuario estudiante = crearUsuario("perfil-admin-estudiante", EstadoUsuario.ACTIVO);
        asignarRol(estudiante, "estudiante");
        String tokenEstudiante = login(estudiante.getCorreo());
        String body = """
            {"cargo":"Coordinador","nivelAcceso":"total"}
            """;

        mockMvc.perform(post("/api/usuarios/{idUsuario}/perfiles/administrador", objetivo.getIdUsuario())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/usuarios/{idUsuario}/perfiles/administrador", objetivo.getIdUsuario())
                .header("Authorization", "Bearer " + tokenEstudiante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isForbidden());
    }

    @Test
    void asignacionDeRolesRequiereAdmin() throws Exception {
        Usuario objetivo = crearUsuario("roles-protegido", EstadoUsuario.ACTIVO);
        Usuario estudiante = crearUsuario("roles-estudiante", EstadoUsuario.ACTIVO);
        asignarRol(estudiante, "estudiante");
        String tokenEstudiante = login(estudiante.getCorreo());

        mockMvc.perform(post("/api/admin/usuarios/{idUsuario}/roles", objetivo.getIdUsuario())
                .contentType(MediaType.APPLICATION_JSON)
                .content(rolBody("estudiante")))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/admin/usuarios/{idUsuario}/roles", objetivo.getIdUsuario())
                .header("Authorization", "Bearer " + tokenEstudiante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rolBody("estudiante")))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminAsignaYReactivaRolExistente() throws Exception {
        Usuario admin = crearUsuario("roles-admin", EstadoUsuario.ACTIVO);
        asignarRol(admin, "administrador");
        String tokenAdmin = login(admin.getCorreo());
        Usuario objetivo = crearUsuario("roles-objetivo", EstadoUsuario.ACTIVO);

        mockMvc.perform(post("/api/admin/usuarios/{idUsuario}/roles", objetivo.getIdUsuario())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rolBody("estudiante")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idUsuario").value(objetivo.getIdUsuario()))
            .andExpect(jsonPath("$.roles[0].nombre").value("estudiante"))
            .andExpect(jsonPath("$.perfilEstudiante").doesNotExist());

        UsuarioRol asignacion = usuarioRolRepository
            .findByUsuarioIdUsuarioAndRolNombreIgnoreCase(objetivo.getIdUsuario(), "estudiante")
            .orElseThrow();
        asignacion.setActivo(Boolean.FALSE);
        usuarioRolRepository.saveAndFlush(asignacion);

        mockMvc.perform(post("/api/admin/usuarios/{idUsuario}/roles", objetivo.getIdUsuario())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rolBody("estudiante")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.roles[0].nombre").value("estudiante"));

        assertTrue(usuarioRolRepository.existsByUsuarioIdUsuarioAndRolNombreIgnoreCaseAndActivoTrue(
            objetivo.getIdUsuario(),
            "estudiante"
        ));
    }

    @Test
    void adminNoPuedeAsignarRolInexistenteOInactivo() throws Exception {
        Usuario admin = crearUsuario("roles-validacion-admin", EstadoUsuario.ACTIVO);
        asignarRol(admin, "administrador");
        String tokenAdmin = login(admin.getCorreo());
        Usuario objetivo = crearUsuario("roles-validacion-objetivo", EstadoUsuario.ACTIVO);

        mockMvc.perform(post("/api/admin/usuarios/{idUsuario}/roles", objetivo.getIdUsuario())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rolBody("no_existe")))
            .andExpect(status().isNotFound());

        Rol rolInactivo = rolRepository.findByNombreIgnoreCase("docente").orElseThrow();
        rolInactivo.inactivar();
        rolRepository.saveAndFlush(rolInactivo);

        mockMvc.perform(post("/api/admin/usuarios/{idUsuario}/roles", objetivo.getIdUsuario())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rolBody("docente")))
            .andExpect(status().isBadRequest());
    }

    @Test
    void crearActividadUsaAdministradorAutenticado() throws Exception {
        Usuario admin = crearUsuario("crear-actividad-auth", EstadoUsuario.ACTIVO);
        asignarRol(admin, "administrador");
        crearPerfilAdministrador(admin);
        String token = login(admin.getCorreo());

        AmbitoActividad diem = ambitoActividadRepository.save(new AmbitoActividad(
            "DIEM AUTH",
            "Ambito DIEM para auth",
            true
        ));
        CategoriaDIEM categoria = categoriaDIEMRepository.save(new CategoriaDIEM(
            "Concurso Auth",
            "Categoria de prueba auth",
            "Criterios",
            diem
        ));

        Map<String, Object> body = Map.ofEntries(
            Map.entry("idAmbitoActividad", diem.getId()),
            Map.entry("idCategoriaDiem", categoria.getId()),
            Map.entry("nombre", "Actividad con admin autenticado"),
            Map.entry("descripcion", "Creada desde test de auth"),
            Map.entry("fechaInicio", "2026-06-15T09:00:00"),
            Map.entry("fechaFin", "2026-06-15T17:00:00"),
            Map.entry("modalidad", "presencial"),
            Map.entry("cupoMaximo", 40),
            Map.entry("ubicacion", "Auditorio"),
            Map.entry("responsableNombre", "Equipo DIEM"),
            Map.entry("puntosBase", 10)
        );

        mockMvc.perform(post("/api/admin/actividades")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(actividadBody(body)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idAdministradorCreador").value(admin.getIdUsuario()))
            .andExpect(jsonPath("$.nombre").value("Actividad con admin autenticado"));
    }

    private String login(String correo) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody(correo, CONTRASENA)))
            .andExpect(status().isOk())
            .andReturn();

        return extraerCampoTexto(result.getResponse().getContentAsString(), "accessToken");
    }

    private String loginBody(String correo, String contrasena) {
        return """
            {"correo":"%s","contrasena":"%s"}
            """.formatted(correo, contrasena);
    }

    private String rolBody(String nombreRol) {
        return """
            {"nombreRol":"%s"}
            """.formatted(nombreRol);
    }

    private String actividadBody(Map<String, Object> body) {
        return """
            {
              "idAmbitoActividad": %s,
              "idCategoriaDiem": %s,
              "nombre": "%s",
              "descripcion": "%s",
              "fechaInicio": "%s",
              "fechaFin": "%s",
              "modalidad": "%s",
              "cupoMaximo": %s,
              "ubicacion": "%s",
              "responsableNombre": "%s",
              "puntosBase": %s
            }
            """.formatted(
            body.get("idAmbitoActividad"),
            body.get("idCategoriaDiem"),
            body.get("nombre"),
            body.get("descripcion"),
            body.get("fechaInicio"),
            body.get("fechaFin"),
            body.get("modalidad"),
            body.get("cupoMaximo"),
            body.get("ubicacion"),
            body.get("responsableNombre"),
            body.get("puntosBase")
        );
    }

    private String extraerCampoTexto(String json, String campo) {
        String marcador = "\"" + campo + "\":\"";
        int inicio = json.indexOf(marcador);
        if (inicio < 0) {
            throw new IllegalArgumentException("No se encontro el campo " + campo);
        }

        int inicioValor = inicio + marcador.length();
        int finValor = json.indexOf('"', inicioValor);
        return json.substring(inicioValor, finValor);
    }

    private Usuario crearUsuario(String sufijo, EstadoUsuario estado) {
        String codigo = Integer.toUnsignedString(sufijo.hashCode());
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Usuario " + sufijo);
        usuario.setDocumento("DOC-AUTH-" + codigo);
        usuario.setTelefono("88880000");
        usuario.setCorreo("auth-" + codigo + "@uam.edu.ni");
        usuario.setContrasenaHash(passwordEncoder.encode(CONTRASENA));
        usuario.setSexo("N");
        usuario.setTallaCamisa("M");
        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }

    private void asignarRol(Usuario usuario, String nombreRol) {
        Rol rol = rolRepository.findByNombreIgnoreCase(nombreRol)
            .orElseGet(() -> rolRepository.save(new Rol(nombreRol, "Rol " + nombreRol)));

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRol.setActivo(Boolean.TRUE);
        UsuarioRol guardado = usuarioRolRepository.saveAndFlush(usuarioRol);
        usuario.getUsuarioRoles().add(guardado);
    }

    private PerfilAdministrador crearPerfilAdministrador(Usuario usuario) {
        PerfilAdministrador perfil = new PerfilAdministrador();
        perfil.setUsuario(usuario);
        perfil.setCargo("Coordinador");
        perfil.setNivelAcceso("total");
        return perfilAdministradorRepository.save(perfil);
    }
}
