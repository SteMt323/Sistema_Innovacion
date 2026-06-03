package ni.edu.uam.innovacion.modules.activity.service;

import java.util.List;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.activity.dto.ActividadResponse;
import ni.edu.uam.innovacion.modules.activity.dto.ActualizarActividadRequest;
import ni.edu.uam.innovacion.modules.activity.dto.CrearActividadRequest;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.mapper.ActividadMapper;
import ni.edu.uam.innovacion.modules.activity.repository.ActividadRepository;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaDIEM;
import ni.edu.uam.innovacion.modules.catalog.repository.AmbitoActividadRepository;
import ni.edu.uam.innovacion.modules.catalog.repository.CategoriaDIEMRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final AmbitoActividadRepository ambitoActividadRepository;
    private final CategoriaDIEMRepository categoriaDIEMRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;
    private final UsuarioRepository usuarioRepository;

    public ActividadService(
        ActividadRepository actividadRepository,
        AmbitoActividadRepository ambitoActividadRepository,
        CategoriaDIEMRepository categoriaDIEMRepository,
        PerfilAdministradorRepository perfilAdministradorRepository,
        UsuarioRepository usuarioRepository
    ) {
        this.actividadRepository = actividadRepository;
        this.ambitoActividadRepository = ambitoActividadRepository;
        this.categoriaDIEMRepository = categoriaDIEMRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ActividadResponse crear(CrearActividadRequest request) {
        validarDatosBasicos(
            request.nombre(),
            request.fechaInicio(),
            request.fechaFin(),
            request.modalidad(),
            request.cupoMaximo(),
            request.puntosBase()
        );

        AmbitoActividad ambitoActividad = obtenerAmbitoActivo(request.idAmbitoActividad());
        CategoriaDIEM categoriaDiem = obtenerCategoriaActivaSiAplica(request.idCategoriaDiem());
        validarCategoriaParaAmbito(ambitoActividad, categoriaDiem);

        PerfilAdministrador administradorCreador = obtenerAdministrador(request.idAdministradorCreador());
        Usuario responsableUsuario = obtenerUsuarioSiAplica(request.idResponsableUsuario());

        Actividad actividad = ActividadMapper.toEntity(
            request,
            ambitoActividad,
            categoriaDiem,
            administradorCreador,
            responsableUsuario
        );

        return ActividadMapper.toResponse(actividadRepository.save(actividad));
    }

    @Transactional(readOnly = true)
    public List<ActividadResponse> listarTodas() {
        return actividadRepository.findAllByOrderByFechaInicioDesc()
            .stream()
            .map(ActividadMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ActividadResponse> listarDisponibles() {
        return actividadRepository.findByEstadoOrderByFechaInicioAsc(EstadoActividad.PUBLICADA)
            .stream()
            .map(ActividadMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public ActividadResponse buscarPorId(Long idActividad) {
        return ActividadMapper.toResponse(obtenerActividadPorId(idActividad));
    }

    @Transactional(readOnly = true)
    public ActividadResponse buscarDisponiblePorId(Long idActividad) {
        Actividad actividad = obtenerActividadPorId(idActividad);
        if (!EstadoActividad.PUBLICADA.equals(actividad.getEstado())) {
            throw new ResourceNotFoundException("No existe una actividad disponible con id " + idActividad);
        }
        return ActividadMapper.toResponse(actividad);
    }

    public ActividadResponse actualizar(Long idActividad, ActualizarActividadRequest request) {
        validarDatosBasicos(
            request.nombre(),
            request.fechaInicio(),
            request.fechaFin(),
            request.modalidad(),
            request.cupoMaximo(),
            request.puntosBase()
        );

        Actividad actividad = obtenerActividadPorId(idActividad);
        if (EstadoActividad.ARCHIVADA.equals(actividad.getEstado())) {
            throw new BadRequestException("No se puede actualizar una actividad archivada");
        }

        AmbitoActividad ambitoActividad = obtenerAmbitoActivo(request.idAmbitoActividad());
        CategoriaDIEM categoriaDiem = obtenerCategoriaActivaSiAplica(request.idCategoriaDiem());
        validarCategoriaParaAmbito(ambitoActividad, categoriaDiem);

        Usuario responsableUsuario = obtenerUsuarioSiAplica(request.idResponsableUsuario());

        ActividadMapper.updateEntity(
            actividad,
            request,
            ambitoActividad,
            categoriaDiem,
            responsableUsuario
        );

        return ActividadMapper.toResponse(actividadRepository.save(actividad));
    }

    public ActividadResponse publicar(Long idActividad) {
        Actividad actividad = obtenerActividadPorId(idActividad);
        validarEstadoActual(actividad, EstadoActividad.BORRADOR);
        actividad.setEstado(EstadoActividad.PUBLICADA);
        return ActividadMapper.toResponse(actividadRepository.save(actividad));
    }

    public ActividadResponse iniciar(Long idActividad) {
        Actividad actividad = obtenerActividadPorId(idActividad);
        validarEstadoActual(actividad, EstadoActividad.PUBLICADA);
        actividad.setEstado(EstadoActividad.EN_CURSO);
        return ActividadMapper.toResponse(actividadRepository.save(actividad));
    }

    public ActividadResponse finalizar(Long idActividad) {
        Actividad actividad = obtenerActividadPorId(idActividad);
        validarEstadoActual(actividad, EstadoActividad.PUBLICADA, EstadoActividad.EN_CURSO);
        actividad.setEstado(EstadoActividad.FINALIZADA);
        return ActividadMapper.toResponse(actividadRepository.save(actividad));
    }

    public ActividadResponse cancelar(Long idActividad) {
        Actividad actividad = obtenerActividadPorId(idActividad);
        validarEstadoActual(actividad, EstadoActividad.BORRADOR, EstadoActividad.PUBLICADA, EstadoActividad.EN_CURSO);
        actividad.setEstado(EstadoActividad.CANCELADA);
        return ActividadMapper.toResponse(actividadRepository.save(actividad));
    }

    public ActividadResponse archivar(Long idActividad) {
        Actividad actividad = obtenerActividadPorId(idActividad);
        validarEstadoActual(actividad, EstadoActividad.FINALIZADA, EstadoActividad.CANCELADA);
        actividad.setEstado(EstadoActividad.ARCHIVADA);
        return ActividadMapper.toResponse(actividadRepository.save(actividad));
    }

    private Actividad obtenerActividadPorId(Long idActividad) {
        return actividadRepository.findById(idActividad)
            .orElseThrow(() -> new ResourceNotFoundException("No existe la actividad con id " + idActividad));
    }

    private AmbitoActividad obtenerAmbitoActivo(Long idAmbitoActividad) {
        AmbitoActividad ambitoActividad = ambitoActividadRepository.findById(idAmbitoActividad)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el ambito de actividad con id " + idAmbitoActividad
            ));

        if (!ambitoActividad.estaActivo()) {
            throw new BadRequestException("El ambito de actividad debe estar activo");
        }

        return ambitoActividad;
    }

    private CategoriaDIEM obtenerCategoriaActivaSiAplica(Long idCategoriaDiem) {
        if (idCategoriaDiem == null) {
            return null;
        }

        CategoriaDIEM categoriaDiem = categoriaDIEMRepository.findById(idCategoriaDiem)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe la categoria DIEM con id " + idCategoriaDiem
            ));

        if (!categoriaDiem.estaActivo()) {
            throw new BadRequestException("La categoria DIEM debe estar activa");
        }

        return categoriaDiem;
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministradorCreador) {
        return perfilAdministradorRepository.findById(idAdministradorCreador)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el perfil administrador con id " + idAdministradorCreador
            ));
    }

    private Usuario obtenerUsuarioSiAplica(Long idResponsableUsuario) {
        if (idResponsableUsuario == null) {
            return null;
        }

        return usuarioRepository.findById(idResponsableUsuario)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el usuario responsable con id " + idResponsableUsuario
            ));
    }

    private void validarCategoriaParaAmbito(AmbitoActividad ambitoActividad, CategoriaDIEM categoriaDiem) {
        boolean requiereCategoria = Boolean.TRUE.equals(ambitoActividad.getRequiereCategoria());

        if (requiereCategoria && categoriaDiem == null) {
            throw new BadRequestException("El ambito seleccionado requiere una categoria DIEM");
        }

        if (!requiereCategoria && categoriaDiem != null) {
            throw new BadRequestException("El ambito seleccionado no permite categoria DIEM");
        }

        if (categoriaDiem != null && !categoriaDiem.getAmbitoActividad().getId().equals(ambitoActividad.getId())) {
            throw new BadRequestException("La categoria DIEM no pertenece al ambito seleccionado");
        }
    }

    private void validarDatosBasicos(
        String nombre,
        java.time.LocalDateTime fechaInicio,
        java.time.LocalDateTime fechaFin,
        Object modalidad,
        Integer cupoMaximo,
        Integer puntosBase
    ) {
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre de la actividad es obligatorio");
        }

        if (fechaInicio == null) {
            throw new BadRequestException("La fecha de inicio es obligatoria");
        }

        if (fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new BadRequestException("La fecha de finalizacion no puede ser anterior a la fecha de inicio");
        }

        if (modalidad == null) {
            throw new BadRequestException("La modalidad es obligatoria");
        }

        if (cupoMaximo != null && cupoMaximo <= 0) {
            throw new BadRequestException("El cupo maximo debe ser positivo");
        }

        if (puntosBase != null && puntosBase < 0) {
            throw new BadRequestException("Los puntos base no pueden ser negativos");
        }
    }

    private void validarEstadoActual(Actividad actividad, EstadoActividad... estadosPermitidos) {
        for (EstadoActividad estadoPermitido : estadosPermitidos) {
            if (estadoPermitido.equals(actividad.getEstado())) {
                return;
            }
        }

        throw new BadRequestException(
            "El estado actual de la actividad no permite esta transicion: " + actividad.getEstado().getValor()
        );
    }
}
