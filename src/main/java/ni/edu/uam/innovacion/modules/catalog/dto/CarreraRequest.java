package ni.edu.uam.innovacion.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para crear o actualizar una carrera.
 */
public class CarreraRequest {


    @NotBlank(message = "El nombre de la carrera es obligatorio")
    @Size(max = 120, message = "El nombre de la carrera no puede superar los 120 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;


    @NotBlank(message = "El código de la carrera es obligatorio")
    @Size(max = 30, message = "El código de la carrera no puede superar los 30 caracteres")
    private String codigo;

    /**
     * Identificador de la facultad a la que pertenece la carrera.
     *
     * No se recibe el objeto Facultad completo, solo su id.
     * Luego el Service se encarga de buscar la facultad real
     * en la base de datos.
     */
    @NotNull(message = "La facultad de la carrera es obligatoria")
    @Positive(message = "El id de la facultad debe ser un número positivo")
    private Long idFacultad;

    public CarreraRequest() {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(Long idFacultad) {
        this.idFacultad = idFacultad;
    }
}