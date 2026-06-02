package ni.edu.uam.innovacion.modules.user.service;

import java.util.List;
import ni.edu.uam.innovacion.common.exception.DuplicateResourceException;
import ni.edu.uam.innovacion.modules.user.dto.CrearRolRequest;
import ni.edu.uam.innovacion.modules.user.dto.RolResponse;
import ni.edu.uam.innovacion.modules.user.entity.Rol;
import ni.edu.uam.innovacion.modules.user.mapper.UsuarioMapper;
import ni.edu.uam.innovacion.modules.user.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;

    public RolService(RolRepository rolRepository, UsuarioMapper usuarioMapper) {
        this.rolRepository = rolRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional(readOnly = true)
    public List<RolResponse> listarRoles() {
        return rolRepository.findAll().stream()
            .map(usuarioMapper::toRolResponse)
            .toList();
    }

    @Transactional
    public RolResponse crearRol(CrearRolRequest request) {
        String nombre = normalizarNombreRol(request.nombre());
        if (rolRepository.existsByNombreIgnoreCase(nombre)) {
            throw new DuplicateResourceException("Ya existe un rol con el nombre " + nombre);
        }

        Rol rol = new Rol();
        rol.setNombre(nombre);
        rol.setDescripcion(limpiar(request.descripcion()));
        return usuarioMapper.toRolResponse(rolRepository.save(rol));
    }

    static String normalizarNombreRol(String nombre) {
        return nombre == null ? null : nombre.trim().toLowerCase();
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
