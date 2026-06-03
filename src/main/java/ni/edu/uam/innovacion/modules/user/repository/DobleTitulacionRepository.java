package ni.edu.uam.innovacion.modules.user.repository;

import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.user.entity.DobleTitulacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DobleTitulacionRepository extends JpaRepository<DobleTitulacion, Long> {
    List<DobleTitulacion> findByPerfilEstudianteIdUsuarioOrderByFechaRegistroDesc(Long idUsuario);

    List<DobleTitulacion> findByPerfilEstudianteIdUsuarioAndEstadoOrderByFechaRegistroDesc(
        Long idUsuario,
        EstadoRegistro estado
    );

    Optional<DobleTitulacion> findByPerfilEstudianteIdUsuarioAndCarreraSecundariaId(
        Long idUsuario,
        Long idCarreraSecundaria
    );

    boolean existsByPerfilEstudianteIdUsuarioAndCarreraSecundariaIdAndEstado(
        Long idUsuario,
        Long idCarreraSecundaria,
        EstadoRegistro estado
    );

    boolean existsByPerfilEstudianteIdUsuarioAndEstado(Long idUsuario, EstadoRegistro estado);
}
