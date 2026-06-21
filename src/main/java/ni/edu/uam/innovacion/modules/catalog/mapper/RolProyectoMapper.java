package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.RolProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.RolProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.RolProyecto;

/**
 * Mapper encargado de convertir entre la entidad RolProyecto
 * y sus DTOs de entrada y salida.
 *
 * Esta clase evita exponer directamente la entidad desde la API
 * y mantiene separada la lógica de transformación de datos.
 */
public class RolProyectoMapper {

    private RolProyectoMapper() {
        /*
         * Constructor privado porque esta clase solo tendrá métodos estáticos.
         * No necesitamos crear objetos de RolProyectoMapper.
         */
    }

    /**
     * Convierte un RolProyectoRequest en una entidad RolProyecto.
     *
     * Se usa al crear un nuevo rol de proyecto.
     */
    public static RolProyecto toEntity(RolProyectoRequest request) {
        return new RolProyecto(
                request.getNombre(),
                request.getDescripcion()
        );
    }

    /**
     * Convierte una entidad RolProyecto en RolProyectoResponse.
     *
     * Se usa para devolver datos al frontend o a Postman.
     */
    public static RolProyectoResponse toResponse(RolProyecto rolProyecto) {
        return new RolProyectoResponse(
                rolProyecto.getId(),
                rolProyecto.getNombre(),
                rolProyecto.getDescripcion(),
                rolProyecto.getEstado()
        );
    }

    /**
     * Actualiza los datos editables de una entidad RolProyecto
     * a partir de un RolProyectoRequest.
     *
     * No modifica el id ni el estado, porque esos datos se manejan
     * por separado dentro del service.
     */
    public static void updateEntity(RolProyecto rolProyecto, RolProyectoRequest request) {
        rolProyecto.setNombre(request.getNombre());
        rolProyecto.setDescripcion(request.getDescripcion());
    }
}