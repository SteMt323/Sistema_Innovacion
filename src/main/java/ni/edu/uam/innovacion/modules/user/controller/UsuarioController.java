package ni.edu.uam.innovacion.modules.user.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import ni.edu.uam.innovacion.modules.user.dto.ActualizarUsuarioRequest;
import ni.edu.uam.innovacion.modules.user.dto.CambiarContrasenaRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilAdministradorRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilDocenteRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilEstudianteRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilMentorRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearPerfilParticipanteExternoRequest;
import ni.edu.uam.innovacion.modules.user.dto.CrearUsuarioRequest;
import ni.edu.uam.innovacion.modules.user.dto.PerfilAdministradorResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilDocenteResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilEstudianteResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilMentorResponse;
import ni.edu.uam.innovacion.modules.user.dto.PerfilParticipanteExternoResponse;
import ni.edu.uam.innovacion.modules.user.dto.UsuarioResponse;
import ni.edu.uam.innovacion.modules.user.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        UsuarioResponse response = usuarioService.crearUsuario(request);
        return ResponseEntity
            .created(URI.create("/api/usuarios/" + response.idUsuario()))
            .body(response);
    }

    @GetMapping
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{idUsuario}")
    public UsuarioResponse obtenerUsuario(@PathVariable Long idUsuario) {
        return usuarioService.obtenerUsuario(idUsuario);
    }

    @PutMapping("/{idUsuario}")
    public UsuarioResponse actualizarUsuario(
        @PathVariable Long idUsuario,
        @Valid @RequestBody ActualizarUsuarioRequest request
    ) {
        return usuarioService.actualizarUsuario(idUsuario, request);
    }

    @PatchMapping("/{idUsuario}/contrasena")
    public UsuarioResponse cambiarContrasena(
        @PathVariable Long idUsuario,
        @Valid @RequestBody CambiarContrasenaRequest request
    ) {
        return usuarioService.cambiarContrasena(idUsuario, request);
    }

    @PostMapping("/{idUsuario}/perfiles/estudiante")
    public ResponseEntity<PerfilEstudianteResponse> crearPerfilEstudiante(
        @PathVariable Long idUsuario,
        @Valid @RequestBody CrearPerfilEstudianteRequest request
    ) {
        PerfilEstudianteResponse response = usuarioService.crearPerfilEstudiante(idUsuario, request);
        return ResponseEntity
            .created(URI.create("/api/usuarios/" + idUsuario + "/perfiles/estudiante"))
            .body(response);
    }

    @GetMapping("/{idUsuario}/perfiles/estudiante")
    public PerfilEstudianteResponse obtenerPerfilEstudiante(@PathVariable Long idUsuario) {
        return usuarioService.obtenerPerfilEstudiante(idUsuario);
    }

    @PostMapping("/{idUsuario}/perfiles/administrador")
    public ResponseEntity<PerfilAdministradorResponse> crearPerfilAdministrador(
        @PathVariable Long idUsuario,
        @Valid @RequestBody CrearPerfilAdministradorRequest request
    ) {
        PerfilAdministradorResponse response = usuarioService.crearPerfilAdministrador(idUsuario, request);
        return ResponseEntity
            .created(URI.create("/api/usuarios/" + idUsuario + "/perfiles/administrador"))
            .body(response);
    }

    @GetMapping("/{idUsuario}/perfiles/administrador")
    public PerfilAdministradorResponse obtenerPerfilAdministrador(@PathVariable Long idUsuario) {
        return usuarioService.obtenerPerfilAdministrador(idUsuario);
    }

    @PostMapping("/{idUsuario}/perfiles/docente")
    public ResponseEntity<PerfilDocenteResponse> crearPerfilDocente(
        @PathVariable Long idUsuario,
        @Valid @RequestBody CrearPerfilDocenteRequest request
    ) {
        PerfilDocenteResponse response = usuarioService.crearPerfilDocente(idUsuario, request);
        return ResponseEntity
            .created(URI.create("/api/usuarios/" + idUsuario + "/perfiles/docente"))
            .body(response);
    }

    @GetMapping("/{idUsuario}/perfiles/docente")
    public PerfilDocenteResponse obtenerPerfilDocente(@PathVariable Long idUsuario) {
        return usuarioService.obtenerPerfilDocente(idUsuario);
    }

    @PostMapping("/{idUsuario}/perfiles/mentor")
    public ResponseEntity<PerfilMentorResponse> crearPerfilMentor(
        @PathVariable Long idUsuario,
        @Valid @RequestBody CrearPerfilMentorRequest request
    ) {
        PerfilMentorResponse response = usuarioService.crearPerfilMentor(idUsuario, request);
        return ResponseEntity
            .created(URI.create("/api/usuarios/" + idUsuario + "/perfiles/mentor"))
            .body(response);
    }

    @GetMapping("/{idUsuario}/perfiles/mentor")
    public PerfilMentorResponse obtenerPerfilMentor(@PathVariable Long idUsuario) {
        return usuarioService.obtenerPerfilMentor(idUsuario);
    }

    @PostMapping("/{idUsuario}/perfiles/participante-externo")
    public ResponseEntity<PerfilParticipanteExternoResponse> crearPerfilParticipanteExterno(
        @PathVariable Long idUsuario,
        @Valid @RequestBody CrearPerfilParticipanteExternoRequest request
    ) {
        PerfilParticipanteExternoResponse response = usuarioService.crearPerfilParticipanteExterno(idUsuario, request);
        return ResponseEntity
            .created(URI.create("/api/usuarios/" + idUsuario + "/perfiles/participante-externo"))
            .body(response);
    }

    @GetMapping("/{idUsuario}/perfiles/participante-externo")
    public PerfilParticipanteExternoResponse obtenerPerfilParticipanteExterno(@PathVariable Long idUsuario) {
        return usuarioService.obtenerPerfilParticipanteExterno(idUsuario);
    }
}
