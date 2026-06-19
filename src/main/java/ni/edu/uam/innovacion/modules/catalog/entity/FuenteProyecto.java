package ni.edu.uam.innovacion.modules.catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;

/**
 * Entidad que representa el catálogo de fuentes de proyecto.
 *
 * Una fuente de proyecto indica el origen específico desde donde nace
 * o se vincula un proyecto de innovación o emprendimiento.
 *
 * Ejemplos:
 * - Programa PIA
 * - Hackathon Nicaragua
 * - Rally Nacional de Innovación
 * - Rally Latinoamericano de Innovación
 * - Convocatoria externa
 *
 * Cada fuente pertenece a una categoría de fuente de proyecto.
 */
@Entity
@Table(
        name = "fuentes_proyecto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_fuentes_proyecto_nombre",
                        columnNames = "nombre"
                )
        }
)
@AttributeOverrides({

        @AttributeOverride(
                name = "id",
                column = @Column(name = "id_fuente_proyecto")
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
public class FuenteProyecto extends BaseCatalog {

    /**
     * Categoría a la que pertenece la fuente de proyecto.
     *
     * Ejemplo:
     * Categoría: CONCURSO
     * Fuente: Hackathon Nicaragua
     *
     * Categoría: PROGRAMA_PIA
     * Fuente: Programa de Pre-Incubación
     */
    @NotNull(message = "La categoría de fuente de proyecto es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_categoria_fuente_proyecto",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_fuentes_proyecto_categoria_fuente")
    )
    private CategoriaFuenteProyecto categoriaFuenteProyecto;

    public FuenteProyecto() {
    }

    public FuenteProyecto(
            String nombre,
            String descripcion,
            CategoriaFuenteProyecto categoriaFuenteProyecto
    ) {
        super(nombre, descripcion);
        this.categoriaFuenteProyecto = categoriaFuenteProyecto;
    }

    public CategoriaFuenteProyecto getCategoriaFuenteProyecto() {
        return categoriaFuenteProyecto;
    }

    public void setCategoriaFuenteProyecto(CategoriaFuenteProyecto categoriaFuenteProyecto) {
        this.categoriaFuenteProyecto = categoriaFuenteProyecto;
    }
}