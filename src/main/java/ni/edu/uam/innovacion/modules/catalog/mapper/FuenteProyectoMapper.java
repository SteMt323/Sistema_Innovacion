package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaFuenteProyecto;
import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;

/**
 * Mapper encargado de convertir entre DTOs y la entidad FuenteProyecto.
 */
public class FuenteProyectoMapper {

    /**
     * Constructor privado para evitar que esta clase sea instanciada.
     */
    private FuenteProyectoMapper() {
    }

    public static FuenteProyecto toEntity(
            FuenteProyectoRequest request,
            CategoriaFuenteProyecto categoriaFuenteProyecto
    ) {
        FuenteProyecto fuenteProyecto = new FuenteProyecto();

        fuenteProyecto.setNombre(request.getNombre());
        fuenteProyecto.setDescripcion(request.getDescripcion());
        fuenteProyecto.setCategoriaFuenteProyecto(categoriaFuenteProyecto);

        return fuenteProyecto;
    }

    public static FuenteProyectoResponse toResponse(FuenteProyecto fuenteProyecto) {
        CategoriaFuenteProyecto categoriaFuenteProyecto =
                fuenteProyecto.getCategoriaFuenteProyecto();

        return new FuenteProyectoResponse(
                fuenteProyecto.getId(),
                fuenteProyecto.getNombre(),
                fuenteProyecto.getDescripcion(),
                fuenteProyecto.getEstado(),
                categoriaFuenteProyecto != null ? categoriaFuenteProyecto.getId() : null,
                categoriaFuenteProyecto != null ? categoriaFuenteProyecto.getNombre() : null
        );
    }

    /**
     * Actualiza una entidad FuenteProyecto existente
     * usando los datos recibidos en el Request.
     */
    public static void updateEntity(
            FuenteProyecto fuenteProyecto,
            FuenteProyectoRequest request,
            CategoriaFuenteProyecto categoriaFuenteProyecto
    ) {
        fuenteProyecto.setNombre(request.getNombre());
        fuenteProyecto.setDescripcion(request.getDescripcion());
        fuenteProyecto.setCategoriaFuenteProyecto(categoriaFuenteProyecto);
    }
}