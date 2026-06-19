package ni.edu.uam.innovacion.modules.catalog.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;

/**
 * Entidad que representa el catálogo de categorías de fuente de proyecto.
 *
 * Este catálogo permite clasificar el origen o tipo general de una fuente
 * relacionada con un proyecto de innovación o emprendimiento.
 *
 * Ejemplos de categorías:
 * - PROGRAMA_PIA
 * - CONCURSO
 * - ACTIVIDAD_INNOVACION
 * - EXTERNO
 * - OTRO
 *
 * Su objetivo es organizar mejor los proyectos dentro del sistema,
 * facilitar los filtros administrativos y apoyar la generación de reportes.
 */
@Entity
@Table(
        name = "categorias_fuente_proyecto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_categorias_fuente_proyecto_nombre",
                        columnNames = "nombre"
                )
        }
)
@AttributeOverrides({

        @AttributeOverride(
                name = "id",
                column = @Column(name = "id_categoria_fuente_proyecto")
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
public class CategoriaFuenteProyecto extends BaseCatalog {

    public CategoriaFuenteProyecto() {
    }

    public CategoriaFuenteProyecto(String nombre, String descripcion) {
        super(nombre, descripcion);
    }
}