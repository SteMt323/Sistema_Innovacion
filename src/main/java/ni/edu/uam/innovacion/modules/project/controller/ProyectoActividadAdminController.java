package ni.edu.uam.innovacion.modules.project.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarProyectoActividadRequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearProyectoActividadRequest;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoActividadResponse;
import ni.edu.uam.innovacion.modules.project.enums.TipoVinculoProyectoActividad;
import ni.edu.uam.innovacion.modules.project.service.ProyectoActividadService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/proyecto-actividades")
public class ProyectoActividadAdminController {

    private final ProyectoActividadService proyectoActividadService;

    public ProyectoActividadAdminController(ProyectoActividadService proyectoActividadService) {
        this.proyectoActividadService = proyectoActividadService;
    }

    /**
     * Vincula un proyecto con una actividad.
     *
     * El administrador que realiza la vinculación se obtiene desde el JWT.
     */
    @PostMapping
    public ResponseEntity<ProyectoActividadResponse> crear(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CrearProyectoActividadRequest request
    ) {
        ProyectoActividadResponse response =
                proyectoActividadService.crear(request, obtenerIdUsuario(jwt));

        return ResponseEntity
                .created(URI.create(
                        "/api/admin/proyecto-actividades/" + response.idProyectoActividad()
                ))
                .body(response);
    }

    /**
     * Busca una vinculación por su id.
     */
    @GetMapping("/{idProyectoActividad}")
    public ProyectoActividadResponse buscarPorId(
            @PathVariable Long idProyectoActividad
    ) {
        return proyectoActividadService.buscarPorId(idProyectoActividad);
    }

    /**
     * Lista todas las actividades vinculadas a un proyecto.
     */
    @GetMapping("/proyecto/{idProyecto}")
    public List<ProyectoActividadResponse> listarPorProyecto(
            @PathVariable Long idProyecto
    ) {
        return proyectoActividadService.listarPorProyecto(idProyecto);
    }

    /**
     * Lista todos los proyectos vinculados a una actividad.
     */
    @GetMapping("/actividad/{idActividad}")
    public List<ProyectoActividadResponse> listarPorActividad(
            @PathVariable Long idActividad
    ) {
        return proyectoActividadService.listarPorActividad(idActividad);
    }

    /**
     * Lista vínculos por tipo.
     *
     * Ejemplos válidos:
     * - origen
     * - seguimiento
     * - presentacion
     * - formacion
     * - concurso
     * - mentoria
     * - otro
     */
    @GetMapping("/tipo/{tipoVinculo}")
    public List<ProyectoActividadResponse> listarPorTipoVinculo(
            @PathVariable String tipoVinculo
    ) {
        return proyectoActividadService.listarPorTipoVinculo(
                TipoVinculoProyectoActividad.fromValue(tipoVinculo)
        );
    }

    /**
     * Lista vínculos de un proyecto filtrados por tipo de vínculo.
     */
    @GetMapping("/proyecto/{idProyecto}/tipo/{tipoVinculo}")
    public List<ProyectoActividadResponse> listarPorProyectoYTipoVinculo(
            @PathVariable Long idProyecto,
            @PathVariable String tipoVinculo
    ) {
        return proyectoActividadService.listarPorProyectoYTipoVinculo(
                idProyecto,
                TipoVinculoProyectoActividad.fromValue(tipoVinculo)
        );
    }

    /**
     * Lista la actividad marcada como origen de un proyecto.
     */
    @GetMapping("/proyecto/{idProyecto}/origen")
    public List<ProyectoActividadResponse> listarActividadOrigen(
            @PathVariable Long idProyecto
    ) {
        return proyectoActividadService.listarActividadOrigen(idProyecto);
    }

    /**
     * Lista los vínculos registrados por un administrador específico.
     */
    @GetMapping("/administrador/{idAdministrador}")
    public List<ProyectoActividadResponse> listarPorAdministrador(
            @PathVariable Long idAdministrador
    ) {
        return proyectoActividadService.listarPorAdministrador(idAdministrador);
    }

    /**
     * Actualiza el tipo de vínculo, si es actividad origen y las observaciones.
     *
     * No cambia el proyecto ni la actividad asociada.
     */
    @PutMapping("/{idProyectoActividad}")
    public ProyectoActividadResponse actualizar(
            @PathVariable Long idProyectoActividad,
            @Valid @RequestBody ActualizarProyectoActividadRequest request
    ) {
        return proyectoActividadService.actualizar(idProyectoActividad, request);
    }

    /**
     * Alternativa semántica para actualizar parcialmente el vínculo.
     *
     * Usa la misma lógica del método actualizar.
     */
    @PatchMapping("/{idProyectoActividad}")
    public ProyectoActividadResponse actualizarParcial(
            @PathVariable Long idProyectoActividad,
            @Valid @RequestBody ActualizarProyectoActividadRequest request
    ) {
        return proyectoActividadService.actualizar(idProyectoActividad, request);
    }

    private Long obtenerIdUsuario(Jwt jwt) {
        Number idUsuario = jwt.getClaim("idUsuario");
        return idUsuario.longValue();
    }
}