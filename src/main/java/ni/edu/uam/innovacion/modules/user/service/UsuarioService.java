package ni.edu.uam.innovacion.modules.user.service;

import java.time.LocalDateTime;
import java.util.List;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.user.dto.ActualizarUsuarioRequest;
import ni.edu.uam.innovacion.modules.user.dto.AsignarRolRequest;
import ni.edu.uam.innovacion.modules.user.dto.CambiarContrasenaRequest;
import ni.edu.uam.innovacion.modules.user.dto.CambiarEstadoUsuarioRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilAdministradorRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilDocenteRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilEstudianteRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilMentorRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilParticipanteExternoRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearUsuarioRequest;
import ni.edu.uam.innovacion.modules.user.dto.PerfilAdministradorResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilDocenteResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilEstudianteResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilMentorResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilParticipanteExternoResponse;
import ni.edu.uam.innovacion.modules.user.dto.UsuarioResponse;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.PerfilDocente;
import ni.edu.uam.innovacion.modules.user.entity.PerfilEstudiante;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;
import ni.edu.uam.innovacion.modules.user.entity.PerfilParticipanteExterno;
import ni.edu.uam.innovacion.modules.user.entity.Rol;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.entity.UsuarioRol;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.mapper.UsuarioMapper;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.PerfilDocenteRepository;
import ni.edu.uam.innovacion.modules.user.repository.PerfilEstudianteRepository;
import ni.edu.uam.innovacion.modules.user.repository.PerfilMentorRepository;
import ni.edu.uam.innovacion.modules.user.repository.PerfilParticipanteExternoRepository;
import ni.edu.uam.innovacion.modules.user.repository.RolRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private static final String ROL_ESTUDIANTE = "estudiante";
    private static final String ROL_ADMINISTRADOR = "administrador";
    private static final String ROL_DOCENTE = "docente";
    private static final String ROL_MENTOR = "mentor";
    private static final String ROL_PARTICIPANTE_EXTERNO = "participante_externo";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PerfilEstudianteRepository perfilEstudianteRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;
    private final PerfilDocenteRepository perfilDocenteRepository;
    private final PerfilMentorRepository perfilMentorRepository;
    private final PerfilParticipanteExternoRepository perfilParticipanteExternoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(
        UsuarioRepository usuarioRepository,
        RolRepository rolRepository,
        UsuarioRolRepository usuarioRolRepository,
        PerfilEstudianteRepository perfilEstudianteRepository,
        PerfilAdministradorRepository perfilAdministradorRepository,
        PerfilDocenteRepository perfilDocenteRepository,
        PerfilMentorRepository perfilMentorRepository,
        PerfilParticipanteExternoRepository perfilParticipanteExternoRepository,
        PasswordEncoder passwordEncoder,
        UsuarioMapper usuarioMapper
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.perfilEstudianteRepository = perfilEstudianteRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
        this.perfilDocenteRepository = perfilDocenteRepository;
        this.perfilMentorRepository = perfilMentorRepository;
        this.perfilParticipanteExternoRepository = perfilParticipanteExternoRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public UsuarioResponse crearUsuario(CrearUsuarioRequest request) {
        String correo = normalizarCorreo(request.correo());
        String documento = limpiar(request.documento());

        validarCorreoDisponible(correo);
        validarDocumentoDisponible(documento);

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(limpiar(request.nombreCompleto()));
        usuario.setDocumento(documento);
        usuario.setTelefono(limpiar(request.telefono()));
        usuario.setCorreo(correo);
        usuario.setContrasenaHash(passwordEncoder.encode(request.contrasena()));
        usuario.setSexo(limpiar(request.sexo()));
        usuario.setTallaCamisa(limpiar(request.tallaCamisa()));
        usuario.setEstado(EstadoUsuario.ACTIVO);

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
            .map(usuarioMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuario(Long idUsuario) {
        return usuarioMapper.toResponse(buscarUsuario(idUsuario));
    }

    @Transactional
    public UsuarioResponse actualizarUsuario(Long idUsuario, ActualizarUsuarioRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        String correo = normalizarCorreo(request.correo());
        String documento = limpiar(request.documento());

        if (usuarioRepository.existsByCorreoIgnoreCaseAndIdUsuarioNot(correo, idUsuario)) {
            throw new DuplicateResourceException("Ya existe otro usuario con el correo " + correo);
        }
        if (usuarioRepository.existsByDocumentoAndIdUsuarioNot(documento, idUsuario)) {
            throw new DuplicateResourceException("Ya existe otro usuario con el documento " + documento);
        }

        usuario.setNombreCompleto(limpiar(request.nombreCompleto()));
        usuario.setDocumento(documento);
        usuario.setTelefono(limpiar(request.telefono()));
        usuario.setCorreo(correo);
        usuario.setSexo(limpiar(request.sexo()));
        usuario.setTallaCamisa(limpiar(request.tallaCamisa()));

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse cambiarContrasena(Long idUsuario, CambiarContrasenaRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        usuario.setContrasenaHash(passwordEncoder.encode(request.contrasena()));
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse cambiarEstado(Long idUsuario, CambiarEstadoUsuarioRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        usuario.setEstado(request.estado());
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse asignarRol(Long idUsuario, AsignarRolRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        String nombreRol = RolService.normalizarNombreRol(request.nombreRol());
        Rol rol = rolRepository.findByNombreIgnoreCase(nombreRol)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el rol " + nombreRol));

        UsuarioRol usuarioRol = usuarioRolRepository
            .findByUsuarioIdUsuarioAndRolNombreIgnoreCase(idUsuario, nombreRol)
            .orElseGet(UsuarioRol::new);

        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRol.setActivo(Boolean.TRUE);
        usuarioRol.setFechaAsignacion(LocalDateTime.now());
        usuarioRolRepository.saveAndFlush(usuarioRol);

        return usuarioMapper.toResponse(buscarUsuario(idUsuario));
    }

    @Transactional
    public void desactivarRol(Long idUsuario, String nombreRol) {
        buscarUsuario(idUsuario);
        String rolNormalizado = RolService.normalizarNombreRol(nombreRol);
        validarRolSinPerfilAsociado(idUsuario, rolNormalizado);

        UsuarioRol usuarioRol = usuarioRolRepository
            .findByUsuarioIdUsuarioAndRolNombreIgnoreCase(idUsuario, rolNormalizado)
            .orElseThrow(() -> new ResourceNotFoundException(
                "El usuario no tiene asignado el rol " + rolNormalizado
            ));

        usuarioRol.setActivo(Boolean.FALSE);
        usuarioRolRepository.save(usuarioRol);
    }

    @Transactional
    public PerfilEstudianteResponse crearPerfilEstudiante(Long idUsuario, CrearPerfilEstudianteRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        validarRolActivo(idUsuario, ROL_ESTUDIANTE, "Para crear perfil estudiante el usuario debe tener rol estudiante activo");

        if (perfilEstudianteRepository.existsById(idUsuario)) {
            throw new DuplicateResourceException("El usuario ya tiene perfil estudiante");
        }

        String cif = limpiar(request.cif());
        if (perfilEstudianteRepository.existsByCifIgnoreCase(cif)) {
            throw new DuplicateResourceException("Ya existe un perfil estudiante con el CIF " + cif);
        }

        String correoInstitucional = normalizarCorreoOpcional(request.correoInstitucional());
        if (correoInstitucional != null && perfilEstudianteRepository.existsByCorreoInstitucionalIgnoreCase(correoInstitucional)) {
            throw new DuplicateResourceException("Ya existe un perfil estudiante con el correo institucional " + correoInstitucional);
        }

        PerfilEstudiante perfil = new PerfilEstudiante();
        perfil.setUsuario(usuario);
        perfil.setCif(cif);
        perfil.setCorreoInstitucional(correoInstitucional);
        perfil.setIdCarreraPrincipal(request.idCarreraPrincipal());
        perfil.setDobleTitular(Boolean.TRUE.equals(request.dobleTitular()));

        PerfilEstudiante guardado = perfilEstudianteRepository.save(perfil);
        usuario.setPerfilEstudiante(guardado);
        return usuarioMapper.toPerfilEstudianteResponse(guardado);
    }

    @Transactional(readOnly = true)
    public PerfilEstudianteResponse obtenerPerfilEstudiante(Long idUsuario) {
        PerfilEstudiante perfil = perfilEstudianteRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene perfil estudiante"));
        return usuarioMapper.toPerfilEstudianteResponse(perfil);
    }

    @Transactional
    public PerfilAdministradorResponse crearPerfilAdministrador(Long idUsuario, CrearPerfilAdministradorRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        validarRolActivo(idUsuario, ROL_ADMINISTRADOR, "Para crear perfil administrador el usuario debe tener rol administrador activo");

        if (perfilAdministradorRepository.existsById(idUsuario)) {
            throw new DuplicateResourceException("El usuario ya tiene perfil administrador");
        }

        PerfilAdministrador perfil = new PerfilAdministrador();
        perfil.setUsuario(usuario);
        perfil.setCargo(limpiar(request.cargo()));
        perfil.setNivelAcceso(limpiar(request.nivelAcceso()));

        PerfilAdministrador guardado = perfilAdministradorRepository.save(perfil);
        usuario.setPerfilAdministrador(guardado);
        return usuarioMapper.toPerfilAdministradorResponse(guardado);
    }

    @Transactional(readOnly = true)
    public PerfilAdministradorResponse obtenerPerfilAdministrador(Long idUsuario) {
        PerfilAdministrador perfil = perfilAdministradorRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene perfil administrador"));
        return usuarioMapper.toPerfilAdministradorResponse(perfil);
    }

    @Transactional
    public PerfilDocenteResponse crearPerfilDocente(Long idUsuario, CrearPerfilDocenteRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        validarRolActivo(idUsuario, ROL_DOCENTE, "Para crear perfil docente el usuario debe tener rol docente activo");

        if (perfilDocenteRepository.existsById(idUsuario)) {
            throw new DuplicateResourceException("El usuario ya tiene perfil docente");
        }

        PerfilDocente perfil = new PerfilDocente();
        perfil.setUsuario(usuario);
        perfil.setAreaAcademica(limpiar(request.areaAcademica()));
        perfil.setCargo(limpiar(request.cargo()));
        perfil.setGradoAcademico(request.gradoAcademico());
        perfil.setTituloUniversitario(limpiar(request.tituloUniversitario()));
        perfil.setIdFacultad(request.idFacultad());

        PerfilDocente guardado = perfilDocenteRepository.save(perfil);
        usuario.setPerfilDocente(guardado);
        return usuarioMapper.toPerfilDocenteResponse(guardado);
    }

    @Transactional(readOnly = true)
    public PerfilDocenteResponse obtenerPerfilDocente(Long idUsuario) {
        PerfilDocente perfil = perfilDocenteRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene perfil docente"));
        return usuarioMapper.toPerfilDocenteResponse(perfil);
    }

    @Transactional
    public PerfilMentorResponse crearPerfilMentor(Long idUsuario, CrearPerfilMentorRequest request) {
        Usuario usuario = buscarUsuario(idUsuario);
        validarRolActivo(idUsuario, ROL_MENTOR, "Para crear perfil mentor el usuario debe tener rol mentor activo");

        if (perfilMentorRepository.existsById(idUsuario)) {
            throw new DuplicateResourceException("El usuario ya tiene perfil mentor");
        }

        PerfilMentor perfil = new PerfilMentor();
        perfil.setUsuario(usuario);
        perfil.setAreaExperiencia(limpiar(request.areaExperiencia()));
        perfil.setEspecialidad(limpiar(request.especialidad()));
        perfil.setInstitucion(limpiar(request.institucion()));
        perfil.setTipoAcompanamiento(limpiar(request.tipoAcompanamiento()));
        perfil.setGradoAcademico(request.gradoAcademico());
        perfil.setTituloUniversitario(limpiar(request.tituloUniversitario()));

        PerfilMentor guardado = perfilMentorRepository.save(perfil);
        usuario.setPerfilMentor(guardado);
        return usuarioMapper.toPerfilMentorResponse(guardado);
    }

    @Transactional(readOnly = true)
    public PerfilMentorResponse obtenerPerfilMentor(Long idUsuario) {
        PerfilMentor perfil = perfilMentorRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene perfil mentor"));
        return usuarioMapper.toPerfilMentorResponse(perfil);
    }

    @Transactional
    public PerfilParticipanteExternoResponse crearPerfilParticipanteExterno(
        Long idUsuario,
        CrearPerfilParticipanteExternoRequest request
    ) {
        Usuario usuario = buscarUsuario(idUsuario);
        validarRolActivo(
            idUsuario,
            ROL_PARTICIPANTE_EXTERNO,
            "Para crear perfil participante externo el usuario debe tener rol participante_externo activo"
        );

        if (perfilParticipanteExternoRepository.existsById(idUsuario)) {
            throw new DuplicateResourceException("El usuario ya tiene perfil participante externo");
        }

        PerfilParticipanteExterno perfil = new PerfilParticipanteExterno();
        perfil.setUsuario(usuario);
        perfil.setOcupacion(limpiar(request.ocupacion()));
        perfil.setInstitucionProcedencia(limpiar(request.institucionProcedencia()));

        PerfilParticipanteExterno guardado = perfilParticipanteExternoRepository.save(perfil);
        usuario.setPerfilParticipanteExterno(guardado);
        return usuarioMapper.toPerfilParticipanteExternoResponse(guardado);
    }

    @Transactional(readOnly = true)
    public PerfilParticipanteExternoResponse obtenerPerfilParticipanteExterno(Long idUsuario) {
        PerfilParticipanteExterno perfil = perfilParticipanteExternoRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene perfil participante externo"));
        return usuarioMapper.toPerfilParticipanteExternoResponse(perfil);
    }

    private Usuario buscarUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con id " + idUsuario));
    }

    private void validarRolActivo(Long idUsuario, String nombreRol, String mensaje) {
        if (!usuarioRolRepository.existsByUsuarioIdUsuarioAndRolNombreIgnoreCaseAndActivoTrue(idUsuario, nombreRol)) {
            throw new BadRequestException(mensaje);
        }
    }

    private void validarRolSinPerfilAsociado(Long idUsuario, String nombreRol) {
        if (ROL_ESTUDIANTE.equals(nombreRol) && perfilEstudianteRepository.existsById(idUsuario)) {
            throw new BadRequestException("No se puede desactivar el rol estudiante porque el usuario tiene perfil estudiante");
        }
        if (ROL_ADMINISTRADOR.equals(nombreRol) && perfilAdministradorRepository.existsById(idUsuario)) {
            throw new BadRequestException("No se puede desactivar el rol administrador porque el usuario tiene perfil administrador");
        }
        if (ROL_DOCENTE.equals(nombreRol) && perfilDocenteRepository.existsById(idUsuario)) {
            throw new BadRequestException("No se puede desactivar el rol docente porque el usuario tiene perfil docente");
        }
        if (ROL_MENTOR.equals(nombreRol) && perfilMentorRepository.existsById(idUsuario)) {
            throw new BadRequestException("No se puede desactivar el rol mentor porque el usuario tiene perfil mentor");
        }
        if (ROL_PARTICIPANTE_EXTERNO.equals(nombreRol) && perfilParticipanteExternoRepository.existsById(idUsuario)) {
            throw new BadRequestException(
                "No se puede desactivar el rol participante_externo porque el usuario tiene perfil participante externo"
            );
        }
    }

    private void validarCorreoDisponible(String correo) {
        if (usuarioRepository.existsByCorreoIgnoreCase(correo)) {
            throw new DuplicateResourceException("Ya existe un usuario con el correo " + correo);
        }
    }

    private void validarDocumentoDisponible(String documento) {
        if (usuarioRepository.existsByDocumento(documento)) {
            throw new DuplicateResourceException("Ya existe un usuario con el documento " + documento);
        }
    }

    private String normalizarCorreo(String correo) {
        return correo == null ? null : correo.trim().toLowerCase();
    }

    private String normalizarCorreoOpcional(String correo) {
        String correoNormalizado = normalizarCorreo(correo);
        return correoNormalizado == null || correoNormalizado.isBlank() ? null : correoNormalizado;
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
