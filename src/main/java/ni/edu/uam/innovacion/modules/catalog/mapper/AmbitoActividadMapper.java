package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.AmbitoActividadRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.AmbitoActividadResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;

/*
 * Mapper encargado de convertir entre DTOs y la entidad AmbitoActividad.

 */
public class AmbitoActividadMapper {

    /**
     * Constructor privado para evitar que esta clase sea instanciada.
     */
    private AmbitoActividadMapper() {
    }


    public static AmbitoActividad toEntity(AmbitoActividadRequest request) {
        AmbitoActividad ambitoActividad = new AmbitoActividad();

        ambitoActividad.setNombre(request.getNombre());
        ambitoActividad.setDescripcion(request.getDescripcion());
        ambitoActividad.setRequiereCategoria(request.getRequiereCategoria());

        return ambitoActividad;
    }


    public static AmbitoActividadResponse toResponse(AmbitoActividad ambitoActividad) {
        return new AmbitoActividadResponse(
                ambitoActividad.getId(),
                ambitoActividad.getNombre(),
                ambitoActividad.getDescripcion(),
                ambitoActividad.getRequiereCategoria(),
                ambitoActividad.getEstado()
        );
    }

    /**
     * Actualiza una entidad AmbitoActividad existente usando los datos
     * recibidos en el Request.
     */
    public static void updateEntity(
            AmbitoActividad ambitoActividad,
            AmbitoActividadRequest request
    ) {
        ambitoActividad.setNombre(request.getNombre());
        ambitoActividad.setDescripcion(request.getDescripcion());
        ambitoActividad.setRequiereCategoria(request.getRequiereCategoria());
    }
}