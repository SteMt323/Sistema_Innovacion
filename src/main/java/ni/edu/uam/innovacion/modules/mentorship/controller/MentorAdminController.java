package ni.edu.uam.innovacion.modules.mentorship.controller;

import jakarta.validation.Valid;
import java.util.List;
import ni.edu.uam.innovacion.modules.mentorship.dto.ActualizarPerfilMentorRequest;
import ni.edu.uam.innovacion.modules.mentorship.dto.MentorAdminResponse;
import ni.edu.uam.innovacion.modules.mentorship.service.MentorAdminService;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/mentores")
public class MentorAdminController {

    private final MentorAdminService mentorAdminService;

    public MentorAdminController(MentorAdminService mentorAdminService) {
        this.mentorAdminService = mentorAdminService;
    }

    @GetMapping
    public List<MentorAdminResponse> listar(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) String estadoUsuario,
        @RequestParam(defaultValue = "false") boolean soloConMentoriasActivas
    ) {
        return mentorAdminService.listar(
            q,
            EstadoUsuario.fromValue(estadoUsuario),
            soloConMentoriasActivas
        );
    }

    @GetMapping("/{idMentor}")
    public MentorAdminResponse obtenerPorId(@PathVariable Long idMentor) {
        return mentorAdminService.obtenerPorId(idMentor);
    }

    @PutMapping("/{idMentor}")
    public MentorAdminResponse actualizar(
        @PathVariable Long idMentor,
        @Valid @RequestBody ActualizarPerfilMentorRequest request
    ) {
        return mentorAdminService.actualizar(idMentor, request);
    }
}
