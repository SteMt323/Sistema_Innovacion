package ni.edu.uam.innovacion.modules.project.mapper;

import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarProyectoActividadRequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearProyectoActividadRequest;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoActividadResponse;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.project.entity.ProyectoActividad;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

/**
 * Mapper encargado de convertir entre DTOs y la entidad ProyectoActividad.
 *
 * Esta clase mantiene separada la lógica de conversión para que el service
 * se enfoque únicamente en reglas de negocio y validaciones.
 */
public class ProyectoActividadMapper {

    private ProyectoActividadMapper() {
    }

    public static ProyectoActividad toEntity(
            CrearProyectoActividadRequest request,
            Proyecto proyecto,
            Actividad actividad,
            PerfilAdministrador registradoPorAdmin
    ) {
        ProyectoActividad proyectoActividad = new ProyectoActividad();

        proyectoActividad.setProyecto(proyecto);
        proyectoActividad.setActividad(actividad);
        proyectoActividad.setTipoVinculo(request.tipoVinculo());
        proyectoActividad.setEsActividadOrigen(esActividadOrigenNormalizada(request.esActividadOrigen()));
        proyectoActividad.setRegistradoPorAdmin(registradoPorAdmin);
        proyectoActividad.setObservaciones(request.observaciones());

        return proyectoActividad;
    }

    public static void updateEntity(
            ProyectoActividad proyectoActividad,
            ActualizarProyectoActividadRequest request
    ) {
        proyectoActividad.setTipoVinculo(request.tipoVinculo());
        proyectoActividad.setEsActividadOrigen(esActividadOrigenNormalizada(request.esActividadOrigen()));
        proyectoActividad.setObservaciones(request.observaciones());
    }

    public static ProyectoActividadResponse toResponse(ProyectoActividad proyectoActividad) {
        Proyecto proyecto = proyectoActividad.getProyecto();
        Actividad actividad = proyectoActividad.getActividad();
        PerfilAdministrador registradoPorAdmin = proyectoActividad.getRegistradoPorAdmin();
        Usuario usuarioAdmin = registradoPorAdmin == null ? null : registradoPorAdmin.getUsuario();

        return new ProyectoActividadResponse(
                proyectoActividad.getIdProyectoActividad(),

                proyecto == null ? null : proyecto.getIdProyecto(),
                proyecto == null ? null : proyecto.getNombre(),

                actividad == null ? null : actividad.getIdActividad(),
                actividad == null ? null : actividad.getNombre(),

                proyectoActividad.getTipoVinculo(),
                proyectoActividad.getEsActividadOrigen(),

                proyectoActividad.getFechaVinculacion(),

                registradoPorAdmin == null ? null : registradoPorAdmin.getIdUsuario(),
                usuarioAdmin == null ? null : usuarioAdmin.getNombreCompleto(),

                proyectoActividad.getObservaciones()
        );
    }

    private static Boolean esActividadOrigenNormalizada(Boolean esActividadOrigen) {
        return Boolean.TRUE.equals(esActividadOrigen);
    }
}