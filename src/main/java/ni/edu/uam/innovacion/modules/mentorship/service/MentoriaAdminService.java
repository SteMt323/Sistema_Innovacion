package ni.edu.uam.innovacion.modules.mentorship.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.repository.ActividadRepository;
import ni.edu.uam.innovacion.modules.mentorship.dto.ActualizarMentoriaRequest;
import ni.edu.uam.innovacion.modules.mentorship.dto.CrearMentoriaRequest;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentoriaResponse;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentoriaResumenResponse;
import ni.edu.uam.innovacion.modules.mentorship.entity.MentoriaActividad;
import ni.edu.uam.innovacion.modules.mentorship.mapper.MentoriaMapper;
import ni.edu.uam.innovacion.modules.mentorship.repository.MentoriaActividadRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.PerfilMentorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MentoriaAdminService {

    private final MentoriaActividadRepository mentoriaActividadRepository;
    private final ActividadRepository actividadRepository;
    private final PerfilMentorRepository perfilMentorRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;
    private final MentorAdminService mentorAdminService;

    public MentoriaAdminService(
        MentoriaActividadRepository mentoriaActividadRepository,
        ActividadRepository actividadRepository,
        PerfilMentorRepository perfilMentorRepository,
        PerfilAdministradorRepository perfilAdministradorRepository,
        MentorAdminService mentorAdminService
    ) {
        this.mentoriaActividadRepository = mentoriaActividadRepository;
        this.actividadRepository = actividadRepository;
        this.perfilMentorRepository = perfilMentorRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
        this.mentorAdminService = mentorAdminService;
    }

    public MentoriaResponse crear(CrearMentoriaRequest request, Long idAdministradorAutenticado) {
        validarDatosCrear(request);
        validarFechaAsignacion(request.fechaAsignacion());

        Actividad actividad = obtenerActividadModificable(request.idActividad());
        PerfilMentor mentor = obtenerMentorActivo(request.idMentor());
        PerfilAdministrador administrador = obtenerAdministrador(idAdministradorAutenticado);

        Optional<MentoriaActividad> existente = mentoriaActividadRepository
            .findByActividadIdActividadAndMentorIdUsuarioAndRolColaboradorIgnoreCase(
                actividad.getIdActividad(),
                mentor.getIdUsuario(),
                MentoriaActividad.ROL_COLABORADOR_MENTOR
            );

        if (existente.isPresent() && existente.get().estaActivo()) {
            throw new DuplicateResourceException(
                "El mentor ya tiene una mentoria activa registrada en la actividad"
            );
        }

        MentoriaActividad mentoria = existente.orElseGet(MentoriaActividad::new);
        mentoria.setActividad(actividad);
        mentoria.setMentor(mentor);
        mentoria.setAgregadoPorAdmin(administrador);
        mentoria.setRolColaborador(MentoriaActividad.ROL_COLABORADOR_MENTOR);
        mentoria.setFechaAsignacion(resolverFechaAsignacion(request.fechaAsignacion()));
        mentoria.setObservaciones(request.observaciones());
        mentoria.activar();

        return MentoriaMapper.toResponse(mentoriaActividadRepository.save(mentoria));
    }

    @Transactional(readOnly = true)
    public List<MentoriaResponse> listar(
        Long idActividad,
        Long idMentor,
        EstadoRegistro estado,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
    ) {
        validarRangoFechas(fechaDesde, fechaHasta);
        Specification<MentoriaActividad> specification = crearFiltros(
            idActividad,
            idMentor,
            estado,
            fechaDesde,
            fechaHasta
        );

        return mentoriaActividadRepository.findAll(
                specification,
                Sort.by(Sort.Direction.DESC, "fechaAsignacion")
            )
            .stream()
            .map(MentoriaMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public MentoriaResponse obtenerPorId(Long idMentoria) {
        return MentoriaMapper.toResponse(obtenerMentoria(idMentoria));
    }

    public MentoriaResponse actualizar(Long idMentoria, ActualizarMentoriaRequest request) {
        validarFechaAsignacion(request.fechaAsignacion());

        MentoriaActividad mentoria = obtenerMentoria(idMentoria);
        validarMentoriaEditable(mentoria);
        validarActividadPermiteMentorias(mentoria.getActividad());

        mentoria.setFechaAsignacion(resolverFechaAsignacion(request.fechaAsignacion(), mentoria.getFechaAsignacion()));
        mentoria.setObservaciones(request.observaciones());

        return MentoriaMapper.toResponse(mentoriaActividadRepository.save(mentoria));
    }

    public MentoriaResponse activar(Long idMentoria) {
        MentoriaActividad mentoria = obtenerMentoria(idMentoria);
        validarActividadPermiteMentorias(mentoria.getActividad());
        mentoria.activar();
        return MentoriaMapper.toResponse(mentoriaActividadRepository.save(mentoria));
    }

    public MentoriaResponse inactivar(Long idMentoria) {
        MentoriaActividad mentoria = obtenerMentoria(idMentoria);
        validarActividadPermiteMentorias(mentoria.getActividad());
        if (mentoria.estaArchivado()) {
            throw new BadRequestException("No se puede inactivar una mentoria archivada");
        }
        mentoria.inactivar();
        return MentoriaMapper.toResponse(mentoriaActividadRepository.save(mentoria));
    }

    public MentoriaResponse archivar(Long idMentoria) {
        MentoriaActividad mentoria = obtenerMentoria(idMentoria);
        validarActividadPermiteMentorias(mentoria.getActividad());
        mentoria.archivar();
        return MentoriaMapper.toResponse(mentoriaActividadRepository.save(mentoria));
    }

    @Transactional(readOnly = true)
    public MentoriaResumenResponse obtenerResumen() {
        return new MentoriaResumenResponse(
            mentorAdminService.totalMentoresRegistrados(),
            mentorAdminService.totalMentoresActivos(),
            totalMentoriasActivas(),
            totalMentoriasInactivas(),
            totalMentoriasArchivadas(),
            actividadesConMentoriasActivas()
        );
    }

    @Transactional(readOnly = true)
    public long totalMentoriasActivas() {
        return mentoriaActividadRepository.countByRolColaboradorIgnoreCaseAndEstado(
            MentoriaActividad.ROL_COLABORADOR_MENTOR,
            EstadoRegistro.ACTIVO
        );
    }

    @Transactional(readOnly = true)
    public long totalMentoriasInactivas() {
        return mentoriaActividadRepository.countByRolColaboradorIgnoreCaseAndEstado(
            MentoriaActividad.ROL_COLABORADOR_MENTOR,
            EstadoRegistro.INACTIVO
        );
    }

    @Transactional(readOnly = true)
    public long totalMentoriasArchivadas() {
        return mentoriaActividadRepository.countByRolColaboradorIgnoreCaseAndEstado(
            MentoriaActividad.ROL_COLABORADOR_MENTOR,
            EstadoRegistro.ARCHIVADO
        );
    }

    @Transactional(readOnly = true)
    public long actividadesConMentoriasActivas() {
        return mentoriaActividadRepository.countActividadesDistintasPorRolYEstado(
            MentoriaActividad.ROL_COLABORADOR_MENTOR,
            EstadoRegistro.ACTIVO
        );
    }

    private Specification<MentoriaActividad> crearFiltros(
        Long idActividad,
        Long idMentor,
        EstadoRegistro estado,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
    ) {
        Specification<MentoriaActividad> specification = (root, query, builder) ->
            builder.equal(
                builder.lower(root.get("rolColaborador")),
                MentoriaActividad.ROL_COLABORADOR_MENTOR
            );

        if (idActividad != null) {
            specification = specification.and((root, query, builder) ->
                builder.equal(root.get("actividad").get("idActividad"), idActividad));
        }
        if (idMentor != null) {
            specification = specification.and((root, query, builder) ->
                builder.equal(root.get("mentor").get("idUsuario"), idMentor));
        }
        if (estado != null) {
            specification = specification.and((root, query, builder) ->
                builder.equal(root.get("estado"), estado));
        }
        if (fechaDesde != null) {
            specification = specification.and((root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("fechaAsignacion"), fechaDesde));
        }
        if (fechaHasta != null) {
            specification = specification.and((root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("fechaAsignacion"), fechaHasta));
        }
        return specification;
    }

    private MentoriaActividad obtenerMentoria(Long idMentoria) {
        return mentoriaActividadRepository.findById(idMentoria)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe la mentoria con id " + idMentoria
            ));
    }

    private Actividad obtenerActividadModificable(Long idActividad) {
        Actividad actividad = actividadRepository.findById(idActividad)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe la actividad con id " + idActividad
            ));
        validarActividadPermiteMentorias(actividad);
        return actividad;
    }

    private void validarActividadPermiteMentorias(Actividad actividad) {
        EstadoActividad estado = actividad.getEstado();
        if (EstadoActividad.FINALIZADA.equals(estado)
            || EstadoActividad.CANCELADA.equals(estado)
            || EstadoActividad.ARCHIVADA.equals(estado)) {
            throw new BadRequestException(
                "No se pueden modificar mentorias de una actividad " + estado.getValor()
            );
        }
    }

    private PerfilMentor obtenerMentorActivo(Long idMentor) {
        PerfilMentor mentor = perfilMentorRepository.findById(idMentor)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el perfil mentor con id " + idMentor
            ));
        if (!EstadoUsuario.ACTIVO.equals(mentor.getUsuario().getEstado())) {
            throw new BadRequestException("El usuario mentor debe estar activo");
        }
        return mentor;
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministrador) {
        return perfilAdministradorRepository.findById(idAdministrador)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el perfil administrador con id " + idAdministrador
            ));
    }

    private void validarMentoriaEditable(MentoriaActividad mentoria) {
        if (mentoria.estaArchivado()) {
            throw new BadRequestException("No se puede modificar una mentoria archivada");
        }
    }

    private void validarDatosCrear(CrearMentoriaRequest request) {
        if (request.idActividad() == null) {
            throw new BadRequestException("El id de la actividad es obligatorio");
        }
        if (request.idMentor() == null) {
            throw new BadRequestException("El id del mentor es obligatorio");
        }
    }

    private void validarFechaAsignacion(LocalDateTime fechaAsignacion) {
        if (fechaAsignacion != null && fechaAsignacion.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("La fecha de asignacion no puede ser futura");
        }
    }

    private void validarRangoFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new BadRequestException("La fecha desde no puede ser posterior a la fecha hasta");
        }
    }

    private LocalDateTime resolverFechaAsignacion(LocalDateTime fechaAsignacion) {
        return resolverFechaAsignacion(fechaAsignacion, LocalDateTime.now());
    }

    private LocalDateTime resolverFechaAsignacion(
        LocalDateTime fechaAsignacion,
        LocalDateTime valorPorDefecto
    ) {
        return fechaAsignacion == null ? valorPorDefecto : fechaAsignacion;
    }
}
