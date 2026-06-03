package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * DTO usado para devolver información de una carrera.
 */
public class CarreraResponse {


    private Long id;
    private String nombre;
    private String descripcion;
    private String codigo;
    private EstadoRegistro estado;
    private Long idFacultad;
    private String nombreFacultad;
    private String codigoFacultad;
    public CarreraResponse() {
    }

    public CarreraResponse(
            Long id,
            String nombre,
            String descripcion,
            String codigo,
            EstadoRegistro estado,
            Long idFacultad,
            String nombreFacultad,
            String codigoFacultad
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.estado = estado;
        this.idFacultad = idFacultad;
        this.nombreFacultad = nombreFacultad;
        this.codigoFacultad = codigoFacultad;
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

    public Long getIdFacultad() {
        return idFacultad;
    }

    public String getNombreFacultad() {
        return nombreFacultad;
    }

    public String getCodigoFacultad() {
        return codigoFacultad;
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

    public void setIdFacultad(Long idFacultad) {
        this.idFacultad = idFacultad;
    }

    public void setNombreFacultad(String nombreFacultad) {
        this.nombreFacultad = nombreFacultad;
    }

    public void setCodigoFacultad(String codigoFacultad) {
        this.codigoFacultad = codigoFacultad;
    }
}