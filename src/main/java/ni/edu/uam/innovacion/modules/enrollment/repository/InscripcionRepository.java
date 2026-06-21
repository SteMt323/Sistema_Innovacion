package ni.edu.uam.innovacion.modules.enrollment.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.modules.enrollment.entity.Inscripcion;
import ni.edu.uam.innovacion.modules.enrollment.enums.EstadoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para gestionar las inscripciones de usuarios en actividades.
 *
 * Permite validar duplicidad, consultar inscripciones por usuario,
 * por actividad, por estado y contar inscripciones para control de cupos.
 */
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    /**
     * Busca una inscripción específica de un usuario en una actividad.
     *
     * Se usa para validar la regla:
     * un usuario no puede inscribirse dos veces en la misma actividad.
     */
    Optional<Inscripcion> findByUsuario_IdUsuarioAndActividad_IdActividad(
            Long idUsuario,
            Long idActividad
    );

    /**
     * Verifica si ya existe una inscripción para el mismo usuario y actividad.
     */
    boolean existsByUsuario_IdUsuarioAndActividad_IdActividad(
            Long idUsuario,
            Long idActividad
    );

    /**
     * Lista todas las inscripciones ordenadas por fecha descendente.
     */
    List<Inscripcion> findAllByOrderByFechaInscripcionDesc();

    /**
     * Lista las inscripciones de un usuario.
     */
    List<Inscripcion> findByUsuario_IdUsuarioOrderByFechaInscripcionDesc(
            Long idUsuario
    );

    /**
     * Lista las inscripciones de una actividad.
     */
    List<Inscripcion> findByActividad_IdActividadOrderByFechaInscripcionDesc(
            Long idActividad
    );

    /**
     * Lista inscripciones por estado.
     */
    List<Inscripcion> findByEstadoOrderByFechaInscripcionDesc(
            EstadoInscripcion estado
    );

    /**
     * Lista inscripciones por actividad y estado.
     */
    List<Inscripcion> findByActividad_IdActividadAndEstadoOrderByFechaInscripcionDesc(
            Long idActividad,
            EstadoInscripcion estado
    );

    /**
     * Lista inscripciones por usuario y estado.
     */
    List<Inscripcion> findByUsuario_IdUsuarioAndEstadoOrderByFechaInscripcionDesc(
            Long idUsuario,
            EstadoInscripcion estado
    );

    /**
     * Cuenta inscripciones de una actividad según varios estados.
     *
     * Será útil para validar cupos, tomando en cuenta únicamente
     * estados que ocupan cupo, por ejemplo REGISTRADA, PENDIENTE o CONFIRMADA.
     */
    long countByActividad_IdActividadAndEstadoIn(
            Long idActividad,
            Collection<EstadoInscripcion> estados
    );
}