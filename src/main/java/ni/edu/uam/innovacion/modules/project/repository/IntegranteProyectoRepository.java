package ni.edu.uam.innovacion.modules.project.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.project.entity.IntegranteProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a la tabla integrantes_proyecto.
 *
 * Permite consultar integrantes por proyecto, usuario, rol,
 * administrador que registró y estado del registro.
 */
public interface IntegranteProyectoRepository extends JpaRepository<IntegranteProyecto, Long> {

    /**
     * Lista todos los integrantes ordenados por fecha de vinculación descendente.
     */
    List<IntegranteProyecto> findAllByOrderByFechaVinculacionDesc();

    /**
     * Lista los integrantes de un proyecto específico.
     */
    List<IntegranteProyecto> findByProyecto_IdProyectoOrderByFechaVinculacionDesc(
            Long idProyecto
    );

    /**
     * Lista los integrantes de un proyecto filtrados por estado.
     *
     * Ejemplo: integrantes activos de un proyecto.
     */
    List<IntegranteProyecto> findByProyecto_IdProyectoAndEstadoOrderByFechaVinculacionDesc(
            Long idProyecto,
            EstadoRegistro estado
    );

    /**
     * Lista los proyectos en los que participa un usuario.
     */
    List<IntegranteProyecto> findByUsuario_IdUsuarioOrderByFechaVinculacionDesc(
            Long idUsuario
    );

    /**
     * Lista los proyectos activos en los que participa un usuario.
     */
    List<IntegranteProyecto> findByUsuario_IdUsuarioAndEstadoOrderByFechaVinculacionDesc(
            Long idUsuario,
            EstadoRegistro estado
    );

    /**
     * Lista integrantes según el rol que cumplen dentro del proyecto.
     */
    List<IntegranteProyecto> findByRolProyecto_IdOrderByFechaVinculacionDesc(
            Long idRolProyecto
    );

    /**
     * Lista los integrantes registrados por un administrador específico.
     */
    List<IntegranteProyecto> findByRegistradoPorAdmin_IdUsuarioOrderByFechaVinculacionDesc(
            Long idAdministrador
    );

    /**
     * Busca la relación entre un proyecto y un usuario.
     *
     * Sirve para validar si el usuario ya pertenece al proyecto.
     */
    Optional<IntegranteProyecto> findByProyecto_IdProyectoAndUsuario_IdUsuario(
            Long idProyecto,
            Long idUsuario
    );

    /**
     * Verifica si un usuario ya fue agregado a un proyecto.
     *
     * Esto respalda la regla de no duplicar integrantes en el mismo proyecto.
     */
    boolean existsByProyecto_IdProyectoAndUsuario_IdUsuario(
            Long idProyecto,
            Long idUsuario
    );

    /**
     * Verifica si un proyecto tiene integrantes activos.
     *
     * Puede servir luego para validaciones antes de archivar o cerrar procesos.
     */
    boolean existsByProyecto_IdProyectoAndEstado(
            Long idProyecto,
            EstadoRegistro estado
    );
}