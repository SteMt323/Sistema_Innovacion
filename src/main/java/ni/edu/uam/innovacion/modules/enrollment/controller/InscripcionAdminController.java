package ni.edu.uam.innovacion.modules.enrollment.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import ni.edu.uam.innovacion.modules.enrollment.dto.ActualizarInscripcionRequest;
import ni.edu.uam.innovacion.modules.enrollment.dto.CambiarEstadoInscripcionRequest;
import ni.edu.uam.innovacion.modules.enrollment.dto.CrearInscripcionRequest;
import ni.edu.uam.innovacion.modules.enrollment.dto.InscripcionResponse;
import ni.edu.uam.innovacion.modules.enrollment.enums.EstadoInscripcion;
import ni.edu.uam.innovacion.modules.enrollment.service.InscripcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller administrativo para gestionar inscripciones a actividades.
 *
 * Este controller permite registrar, consultar, actualizar y cambiar
 * el estado de las inscripciones.
 *
 * Importante:
 * Una inscripción no representa participación validada.
 * La participación real se gestionará posteriormente desde el módulo
 * de participaciones.
 */
@RestController
@RequestMapping("/api/admin/inscripciones")
public class InscripcionAdminController {

    private final InscripcionService inscripcionService;

    public InscripcionAdminController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    /**
     * Crea una nueva inscripción de un usuario a una actividad.
     */
    @PostMapping
    public ResponseEntity<InscripcionResponse> crear(
            @Valid @RequestBody CrearInscripcionRequest request
    ) {
        InscripcionResponse response = inscripcionService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/admin/inscripciones/" + response.idInscripcion()))
                .body(response);
    }

    /**
     * Lista todas las inscripciones registradas.
     */
    @GetMapping
    public List<InscripcionResponse> listarTodas() {
        return inscripcionService.listarTodas();
    }

    /**
     * Busca una inscripción por id.
     */
    @GetMapping("/{idInscripcion}")
    public InscripcionResponse buscarPorId(@PathVariable Long idInscripcion) {
        return inscripcionService.buscarPorId(idInscripcion);
    }

    /**
     * Lista las inscripciones de un usuario específico.
     */
    @GetMapping("/usuario/{idUsuario}")
    public List<InscripcionResponse> listarPorUsuario(
            @PathVariable Long idUsuario
    ) {
        return inscripcionService.listarPorUsuario(idUsuario);
    }

    /**
     * Lista las inscripciones de una actividad específica.
     */
    @GetMapping("/actividad/{idActividad}")
    public List<InscripcionResponse> listarPorActividad(
            @PathVariable Long idActividad
    ) {
        return inscripcionService.listarPorActividad(idActividad);
    }

    /**
     * Lista inscripciones por estado.
     *
     * Ejemplo:
     * /api/admin/inscripciones/estado/registrada
     * /api/admin/inscripciones/estado/confirmada
     */
    @GetMapping("/estado/{estado}")
    public List<InscripcionResponse> listarPorEstado(
            @PathVariable String estado
    ) {
        return inscripcionService.listarPorEstado(
                EstadoInscripcion.fromValue(estado)
        );
    }

    /**
     * Lista inscripciones de una actividad filtradas por estado.
     */
    @GetMapping("/actividad/{idActividad}/estado/{estado}")
    public List<InscripcionResponse> listarPorActividadYEstado(
            @PathVariable Long idActividad,
            @PathVariable String estado
    ) {
        return inscripcionService.listarPorActividadYEstado(
                idActividad,
                EstadoInscripcion.fromValue(estado)
        );
    }

    /**
     * Lista inscripciones de un usuario filtradas por estado.
     */
    @GetMapping("/usuario/{idUsuario}/estado/{estado}")
    public List<InscripcionResponse> listarPorUsuarioYEstado(
            @PathVariable Long idUsuario,
            @PathVariable String estado
    ) {
        return inscripcionService.listarPorUsuarioYEstado(
                idUsuario,
                EstadoInscripcion.fromValue(estado)
        );
    }

    /**
     * Actualiza únicamente las observaciones de una inscripción.
     */
    @PutMapping("/{idInscripcion}")
    public InscripcionResponse actualizar(
            @PathVariable Long idInscripcion,
            @Valid @RequestBody ActualizarInscripcionRequest request
    ) {
        return inscripcionService.actualizar(idInscripcion, request);
    }

    /**
     * Cambia el estado de una inscripción usando un request.
     */
    @PatchMapping("/{idInscripcion}/estado")
    public InscripcionResponse cambiarEstado(
            @PathVariable Long idInscripcion,
            @Valid @RequestBody CambiarEstadoInscripcionRequest request
    ) {
        return inscripcionService.cambiarEstado(idInscripcion, request);
    }

    /**
     * Cambia una inscripción a estado pendiente.
     */
    @PatchMapping("/{idInscripcion}/pendiente")
    public InscripcionResponse dejarPendiente(@PathVariable Long idInscripcion) {
        return inscripcionService.dejarPendiente(idInscripcion);
    }

    /**
     * Confirma una inscripción.
     */
    @PatchMapping("/{idInscripcion}/confirmar")
    public InscripcionResponse confirmar(@PathVariable Long idInscripcion) {
        return inscripcionService.confirmar(idInscripcion);
    }

    /**
     * Cancela una inscripción.
     */
    @PatchMapping("/{idInscripcion}/cancelar")
    public InscripcionResponse cancelar(@PathVariable Long idInscripcion) {
        return inscripcionService.cancelar(idInscripcion);
    }

    /**
     * Rechaza una inscripción.
     */
    @PatchMapping("/{idInscripcion}/rechazar")
    public InscripcionResponse rechazar(@PathVariable Long idInscripcion) {
        return inscripcionService.rechazar(idInscripcion);
    }
}