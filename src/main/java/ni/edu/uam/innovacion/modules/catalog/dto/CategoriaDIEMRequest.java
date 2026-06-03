package ni.edu.uam.innovacion.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para crear o actualizar una categoría DIEM.
 */
public class CategoriaDIEMRequest {


    @NotBlank(message = "El nombre de la categoría DIEM es obligatorio")
    @Size(max = 80, message = "El nombre de la categoría DIEM no puede superar los 80 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;


    @Size(max = 500, message = "Los criterios de puntuación no pueden superar los 500 caracteres")
    private String criteriosPuntuacion;

    @NotNull(message = "El ámbito de actividad de la categoría es obligatorio")
    @Positive(message = "El id del ámbito de actividad debe ser un número positivo")
    private Long idAmbitoActividad;

    public CategoriaDIEMRequest() {
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

    public String getCriteriosPuntuacion() {
        return criteriosPuntuacion;
    }

    public void setCriteriosPuntuacion(String criteriosPuntuacion) {
        this.criteriosPuntuacion = criteriosPuntuacion;
    }

    public Long getIdAmbitoActividad() {
        return idAmbitoActividad;
    }

    public void setIdAmbitoActividad(Long idAmbitoActividad) {
        this.idAmbitoActividad = idAmbitoActividad;
    }
}