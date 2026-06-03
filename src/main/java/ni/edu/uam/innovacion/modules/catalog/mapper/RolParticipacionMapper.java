package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.RolParticipacionRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.RolParticipacionResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.RolParticipacion;

/**
 * Mapper encargado de convertir entre DTOs y la entidad RolParticipacion..
 */
public class RolParticipacionMapper {

    /*
     * Constructor privado para evitar que esta clase sea instanciada.
     */
    private RolParticipacionMapper() {
    }

    /**
     * Convierte un RolParticipacionRequest en una entidad RolParticipacion.
     */
    public static RolParticipacion toEntity(RolParticipacionRequest request) {
        RolParticipacion rolParticipacion = new RolParticipacion();

        updateEntity(rolParticipacion, request);

        return rolParticipacion;
    }

    /**
     * Convierte una entidad RolParticipacion en un RolParticipacionResponse.
     */
    public static RolParticipacionResponse toResponse(RolParticipacion rolParticipacion) {
        return new RolParticipacionResponse(
                rolParticipacion.getId(),
                rolParticipacion.getNombre(),
                rolParticipacion.getDescripcion(),
                rolParticipacion.getEstado()
        );
    }

    /**
     * Actualiza una entidad RolParticipacion existente con los datos del request.
     */
    public static void updateEntity(
            RolParticipacion rolParticipacion,
            RolParticipacionRequest request
    ) {
        rolParticipacion.setNombre(request.getNombre());
        rolParticipacion.setDescripcion(request.getDescripcion());
    }
}