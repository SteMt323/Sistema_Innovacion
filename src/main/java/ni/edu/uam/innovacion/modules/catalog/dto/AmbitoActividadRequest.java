package ni.edu.uam.innovacion.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class AmbitoActividadRequest {


    @NotBlank(message = "El nombre del ámbito de actividad es obligatorio")
    @Size(max = 50, message = "El nombre del ámbito de actividad no puede superar los 50 caracteres")
    private String nombre;


    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;


    @NotNull(message = "Debe indicar si el ámbito requiere categoría")
    private Boolean requiereCategoria;

    public AmbitoActividadRequest() {
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

    public Boolean getRequiereCategoria() {
        return requiereCategoria;
    }

    public void setRequiereCategoria(Boolean requiereCategoria) {
        this.requiereCategoria = requiereCategoria;
    }
}