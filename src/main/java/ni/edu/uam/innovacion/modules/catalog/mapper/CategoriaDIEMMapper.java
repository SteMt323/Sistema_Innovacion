package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaDIEMRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.CategoriaDIEMResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaDIEM;


public class CategoriaDIEMMapper {

    private CategoriaDIEMMapper() {
    }

    /**
     * Convierte un CategoriaDIEMRequest en una entidad CategoriaDIEM.

     */
    public static CategoriaDIEM toEntity(
            CategoriaDIEMRequest request,
            AmbitoActividad ambitoActividad
    ) {
        CategoriaDIEM categoriaDIEM = new CategoriaDIEM();

        updateEntity(categoriaDIEM, request, ambitoActividad);

        return categoriaDIEM;
    }

    /**
     * Convierte una entidad CategoriaDIEM en un CategoriaDIEMResponse.
     */
    public static CategoriaDIEMResponse toResponse(CategoriaDIEM categoriaDIEM) {
        AmbitoActividad ambitoActividad = categoriaDIEM.getAmbitoActividad();

        return new CategoriaDIEMResponse(
                categoriaDIEM.getId(),
                categoriaDIEM.getNombre(),
                categoriaDIEM.getDescripcion(),
                categoriaDIEM.getCriteriosPuntuacion(),
                categoriaDIEM.getEstado(),
                ambitoActividad.getId(),
                ambitoActividad.getNombre(),
                ambitoActividad.getRequiereCategoria()
        );
    }

    /**
     * Actualiza una entidad CategoriaDIEM existente con los datos del request.
     */
    public static void updateEntity(
            CategoriaDIEM categoriaDIEM,
            CategoriaDIEMRequest request,
            AmbitoActividad ambitoActividad
    ) {
        categoriaDIEM.setNombre(request.getNombre());
        categoriaDIEM.setDescripcion(request.getDescripcion());
        categoriaDIEM.setCriteriosPuntuacion(request.getCriteriosPuntuacion());
        categoriaDIEM.setAmbitoActividad(ambitoActividad);
    }
}