package ni.edu.uam.innovacion.modules.project.mapper;

import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaFuenteProyecto;
import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;
import ni.edu.uam.innovacion.modules.project.dto.ActualizarProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearProyectoRequest;
import ni.edu.uam.innovacion.modules.project.dto.ProyectoResponse;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

/**
 * Mapper encargado de convertir entre DTOs y la entidad Proyecto.
 *
 * Permite mantener separada la lógica de conversión de datos
 * para que el service se enfoque en reglas de negocio y validaciones.
 */
public class ProyectoMapper {

    private ProyectoMapper() {
    }

    public static Proyecto toEntity(
            CrearProyectoRequest request,
            FuenteProyecto fuenteProyecto,
            PerfilAdministrador administradorRegistro
    ) {
        Proyecto proyecto = new Proyecto();

        proyecto.setFuenteProyecto(fuenteProyecto);
        proyecto.setAdministradorRegistro(administradorRegistro);
        proyecto.setNombre(request.nombre());
        proyecto.setDescripcion(request.descripcion());
        proyecto.setFechaInicio(request.fechaInicio());
        proyecto.setFechaFin(request.fechaFin());

        return proyecto;
    }

    public static void updateEntity(
            Proyecto proyecto,
            ActualizarProyectoRequest request,
            FuenteProyecto fuenteProyecto
    ) {
        proyecto.setFuenteProyecto(fuenteProyecto);
        proyecto.setNombre(request.nombre());
        proyecto.setDescripcion(request.descripcion());
        proyecto.setFechaInicio(request.fechaInicio());
        proyecto.setFechaFin(request.fechaFin());
    }

    public static ProyectoResponse toResponse(Proyecto proyecto) {
        FuenteProyecto fuenteProyecto = proyecto.getFuenteProyecto();

        CategoriaFuenteProyecto categoriaFuenteProyecto =
                fuenteProyecto == null ? null : fuenteProyecto.getCategoriaFuenteProyecto();

        PerfilAdministrador administradorRegistro = proyecto.getAdministradorRegistro();

        Usuario usuarioAdministrador =
                administradorRegistro == null ? null : administradorRegistro.getUsuario();

        return new ProyectoResponse(
                proyecto.getIdProyecto(),

                fuenteProyecto == null ? null : fuenteProyecto.getId(),
                fuenteProyecto == null ? null : fuenteProyecto.getNombre(),

                categoriaFuenteProyecto == null ? null : categoriaFuenteProyecto.getId(),
                categoriaFuenteProyecto == null ? null : categoriaFuenteProyecto.getNombre(),

                administradorRegistro == null ? null : administradorRegistro.getIdUsuario(),
                usuarioAdministrador == null ? null : usuarioAdministrador.getNombreCompleto(),

                proyecto.getNombre(),
                proyecto.getDescripcion(),

                proyecto.getFechaRegistro(),
                proyecto.getFechaInicio(),
                proyecto.getFechaFin(),

                proyecto.getEstado(),

                proyecto.getCreadoEn(),
                proyecto.getActualizadoEn()
        );
    }
}