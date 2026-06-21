package ni.edu.uam.innovacion.modules.project.repository;

import ni.edu.uam.innovacion.modules.project.entity.AsignacionMentorProyecto;
import ni.edu.uam.innovacion.modules.project.enums.EstadoAsignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a la tabla asignaciones_mentor_proyecto.
 *
 * Permite consultar asignaciones de mentores por proyecto, mentor,
 * administrador y estado.
 */
public interface AsignacionMentorProyectoRepository extends JpaRepository<AsignacionMentorProyecto, Long> {

    /**
     * Lista todas las asignaciones ordenadas por fecha de asignación descendente.
     */
    List<AsignacionMentorProyecto> findAllByOrderByFechaAsignacionDesc();

    /**
     * Lista las asignaciones de mentoría de un proyecto específico.
     */
    List<AsignacionMentorProyecto> findByProyecto_IdProyectoOrderByFechaAsignacionDesc(
            Long idProyecto
    );

    /**
     * Lista las asignaciones de un proyecto filtradas por estado.
     *
     * Ejemplo: mentores activos de un proyecto.
     */
    List<AsignacionMentorProyecto> findByProyecto_IdProyectoAndEstadoOrderByFechaAsignacionDesc(
            Long idProyecto,
            EstadoAsignacion estado
    );

    /**
     * Lista los proyectos asignados a un mentor específico.
     */
    List<AsignacionMentorProyecto> findByMentor_IdUsuarioOrderByFechaAsignacionDesc(
            Long idMentor
    );

    /**
     * Lista los proyectos asignados a un mentor según estado.
     */
    List<AsignacionMentorProyecto> findByMentor_IdUsuarioAndEstadoOrderByFechaAsignacionDesc(
            Long idMentor,
            EstadoAsignacion estado
    );

    /**
     * Lista asignaciones según su estado.
     */
    List<AsignacionMentorProyecto> findByEstadoOrderByFechaAsignacionDesc(
            EstadoAsignacion estado
    );

    /**
     * Lista asignaciones registradas por un administrador específico.
     */
    List<AsignacionMentorProyecto> findByAdministradorRegistro_IdUsuarioOrderByFechaAsignacionDesc(
            Long idAdministrador
    );

    /**
     * Busca la asignación entre un proyecto y un mentor.
     *
     * Sirve para validar si el mentor ya fue asignado al proyecto.
     */
    Optional<AsignacionMentorProyecto> findByProyecto_IdProyectoAndMentor_IdUsuario(
            Long idProyecto,
            Long idMentor
    );

    /**
     * Verifica si un mentor ya fue asignado a un proyecto.
     *
     * Esto evita duplicar la relación proyecto-mentor.
     */
    boolean existsByProyecto_IdProyectoAndMentor_IdUsuario(
            Long idProyecto,
            Long idMentor
    );

    /**
     * Verifica si un mentor tiene una asignación activa dentro de un proyecto.
     */
    boolean existsByProyecto_IdProyectoAndMentor_IdUsuarioAndEstado(
            Long idProyecto,
            Long idMentor,
            EstadoAsignacion estado
    );

    /**
     * Verifica si un proyecto tiene al menos una mentoría activa.
     */
    boolean existsByProyecto_IdProyectoAndEstado(
            Long idProyecto,
            EstadoAsignacion estado
    );
}