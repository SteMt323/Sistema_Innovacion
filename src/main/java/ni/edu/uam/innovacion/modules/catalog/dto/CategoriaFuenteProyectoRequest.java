package ni.edu.uam.innovacion.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios
 * al crear o actualizar una categoría de fuente de proyecto.
 *
 * Este catálogo permite clasificar el origen general
 * de un proyecto dentro del sistema.
 *
 * Ejemplos:
 * - PROGRAMA_PIA
 * - CONCURSO
 * - ACTIVIDAD_INNOVACION
 * - EXTERNO
 * - OTRO
 */
public class CategoriaFuenteProyectoRequest {

    @NotBlank(message = "El nombre de la categoría de fuente de proyecto es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    public CategoriaFuenteProyectoRequest() {
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