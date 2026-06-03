package ni.edu.uam.innovacion.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para crear o actualizar un rol de participación.
 */
public class RolParticipacionRequest {

    /**
     * Nombre del rol de participación.
     */
    @NotBlank(message = "El nombre del rol de participación es obligatorio")
    @Size(max = 80, message = "El nombre del rol de participación no puede superar los 80 caracteres")
    private String nombre;

    /**
     * Descripción del rol de participación.
     */
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    public RolParticipacionRequest() {
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
}