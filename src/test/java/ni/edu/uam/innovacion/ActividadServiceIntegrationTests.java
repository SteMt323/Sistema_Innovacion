package ni.edu.uam.innovacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.modules.activity.dto.ActividadResponse;
import ni.edu.uam.innovacion.modules.activity.dto.ActualizarActividadRequest;
import ni.edu.uam.innovacion.modules.activity.dto.CrearActividadRequest;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.enums.ModalidadActividad;
import ni.edu.uam.innovacion.modules.activity.service.ActividadService;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaDIEM;
import ni.edu.uam.innovacion.modules.catalog.repository.AmbitoActividadRepository;
import ni.edu.uam.innovacion.modules.catalog.repository.CategoriaDIEMRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ActividadServiceIntegrationTests {

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private AmbitoActividadRepository ambitoActividadRepository;

    @Autowired
    private CategoriaDIEMRepository categoriaDIEMRepository;

    @Autowired
    private PerfilAdministradorRepository perfilAdministradorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void creaActividadDiemConCategoriaYResponsable() {
        AmbitoActividad diem = crearAmbito("DIEM", true);
        CategoriaDIEM categoria = crearCategoria("Evento", diem);
        PerfilAdministrador administrador = crearAdministrador("crea-diem");
        Usuario responsable = crearUsuario("responsable-diem");

        ActividadResponse response = actividadService.crear(new CrearActividadRequest(
            diem.getId(),
            categoria.getId(),
            administrador.getIdUsuario(),
            responsable.getIdUsuario(),
            "Hackathon UAM",
            "Actividad de innovacion",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            ModalidadActividad.PRESENCIAL,
            50,
            "Auditorio",
            "Equipo DIEM",
            20
        ));

        assertNotNull(response.idActividad());
        assertEquals("Hackathon UAM", response.nombre());
        assertEquals(EstadoActividad.BORRADOR, response.estado());
        assertEquals(ModalidadActividad.PRESENCIAL, response.modalidad());
        assertEquals(diem.getId(), response.idAmbitoActividad());
        assertEquals(categoria.getId(), response.idCategoriaDiem());
        assertEquals(administrador.getIdUsuario(), response.idAdministradorCreador());
        assertEquals(responsable.getIdUsuario(), response.idResponsableUsuario());
        assertEquals(20, response.puntosBase());
    }

    @Test
    void rechazaActividadDiemSinCategoria() {
        AmbitoActividad diem = crearAmbito("DIEM", true);
        PerfilAdministrador administrador = crearAdministrador("diem-sin-categoria");

        CrearActividadRequest request = actividadRequest(diem, null, administrador, "diem-sin-categoria");

        assertThrows(BadRequestException.class, () -> actividadService.crear(request));
    }

    @Test
    void rechazaActividadExternaConCategoriaDiem() {
        AmbitoActividad diem = crearAmbito("DIEM", true);
        AmbitoActividad externa = crearAmbito("EXTERNA", false);
        CategoriaDIEM categoria = crearCategoria("Concurso", diem);
        PerfilAdministrador administrador = crearAdministrador("externa-con-categoria");

        CrearActividadRequest request = actividadRequest(externa, categoria, administrador, "externa-con-categoria");

        assertThrows(BadRequestException.class, () -> actividadService.crear(request));
    }

    @Test
    void rechazaCategoriaDeOtroAmbito() {
        AmbitoActividad diem = crearAmbito("DIEM", true);
        AmbitoActividad otroDiem = crearAmbito("DIEM SECUNDARIO", true);
        CategoriaDIEM categoriaDeOtroAmbito = crearCategoria("Formacion", otroDiem);
        PerfilAdministrador administrador = crearAdministrador("categoria-otro-ambito");

        CrearActividadRequest request = actividadRequest(
            diem,
            categoriaDeOtroAmbito,
            administrador,
            "categoria-otro-ambito"
        );

        assertThrows(BadRequestException.class, () -> actividadService.crear(request));
    }

    @Test
    void rechazaFechasCuposYPuntosInvalidos() {
        AmbitoActividad diem = crearAmbito("DIEM", true);
        CategoriaDIEM categoria = crearCategoria("Proyecto", diem);
        PerfilAdministrador administrador = crearAdministrador("validaciones");
        LocalDateTime inicio = LocalDateTime.now().plusDays(3);

        assertThrows(BadRequestException.class, () -> actividadService.crear(new CrearActividadRequest(
            diem.getId(),
            categoria.getId(),
            administrador.getIdUsuario(),
            null,
            "Actividad fechas invalidas",
            null,
            inicio,
            inicio.minusDays(1),
            ModalidadActividad.HIBRIDA,
            10,
            "Aula",
            null,
            5
        )));

        assertThrows(BadRequestException.class, () -> actividadService.crear(new CrearActividadRequest(
            diem.getId(),
            categoria.getId(),
            administrador.getIdUsuario(),
            null,
            "Actividad cupo invalido",
            null,
            inicio,
            inicio.plusDays(1),
            ModalidadActividad.HIBRIDA,
            0,
            "Aula",
            null,
            5
        )));

        assertThrows(BadRequestException.class, () -> actividadService.crear(new CrearActividadRequest(
            diem.getId(),
            categoria.getId(),
            administrador.getIdUsuario(),
            null,
            "Actividad puntos invalidos",
            null,
            inicio,
            inicio.plusDays(1),
            ModalidadActividad.HIBRIDA,
            10,
            "Aula",
            null,
            -1
        )));
    }

    @Test
    void actualizaActividadSinCambiarAdministradorNiEstado() {
        AmbitoActividad diem = crearAmbito("DIEM", true);
        AmbitoActividad externa = crearAmbito("EXTERNA", false);
        CategoriaDIEM categoria = crearCategoria("Evento", diem);
        PerfilAdministrador administrador = crearAdministrador("actualiza");
        ActividadResponse creada = actividadService.crear(actividadRequest(diem, categoria, administrador, "actualiza"));

        ActividadResponse actualizada = actividadService.actualizar(
            creada.idActividad(),
            new ActualizarActividadRequest(
                externa.getId(),
                null,
                null,
                "Actividad externa actualizada",
                "Nueva descripcion",
                LocalDateTime.now().plusDays(5),
                null,
                ModalidadActividad.VIRTUAL,
                null,
                "Sala virtual",
                "Responsable externo",
                null
            )
        );

        assertEquals(creada.idAdministradorCreador(), actualizada.idAdministradorCreador());
        assertEquals(EstadoActividad.BORRADOR, actualizada.estado());
        assertEquals(externa.getId(), actualizada.idAmbitoActividad());
        assertNull(actualizada.idCategoriaDiem());
        assertEquals("Actividad externa actualizada", actualizada.nombre());
        assertEquals(0, actualizada.puntosBase());
    }

    @Test
    void publicaListaDisponiblesEImpideTransicionInvalida() {
        AmbitoActividad diem = crearAmbito("DIEM", true);
        CategoriaDIEM categoria = crearCategoria("Concurso", diem);
        PerfilAdministrador administrador = crearAdministrador("transiciones");
        ActividadResponse creada = actividadService.crear(actividadRequest(diem, categoria, administrador, "transiciones"));

        assertEquals(EstadoActividad.BORRADOR, creada.estado());
        assertTrue(actividadService.listarDisponibles().isEmpty());

        ActividadResponse publicada = actividadService.publicar(creada.idActividad());

        assertEquals(EstadoActividad.PUBLICADA, publicada.estado());
        List<ActividadResponse> disponibles = actividadService.listarDisponibles();
        assertEquals(1, disponibles.size());
        assertEquals(publicada.idActividad(), disponibles.get(0).idActividad());
        assertThrows(BadRequestException.class, () -> actividadService.publicar(publicada.idActividad()));

        ActividadResponse iniciada = actividadService.iniciar(publicada.idActividad());

        assertEquals(EstadoActividad.EN_CURSO, iniciada.estado());
        assertFalse(actividadService.listarDisponibles().stream()
            .anyMatch(actividad -> actividad.idActividad().equals(iniciada.idActividad())));

        ActividadResponse finalizada = actividadService.finalizar(iniciada.idActividad());
        ActividadResponse archivada = actividadService.archivar(finalizada.idActividad());

        assertEquals(EstadoActividad.ARCHIVADA, archivada.estado());
        assertThrows(BadRequestException.class, () -> actividadService.actualizar(
            archivada.idActividad(),
            new ActualizarActividadRequest(
                diem.getId(),
                categoria.getId(),
                null,
                "No debe actualizar",
                null,
                LocalDateTime.now().plusDays(10),
                null,
                ModalidadActividad.PRESENCIAL,
                10,
                null,
                null,
                0
            )
        ));
    }

    private CrearActividadRequest actividadRequest(
        AmbitoActividad ambitoActividad,
        CategoriaDIEM categoriaDiem,
        PerfilAdministrador administrador,
        String sufijo
    ) {
        return new CrearActividadRequest(
            ambitoActividad.getId(),
            categoriaDiem == null ? null : categoriaDiem.getId(),
            administrador.getIdUsuario(),
            null,
            "Actividad " + sufijo,
            "Descripcion " + sufijo,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            ModalidadActividad.HIBRIDA,
            25,
            "Campus UAM",
            "Responsable " + sufijo,
            15
        );
    }

    private AmbitoActividad crearAmbito(String nombre, boolean requiereCategoria) {
        return ambitoActividadRepository.save(new AmbitoActividad(
            nombre,
            "Ambito " + nombre,
            requiereCategoria
        ));
    }

    private CategoriaDIEM crearCategoria(String nombre, AmbitoActividad ambitoActividad) {
        return categoriaDIEMRepository.save(new CategoriaDIEM(
            nombre,
            "Categoria " + nombre,
            "Criterios " + nombre,
            ambitoActividad
        ));
    }

    private PerfilAdministrador crearAdministrador(String sufijo) {
        Usuario usuario = crearUsuario("admin-" + sufijo);
        PerfilAdministrador perfilAdministrador = new PerfilAdministrador();
        perfilAdministrador.setUsuario(usuario);
        perfilAdministrador.setCargo("Coordinador");
        perfilAdministrador.setNivelAcceso("total");
        return perfilAdministradorRepository.save(perfilAdministrador);
    }

    private Usuario crearUsuario(String sufijo) {
        String codigo = codigoUnico(sufijo);
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Usuario " + sufijo);
        usuario.setDocumento("DOC-ACT-" + codigo);
        usuario.setTelefono("88880000");
        usuario.setCorreo("actividad-" + codigo + "@uam.edu.ni");
        usuario.setContrasenaHash("hash-" + codigo);
        usuario.setSexo("N");
        usuario.setTallaCamisa("M");
        usuario.setEstado(EstadoUsuario.ACTIVO);
        return usuarioRepository.save(usuario);
    }

    private String codigoUnico(String valor) {
        return Integer.toUnsignedString(valor.hashCode());
    }
}
