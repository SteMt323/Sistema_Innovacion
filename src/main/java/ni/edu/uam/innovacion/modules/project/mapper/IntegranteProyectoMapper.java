package ni.edu.uam.innovacion.modules.project.mapper;

import ni.edu.uam.innovacion.modules.catalog.entity.RolProyecto;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarIntegranteProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearIntegranteProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.IntegranteProyectoResponse;
import ni.edu.uam.innovacion.modules.project.entity.IntegranteProyecto;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

/**
 * Mapper encargado de convertir entre la entidad IntegranteProyecto
 * y sus DTOs de entrada y salida.
 */
public class IntegranteProyectoMapper {

    private IntegranteProyectoMapper() {
    }

    /**
     * Convierte un CrearIntegranteProyectoRequest en una entidad IntegranteProyecto.
     *
     * El administrador se recibe desde el service porque viene del JWT.
     */
    public static IntegranteProyecto toEntity(
            CrearIntegranteProyectoRequest request,
            Proyecto proyecto,
            Usuario usuario,
            RolProyecto rolProyecto,
            PerfilAdministrador registradoPorAdmin
    ) {
        IntegranteProyecto integranteProyecto = new IntegranteProyecto();

        integranteProyecto.setProyecto(proyecto);
        integranteProyecto.setUsuario(usuario);
        integranteProyecto.setRolProyecto(rolProyecto);
        integranteProyecto.setFechaVinculacion(request.fechaVinculacion());
        integranteProyecto.setObservaciones(request.observaciones());
        integranteProyecto.setRegistradoPorAdmin(registradoPorAdmin);

        return integranteProyecto;
    }

    /**
     * Convierte una entidad IntegranteProyecto en IntegranteProyectoResponse.
     */
    public static IntegranteProyectoResponse toResponse(
            IntegranteProyecto integranteProyecto
    ) {
        Proyecto proyecto = integranteProyecto.getProyecto();
        Usuario usuario = integranteProyecto.getUsuario();
        RolProyecto rolProyecto = integranteProyecto.getRolProyecto();
        PerfilAdministrador administrador = integranteProyecto.getRegistradoPorAdmin();

        return new IntegranteProyectoResponse(
                integranteProyecto.getIdIntegranteProyecto(),

                proyecto.getIdProyecto(),
                proyecto.getNombre(),

                usuario.getIdUsuario(),
                usuario.getNombreCompleto(),
                usuario.getCorreo(),
                usuario.getDocumento(),

                rolProyecto.getId(),
                rolProyecto.getNombre(),

                integranteProyecto.getFechaVinculacion(),
                integranteProyecto.getEstado(),
                integranteProyecto.getObservaciones(),

                administrador.getIdUsuario(),
                administrador.getUsuario().getNombreCompleto(),

                integranteProyecto.getCreadoEn(),
                integranteProyecto.getActualizadoEn()
        );
    }

    /**
     * Actualiza los datos editables de un integrante de proyecto.
     *
     * No se actualiza el proyecto ni el usuario, porque esa es la relación
     * principal del registro.
     */
    public static void updateEntity(
            IntegranteProyecto integranteProyecto,
            ActualizarIntegranteProyectoRequest request,
            RolProyecto rolProyecto
    ) {
        integranteProyecto.setRolProyecto(rolProyecto);

        if (request.fechaVinculacion() != null) {
            integranteProyecto.setFechaVinculacion(request.fechaVinculacion());
        }

        integranteProyecto.setObservaciones(request.observaciones());
    }
}