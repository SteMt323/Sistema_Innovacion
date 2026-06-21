package ni.edu.uam.innovacion.modules.catalog.repository;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.RolProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a la tabla roles_proyecto.
 *
 * Este repository permite consultar, crear, actualizar y cambiar
 * el estado de los roles utilizados dentro de los proyectos.
 */
public interface RolProyectoRepository extends JpaRepository<RolProyecto, Long> {

    /**
     * Busca un rol de proyecto por nombre sin distinguir mayúsculas o minúsculas.
     *
     * Se usa para validar duplicados al actualizar.
     */
    Optional<RolProyecto> findByNombreIgnoreCase(String nombre);

    /**
     * Verifica si ya existe un rol de proyecto con el mismo nombre.
     *
     * Se usa antes de crear un nuevo registro.
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Lista todos los roles de proyecto ordenados alfabéticamente.
     */
    List<RolProyecto> findAllByOrderByNombreAsc();

    /**
     * Lista los roles de proyecto filtrados por estado.
     *
     * Ejemplo: solo roles activos.
     */
    List<RolProyecto> findByEstadoOrderByNombreAsc(EstadoRegistro estado);
}