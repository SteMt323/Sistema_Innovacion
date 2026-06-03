package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.FuenteProyectoResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;

/**
 * Mapper encargado de convertir entre DTOs y la entidad FuenteProyecto.
 */
public class FuenteProyectoMapper {

    /*
     * Constructor privado para evitar crear objetos de esta clase.
     */
    private FuenteProyectoMapper() {
    }

    /**
     * Convierte un FuenteProyectoRequest en una entidad FuenteProyecto.
     */
    public static FuenteProyecto toEntity(FuenteProyectoRequest request) {
        FuenteProyecto fuenteProyecto = new FuenteProyecto();

        updateEntity(fuenteProyecto, request);

        return fuenteProyecto;
    }

    /**
     * Convierte una entidad FuenteProyecto en un FuenteProyectoResponse.
     */
    public static FuenteProyectoResponse toResponse(FuenteProyecto fuenteProyecto) {
        return new FuenteProyectoResponse(
                fuenteProyecto.getId(),
                fuenteProyecto.getNombre(),
                fuenteProyecto.getDescripcion(),
                fuenteProyecto.getCategoria(),
                fuenteProyecto.getEstado()
        );
    }

    /**
     * Actualiza una entidad FuenteProyecto existente con los datos
     * recibidos en el request.
     */
    public static void updateEntity(
            FuenteProyecto fuenteProyecto,
            FuenteProyectoRequest request
    ) {
        fuenteProyecto.setNombre(request.getNombre());
        fuenteProyecto.setDescripcion(request.getDescripcion());
        fuenteProyecto.setCategoria(request.getCategoria());
    }
}