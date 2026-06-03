package ni.edu.uam.innovacion.modules.catalog.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;
import ni.edu.uam.innovacion.common.enums.CategoriaFuenteProyecto;

/*
 * Entidad que representa el catálogo de fuentes de proyecto.
 *
 * Una fuente de proyecto indica de dónde nace o de dónde proviene
 * un proyecto registrado en el sistema.
 *
 * Ejemplos:
 * - Programa PIA
 * - Hackathon Nicaragua
 * - Rally Nacional de Innovación
 * - Rally Latinoamericano de Innovación
 * - Actividad externa
 * - Otro
 *
 * Este catálogo es importante porque permite clasificar los proyectos
 * según su origen, lo cual ayuda a generar reportes, estadísticas
 * y trazabilidad institucional.
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

    /*
     * Categoría general de la fuente del proyecto.
     *
     * Este campo permite agrupar las fuentes según su naturaleza.
     *
     * Ejemplos:
     *
     * PROGRAMA_PIA:
     * Proyectos que nacen desde el Programa de Pre-Incubación,
     * Incubación y Aceleración.
     *
     * CONCURSO:
     * Proyectos que surgen desde competencias como hackathones
     * o rallies.
     *
     * ACTIVIDAD_INNOVACION:
     * Proyectos que nacen desde talleres, cursos, mentorías,
     * diplomados u otras actividades internas de innovación.
     *
     * EXTERNO:
     * Proyectos registrados desde espacios externos a la DIEM.
     *
     * OTRO:
     * Casos que no encajan directamente en las categorías anteriores.
     *
     * EnumType.STRING hace que en la base de datos se guarde el nombre
     * del enum, por ejemplo "PROGRAMA_PIA", en lugar de un número.
     */
    @NotNull(message = "La categoría de la fuente del proyecto es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 40)
    private CategoriaFuenteProyecto categoria;

    public FuenteProyecto() {
    }

    public FuenteProyecto(
            String nombre,
            String descripcion,
            CategoriaFuenteProyecto categoria
    ) {
        super(nombre, descripcion);
        this.categoria = categoria;
    }

    public CategoriaFuenteProyecto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaFuenteProyecto categoria) {
        this.categoria = categoria;
    }
}