package ni.edu.uam.innovacion.modules.mentorship.service;

import java.util.List;
import java.util.Map;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.mentorship.dto.ActualizarPerfilMentorRequest;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentorAdminResponse;
import ni.edu.uam.innovacion.modules.mentorship.entity.MentoriaActividad;
import ni.edu.uam.innovacion.modules.mentorship.repository.MentorMentoriaActivaProjection;
import ni.edu.uam.innovacion.modules.mentorship.repository.MentoriaActividadRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilMentorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MentorAdminService {

    private final PerfilMentorRepository perfilMentorRepository;
    private final MentoriaActividadRepository mentoriaActividadRepository;

    public MentorAdminService(
        PerfilMentorRepository perfilMentorRepository,
        MentoriaActividadRepository mentoriaActividadRepository
    ) {
        this.perfilMentorRepository = perfilMentorRepository;
        this.mentoriaActividadRepository = mentoriaActividadRepository;
    }

    @Transactional(readOnly = true)
    public List<MentorAdminResponse> listar(
        String q,
        EstadoUsuario estadoUsuario,
        boolean soloConMentoriasActivas
    ) {
        List<PerfilMentor> perfiles = estadoUsuario == null
            ? perfilMentorRepository.findAllByOrderByUsuarioNombreCompletoAsc()
            : perfilMentorRepository.findByUsuarioEstadoOrderByUsuarioNombreCompletoAsc(estadoUsuario);

        Map<Long, Long> mentoriasActivas = mentoriaActividadRepository
            .contarActivasPorMentor(
                MentoriaActividad.ROL_COLABORADOR_MENTOR,
                EstadoRegistro.ACTIVO
            )
            .stream()
            .collect(java.util.stream.Collectors.toMap(
                MentorMentoriaActivaProjection::getIdMentor,
                MentorMentoriaActivaProjection::getTotalActivas
            ));

        String filtro = normalizar(q);
        return perfiles.stream()
            .filter(perfil -> coincide(perfil, filtro))
            .map(perfil -> toResponse(perfil, mentoriasActivas.getOrDefault(perfil.getIdUsuario(), 0L)))
            .filter(mentor -> !soloConMentoriasActivas || mentor.mentoriasActivas() > 0)
            .toList();
    }

    @Transactional(readOnly = true)
    public MentorAdminResponse obtenerPorId(Long idMentor) {
        PerfilMentor perfil = obtenerPerfilMentor(idMentor);
        long mentoriasActivas = mentoriaActividadRepository.countByMentorIdUsuarioAndRolColaboradorIgnoreCaseAndEstado(
            idMentor,
            MentoriaActividad.ROL_COLABORADOR_MENTOR,
            EstadoRegistro.ACTIVO
        );
        return toResponse(perfil, mentoriasActivas);
    }

    public MentorAdminResponse actualizar(Long idMentor, ActualizarPerfilMentorRequest request) {
        PerfilMentor perfil = obtenerPerfilMentor(idMentor);
        perfil.setAreaExperiencia(limpiar(request.areaExperiencia()));
        perfil.setEspecialidad(limpiar(request.especialidad()));
        perfil.setInstitucion(limpiar(request.institucion()));
        perfil.setTipoAcompanamiento(limpiar(request.tipoAcompanamiento()));
        perfil.setGradoAcademico(request.gradoAcademico());
        perfil.setTituloUniversitario(limpiar(request.tituloUniversitario()));

        PerfilMentor guardado = perfilMentorRepository.save(perfil);
        long mentoriasActivas = mentoriaActividadRepository.countByMentorIdUsuarioAndRolColaboradorIgnoreCaseAndEstado(
            idMentor,
            MentoriaActividad.ROL_COLABORADOR_MENTOR,
            EstadoRegistro.ACTIVO
        );
        return toResponse(guardado, mentoriasActivas);
    }

    @Transactional(readOnly = true)
    public long totalMentoresRegistrados() {
        return perfilMentorRepository.count();
    }

    @Transactional(readOnly = true)
    public long totalMentoresActivos() {
        return perfilMentorRepository.countByUsuarioEstado(EstadoUsuario.ACTIVO);
    }

    private PerfilMentor obtenerPerfilMentor(Long idMentor) {
        return perfilMentorRepository.findById(idMentor)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el perfil mentor con id " + idMentor
            ));
    }

    private MentorAdminResponse toResponse(PerfilMentor perfil, long mentoriasActivas) {
        return new MentorAdminResponse(
            perfil.getIdUsuario(),
            perfil.getUsuario().getNombreCompleto(),
            perfil.getUsuario().getDocumento(),
            perfil.getUsuario().getTelefono(),
            perfil.getUsuario().getCorreo(),
            perfil.getUsuario().getEstado(),
            perfil.getAreaExperiencia(),
            perfil.getEspecialidad(),
            perfil.getInstitucion(),
            perfil.getTipoAcompanamiento(),
            perfil.getGradoAcademico(),
            perfil.getTituloUniversitario(),
            mentoriasActivas
        );
    }

    private boolean coincide(PerfilMentor perfil, String filtro) {
        if (filtro == null) {
            return true;
        }
        return contiene(perfil.getUsuario().getNombreCompleto(), filtro)
            || contiene(perfil.getUsuario().getCorreo(), filtro)
            || contiene(perfil.getUsuario().getDocumento(), filtro)
            || contiene(perfil.getAreaExperiencia(), filtro)
            || contiene(perfil.getEspecialidad(), filtro)
            || contiene(perfil.getInstitucion(), filtro)
            || contiene(perfil.getTipoAcompanamiento(), filtro);
    }

    private boolean contiene(String valor, String filtro) {
        return valor != null && valor.toLowerCase().contains(filtro);
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim().toLowerCase();
        return limpio.isBlank() ? null : limpio;
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
