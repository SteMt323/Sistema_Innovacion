package ni.edu.uam.innovacion.modules.catalog.mapper;

import ni.edu.uam.innovacion.modules.catalog.dto.CarreraRequest;
import ni.edu.uam.innovacion.modules.catalog.dto.CarreraResponse;
import ni.edu.uam.innovacion.modules.catalog.entity.Carrera;
import ni.edu.uam.innovacion.modules.catalog.entity.Facultad;

/**
 * Mapper encargado de convertir entre DTOs y la entidad Carrera.
 */
public class CarreraMapper {

    private CarreraMapper() {
    }


    public static Carrera toEntity(CarreraRequest request, Facultad facultad) {
        Carrera carrera = new Carrera();

        carrera.setNombre(request.getNombre());
        carrera.setDescripcion(request.getDescripcion());
        carrera.setCodigo(request.getCodigo());
        carrera.setFacultad(facultad);

        return carrera;
    }


    public static CarreraResponse toResponse(Carrera carrera) {
        Facultad facultad = carrera.getFacultad();

        return new CarreraResponse(
                carrera.getId(),
                carrera.getNombre(),
                carrera.getDescripcion(),
                carrera.getCodigo(),
                carrera.getEstado(),
                facultad.getId(),
                facultad.getNombre(),
                facultad.getCodigo()
        );
    }


    public static void updateEntity(Carrera carrera, CarreraRequest request, Facultad facultad) {
        carrera.setNombre(request.getNombre());
        carrera.setDescripcion(request.getDescripcion());
        carrera.setCodigo(request.getCodigo());
        carrera.setFacultad(facultad);
    }
}