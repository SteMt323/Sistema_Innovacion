package ni.edu.uam.innovacion.modules.catalog.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.catalog.dto.CarreraRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.CarreraResponse;
import ni.edu.uam.innovacion.modules.catalog.service.CarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para administrar el catálogo de carreras.
 *
 * El Controller recibe las peticiones HTTP desde Postman o desde el frontend.
 *
 * Permite:
 * - listar carreras
 * - listar carreras activas
 * - listar carreras por facultad
 * - buscar carrera por id
 * - crear carrera
 * - actualizar carrera
 * - activar, inactivar o archivar carrera
 */
@RestController
@RequestMapping("/api/admin/catalog/carreras")
public class CarreraController {

    private final CarreraService carreraService;

    /**
     * Constructor para inyectar el service.
     */
    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }


    @GetMapping
    public ResponseEntity<List<CarreraResponse>> listarTodas() {
        return ResponseEntity.ok(carreraService.listarTodas());
    }


    @GetMapping("/activas")
    public ResponseEntity<List<CarreraResponse>> listarActivas() {
        return ResponseEntity.ok(carreraService.listarActivas());
    }


    @GetMapping("/facultad/{idFacultad}")
    public ResponseEntity<List<CarreraResponse>> listarPorFacultad(
            @PathVariable Long idFacultad
    ) {
        return ResponseEntity.ok(carreraService.listarPorFacultad(idFacultad));
    }


    @GetMapping("/facultad/{idFacultad}/activas")
    public ResponseEntity<List<CarreraResponse>> listarActivasPorFacultad(
            @PathVariable Long idFacultad
    ) {
        return ResponseEntity.ok(carreraService.listarActivasPorFacultad(idFacultad));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CarreraResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carreraService.buscarPorId(id));
    }


    @PostMapping
    public ResponseEntity<CarreraResponse> crear(
            @Valid @RequestBody CarreraRequest request
    ) {
        CarreraResponse response = carreraService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CarreraResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CarreraRequest request
    ) {
        return ResponseEntity.ok(carreraService.actualizar(id, request));
    }


    @PatchMapping("/{id}/activar")
    public ResponseEntity<CarreraResponse> activar(@PathVariable Long id) {
        return ResponseEntity.ok(carreraService.activar(id));
    }


    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<CarreraResponse> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(carreraService.inactivar(id));
    }


    @PatchMapping("/{id}/archivar")
    public ResponseEntity<CarreraResponse> archivar(@PathVariable Long id) {
        return ResponseEntity.ok(carreraService.archivar(id));
    }
}