package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * DTO usado para devolver información de un ámbito de actividad.
 */
public class AmbitoActividadResponse {


    private Long id;


    private String nombre;


    private String descripcion;


    private Boolean requiereCategoria;


    private EstadoRegistro estado;

    public AmbitoActividadResponse() {
    }

    public AmbitoActividadResponse(
            Long id,
            String nombre,
            String descripcion,
            Boolean requiereCategoria,
            EstadoRegistro estado
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.requiereCategoria = requiereCategoria;
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

    public Boolean getRequiereCategoria() {
        return requiereCategoria;
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

    public void setRequiereCategoria(Boolean requiereCategoria) {
        this.requiereCategoria = requiereCategoria;
    }

    public void setEstado(EstadoRegistro estado) {
        this.estado = estado;
    }
}