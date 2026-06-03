package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.FacultadRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.FacultadResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.Facultad;

/*
 * Mapper encargado de convertir entre DTOs y la entidad Facultad.
 *
 * Un mapper sirve para separar la lógica de conversión de datos.
 * Así evitamos hacer estas conversiones directamente en el Controller
 * o en el Service.
 *
 * Convierte:
 * - FacultadRequest -> Facultad
 * - Facultad -> FacultadResponse
 */

public class FacultadMapper {

    private  FacultadMapper() {
    }

    /*
     Convierte un FacultadRequest en una entidad Facultad.
     */

    public static Facultad toEntity(FacultadRequest request) {
        Facultad facultad = new Facultad();

        facultad.setNombre(request.getNombre());
        facultad.setDescripcion(request.getDescripcion());
        facultad.setCodigo(request.getCodigo());

        return facultad;
    }

    public static FacultadResponse toResponse(Facultad facultad) {
        return new FacultadResponse(
                facultad.getId(),
                facultad.getNombre(),
                facultad.getDescripcion(),
                facultad.getCodigo(),
                facultad.getEstado()
        );
    }

    public static void updateEntity(Facultad facultad, FacultadRequest request) {
        facultad.setNombre(request.getNombre());
        facultad.setDescripcion(request.getDescripcion());
        facultad.setCodigo(request.getCodigo());
    }

}
