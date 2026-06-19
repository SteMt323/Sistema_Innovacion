package ni.edu.uam.innovacion.modules.catalog.dto;

import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * DTO utilizado para devolver al cliente la información
 * de una categoría de fuente de proyecto.
 *
 * Incluye el estado del registro para permitir al frontend
 * diferenciar entre categorías activas, inactivas o archivadas.
 */
public class CategoriaFuenteProyectoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private EstadoRegistro estado;

    public CategoriaFuenteProyectoResponse() {
    }

    public CategoriaFuenteProyectoResponse(
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
}