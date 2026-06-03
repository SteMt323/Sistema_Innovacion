package ni.edu.uam.innovacion.modules.catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;


@Entity
@Table(
        name = "facultades",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_facultades_nombre", columnNames = "nombre"),
                @UniqueConstraint(name = "uk_facultades_codigo", columnNames = "codigo")
        }
)
@AttributeOverrides({

        @AttributeOverride(
                name = "id",
                column = @Column(name = "id_facultad")
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
public class Facultad extends BaseCatalog {

    @NotBlank(message = "El código de la facultad es obligatorio")
    @Size(max = 30, message = "El código de la facultad no puede superar los 30 caracteres")
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    public Facultad() {}

    public Facultad(String nombre, String descripcion, String codigo) {
        super(nombre, descripcion);
        setCodigo(codigo);
    }

    /**
     * Devuelve el código de la facultad.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Asigna o modifica el código de la facultad.
     */
    public void setCodigo(String codigo) {
        if (codigo != null) {
            this.codigo = codigo.trim().toUpperCase();
        } else {
            this.codigo = null;
        }
    }

}
