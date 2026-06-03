package ni.edu.uam.innovacion.modules.catalog.service;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.dto.CarreraRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.CarreraResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.Carrera;
import ni.edu.uam.innovacion.modules.catalog.entity.Facultad;
import ni.edu.uam.innovacion.modules.catalog.mapper.CarreraMapper;
import ni.edu.uam.innovacion.modules.catalog.repository.CarreraRepository;
import ni.edu.uam.innovacion.modules.catalog.repository.FacultadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio del catálogo de carreras.
 */
@Service
@Transactional
public class CarreraService {

    private final CarreraRepository carreraRepository;
    private final FacultadRepository facultadRepository;


    public CarreraService(
            CarreraRepository carreraRepository,
            FacultadRepository facultadRepository
    ) {
        this.carreraRepository = carreraRepository;
        this.facultadRepository = facultadRepository;
    }


    @Transactional(readOnly = true)
    public List<CarreraResponse> listarTodas() {
        return carreraRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(CarreraMapper::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<CarreraResponse> listarActivas() {
        return carreraRepository.findByEstadoOrderByNombreAsc(EstadoRegistro.ACTIVO)
                .stream()
                .map(CarreraMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CarreraResponse> listarPorFacultad(Long idFacultad) {
        verificarFacultadExiste(idFacultad);

        return carreraRepository.findByFacultad_IdOrderByNombreAsc(idFacultad)
                .stream()
                .map(CarreraMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CarreraResponse> listarActivasPorFacultad(Long idFacultad) {
        verificarFacultadExiste(idFacultad);

        return carreraRepository.findByFacultad_IdAndEstadoOrderByNombreAsc(
                        idFacultad,
                        EstadoRegistro.ACTIVO
                )
                .stream()
                .map(CarreraMapper::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public CarreraResponse buscarPorId(Long id) {
        Carrera carrera = obtenerCarreraPorId(id);
        return CarreraMapper.toResponse(carrera);
    }

    /**
     * Crea una nueva carrera.
     */
    public CarreraResponse crear(CarreraRequest request) {
        normalizarRequest(request);

        Facultad facultad = obtenerFacultadActivaPorId(request.getIdFacultad());

        if (carreraRepository.existsByCodigoIgnoreCase(request.getCodigo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una carrera con ese código"
            );
        }

        if (carreraRepository.existsByNombreIgnoreCaseAndFacultad_Id(
                request.getNombre(),
                request.getIdFacultad()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una carrera con ese nombre en la facultad seleccionada"
            );
        }

        Carrera carrera = CarreraMapper.toEntity(request, facultad);
        Carrera carreraGuardada = carreraRepository.save(carrera);

        return CarreraMapper.toResponse(carreraGuardada);
    }

    /**
     * Actualiza una carrera existente.
     */
    public CarreraResponse actualizar(Long id, CarreraRequest request) {
        normalizarRequest(request);

        Carrera carrera = obtenerCarreraPorId(id);
        Facultad facultad = obtenerFacultadActivaPorId(request.getIdFacultad());

        carreraRepository.findByCodigoIgnoreCase(request.getCodigo())
                .ifPresent(carreraExistente -> {
                    if (!carreraExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otra carrera con ese código"
                        );
                    }
                });

        carreraRepository.findByNombreIgnoreCaseAndFacultad_Id(
                        request.getNombre(),
                        request.getIdFacultad()
                )
                .ifPresent(carreraExistente -> {
                    if (!carreraExistente.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe otra carrera con ese nombre en la facultad seleccionada"
                        );
                    }
                });

        CarreraMapper.updateEntity(carrera, request, facultad);

        Carrera carreraActualizada = carreraRepository.save(carrera);
        return CarreraMapper.toResponse(carreraActualizada);
    }


    public CarreraResponse activar(Long id) {
        Carrera carrera = obtenerCarreraPorId(id);
        carrera.activar();

        return CarreraMapper.toResponse(carreraRepository.save(carrera));
    }


    public CarreraResponse inactivar(Long id) {
        Carrera carrera = obtenerCarreraPorId(id);
        carrera.inactivar();

        return CarreraMapper.toResponse(carreraRepository.save(carrera));
    }


    public CarreraResponse archivar(Long id) {
        Carrera carrera = obtenerCarreraPorId(id);
        carrera.archivar();

        return CarreraMapper.toResponse(carreraRepository.save(carrera));
    }


    private Carrera obtenerCarreraPorId(Long id) {
        return carreraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la carrera con id: " + id
                ));
    }


    private Facultad obtenerFacultadPorId(Long idFacultad) {
        return facultadRepository.findById(idFacultad)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la facultad con id: " + idFacultad
                ));
    }


    private Facultad obtenerFacultadActivaPorId(Long idFacultad) {
        Facultad facultad = obtenerFacultadPorId(idFacultad);

        if (!facultad.estaActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La facultad seleccionada no está activa"
            );
        }

        return facultad;
    }


    private void verificarFacultadExiste(Long idFacultad) {
        if (!facultadRepository.existsById(idFacultad)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontró la facultad con id: " + idFacultad
            );
        }
    }


    private void normalizarRequest(CarreraRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim());
        }

        if (request.getDescripcion() != null) {
            request.setDescripcion(request.getDescripcion().trim());
        }

        if (request.getCodigo() != null) {
            request.setCodigo(request.getCodigo().trim().toUpperCase());
        }
    }
}