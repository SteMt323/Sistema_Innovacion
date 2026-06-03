package ni.edu.uam.innovacion.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.common.enums.CategoriaFuenteProyecto;

/**
 * DTO usado para crear o actualizar una fuente de proyecto.
 */
public class FuenteProyectoRequest {

    /**
     * Nombre de la fuente del proyecto.
     */
    @NotBlank(message = "El nombre de la fuente del proyecto es obligatorio")
    @Size(max = 120, message = "El nombre de la fuente del proyecto no puede superar los 120 caracteres")
    private String nombre;

    /**
     * Descripción general de la fuente del proyecto.
     */
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    /**
     * Categoría general de la fuente del proyecto.
     */
    @NotNull(message = "La categoría de la fuente del proyecto es obligatoria")
    private CategoriaFuenteProyecto categoria;

    public FuenteProyectoRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CategoriaFuenteProyecto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaFuenteProyecto categoria) {
        this.categoria = categoria;
    }
}