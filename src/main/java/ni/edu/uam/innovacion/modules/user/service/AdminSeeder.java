package ni.edu.uam.innovacion.modules.user.service;

import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.catalog.entity.Rol;
import ni.edu.uam.innovacion.modules.catalog.repository.RolRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.entity.UsuarioRol;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Garantiza que exista al menos un administrador con su rol activo en
 * {@code usuario_roles}, para que el portal admin funcione de forma
 * determinista tras un despliegue limpio.
 *
 * El rol administrativo (la authority {@code ROLE_ADMINISTRADOR} del JWT) se
 * deriva exclusivamente de {@code usuario_roles}, no de {@code perfiles_administrador}.
 * Por eso una cuenta insertada a mano sin esa fila recibe 403 en {@code /api/admin/**};
 * este seeder la repara o crea la cuenta completa.
 *
 * Es idempotente: en cada arranque solo crea lo que falta y reactiva el rol si
 * estaba inactivo. Corre despues de {@link ni.edu.uam.innovacion.modules.catalog.service.RolSeeder}.
 *
 * Seguridad: la contrasena por defecto es solo para desarrollo. En produccion
 * definir {@code BOOTSTRAP_ADMIN_*} o desactivar con {@code app.bootstrap.admin.enabled=false}.
 */
@Component
@Order(2)
public class AdminSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);
    private static final String ROL_ADMINISTRADOR = "administrador";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;
    private final PasswordEncoder passwordEncoder;

    private final boolean enabled;
    private final String correo;
    private final String contrasena;
    private final String nombre;
    private final String documento;

    public AdminSeeder(
        UsuarioRepository usuarioRepository,
        RolRepository rolRepository,
        UsuarioRolRepository usuarioRolRepository,
        PerfilAdministradorRepository perfilAdministradorRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.bootstrap.admin.enabled:true}") boolean enabled,
        @Value("${app.bootstrap.admin.correo:admin@uam.edu.ni}") String correo,
        @Value("${app.bootstrap.admin.contrasena:Admin12345}") String contrasena,
        @Value("${app.bootstrap.admin.nombre:Administrador del Sistema}") String nombre,
        @Value("${app.bootstrap.admin.documento:ADMIN-0001}") String documento
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
        this.passwordEncoder = passwordEncoder;
        this.enabled = enabled;
        this.correo = correo == null ? null : correo.trim().toLowerCase();
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.documento = documento == null ? null : documento.trim();
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }
        if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            log.warn("Bootstrap admin desactivado: faltan correo o contrasena en la configuracion");
            return;
        }

        Rol rolAdmin = rolRepository.findByNombreIgnoreCase(ROL_ADMINISTRADOR).orElse(null);
        if (rolAdmin == null || !rolAdmin.estaActivo()) {
            log.warn("Bootstrap admin: no existe el rol '{}' activo todavia; se omite", ROL_ADMINISTRADOR);
            return;
        }

        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo).orElse(null);
        if (usuario == null) {
            if (documento == null || documento.isBlank() || usuarioRepository.existsByDocumento(documento)) {
                log.warn(
                    "Bootstrap admin: no se pudo crear la cuenta '{}' (documento ausente o en uso). "
                        + "Asigne el rol manualmente o ajuste app.bootstrap.admin.documento",
                    correo
                );
                return;
            }
            usuario = new Usuario();
            usuario.setNombreCompleto(nombre);
            usuario.setDocumento(documento);
            usuario.setCorreo(correo);
            usuario.setContrasenaHash(passwordEncoder.encode(contrasena));
            usuario.setEstado(EstadoUsuario.ACTIVO);
            usuario = usuarioRepository.save(usuario);
            log.info("Bootstrap admin: cuenta administradora creada para {}", correo);
        }

        garantizarRolAdministrador(usuario, rolAdmin);
        garantizarPerfilAdministrador(usuario);
    }

    private void garantizarRolAdministrador(Usuario usuario, Rol rolAdmin) {
        UsuarioRol usuarioRol = usuarioRolRepository
            .findByUsuarioIdUsuarioAndRolNombreIgnoreCase(usuario.getIdUsuario(), ROL_ADMINISTRADOR)
            .orElseGet(UsuarioRol::new);

        boolean nuevo = usuarioRol.getUsuario() == null;
        boolean estabaInactivo = Boolean.FALSE.equals(usuarioRol.getActivo());

        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rolAdmin);
        usuarioRol.setActivo(Boolean.TRUE);
        if (usuarioRol.getFechaAsignacion() == null) {
            usuarioRol.setFechaAsignacion(LocalDateTime.now());
        }
        usuarioRolRepository.save(usuarioRol);

        if (nuevo) {
            log.info("Bootstrap admin: rol '{}' asignado a {}", ROL_ADMINISTRADOR, usuario.getCorreo());
        } else if (estabaInactivo) {
            log.info("Bootstrap admin: rol '{}' reactivado para {}", ROL_ADMINISTRADOR, usuario.getCorreo());
        }
    }

    private void garantizarPerfilAdministrador(Usuario usuario) {
        if (perfilAdministradorRepository.existsById(usuario.getIdUsuario())) {
            return;
        }
        PerfilAdministrador perfil = new PerfilAdministrador();
        perfil.setUsuario(usuario);
        perfil.setCargo("Administrador");
        perfil.setNivelAcceso("total");
        perfilAdministradorRepository.save(perfil);
        log.info("Bootstrap admin: perfil administrador creado para {}", usuario.getCorreo());
    }
}
