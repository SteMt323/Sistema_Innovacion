package ni.edu.uam.innovacion.modules.project.service;

import java.time.LocalDate;
import java.util.List;
import ni.edu.uam.innovacion.common.exception.BadRequestException;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.common.exception.ResourceNotFoundException;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarHistorialFasePIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CerrarHistorialFasePIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearHistorialFasePIARequest;
import ni.edu.uam.innovacion.modules.project.dto.HistorialFasePIAResponse;
import ni.edu.uam.innovacion.modules.project.entity.HistorialFasePIA;
import ni.edu.uam.innovacion.modules.project.entity.ProyectoPIA;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;
import ni.edu.uam.innovacion.modules.project.mapper.HistorialFasePIAMapper;
import ni.edu.uam.innovacion.modules.project.repository.HistorialFasePIARepository;
import ni.edu.uam.innovacion.modules.project.repository.ProyectoPIARepository;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.repository.PerfilAdministradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HistorialFasePIAService {

    private final HistorialFasePIARepository historialFasePIARepository;
    private final ProyectoPIARepository proyectoPIARepository;
    private final PerfilAdministradorRepository perfilAdministradorRepository;

    public HistorialFasePIAService(
            HistorialFasePIARepository historialFasePIARepository,
            ProyectoPIARepository proyectoPIARepository,
            PerfilAdministradorRepository perfilAdministradorRepository
    ) {
        this.historialFasePIARepository = historialFasePIARepository;
        this.proyectoPIARepository = proyectoPIARepository;
        this.perfilAdministradorRepository = perfilAdministradorRepository;
    }

    public HistorialFasePIAResponse crear(
            CrearHistorialFasePIARequest request,
            Long idAdministradorAutenticado
    ) {
        validarDatosBasicos(
                request.fase(),
                request.fechaInicio(),
                request.fechaFin()
        );

        ProyectoPIA proyectoPIA = obtenerProyectoPIA(request.idProyectoPIA());
        validarProyectoPIAModificable(proyectoPIA);

        validarFaseVigenteDisponibleSiAplica(
                request.idProyectoPIA(),
                request.fechaFin()
        );

        validarHistorialNoDuplicado(
                request.idProyectoPIA(),
                request.fase(),
                request.fechaInicio()
        );

        PerfilAdministrador registradoPorAdmin = obtenerAdministrador(idAdministradorAutenticado);

        HistorialFasePIA historial = HistorialFasePIAMapper.toEntity(
                request,
                proyectoPIA,
                registradoPorAdmin
        );

        HistorialFasePIA historialGuardado = historialFasePIARepository.save(historial);

        sincronizarFaseActualSiEsVigente(historialGuardado);

        return HistorialFasePIAMapper.toResponse(historialGuardado);
    }

    @Transactional(readOnly = true)
    public List<HistorialFasePIAResponse> listarTodos() {
        return historialFasePIARepository.findAllByOrderByFechaInicioDescCreadoEnDesc()
                .stream()
                .map(HistorialFasePIAMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HistorialFasePIAResponse> listarPorProyectoPIA(Long idProyectoPIA) {
        obtenerProyectoPIA(idProyectoPIA);

        return historialFasePIARepository
                .findByProyectoPIA_IdProyectoPIAOrderByFechaInicioDescCreadoEnDesc(idProyectoPIA)
                .stream()
                .map(HistorialFasePIAMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HistorialFasePIAResponse> listarPorProyecto(Long idProyecto) {
        return historialFasePIARepository
                .findByProyectoPIA_Proyecto_IdProyectoOrderByFechaInicioDescCreadoEnDesc(idProyecto)
                .stream()
                .map(HistorialFasePIAMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HistorialFasePIAResponse> listarPorFase(FasePIA fase) {
        if (fase == null) {
            throw new BadRequestException("La fase es obligatoria");
        }

        return historialFasePIARepository.findByFaseOrderByFechaInicioDescCreadoEnDesc(fase)
                .stream()
                .map(HistorialFasePIAMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HistorialFasePIAResponse> listarPorAdministrador(Long idAdministrador) {
        obtenerAdministrador(idAdministrador);

        return historialFasePIARepository
                .findByRegistradoPorAdmin_IdUsuarioOrderByFechaInicioDescCreadoEnDesc(idAdministrador)
                .stream()
                .map(HistorialFasePIAMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public HistorialFasePIAResponse buscarPorId(Long idHistorialFase) {
        return HistorialFasePIAMapper.toResponse(
                obtenerHistorialPorId(idHistorialFase)
        );
    }

    @Transactional(readOnly = true)
    public HistorialFasePIAResponse buscarFaseVigentePorProyectoPIA(Long idProyectoPIA) {
        obtenerProyectoPIA(idProyectoPIA);

        HistorialFasePIA historial = historialFasePIARepository
                .findFirstByProyectoPIA_IdProyectoPIAAndFechaFinIsNullOrderByFechaInicioDescCreadoEnDesc(
                        idProyectoPIA
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El proyecto PIA no tiene una fase vigente registrada"
                ));

        return HistorialFasePIAMapper.toResponse(historial);
    }

    public HistorialFasePIAResponse actualizar(
            Long idHistorialFase,
            ActualizarHistorialFasePIARequest request
    ) {
        validarDatosBasicos(
                request.fase(),
                request.fechaInicio(),
                request.fechaFin()
        );

        HistorialFasePIA historial = obtenerHistorialPorId(idHistorialFase);
        ProyectoPIA proyectoPIA = historial.getProyectoPIA();

        validarProyectoPIAModificable(proyectoPIA);

        validarFaseVigenteDisponibleParaActualizar(
                historial,
                request.fechaFin()
        );

        validarHistorialNoDuplicadoParaActualizar(
                proyectoPIA.getIdProyectoPIA(),
                request.fase(),
                request.fechaInicio(),
                idHistorialFase
        );

        HistorialFasePIAMapper.updateEntity(historial, request);

        HistorialFasePIA historialActualizado = historialFasePIARepository.save(historial);

        sincronizarFaseActualSiEsVigente(historialActualizado);

        return HistorialFasePIAMapper.toResponse(historialActualizado);
    }

    public HistorialFasePIAResponse cerrarPorId(
            Long idHistorialFase,
            CerrarHistorialFasePIARequest request
    ) {
        HistorialFasePIA historial = obtenerHistorialPorId(idHistorialFase);

        validarProyectoPIAModificable(historial.getProyectoPIA());

        if (historial.tieneFechaFin()) {
            throw new BadRequestException("La fase ya se encuentra cerrada");
        }

        validarFechaCierre(historial.getFechaInicio(), request.fechaFin());

        historial.setFechaFin(request.fechaFin());

        if (request.observaciones() != null && !request.observaciones().isBlank()) {
            historial.setObservaciones(request.observaciones());
        }

        return HistorialFasePIAMapper.toResponse(
                historialFasePIARepository.save(historial)
        );
    }

    public HistorialFasePIAResponse cerrarFaseVigentePorProyectoPIA(
            Long idProyectoPIA,
            CerrarHistorialFasePIARequest request
    ) {
        obtenerProyectoPIA(idProyectoPIA);

        HistorialFasePIA historial = historialFasePIARepository
                .findFirstByProyectoPIA_IdProyectoPIAAndFechaFinIsNullOrderByFechaInicioDescCreadoEnDesc(
                        idProyectoPIA
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El proyecto PIA no tiene una fase vigente para cerrar"
                ));

        validarProyectoPIAModificable(historial.getProyectoPIA());
        validarFechaCierre(historial.getFechaInicio(), request.fechaFin());

        historial.setFechaFin(request.fechaFin());

        if (request.observaciones() != null && !request.observaciones().isBlank()) {
            historial.setObservaciones(request.observaciones());
        }

        return HistorialFasePIAMapper.toResponse(
                historialFasePIARepository.save(historial)
        );
    }

    private HistorialFasePIA obtenerHistorialPorId(Long idHistorialFase) {
        return historialFasePIARepository.findById(idHistorialFase)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el historial de fase PIA con id " + idHistorialFase
                ));
    }

    private ProyectoPIA obtenerProyectoPIA(Long idProyectoPIA) {
        return proyectoPIARepository.findById(idProyectoPIA)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el proyecto PIA con id " + idProyectoPIA
                ));
    }

    private PerfilAdministrador obtenerAdministrador(Long idAdministrador) {
        return perfilAdministradorRepository.findById(idAdministrador)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el perfil administrador con id " + idAdministrador
                ));
    }

    private void validarDatosBasicos(
            FasePIA fase,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        if (fase == null) {
            throw new BadRequestException("La fase es obligatoria");
        }

        if (fechaInicio == null) {
            throw new BadRequestException("La fecha de inicio es obligatoria");
        }

        if (fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new BadRequestException(
                    "La fecha de finalización no puede ser anterior a la fecha de inicio"
            );
        }
    }

    private void validarFechaCierre(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        if (fechaFin == null) {
            throw new BadRequestException("La fecha de finalización es obligatoria");
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw new BadRequestException(
                    "La fecha de finalización no puede ser anterior a la fecha de inicio"
            );
        }
    }

    private void validarProyectoPIAModificable(ProyectoPIA proyectoPIA) {
        if (!proyectoPIA.puedeModificarse()) {
            throw new BadRequestException(
                    "No se puede modificar el historial de un proyecto PIA finalizado o retirado"
            );
        }
    }

    private void validarFaseVigenteDisponibleSiAplica(
            Long idProyectoPIA,
            LocalDate fechaFin
    ) {
        if (fechaFin != null) {
            return;
        }

        if (historialFasePIARepository.existsByProyectoPIA_IdProyectoPIAAndFechaFinIsNull(idProyectoPIA)) {
            throw new BadRequestException(
                    "El proyecto PIA ya tiene una fase vigente. Debe cerrar la fase actual antes de registrar una nueva"
            );
        }
    }

    private void validarFaseVigenteDisponibleParaActualizar(
            HistorialFasePIA historial,
            LocalDate fechaFin
    ) {
        if (fechaFin != null) {
            return;
        }

        historialFasePIARepository
                .findFirstByProyectoPIA_IdProyectoPIAAndFechaFinIsNullOrderByFechaInicioDescCreadoEnDesc(
                        historial.getProyectoPIA().getIdProyectoPIA()
                )
                .ifPresent(faseVigente -> {
                    if (!faseVigente.getIdHistorialFase().equals(historial.getIdHistorialFase())) {
                        throw new BadRequestException(
                                "El proyecto PIA ya tiene otra fase vigente registrada"
                        );
                    }
                });
    }

    private void validarHistorialNoDuplicado(
            Long idProyectoPIA,
            FasePIA fase,
            LocalDate fechaInicio
    ) {
        if (historialFasePIARepository.existsByProyectoPIA_IdProyectoPIAAndFaseAndFechaInicio(
                idProyectoPIA,
                fase,
                fechaInicio
        )) {
            throw new DuplicateResourceException(
                    "Ya existe un historial para esa fase con la misma fecha de inicio"
            );
        }
    }

    private void validarHistorialNoDuplicadoParaActualizar(
            Long idProyectoPIA,
            FasePIA fase,
            LocalDate fechaInicio,
            Long idHistorialFase
    ) {
        boolean existeDuplicado = historialFasePIARepository
                .findByProyectoPIA_IdProyectoPIAOrderByFechaInicioDescCreadoEnDesc(idProyectoPIA)
                .stream()
                .anyMatch(historial ->
                        !historial.getIdHistorialFase().equals(idHistorialFase)
                                && historial.getFase().equals(fase)
                                && historial.getFechaInicio().equals(fechaInicio)
                );

        if (existeDuplicado) {
            throw new DuplicateResourceException(
                    "Ya existe otro historial para esa fase con la misma fecha de inicio"
            );
        }
    }

    private void sincronizarFaseActualSiEsVigente(HistorialFasePIA historial) {
        if (!historial.faseEstaVigente()) {
            return;
        }

        ProyectoPIA proyectoPIA = historial.getProyectoPIA();

        if (!historial.getFase().equals(proyectoPIA.getFaseActual())) {
            proyectoPIA.setFaseActual(historial.getFase());
            proyectoPIARepository.save(proyectoPIA);
        }
    }
}