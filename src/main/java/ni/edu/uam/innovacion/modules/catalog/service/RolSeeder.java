package ni.edu.uam.innovacion.modules.catalog.service;

import java.util.Map;
import ni.edu.uam.innovacion.modules.catalog.entity.Rol;
import ni.edu.uam.innovacion.modules.catalog.repository.RolRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RolSeeder implements ApplicationRunner {

    private static final Map<String, String> ROLES_BASE = Map.of(
        "estudiante", "Usuario estudiante del sistema",
        "administrador", "Usuario con permisos administrativos",
        "docente", "Usuario docente",
        "mentor", "Usuario mentor",
        "participante_externo", "Usuario externo participante"
    );

    private final RolRepository rolRepository;

    public RolSeeder(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        ROLES_BASE.forEach((nombre, descripcion) -> {
            if (!rolRepository.existsByNombreIgnoreCase(nombre)) {
                rolRepository.save(new Rol(nombre, descripcion));
            }
        });
    }
}
