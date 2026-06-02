package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * DTO usado para devolver información del rol al frontend o a Postman.
 *
 * No se devuelve directamente la entidad Rol para mantener
 * más control sobre qué datos se exponen desde la API.
 */
public class RolResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private EstadoRegistro estado;

    public RolResponse() {
    }

    public RolResponse(Long id, String nombre, String descripcion, EstadoRegistro estado) {
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