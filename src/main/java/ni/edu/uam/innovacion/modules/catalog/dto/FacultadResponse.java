package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

public class FacultadResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private String codigo;
    private EstadoRegistro estado;

    public FacultadResponse() {
    }

    public FacultadResponse(Long id, String nombre, String descripcion, String codigo, EstadoRegistro estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codigo = codigo;
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

    public String getCodigo() {
        return codigo;
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

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setEstado(EstadoRegistro estado) {
        this.estado = estado;
    }




}

