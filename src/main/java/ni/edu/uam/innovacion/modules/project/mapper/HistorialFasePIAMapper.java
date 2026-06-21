package ni.edu.uam.innovacion.modules.project.mapper;

import ni.edu.uam.innovacion.modules.project.dto.ActualizarHistorialFasePIARequest;
import ni.edu.uam.innovacion.modules.project.dto.CrearHistorialFasePIARequest;
import ni.edu.uam.innovacion.modules.project.dto.HistorialFasePIAResponse;
import ni.edu.uam.innovacion.modules.project.entity.HistorialFasePIA;
import ni.edu.uam.innovacion.modules.project.entity.Proyecto;
import ni.edu.uam.innovacion.modules.project.entity.ProyectoPIA;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

public class HistorialFasePIAMapper {

    private HistorialFasePIAMapper() {
    }

    public static HistorialFasePIA toEntity(
            CrearHistorialFasePIARequest request,
            ProyectoPIA proyectoPIA,
            PerfilAdministrador registradoPorAdmin
    ) {
        HistorialFasePIA historial = new HistorialFasePIA();

        historial.setProyectoPIA(proyectoPIA);
        historial.setFase(request.fase());
        historial.setFechaInicio(request.fechaInicio());
        historial.setFechaFin(request.fechaFin());
        historial.setRegistradoPorAdmin(registradoPorAdmin);
        historial.setObservaciones(request.observaciones());

        return historial;
    }

    public static void updateEntity(
            HistorialFasePIA historial,
            ActualizarHistorialFasePIARequest request
    ) {
        historial.setFase(request.fase());
        historial.setFechaInicio(request.fechaInicio());
        historial.setFechaFin(request.fechaFin());
        historial.setObservaciones(request.observaciones());
    }

    public static HistorialFasePIAResponse toResponse(HistorialFasePIA historial) {
        ProyectoPIA proyectoPIA = historial.getProyectoPIA();
        Proyecto proyecto = proyectoPIA == null ? null : proyectoPIA.getProyecto();

        PerfilAdministrador administrador = historial.getRegistradoPorAdmin();
        Usuario usuarioAdministrador = administrador == null ? null : administrador.getUsuario();

        return new HistorialFasePIAResponse(
                historial.getIdHistorialFase(),

                proyectoPIA == null ? null : proyectoPIA.getIdProyectoPIA(),

                proyecto == null ? null : proyecto.getIdProyecto(),
                proyecto == null ? null : proyecto.getNombre(),

                historial.getFase(),
                historial.getFechaInicio(),
                historial.getFechaFin(),
                historial.faseEstaVigente(),

                administrador == null ? null : administrador.getIdUsuario(),
                usuarioAdministrador == null ? null : usuarioAdministrador.getNombreCompleto(),

                historial.getObservaciones(),
                historial.getCreadoEn()
        );
    }
}