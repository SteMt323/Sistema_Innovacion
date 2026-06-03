package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.CategoriaFuenteProyecto;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * DTO usado para devolver información de una fuente de proyecto.
 */
public class FuenteProyectoResponse {

    /**
     * Identificador único de la fuente de proyecto.
     */
    private Long id;

     private String nombre;

     /**
     * Descripción general de la fuente del proyecto.
     */
    private String descripcion;

    /**
     * Categoría general de la fuente del proyecto.
     */
    private CategoriaFuenteProyecto categoria;

    /**
     * Estado administrativo del registro.
     */
    private EstadoRegistro estado;

    public FuenteProyectoResponse() {
    }

    public FuenteProyectoResponse(
            Long id,
            String nombre,
            String descripcion,
            CategoriaFuenteProyecto categoria,
            EstadoRegistro estado
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
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

    public CategoriaFuenteProyecto getCategoria() {
        return categoria;
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

    public void setCategoria(CategoriaFuenteProyecto categoria) {
        this.categoria = categoria;
    }

    public void setEstado(EstadoRegistro estado) {
        this.estado = estado;
    }
}