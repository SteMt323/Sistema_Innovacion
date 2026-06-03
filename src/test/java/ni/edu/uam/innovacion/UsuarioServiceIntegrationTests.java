package ni.edu.uam.innovacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.catalog.entity.Carrera;
import ni.edu.uam.innovacion.modules.catalog.entity.Facultad;
import ni.edu.uam.innovacion.modules.catalog.repository.CarreraRepository;
import ni.edu.uam.innovacion.modules.catalog.repository.FacultadRepository;
import ni.edu.uam.innovacion.modules.catalog.service.RolService;
import ni.edu.uam.innovacion.modules.user.dto.ActualizarPerfilEstudianteRequest;
import ni.edu.uam.innovacion.modules.user.dto.AsignarRolRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearDobleTitulacionRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilAdministradorRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilDocenteRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilEstudianteRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilMentorRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilParticipanteExternoRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearUsuarioRequest;
import ni.edu.uam.innovacion.modules.user.dto.DobleTitulacionResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilAdministradorResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilDocenteResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilEstudianteResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilMentorResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilParticipanteExternoResponse;
import ni.edu.uam.innovacion.modules.user.dto.UsuarioResponse;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRolRepository;
import ni.edu.uam.innovacion.modules.user.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UsuarioServiceIntegrationTests {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private FacultadRepository facultadRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Test
    void creaUsuarioConContrasenaHasheada() {
        UsuarioResponse response = usuarioService.crearUsuario(usuarioRequest("hash"));

        Usuario usuario = usuarioRepository.findById(response.idUsuario()).orElseThrow();

        assertNotNull(usuario.getIdUsuario());
        assertNotEquals("secreto123", usuario.getContrasenaHash());
        assertTrue(usuario.getContrasenaHash().startsWith("$2"));
    }

    @Test
    void rechazaCorreoDuplicado() {
        usuarioService.crearUsuario(usuarioRequest("correo"));

        CrearUsuarioRequest duplicado = new CrearUsuarioRequest(
            "Usuario Duplicado",
            "DOC-CORREO-2",
            "88880000",
            "usuario-correo@uam.edu.ni",
            "secreto123",
            "F",
            "M"
        );

        assertThrows(DuplicateResourceException.class, () -> usuarioService.crearUsuario(duplicado));
    }

    @Test
    void rechazaDocumentoDuplicado() {
        usuarioService.crearUsuario(usuarioRequest("documento"));

        CrearUsuarioRequest duplicado = new CrearUsuarioRequest(
            "Usuario Duplicado",
            "DOC-DOCUMENTO",
            "88880000",
            "otro-documento@uam.edu.ni",
            "secreto123",
            "F",
            "M"
        );

        assertThrows(DuplicateResourceException.class, () -> usuarioService.crearUsuario(duplicado));
    }

    @Test
    void listaRolesSembrados() {
        List<String> roles = rolService.listarTodos().stream()
            .map(rol -> rol.getNombre())
            .toList();

        assertTrue(roles.contains("estudiante"));
        assertTrue(roles.contains("administrador"));
        assertTrue(roles.contains("docente"));
        assertTrue(roles.contains("mentor"));
        assertTrue(roles.contains("participante_externo"));
    }

    @Test
    void asignaYDesactivaRol() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("rol"));

        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("estudiante"));

        assertTrue(usuarioRolRepository.existsByUsuarioIdUsuarioAndRolNombreIgnoreCaseAndActivoTrue(
            usuario.idUsuario(),
            "estudiante"
        ));

        usuarioService.desactivarRol(usuario.idUsuario(), "estudiante");

        assertFalse(usuarioRolRepository.existsByUsuarioIdUsuarioAndRolNombreIgnoreCaseAndActivoTrue(
            usuario.idUsuario(),
            "estudiante"
        ));
        assertTrue(usuarioRolRepository.findByUsuarioIdUsuarioAndRolNombreIgnoreCase(
            usuario.idUsuario(),
            "estudiante"
        ).isPresent());
    }

    @Test
    void rechazaPerfilEstudianteSinRolEstudiante() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("sin-rol-estudiante"));
        Carrera carrera = crearCarrera("sin-rol-estudiante");

        assertThrows(BadRequestException.class, () -> usuarioService.crearPerfilEstudiante(
            usuario.idUsuario(),
            new CrearPerfilEstudianteRequest("CIF-SIN-ROL", "sin-rol@uam.edu.ni", carrera.getId())
        ));
    }

    @Test
    void rechazaPerfilAdministradorSinRolAdministrador() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("sin-rol-admin"));

        assertThrows(BadRequestException.class, () -> usuarioService.crearPerfilAdministrador(
            usuario.idUsuario(),
            new CrearPerfilAdministradorRequest("Coordinador", "total")
        ));
    }

    @Test
    void creaYConsultaPerfilEstudiante() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("perfil-estudiante"));
        Carrera carrera = crearCarrera("perfil-estudiante");
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("estudiante"));

        PerfilEstudianteResponse creado = usuarioService.crearPerfilEstudiante(
            usuario.idUsuario(),
            new CrearPerfilEstudianteRequest("CIF-ESTUDIANTE", "perfil-estudiante@uam.edu.ni", carrera.getId())
        );
        PerfilEstudianteResponse consultado = usuarioService.obtenerPerfilEstudiante(usuario.idUsuario());

        assertEquals(usuario.idUsuario(), creado.idUsuario());
        assertEquals("CIF-ESTUDIANTE", creado.cif());
        assertEquals(carrera.getId(), creado.idCarreraPrincipal());
        assertEquals(Boolean.FALSE, creado.dobleTitular());
        assertEquals(creado, consultado);
    }

    @Test
    void rechazaCrearPerfilEstudianteSinCarreraPrincipal() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("sin-carrera-principal"));
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("estudiante"));

        assertThrows(BadRequestException.class, () -> usuarioService.crearPerfilEstudiante(
            usuario.idUsuario(),
            new CrearPerfilEstudianteRequest("CIF-SIN-CARRERA", "sin-carrera@uam.edu.ni", null)
        ));
    }

    @Test
    void rechazaPerfilEstudianteConCarreraInexistente() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("carrera-inexistente"));
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("estudiante"));

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.crearPerfilEstudiante(
            usuario.idUsuario(),
            new CrearPerfilEstudianteRequest("CIF-CARRERA-404", "carrera-404@uam.edu.ni", 999_999L)
        ));
    }

    @Test
    void rechazaPerfilEstudianteConCarreraInactiva() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("carrera-inactiva"));
        Carrera carrera = crearCarrera("carrera-inactiva");
        carrera.inactivar();
        carreraRepository.save(carrera);
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("estudiante"));

        assertThrows(BadRequestException.class, () -> usuarioService.crearPerfilEstudiante(
            usuario.idUsuario(),
            new CrearPerfilEstudianteRequest("CIF-CARRERA-INACTIVA", "carrera-inactiva@uam.edu.ni", carrera.getId())
        ));
    }

    @Test
    void creaDobleTitulacionValidaYSincronizaPerfil() {
        UsuarioResponse usuario = crearUsuarioEstudianteConPerfil("doble-valida");
        Carrera carreraSecundaria = crearCarrera("doble-secundaria");

        DobleTitulacionResponse doble = usuarioService.crearDobleTitulacion(
            usuario.idUsuario(),
            new CrearDobleTitulacionRequest(carreraSecundaria.getId())
        );

        PerfilEstudianteResponse perfil = usuarioService.obtenerPerfilEstudiante(usuario.idUsuario());
        List<DobleTitulacionResponse> dobles = usuarioService.listarDobleTitulaciones(usuario.idUsuario());

        assertNotNull(doble.idDobleTitulacion());
        assertEquals(usuario.idUsuario(), doble.idEstudiante());
        assertEquals(carreraSecundaria.getId(), doble.idCarreraSecundaria());
        assertEquals(EstadoRegistro.ACTIVO, doble.estado());
        assertEquals(Boolean.TRUE, perfil.dobleTitular());
        assertEquals(1, dobles.size());
    }

    @Test
    void rechazaDobleTitulacionDuplicada() {
        UsuarioResponse usuario = crearUsuarioEstudianteConPerfil("doble-duplicada");
        Carrera carreraSecundaria = crearCarrera("doble-duplicada-secundaria");
        CrearDobleTitulacionRequest request = new CrearDobleTitulacionRequest(carreraSecundaria.getId());

        usuarioService.crearDobleTitulacion(usuario.idUsuario(), request);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.crearDobleTitulacion(
            usuario.idUsuario(),
            request
        ));
    }

    @Test
    void rechazaDobleTitulacionConCarreraPrincipal() {
        Carrera carreraPrincipal = crearCarrera("doble-misma-principal");
        UsuarioResponse usuario = crearUsuarioEstudianteConPerfil("doble-misma", carreraPrincipal);

        assertThrows(BadRequestException.class, () -> usuarioService.crearDobleTitulacion(
            usuario.idUsuario(),
            new CrearDobleTitulacionRequest(carreraPrincipal.getId())
        ));
    }

    @Test
    void eliminaDobleTitulacionYSincronizaPerfil() {
        UsuarioResponse usuario = crearUsuarioEstudianteConPerfil("doble-eliminar");
        Carrera carreraSecundaria = crearCarrera("doble-eliminar-secundaria");
        DobleTitulacionResponse doble = usuarioService.crearDobleTitulacion(
            usuario.idUsuario(),
            new CrearDobleTitulacionRequest(carreraSecundaria.getId())
        );

        usuarioService.eliminarDobleTitulacion(usuario.idUsuario(), doble.idDobleTitulacion());

        PerfilEstudianteResponse perfil = usuarioService.obtenerPerfilEstudiante(usuario.idUsuario());
        List<DobleTitulacionResponse> dobles = usuarioService.listarDobleTitulaciones(usuario.idUsuario());

        assertEquals(Boolean.FALSE, perfil.dobleTitular());
        assertTrue(dobles.isEmpty());
    }

    @Test
    void actualizaPerfilEstudiante() {
        UsuarioResponse usuario = crearUsuarioEstudianteConPerfil("actualiza-estudiante");
        Carrera nuevaCarrera = crearCarrera("actualiza-estudiante-nueva");

        PerfilEstudianteResponse actualizado = usuarioService.actualizarPerfilEstudiante(
            usuario.idUsuario(),
            new ActualizarPerfilEstudianteRequest(
                "CIF-ACTUALIZADO",
                "actualizado-estudiante@uam.edu.ni",
                nuevaCarrera.getId()
            )
        );
        UsuarioResponse usuarioActualizado = usuarioService.obtenerUsuario(usuario.idUsuario());

        assertEquals("CIF-ACTUALIZADO", actualizado.cif());
        assertEquals("actualizado-estudiante@uam.edu.ni", actualizado.correoInstitucional());
        assertEquals(nuevaCarrera.getId(), actualizado.idCarreraPrincipal());
        assertEquals(Boolean.FALSE, actualizado.dobleTitular());
        assertEquals(actualizado, usuarioActualizado.perfilEstudiante());
        assertEquals("estudiante", usuarioActualizado.roles().get(0).getNombre());
        assertEquals(EstadoRegistro.ACTIVO, usuarioActualizado.roles().get(0).getEstado());
    }

    @Test
    void rechazaActualizarPerfilEstudianteConCifOCorreoDuplicado() {
        UsuarioResponse primerUsuario = crearUsuarioEstudianteConPerfil("duplicado-estudiante-uno");
        UsuarioResponse segundoUsuario = crearUsuarioEstudianteConPerfil("duplicado-estudiante-dos");
        Carrera carrera = crearCarrera("duplicado-estudiante-nueva");
        PerfilEstudianteResponse primerPerfil = usuarioService.obtenerPerfilEstudiante(primerUsuario.idUsuario());

        assertThrows(DuplicateResourceException.class, () -> usuarioService.actualizarPerfilEstudiante(
            segundoUsuario.idUsuario(),
            new ActualizarPerfilEstudianteRequest(
                primerPerfil.cif(),
                "correo-unico-duplicado@uam.edu.ni",
                carrera.getId()
            )
        ));

        assertThrows(DuplicateResourceException.class, () -> usuarioService.actualizarPerfilEstudiante(
            segundoUsuario.idUsuario(),
            new ActualizarPerfilEstudianteRequest(
                "CIF-UNICO-DUPLICADO",
                primerPerfil.correoInstitucional(),
                carrera.getId()
            )
        ));
    }

    @Test
    void creaYConsultaPerfilAdministrador() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("perfil-admin"));
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("administrador"));

        PerfilAdministradorResponse creado = usuarioService.crearPerfilAdministrador(
            usuario.idUsuario(),
            new CrearPerfilAdministradorRequest("Coordinador de innovacion", "total")
        );
        PerfilAdministradorResponse consultado = usuarioService.obtenerPerfilAdministrador(usuario.idUsuario());

        assertEquals(usuario.idUsuario(), creado.idUsuario());
        assertEquals("Coordinador de innovacion", creado.cargo());
        assertEquals("total", creado.nivelAcceso());
        assertEquals(creado, consultado);
    }

    @Test
    void rechazaPerfilDocenteSinRolDocente() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("sin-rol-docente"));

        assertThrows(BadRequestException.class, () -> usuarioService.crearPerfilDocente(
            usuario.idUsuario(),
            perfilDocenteRequest()
        ));
    }

    @Test
    void creaYConsultaPerfilDocente() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("perfil-docente"));
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("docente"));

        PerfilDocenteResponse creado = usuarioService.crearPerfilDocente(usuario.idUsuario(), perfilDocenteRequest());
        PerfilDocenteResponse consultado = usuarioService.obtenerPerfilDocente(usuario.idUsuario());
        UsuarioResponse usuarioConPerfil = usuarioService.obtenerUsuario(usuario.idUsuario());

        assertEquals(usuario.idUsuario(), creado.idUsuario());
        assertEquals("Innovacion educativa", creado.areaAcademica());
        assertEquals("Docente investigador", creado.cargo());
        assertEquals(GradoAcademico.MAESTRIA, creado.gradoAcademico());
        assertEquals("Maestria en educacion", creado.tituloUniversitario());
        assertNull(creado.idFacultad());
        assertEquals(creado, consultado);
        assertEquals(creado, usuarioConPerfil.perfilDocente());
    }

    @Test
    void rechazaPerfilDocenteDuplicado() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("duplicado-docente"));
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("docente"));
        usuarioService.crearPerfilDocente(usuario.idUsuario(), perfilDocenteRequest());

        assertThrows(DuplicateResourceException.class, () -> usuarioService.crearPerfilDocente(
            usuario.idUsuario(),
            perfilDocenteRequest()
        ));
    }

    @Test
    void rechazaPerfilMentorSinRolMentor() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("sin-rol-mentor"));

        assertThrows(BadRequestException.class, () -> usuarioService.crearPerfilMentor(
            usuario.idUsuario(),
            perfilMentorRequest()
        ));
    }

    @Test
    void creaYConsultaPerfilMentor() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("perfil-mentor"));
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("mentor"));

        PerfilMentorResponse creado = usuarioService.crearPerfilMentor(usuario.idUsuario(), perfilMentorRequest());
        PerfilMentorResponse consultado = usuarioService.obtenerPerfilMentor(usuario.idUsuario());
        UsuarioResponse usuarioConPerfil = usuarioService.obtenerUsuario(usuario.idUsuario());

        assertEquals(usuario.idUsuario(), creado.idUsuario());
        assertEquals("Modelos de negocio", creado.areaExperiencia());
        assertEquals("Emprendimiento", creado.especialidad());
        assertEquals("UAM", creado.institucion());
        assertEquals("Mentoria grupal", creado.tipoAcompanamiento());
        assertEquals(GradoAcademico.DOCTORADO, creado.gradoAcademico());
        assertEquals("Doctorado en innovacion", creado.tituloUniversitario());
        assertEquals(creado, consultado);
        assertEquals(creado, usuarioConPerfil.perfilMentor());
    }

    @Test
    void rechazaPerfilParticipanteExternoSinRolParticipanteExterno() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("sin-rol-externo"));

        assertThrows(BadRequestException.class, () -> usuarioService.crearPerfilParticipanteExterno(
            usuario.idUsuario(),
            perfilParticipanteExternoRequest()
        ));
    }

    @Test
    void creaYConsultaPerfilParticipanteExterno() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("perfil-externo"));
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("participante_externo"));

        PerfilParticipanteExternoResponse creado = usuarioService.crearPerfilParticipanteExterno(
            usuario.idUsuario(),
            perfilParticipanteExternoRequest()
        );
        PerfilParticipanteExternoResponse consultado = usuarioService.obtenerPerfilParticipanteExterno(usuario.idUsuario());
        UsuarioResponse usuarioConPerfil = usuarioService.obtenerUsuario(usuario.idUsuario());

        assertEquals(usuario.idUsuario(), creado.idUsuario());
        assertEquals("Emprendedor", creado.ocupacion());
        assertEquals("Empresa externa", creado.institucionProcedencia());
        assertEquals(creado, consultado);
        assertEquals(creado, usuarioConPerfil.perfilParticipanteExterno());
    }

    @Test
    void bloqueaDesactivarRolConPerfilAsociado() {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest("bloqueo-rol"));
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("mentor"));
        usuarioService.crearPerfilMentor(usuario.idUsuario(), perfilMentorRequest());

        assertThrows(BadRequestException.class, () -> usuarioService.desactivarRol(usuario.idUsuario(), "mentor"));
        assertTrue(usuarioRolRepository.existsByUsuarioIdUsuarioAndRolNombreIgnoreCaseAndActivoTrue(
            usuario.idUsuario(),
            "mentor"
        ));
    }

    private CrearUsuarioRequest usuarioRequest(String sufijo) {
        String normalizado = sufijo.toLowerCase();
        return new CrearUsuarioRequest(
            "Usuario " + sufijo,
            "DOC-" + sufijo.toUpperCase(),
            "88880000",
            "usuario-" + normalizado + "@uam.edu.ni",
            "secreto123",
            "F",
            "M"
        );
    }

    private UsuarioResponse crearUsuarioEstudianteConPerfil(String sufijo) {
        return crearUsuarioEstudianteConPerfil(sufijo, crearCarrera(sufijo + "-principal"));
    }

    private UsuarioResponse crearUsuarioEstudianteConPerfil(String sufijo, Carrera carreraPrincipal) {
        UsuarioResponse usuario = usuarioService.crearUsuario(usuarioRequest(sufijo));
        usuarioService.asignarRol(usuario.idUsuario(), new AsignarRolRequest("estudiante"));
        usuarioService.crearPerfilEstudiante(
            usuario.idUsuario(),
            new CrearPerfilEstudianteRequest(
                "CIF-" + codigoUnico(sufijo),
                "perfil-" + codigoUnico(sufijo) + "@uam.edu.ni",
                carreraPrincipal.getId()
            )
        );
        return usuario;
    }

    private Carrera crearCarrera(String sufijo) {
        String codigo = codigoUnico(sufijo);
        Facultad facultad = facultadRepository.save(new Facultad(
            "Facultad " + sufijo,
            "Facultad de prueba",
            "FAC" + codigo
        ));
        return carreraRepository.save(new Carrera(
            "Carrera " + sufijo,
            "Carrera de prueba",
            "CAR" + codigo,
            facultad
        ));
    }

    private String codigoUnico(String valor) {
        return Integer.toUnsignedString(valor.hashCode());
    }

    private CrearPerfilDocenteRequest perfilDocenteRequest() {
        return new CrearPerfilDocenteRequest(
            "Innovacion educativa",
            "Docente investigador",
            GradoAcademico.MAESTRIA,
            "Maestria en educacion",
            null
        );
    }

    private CrearPerfilMentorRequest perfilMentorRequest() {
        return new CrearPerfilMentorRequest(
            "Modelos de negocio",
            "Emprendimiento",
            "UAM",
            "Mentoria grupal",
            GradoAcademico.DOCTORADO,
            "Doctorado en innovacion"
        );
    }

    private CrearPerfilParticipanteExternoRequest perfilParticipanteExternoRequest() {
        return new CrearPerfilParticipanteExternoRequest(
            "Emprendedor",
            "Empresa externa"
        );
    }
}
