package ni.edu.uam.innovacion.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos al crear o actualizar
 * un rol dentro de un proyecto.
 *
 * Un rol de proyecto representa la función que cumple un usuario
 * dentro de un proyecto, por ejemplo:
 * líder, integrante, desarrollador, diseñador, investigador, entre otros.
 */
public class RolProyectoRequest {

    @NotBlank(message = "El nombre del rol de proyecto es obligatorio")
    @Size(max = 80, message = "El nombre del rol de proyecto no puede superar los 80 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    public RolProyectoRequest() {
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