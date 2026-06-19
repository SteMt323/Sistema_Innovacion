package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * DTO utilizado para devolver al cliente la información
 * de una fuente de proyecto.
 *
 * Incluye los datos básicos de la fuente y la categoría
 * a la que pertenece.
 */
public class FuenteProyectoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private EstadoRegistro estado;

    private Long idCategoriaFuenteProyecto;
    private String nombreCategoriaFuenteProyecto;

    public FuenteProyectoResponse() {
    }

    public FuenteProyectoResponse(
            Long id,
            String nombre,
            String descripcion,
            EstadoRegistro estado,
            Long idCategoriaFuenteProyecto,
            String nombreCategoriaFuenteProyecto
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idCategoriaFuenteProyecto = idCategoriaFuenteProyecto;
        this.nombreCategoriaFuenteProyecto = nombreCategoriaFuenteProyecto;
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

    public Long getIdCategoriaFuenteProyecto() {
        return idCategoriaFuenteProyecto;
    }

    public String getNombreCategoriaFuenteProyecto() {
        return nombreCategoriaFuenteProyecto;
    }
}