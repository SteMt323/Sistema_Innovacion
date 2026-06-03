package ni.edu.uam.innovacion.modules.catalog.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;
import ni.edu.uam.innovacion.common.entity.BaseModel;

@Entity
@Table(
        name = "carreras",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_carreras_codigo", columnNames = "codigo"),
                @UniqueConstraint(name = "uk_carreras_nombre_facultad", columnNames = {"nombre", "id_facultad"})
        }
)

@AttributeOverrides({

        @AttributeOverride(
                name = "id",
                column = @Column(name = "id_carrera")
        ),

        @AttributeOverride(
                name = "nombre",
                column = @Column(name = "nombre", nullable = false, length = 120)
        ),


        @AttributeOverride(
                name = "descripcion",
                column = @Column(name = "descripcion", length = 255)
        ),

        @AttributeOverride(
                name = "estado",
                column = @Column(name = "estado", nullable = false, length = 20)
        )
})
public class Carrera extends BaseCatalog {

    @NotBlank(message = "El código de la carrera es obligatorio")
    @Size(max = 30, message = "El código de la carrera no puede superar los 30 caracteres")
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;


    /*
     * Facultad a la que pertenece la carrera.
     *
     * Relación:
     * Muchas carreras pueden pertenecer a una misma facultad.
     *
     * fetch = FetchType.LAZY significa que la facultad no se carga
     * automáticamente hasta que sea necesaria.
     */

    @NotNull(message = "La facultad de la carrera es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facultad", nullable = false)
    private Facultad facultad;

    public Carrera() {
    }

    public Carrera(String nombre, String descripcion, String codigo, Facultad facultad) {
        super(nombre, descripcion);
        setCodigo(codigo);
        this.facultad = facultad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo != null) {
            this.codigo = codigo.trim().toUpperCase();
        } else {
            this.codigo = null;
        }
    }
    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }

}
