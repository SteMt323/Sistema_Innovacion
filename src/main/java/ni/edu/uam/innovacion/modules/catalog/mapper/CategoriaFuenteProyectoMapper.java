package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaFuenteProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaFuenteProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaFuenteProyecto;

/**
 * Mapper encargado de convertir entre DTOs y la entidad CategoriaFuenteProyecto.
 *
 * Permite separar la lógica de conversión de datos para mantener
 * más limpio el service y el controller.
 */
public class CategoriaFuenteProyectoMapper {

    /**
     * Constructor privado para evitar que esta clase sea instanciada.
     */
    private CategoriaFuenteProyectoMapper() {
    }

    public static CategoriaFuenteProyecto toEntity(CategoriaFuenteProyectoRequest request) {
        CategoriaFuenteProyecto categoriaFuenteProyecto = new CategoriaFuenteProyecto();

        categoriaFuenteProyecto.setNombre(request.getNombre());
        categoriaFuenteProyecto.setDescripcion(request.getDescripcion());

        return categoriaFuenteProyecto;
    }

    public static CategoriaFuenteProyectoResponse toResponse(
            CategoriaFuenteProyecto categoriaFuenteProyecto
    ) {
        return new CategoriaFuenteProyectoResponse(
                categoriaFuenteProyecto.getId(),
                categoriaFuenteProyecto.getNombre(),
                categoriaFuenteProyecto.getDescripcion(),
                categoriaFuenteProyecto.getEstado()
        );
    }

    /**
     * Actualiza una entidad CategoriaFuenteProyecto existente
     * usando los datos recibidos en el Request.
     */
    public static void updateEntity(
            CategoriaFuenteProyecto categoriaFuenteProyecto,
            CategoriaFuenteProyectoRequest request
    ) {
        categoriaFuenteProyecto.setNombre(request.getNombre());
        categoriaFuenteProyecto.setDescripcion(request.getDescripcion());
    }
}