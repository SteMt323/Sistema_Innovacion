package ni.edu.uam.innovacion.modules.project.mapper;

import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarProyectoPIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearProyectoPIARequest;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoPIAResponse;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.project.entity.ProyectoPIA;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

/**
 * Mapper encargado de convertir entre DTOs y la entidad ProyectoPIA.
 *
 * Mantiene separada la lógica de transformación de datos para
 * no cargar el service con código de conversión.
 */
public class ProyectoPIAMapper {

    private ProyectoPIAMapper() {
    }

    public static ProyectoPIA toEntity(
            CrearProyectoPIARequest request,
            Proyecto proyecto,
            PerfilAdministrador registradoPorAdmin
    ) {
        ProyectoPIA proyectoPIA = new ProyectoPIA();

        proyectoPIA.setProyecto(proyecto);
        proyectoPIA.setFaseActual(request.faseActual());
        proyectoPIA.setFechaIngreso(request.fechaIngreso());
        proyectoPIA.setRegistradoPorAdmin(registradoPorAdmin);
        proyectoPIA.setObservaciones(request.observaciones());

        return proyectoPIA;
    }

    public static void updateEntity(
            ProyectoPIA proyectoPIA,
            ActualizarProyectoPIARequest request
    ) {
        proyectoPIA.setFaseActual(request.faseActual());
        proyectoPIA.setFechaIngreso(request.fechaIngreso());
        proyectoPIA.setObservaciones(request.observaciones());
    }

    public static ProyectoPIAResponse toResponse(ProyectoPIA proyectoPIA) {
        Proyecto proyecto = proyectoPIA.getProyecto();
        FuenteProyecto fuenteProyecto = proyecto.getFuenteProyecto();

        PerfilAdministrador registradoPorAdmin = proyectoPIA.getRegistradoPorAdmin();
        Usuario usuarioAdmin = registradoPorAdmin.getUsuario();

        return new ProyectoPIAResponse(
                proyectoPIA.getIdProyectoPIA(),

                proyecto.getIdProyecto(),
                proyecto.getNombre(),
                proyecto.getEstado(),

                fuenteProyecto == null ? null : fuenteProyecto.getId(),
                fuenteProyecto == null ? null : fuenteProyecto.getNombre(),

                proyectoPIA.getFaseActual(),
                proyectoPIA.getFechaIngreso(),
                proyectoPIA.getEstado(),

                registradoPorAdmin.getIdUsuario(),
                usuarioAdmin == null ? null : usuarioAdmin.getNombreCompleto(),

                proyectoPIA.getObservaciones(),

                proyectoPIA.getCreadoEn(),
                proyectoPIA.getActualizadoEn()
        );
    }
}