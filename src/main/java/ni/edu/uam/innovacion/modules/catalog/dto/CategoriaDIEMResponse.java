package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * DTO usado para devolver información de una categoría DIEM.
 */
public class CategoriaDIEMResponse {


    private Long id;


    private String nombre;

    private String descripcion;


    private String criteriosPuntuacion;


    private EstadoRegistro estado;


    private Long idAmbitoActividad;


    private String nombreAmbitoActividad;


    private Boolean requiereCategoriaAmbito;

    public CategoriaDIEMResponse() {
    }

    public CategoriaDIEMResponse(
            Long id,
            String nombre,
            String descripcion,
            String criteriosPuntuacion,
            EstadoRegistro estado,
            Long idAmbitoActividad,
            String nombreAmbitoActividad,
            Boolean requiereCategoriaAmbito
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.criteriosPuntuacion = criteriosPuntuacion;
        this.estado = estado;
        this.idAmbitoActividad = idAmbitoActividad;
        this.nombreAmbitoActividad = nombreAmbitoActividad;
        this.requiereCategoriaAmbito = requiereCategoriaAmbito;
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

    public String getCriteriosPuntuacion() {
        return criteriosPuntuacion;
    }

    public EstadoRegistro getEstado() {
        return estado;
    }

    public Long getIdAmbitoActividad() {
        return idAmbitoActividad;
    }

    public String getNombreAmbitoActividad() {
        return nombreAmbitoActividad;
    }

    public Boolean getRequiereCategoriaAmbito() {
        return requiereCategoriaAmbito;
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

    public void setCriteriosPuntuacion(String criteriosPuntuacion) {
        this.criteriosPuntuacion = criteriosPuntuacion;
    }

    public void setEstado(EstadoRegistro estado) {
        this.estado = estado;
    }

    public void setIdAmbitoActividad(Long idAmbitoActividad) {
        this.idAmbitoActividad = idAmbitoActividad;
    }

    public void setNombreAmbitoActividad(String nombreAmbitoActividad) {
        this.nombreAmbitoActividad = nombreAmbitoActividad;
    }

    public void setRequiereCategoriaAmbito(Boolean requiereCategoriaAmbito) {
        this.requiereCategoriaAmbito = requiereCategoriaAmbito;
    }
}