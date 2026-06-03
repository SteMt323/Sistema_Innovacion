package ni.edu.uam.innovacion.modules.catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;

/*
 * Entidad que representa el catálogo de ámbitos de actividad.
 *
 * Un ámbito indica el origen o contexto general de una actividad.
 *
 * En este sistema, por ahora se manejarán principalmente dos ámbitos:
 *
 * 1. DIEM:
 *    Actividades organizadas directamente por la Dirección de Innovación
 *    y Emprendimiento. Este ámbito sí requiere una categoría específica,
 *    por ejemplo: Evento, Concurso, Formación o Proyecto.
 *
 * 2. Externa:
 *    Actividades externas relacionadas con innovación o emprendimiento.
 *    Este ámbito no requiere categoría DIEM, porque toda actividad externa
 *    se clasifica directamente como externa.
 */
@Entity
@Table(
        name = "ambitos_actividad",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_ambitos_actividad_nombre", columnNames = "nombre")
        }
)
@AttributeOverrides({

        @AttributeOverride(
                name = "id",
                column = @Column(name = "id_ambito_actividad")
        ),

        @AttributeOverride(
                name = "nombre",
                column = @Column(name = "nombre", nullable = false, length = 50)
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
public class AmbitoActividad extends BaseCatalog {

    /*
     * Indica si el ámbito necesita una categoría adicional.
     *
     * Ejemplo:
     *
     * DIEM:
     * requiereCategoria = true
     * porque debe clasificarse como Evento, Concurso, Formación o Proyecto.
     *
     * EXTERNA:
     * requiereCategoria = false
     * porque no necesita categoría DIEM.
     */
    @NotNull(message = "Debe indicar si el ámbito requiere categoría")
    @Column(name = "requiere_categoria", nullable = false)
    private Boolean requiereCategoria = Boolean.FALSE;

    public AmbitoActividad() {
    }

    public AmbitoActividad(String nombre, String descripcion, Boolean requiereCategoria) {
        super(nombre, descripcion);
        this.requiereCategoria = requiereCategoria;
    }

    public Boolean getRequiereCategoria() {
        return requiereCategoria;
    }

    public void setRequiereCategoria(Boolean requiereCategoria) {
        this.requiereCategoria = requiereCategoria;
    }
}