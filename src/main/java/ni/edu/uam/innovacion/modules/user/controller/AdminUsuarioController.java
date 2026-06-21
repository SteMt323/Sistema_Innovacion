package ni.edu.uam.innovacion.modules.user.controller;

import jakarta.validation.Valid;
import ni.edu.uam.innovacion.modules.user.dto.AsignarRolRequest;
import ni.edu.uam.innovacion.modules.user.dto.CambiarEstadoUsuarioRequest;
import ni.edu.uam.innovacion.modules.user.dto.UsuarioResponse;
import ni.edu.uam.innovacion.modules.user.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/{idUsuario}/roles")
    public UsuarioResponse asignarRol(
        @PathVariable Long idUsuario,
        @Valid @RequestBody AsignarRolRequest request
    ) {
        return usuarioService.asignarRol(idUsuario, request);
    }

    @DeleteMapping("/{idUsuario}/roles/{nombreRol}")
    public ResponseEntity<Void> desactivarRol(
        @PathVariable Long idUsuario,
        @PathVariable String nombreRol
    ) {
        usuarioService.desactivarRol(idUsuario, nombreRol);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idUsuario}/estado")
    public UsuarioResponse cambiarEstado(
        @PathVariable Long idUsuario,
        @Valid @RequestBody CambiarEstadoUsuarioRequest request
    ) {
        return usuarioService.cambiarEstado(idUsuario, request);
    }
}
