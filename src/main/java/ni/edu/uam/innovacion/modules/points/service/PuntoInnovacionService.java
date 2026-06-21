package ni.edu.uam.innovacion.modules.points.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.participation.entity.Participacion;
import ni.edu.uam.innovacion.modules.points.dto.CrearAjustePuntosRequest;
import ni.edu.uam.innovacion.modules.points.dto.PuntoInnovacionResponse;
import ni.edu.uam.innovacion.modules.points.dto.ResumenPuntosUsuarioResponse;
import ni.edu.uam.innovacion.modules.points.dto.TopUsuarioPuntosResponse;
import ni.edu.uam.innovacion.modules.points.entity.PuntoInnovacion;
import ni.edu.uam.innovacion.modules.points.enums.EstadoPuntos;
import ni.edu.uam.innovacion.modules.points.enums.InsigniaPuntos;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;
import ni.edu.uam.innovacion.modules.points.mapper.PuntoInnovacionMapper;
import ni.edu.uam.innovacion.modules.points.repository.PuntoInnovacionRepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import ni.edu.uam.innovacion.modules.user.repository.UsuarioRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PuntoInnovacionService {

    private static final String ORIGEN_ACTIVIDAD = "actividad:";

    private final PuntoInnovacionRepository puntoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;

    public PuntoInnovacionService(
        PuntoInnovacionRepository puntoRepository,
        UsuarioRepository usuarioRepository,
        PerfilAdministradorRepository perfilAdministradorRepository
    ) {
        this.puntoRepository = puntoRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
    }

    @Transactional(readOnly = true)
    public List<PuntoInnovacionResponse> listar(
        Long idUsuario,
        Long idParticipacion,
        TipoMovimientoPuntos tipo,
        EstadoPuntos estado,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
    ) {
        validarRangoFechas(fechaDesde, fechaHasta);
        Specification<PuntoInnovacion> specification = crearFiltros(
            idUsuario,
            idParticipacion,
            tipo,
            estado,
            fechaDesde,
            fechaHasta
        );

        return puntoRepository.findAll(
                specification,
                Sort.by(Sort.Direction.DESC, "fechaAsignacion")
            )
            .stream()
            .map(PuntoInnovacionMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public ResumenPuntosUsuarioResponse obtenerResumenUsuario(Long idUsuario) {
        Usuario usuario = obtenerUsuario(idUsuario);
        List<PuntoInnovacion> movimientos =
            puntoRepository.findByUsuarioIdUsuarioOrderByFechaAsignacionDesc(idUsuario);

        long totalActivo = movimientos.stream()
            .filter(PuntoInnovacion::estaActivo)
            .mapToLong(PuntoInnovacion::getCantidad)
            .sum();
        long totalHistorico = movimientos.stream()
            .mapToLong(PuntoInnovacion::getCantidad)
            .sum();
        long movimientosActivos = movimientos.stream()
            .filter(PuntoInnovacion::estaActivo)
            .count();
        long totalOtorgado = movimientos.stream()
            .filter(PuntoInnovacion::estaActivo)
            .mapToLong(PuntoInnovacion::getCantidad)
            .filter(cantidad -> cantidad > 0)
            .sum();
        long totalDebitado = Math.abs(movimientos.stream()
            .filter(PuntoInnovacion::estaActivo)
            .mapToLong(PuntoInnovacion::getCantidad)
            .filter(cantidad -> cantidad < 0)
            .sum());

        PuntoInnovacion ultimo = movimientos.isEmpty() ? null : movimientos.getFirst();
        return new ResumenPuntosUsuarioResponse(
            idUsuario,
            usuario.getNombreCompleto(),
            totalActivo,
            totalHistorico,
            movimientos.size(),
            movimientosActivos,
            totalOtorgado,
            totalDebitado,
            InsigniaPuntos.desdeTotal(totalActivo),
            ultimo == null ? null : ultimo.getFechaAsignacion(),
            ultimo == null ? null : PuntoInnovacionMapper.toResponse(ultimo)
        );
    }

    public PuntoInnovacionResponse crearAjuste(
        CrearAjustePuntosRequest request,
        Long idAdministrador
    ) {
        validarAjuste(request);
        Usuario usuario = obtenerUsuario(request.idUsuario());
        PerfilAdministrador administrador = obtenerAdministrador(idAdministrador);

        PuntoInnovacion punto = new PuntoInnovacion();
        punto.setUsuario(usuario);
        punto.setAdminAjuste(administrador);
        punto.setCantidad(request.cantidad());
        punto.setTipoMovimiento(request.tipoMovimiento());
        punto.setMotivo(request.motivo());
        punto.setOrigen(request.origen());
        punto.setEstado(EstadoPuntos.ACTIVO);

        return PuntoInnovacionMapper.toResponse(puntoRepository.save(punto));
    }

    public PuntoInnovacionResponse anular(Long idPunto) {
        PuntoInnovacion punto = puntoRepository.findById(idPunto)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el movimiento de puntos con id " + idPunto
            ));
        if (!punto.estaActivo()) {
            throw new BadRequestException("El movimiento de puntos ya se encuentra anulado");
        }
        punto.anular();
        return PuntoInnovacionMapper.toResponse(puntoRepository.save(punto));
    }

    public void otorgarPorParticipacion(Participacion participacion) {
        int cantidad = Optional.ofNullable(
            participacion.getInscripcion().getActividad().getPuntosBase()
        ).orElse(0);

        Optional<PuntoInnovacion> existente = puntoRepository
            .findByParticipacionIdParticipacionAndTipoMovimiento(
                participacion.getIdParticipacion(),
                TipoMovimientoPuntos.OTORGAMIENTO
            );

        if (cantidad <= 0) {
            existente.filter(PuntoInnovacion::estaActivo).ifPresent(punto -> {
                punto.anular();
                puntoRepository.save(punto);
            });
            return;
        }

        PuntoInnovacion punto = existente.orElseGet(PuntoInnovacion::new);
        punto.setUsuario(participacion.getInscripcion().getUsuario());
        punto.setParticipacion(participacion);
        punto.setCantidad(cantidad);
        punto.setTipoMovimiento(TipoMovimientoPuntos.OTORGAMIENTO);
        punto.setMotivo("Otorgamiento por participacion validada");
        punto.setOrigen(ORIGEN_ACTIVIDAD + participacion.getInscripcion().getActividad().getIdActividad());
        punto.activar();
        puntoRepository.save(punto);
    }

    public void anularOtorgamientoPorParticipacion(Participacion participacion) {
        puntoRepository.findByParticipacionIdParticipacionAndTipoMovimiento(
                participacion.getIdParticipacion(),
                TipoMovimientoPuntos.OTORGAMIENTO
            )
            .filter(PuntoInnovacion::estaActivo)
            .ifPresent(punto -> {
                punto.anular();
                puntoRepository.save(punto);
            });
    }

    @Transactional(readOnly = true)
    public int obtenerPuntosActivosParticipacion(Long idParticipacion) {
        return puntoRepository.findByParticipacionIdParticipacionAndTipoMovimiento(
                idParticipacion,
                TipoMovimientoPuntos.OTORGAMIENTO
            )
            .filter(PuntoInnovacion::estaActivo)
            .map(PuntoInnovacion::getCantidad)
            .orElse(0);
    }

    @Transactional(readOnly = true)
    public List<TopUsuarioPuntosResponse> obtenerRanking(int limite) {
        validarLimite(limite);
        return puntoRepository.obtenerRanking(
                EstadoPuntos.ACTIVO,
                PageRequest.of(0, limite)
            )
            .stream()
            .map(item -> new TopUsuarioPuntosResponse(
                item.getIdUsuario(),
                item.getNombreUsuario(),
                item.getTotalPuntos(),
                InsigniaPuntos.desdeTotal(item.getTotalPuntos())
            ))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PuntoInnovacionResponse> obtenerMovimientosRecientes() {
        return puntoRepository.findTop10ByOrderByFechaAsignacionDesc()
            .stream()
            .map(PuntoInnovacionMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public long totalPuntosActivos() {
        return Optional.ofNullable(puntoRepository.sumarCantidadPorEstado(EstadoPuntos.ACTIVO))
            .orElse(0L);
    }

    @Transactional(readOnly = true)
    public long movimientosAnulados() {
        return puntoRepository.countByEstado(EstadoPuntos.ANULADO);
    }

    private Specification<PuntoInnovacion> crearFiltros(
        Long idUsuario,
        Long idParticipacion,
        TipoMovimientoPuntos tipo,
        EstadoPuntos estado,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
    ) {
        Specification<PuntoInnovacion> specification =
            (root, query, builder) -> builder.conjunction();

        if (idUsuario != null) {
            specification = specification.and((root, query, builder) ->
                builder.equal(root.get("usuario").get("idUsuario"), idUsuario));
        }
        if (idParticipacion != null) {
            specification = specification.and((root, query, builder) ->
                builder.equal(root.get("participacion").get("idParticipacion"), idParticipacion));
        }
        if (tipo != null) {
            specification = specification.and((root, query, builder) ->
                builder.equal(root.get("tipoMovimiento"), tipo));
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

    private void validarAjuste(CrearAjustePuntosRequest request) {
        if (request.cantidad() == null || request.cantidad() == 0) {
            throw new BadRequestException("La cantidad del ajuste debe ser distinta de cero");
        }
        if (request.tipoMovimiento() == null || !request.tipoMovimiento().esManual()) {
            throw new BadRequestException(
                "Los ajustes manuales solo admiten ajuste_manual, correccion o penalizacion"
            );
        }
        if (TipoMovimientoPuntos.PENALIZACION.equals(request.tipoMovimiento())
            && request.cantidad() > 0) {
            throw new BadRequestException("Una penalizacion debe tener cantidad negativa");
        }
    }

    private void validarRangoFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new BadRequestException("La fecha desde no puede ser posterior a la fecha hasta");
        }
    }

    private void validarLimite(int limite) {
        if (limite < 1 || limite > 100) {
            throw new BadRequestException("El limite debe estar entre 1 y 100");
        }
    }

    private Usuario obtenerUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el usuario con id " + idUsuario
            ));
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministrador) {
        return perfilAdministradorRepository.findById(idAdministrador)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el perfil administrador con id " + idAdministrador
            ));
    }
}
