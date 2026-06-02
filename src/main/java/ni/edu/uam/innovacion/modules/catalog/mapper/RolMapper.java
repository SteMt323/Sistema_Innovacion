package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.RolRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.RolResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.Rol;

/**
 * Mapper encargado de convertir entre DTOs y entidades.

 */
public class RolMapper {

    private RolMapper() {
        /*
         * Constructor privado porque esta clase solo tendrá métodos estáticos.
         * No necesitamos crear objetos de RolMapper.
         */
    }

    /**
     * Convierte un RolRequest en una entidad Rol.
     *
     * Se usa al crear un nuevo rol.
     */
    public static Rol toEntity(RolRequest request) {
        return new Rol(
                request.getNombre(),
                request.getDescripcion()
        );
    }

    /**
     * Convierte una entidad Rol en RolResponse.
     *
     * Se usa para devolver datos al frontend o Postman.
     */
    public static RolResponse toResponse(Rol rol) {
        return new RolResponse(
                rol.getId(),
                rol.getNombre(),
                rol.getDescripcion(),
                rol.getEstado()
        );
    }


    public static void updateEntity(Rol rol, RolRequest request) {
        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
    }
}