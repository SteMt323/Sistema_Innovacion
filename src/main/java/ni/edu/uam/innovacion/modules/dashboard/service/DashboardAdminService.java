package ni.edu.uam.innovacion.modules.dashboard.service;

import java.util.List;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.repository.ActividadRepository;
import ni.edu.uam.innovacion.modules.dashboard.dto.AdminDashboardResponse;
import ni.edu.uam.innovacion.modules.enrollment.enums.EstadoInscripcion;
import ni.edu.uam.innovacion.modules.enrollment.repository.InscripcionRepository;
import ni.edu.uam.innovacion.modules.mentorship.service.MentorAdminService;
import ni.edu.uam.innovacion.modules.mentorship.service.MentoriaAdminService;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;
import ni.edu.uam.innovacion.modules.participation.repository.ParticipacionRepository;
import ni.edu.uam.innovacion.modules.points.dto.PuntoInnovacionResponse;
import ni.edu.uam.innovacion.modules.points.dto.TopUsuarioPuntosResponse;
import ni.edu.uam.innovacion.modules.points.service.PuntoInnovacionService;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardAdminService {

    private final UsuarioRepository usuarioRepository;
    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;
    private final ParticipacionRepository participacionRepository;
    private final PuntoInnovacionService puntoService;
    private final MentorAdminService mentorAdminService;
    private final MentoriaAdminService mentoriaAdminService;

    public DashboardAdminService(
        UsuarioRepository usuarioRepository,
        ActividadRepository actividadRepository,
        InscripcionRepository inscripcionRepository,
        ParticipacionRepository participacionRepository,
        PuntoInnovacionService puntoService,
        MentorAdminService mentorAdminService,
        MentoriaAdminService mentoriaAdminService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.participacionRepository = participacionRepository;
        this.puntoService = puntoService;
        this.mentorAdminService = mentorAdminService;
        this.mentoriaAdminService = mentoriaAdminService;
    }

    public AdminDashboardResponse obtenerResumen() {
        return new AdminDashboardResponse(
            usuarioRepository.count(),
            usuarioRepository.countByEstado(EstadoUsuario.ACTIVO),
            actividadRepository.count(),
            actividadRepository.countByEstado(EstadoActividad.FINALIZADA),
            inscripcionRepository.count(),
            inscripcionRepository.countByEstado(EstadoInscripcion.CONFIRMADA),
            participacionRepository.countByEstado(EstadoParticipacion.VALIDADA),
            participacionRepository.countByEstado(EstadoParticipacion.NO_VALIDADA),
            puntoService.totalPuntosActivos(),
            puntoService.movimientosAnulados(),
            mentorAdminService.totalMentoresRegistrados(),
            mentorAdminService.totalMentoresActivos(),
            mentoriaAdminService.totalMentoriasActivas(),
            mentoriaAdminService.totalMentoriasInactivas(),
            mentoriaAdminService.totalMentoriasArchivadas(),
            mentoriaAdminService.actividadesConMentoriasActivas(),
            puntoService.obtenerRanking(5),
            puntoService.obtenerMovimientosRecientes()
        );
    }

    public List<TopUsuarioPuntosResponse> obtenerTopUsuarios(int limite) {
        return puntoService.obtenerRanking(limite);
    }

    public List<PuntoInnovacionResponse> obtenerMovimientosRecientes() {
        return puntoService.obtenerMovimientosRecientes();
    }
}
