package ni.edu.uam.innovacion.modules.user.repository;

import java.util.Optional;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByDocumento(String documento);

    boolean existsByCorreoIgnoreCaseAndIdUsuarioNot(String correo, Long idUsuario);

    boolean existsByDocumentoAndIdUsuarioNot(String documento, Long idUsuario);

    boolean existsByCedula(String cedula);

    boolean existsByCedulaAndIdUsuarioNot(String cedula, Long idUsuario);

    long countByEstado(EstadoUsuario estado);
}
