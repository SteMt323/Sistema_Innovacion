package ni.edu.uam.innovacion.modules.user.mapper;

import java.util.Comparator;
import java.util.List;
import ni.edu.uam.innovacion.modules.user.dto.PerfilAdministradorResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilDocenteResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilEstudianteResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilMentorResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilParticipanteExternoResponse;
import ni.edu.uam.innovacion.modules.user.dto.RolResponse;
import ni.edu.uam.innovacion.modules.user.dto.UsuarioResponse;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.PerfilDocente;
import ni.edu.uam.innovacion.modules.user.entity.PerfilEstudiante;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;
import ni.edu.uam.innovacion.modules.user.entity.PerfilParticipanteExterno;
import ni.edu.uam.innovacion.modules.user.entity.Rol;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.entity.UsuarioRol;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getIdUsuario(),
            usuario.getNombreCompleto(),
            usuario.getDocumento(),
            usuario.getTelefono(),
            usuario.getCorreo(),
            usuario.getSexo(),
            usuario.getTallaCamisa(),
            usuario.getEstado(),
            usuario.getFechaRegistro(),
            usuario.getUltimoAcceso(),
            toRolResponses(usuario),
            toPerfilEstudianteResponse(usuario.getPerfilEstudiante()),
            toPerfilAdministradorResponse(usuario.getPerfilAdministrador()),
            toPerfilDocenteResponse(usuario.getPerfilDocente()),
            toPerfilMentorResponse(usuario.getPerfilMentor()),
            toPerfilParticipanteExternoResponse(usuario.getPerfilParticipanteExterno())
        );
    }

    public RolResponse toRolResponse(Rol rol) {
        return new RolResponse(rol.getIdRol(), rol.getNombre(), rol.getDescripcion());
    }

    public PerfilEstudianteResponse toPerfilEstudianteResponse(PerfilEstudiante perfil) {
        if (perfil == null) {
            return null;
        }

        return new PerfilEstudianteResponse(
            perfil.getIdUsuario(),
            perfil.getCif(),
            perfil.getCorreoInstitucional(),
            perfil.getIdCarreraPrincipal(),
            perfil.getDobleTitular()
        );
    }

    public PerfilAdministradorResponse toPerfilAdministradorResponse(PerfilAdministrador perfil) {
        if (perfil == null) {
            return null;
        }

        return new PerfilAdministradorResponse(
            perfil.getIdUsuario(),
            perfil.getCargo(),
            perfil.getNivelAcceso()
        );
    }

    public PerfilDocenteResponse toPerfilDocenteResponse(PerfilDocente perfil) {
        if (perfil == null) {
            return null;
        }

        return new PerfilDocenteResponse(
            perfil.getIdUsuario(),
            perfil.getAreaAcademica(),
            perfil.getCargo(),
            perfil.getGradoAcademico(),
            perfil.getTituloUniversitario(),
            perfil.getIdFacultad()
        );
    }

    public PerfilMentorResponse toPerfilMentorResponse(PerfilMentor perfil) {
        if (perfil == null) {
            return null;
        }

        return new PerfilMentorResponse(
            perfil.getIdUsuario(),
            perfil.getAreaExperiencia(),
            perfil.getEspecialidad(),
            perfil.getInstitucion(),
            perfil.getTipoAcompanamiento(),
            perfil.getGradoAcademico(),
            perfil.getTituloUniversitario()
        );
    }

    public PerfilParticipanteExternoResponse toPerfilParticipanteExternoResponse(PerfilParticipanteExterno perfil) {
        if (perfil == null) {
            return null;
        }

        return new PerfilParticipanteExternoResponse(
            perfil.getIdUsuario(),
            perfil.getOcupacion(),
            perfil.getInstitucionProcedencia()
        );
    }

    private List<RolResponse> toRolResponses(Usuario usuario) {
        return usuario.getUsuarioRoles().stream()
            .filter(usuarioRol -> Boolean.TRUE.equals(usuarioRol.getActivo()))
            .map(UsuarioRol::getRol)
            .sorted(Comparator.comparing(Rol::getNombre))
            .map(this::toRolResponse)
            .toList();
    }
}
