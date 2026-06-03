package ni.edu.uam.innovacion.modules.catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Setter;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;

/*
 * Entidad que representa el catálogo de categorías DIEM.
 *
 * Este catálogo permite clasificar las actividades internas de la Dirección
 * de Innovación y Emprendimiento.
 * - Evento
 * - Concurso
 * - Formación
 * - Proyecto
 *
 */
@Entity
@Table(
        name = "categorias_diem",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_categorias_diem_nombre_ambito",
                        columnNames = {"nombre", "id_ambito_actividad"}
                )
        }
)
@AttributeOverrides({

        @AttributeOverride(
                name = "id",
                column = @Column(name = "id_categoria_diem")
        ),

        @AttributeOverride(
                name = "nombre",
                column = @Column(name = "nombre", nullable = false, length = 80)
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
public class CategoriaDIEM extends BaseCatalog {

    /*
     * Criterios de puntuación asociados a la categoría.
     * Ejemplo:
     * Una categoría "Concurso" puede tener criterios distintos
     * a una categoría "Formación".
     */
    @Size(max = 500, message = "Los criterios de puntuación no pueden superar los 500 caracteres")
    @Column(name = "criterios_puntuacion", length = 500)
    private String criteriosPuntuacion;

    /*
     * Ámbito al que pertenece esta categoría.
     */
    @Setter
    @NotNull(message = "El ámbito de actividad de la categoría es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ambito_actividad", nullable = false)
    private AmbitoActividad ambitoActividad;

    public CategoriaDIEM() {
    }

    public CategoriaDIEM(
            String nombre,
            String descripcion,
            String criteriosPuntuacion,
            AmbitoActividad ambitoActividad
    ) {
        super(nombre, descripcion);
        setCriteriosPuntuacion(criteriosPuntuacion);
        this.ambitoActividad = ambitoActividad;
    }

    public String getCriteriosPuntuacion() {
        return criteriosPuntuacion;
    }

    public void setCriteriosPuntuacion(String criteriosPuntuacion) {
        if (criteriosPuntuacion != null) {
            this.criteriosPuntuacion = criteriosPuntuacion.trim();
        } else {
            this.criteriosPuntuacion = null;
        }
    }

    public AmbitoActividad getAmbitoActividad() {
        return ambitoActividad;
    }

    public void setAmbitoActividad(AmbitoActividad ambitoActividad) {
        this.ambitoActividad = ambitoActividad;
    }

}