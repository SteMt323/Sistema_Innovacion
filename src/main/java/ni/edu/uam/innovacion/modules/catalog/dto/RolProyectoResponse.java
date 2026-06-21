package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * DTO utilizado para devolver la información de un rol de proyecto
 * al frontend o a Postman.
 *
 * No se devuelve directamente la entidad RolProyecto para mantener
 * mayor control sobre los datos expuestos desde la API.
 */
public class RolProyectoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private EstadoRegistro estado;

    public RolProyectoResponse() {
    }

    public RolProyectoResponse(Long id, String nombre, String descripcion, EstadoRegistro estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoRegistro getEstado() {
        return estado;
    }
}