package ni.edu.uam.innovacion.modules.activity.mapper;

import ni.edu.uam.innovacion.modules.activity.dto.ActividadResponse;
import ni.edu.uam.innovacion.modules.activity.dto.ActualizarActividadRequest;
import ni.edu.uam.innovacion.modules.activity.dto.CrearActividadRequest;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaDIEM;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

public class ActividadMapper {

    private ActividadMapper() {
    }

    public static Actividad toEntity(
        CrearActividadRequest request,
        AmbitoActividad ambitoActividad,
        CategoriaDIEM categoriaDiem,
        PerfilAdministrador administradorCreador,
        Usuario responsableUsuario
    ) {
        Actividad actividad = new Actividad();
        actividad.setAdministradorCreador(administradorCreador);
        updateCommonFields(actividad, request, ambitoActividad, categoriaDiem, responsableUsuario);
        return actividad;
    }

    public static void updateEntity(
        Actividad actividad,
        ActualizarActividadRequest request,
        AmbitoActividad ambitoActividad,
        CategoriaDIEM categoriaDiem,
        Usuario responsableUsuario
    ) {
        actividad.setAmbitoActividad(ambitoActividad);
        actividad.setCategoriaDiem(categoriaDiem);
        actividad.setResponsableUsuario(responsableUsuario);
        actividad.setNombre(request.nombre());
        actividad.setDescripcion(request.descripcion());
        actividad.setFechaInicio(request.fechaInicio());
        actividad.setFechaFin(request.fechaFin());
        actividad.setModalidad(request.modalidad());
        actividad.setCupoMaximo(request.cupoMaximo());
        actividad.setUbicacion(request.ubicacion());
        actividad.setResponsableNombre(request.responsableNombre());
        actividad.setPuntosBase(puntosBaseNormalizados(request.puntosBase()));
    }

    public static ActividadResponse toResponse(Actividad actividad) {
        AmbitoActividad ambitoActividad = actividad.getAmbitoActividad();
        CategoriaDIEM categoriaDiem = actividad.getCategoriaDiem();
        PerfilAdministrador administradorCreador = actividad.getAdministradorCreador();
        Usuario administradorUsuario = administradorCreador.getUsuario();
        Usuario responsableUsuario = actividad.getResponsableUsuario();

        return new ActividadResponse(
            actividad.getIdActividad(),
            ambitoActividad.getId(),
            ambitoActividad.getNombre(),
            ambitoActividad.getRequiereCategoria(),
            categoriaDiem == null ? null : categoriaDiem.getId(),
            categoriaDiem == null ? null : categoriaDiem.getNombre(),
            administradorCreador.getIdUsuario(),
            administradorUsuario == null ? null : administradorUsuario.getNombreCompleto(),
            responsableUsuario == null ? null : responsableUsuario.getIdUsuario(),
            responsableUsuario == null ? null : responsableUsuario.getNombreCompleto(),
            actividad.getNombre(),
            actividad.getDescripcion(),
            actividad.getFechaInicio(),
            actividad.getFechaFin(),
            actividad.getModalidad(),
            actividad.getEstado(),
            actividad.getCupoMaximo(),
            actividad.getUbicacion(),
            actividad.getResponsableNombre(),
            actividad.getPuntosBase(),
            actividad.getCreadoEn(),
            actividad.getActualizadoEn()
        );
    }

    private static void updateCommonFields(
        Actividad actividad,
        CrearActividadRequest request,
        AmbitoActividad ambitoActividad,
        CategoriaDIEM categoriaDiem,
        Usuario responsableUsuario
    ) {
        actividad.setAmbitoActividad(ambitoActividad);
        actividad.setCategoriaDiem(categoriaDiem);
        actividad.setResponsableUsuario(responsableUsuario);
        actividad.setNombre(request.nombre());
        actividad.setDescripcion(request.descripcion());
        actividad.setFechaInicio(request.fechaInicio());
        actividad.setFechaFin(request.fechaFin());
        actividad.setModalidad(request.modalidad());
        actividad.setCupoMaximo(request.cupoMaximo());
        actividad.setUbicacion(request.ubicacion());
        actividad.setResponsableNombre(request.responsableNombre());
        actividad.setPuntosBase(puntosBaseNormalizados(request.puntosBase()));
    }

    private static Integer puntosBaseNormalizados(Integer puntosBase) {
        return puntosBase == null ? 0 : puntosBase;
    }
}
