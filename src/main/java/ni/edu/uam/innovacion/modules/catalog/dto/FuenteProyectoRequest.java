package ni.edu.uam.innovacion.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios
 * al crear o actualizar una fuente de proyecto.
 *
 * La fuente de proyecto debe estar asociada a una categoría
 * registrada en el catálogo CategoriaFuenteProyecto.
 */
public class FuenteProyectoRequest {

    @NotBlank(message = "El nombre de la fuente de proyecto es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    @NotNull(message = "La categoría de fuente de proyecto es obligatoria")
    private Long idCategoriaFuenteProyecto;

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

    public Long getIdCategoriaFuenteProyecto() {
        return idCategoriaFuenteProyecto;
    }

    public void setIdCategoriaFuenteProyecto(Long idCategoriaFuenteProyecto) {
        this.idCategoriaFuenteProyecto = idCategoriaFuenteProyecto;
    }
}