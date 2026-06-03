package ni.edu.uam.innovacion.modules.catalog.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaDIEMRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaDIEMResponse;
import ni.edu.uam.innovacion.modules.catalog.service.CategoriaDIEMService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/catalog/categorias-diem")
public class CategoriaDIEMController {

    private final CategoriaDIEMService categoriaDIEMService;

    /**
     * Constructor para inyectar el Service.
     */
    public CategoriaDIEMController(CategoriaDIEMService categoriaDIEMService) {
        this.categoriaDIEMService = categoriaDIEMService;
    }

    /**
     * Lista todas las categorías DIEM registradas.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaDIEMResponse>> listarTodas() {
        return ResponseEntity.ok(categoriaDIEMService.listarTodas());
    }

    /**
     * Lista únicamente las categorías DIEM activas.
     */
    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaDIEMResponse>> listarActivas() {
        return ResponseEntity.ok(categoriaDIEMService.listarActivas());
    }

    /**
     * Lista todas las categorías asociadas a un ámbito de actividad.
     */
    @GetMapping("/ambito/{idAmbitoActividad}")
    public ResponseEntity<List<CategoriaDIEMResponse>> listarPorAmbito(
            @PathVariable Long idAmbitoActividad
    ) {
        return ResponseEntity.ok(
                categoriaDIEMService.listarPorAmbito(idAmbitoActividad)
        );
    }

    /**
     * Lista las categorías activas asociadas a un ámbito de actividad.
     */
    @GetMapping("/ambito/{idAmbitoActividad}/activas")
    public ResponseEntity<List<CategoriaDIEMResponse>> listarActivasPorAmbito(
            @PathVariable Long idAmbitoActividad
    ) {
        return ResponseEntity.ok(
                categoriaDIEMService.listarActivasPorAmbito(idAmbitoActividad)
        );
    }

    /**
     * Lista directamente las categorías activas del ámbito DIEM.
     */
    @GetMapping("/diem/activas")
    public ResponseEntity<List<CategoriaDIEMResponse>> listarActivasDelAmbitoDIEM() {
        return ResponseEntity.ok(
                categoriaDIEMService.listarActivasDelAmbitoDIEM()
        );
    }

    /**
     * Busca una categoría DIEM por su id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDIEMResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaDIEMService.buscarPorId(id));
    }

    /**
     * Crea una nueva categoría DIEM.
     */
    @PostMapping
    public ResponseEntity<CategoriaDIEMResponse> crear(
            @Valid @RequestBody CategoriaDIEMRequest request
    ) {
        CategoriaDIEMResponse response = categoriaDIEMService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza una categoría DIEM existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDIEMResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDIEMRequest request
    ) {
        return ResponseEntity.ok(categoriaDIEMService.actualizar(id, request));
    }

    /**
     * Activa una categoría DIEM.
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<CategoriaDIEMResponse> activar(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaDIEMService.activar(id));
    }

    /**
     * Inactiva una categoría DIEM.
     */
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<CategoriaDIEMResponse> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaDIEMService.inactivar(id));
    }

    /**
     * Archiva una categoría DIEM.
     */
    @PatchMapping("/{id}/archivar")
    public ResponseEntity<CategoriaDIEMResponse> archivar(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaDIEMService.archivar(id));
    }
}