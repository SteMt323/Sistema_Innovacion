package ni.edu.uam.innovacion.modules.participation.repository;

import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.modules.participation.entity.Participacion;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipacionRepository extends JpaRepository<Participacion, Long> {

    Optional<Participacion> findByInscripcion_IdInscripcion(Long idInscripcion);

    boolean existsByInscripcion_IdInscripcion(Long idInscripcion);

    boolean existsByInscripcion_IdInscripcionAndEstado(
            Long idInscripcion,
            EstadoParticipacion estado
    );

    List<Participacion> findAllByOrderByCreadoEnDesc();

    List<Participacion> findByEstadoOrderByCreadoEnDesc(
            EstadoParticipacion estado
    );

    List<Participacion> findByInscripcion_Actividad_IdActividadOrderByCreadoEnDesc(
            Long idActividad
    );

    List<Participacion> findByInscripcion_Usuario_IdUsuarioOrderByCreadoEnDesc(
            Long idUsuario
    );

    List<Participacion> findByInscripcion_Usuario_IdUsuarioAndEstadoOrderByCreadoEnDesc(
            Long idUsuario,
            EstadoParticipacion estado
    );

    List<Participacion> findByRolParticipacion_IdOrderByCreadoEnDesc(
            Long idRolParticipacion
    );

    List<Participacion> findByValidadoPorAdmin_IdUsuarioOrderByFechaValidacionDesc(
            Long idAdministrador
    );

    long countByInscripcion_Actividad_IdActividadAndEstado(
            Long idActividad,
            EstadoParticipacion estado
    );

    long countByInscripcion_Usuario_IdUsuarioAndEstado(
            Long idUsuario,
            EstadoParticipacion estado
    );

    long countByEstado(EstadoParticipacion estado);
}