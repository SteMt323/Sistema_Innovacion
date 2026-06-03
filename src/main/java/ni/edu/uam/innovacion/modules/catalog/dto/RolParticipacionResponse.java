package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * DTO usado para devolver información de un rol de participación.
 */
public class RolParticipacionResponse {
    private Long id;

    private String nombre;

    private String descripcion;

    private EstadoRegistro estado;

    public RolParticipacionResponse() {
    }

    public RolParticipacionResponse(
            Long id,
            String nombre,
            String descripcion,
            EstadoRegistro estado
    ) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstado(EstadoRegistro estado) {
        this.estado = estado;
    }
}