package ni.edu.uam.innovacion.modules.catalog.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;

/*
 * Entidad que representa el catálogo de roles de participación.
 *
 * Este catálogo define el papel que un usuario puede cumplir dentro
 * de una actividad específica.
 * Ejemplos:
 * - Participante
 * - Líder de equipo
 * - Mentor
 * - Jurado
 * - Expositor
 * - Facilitador
 * - Organizador
 */
@Entity
@Table(
        name = "roles_participacion",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_roles_participacion_nombre",
                        columnNames = "nombre"
                )
        }
)
@AttributeOverrides({

        @AttributeOverride(
                name = "id",
                column = @Column(name = "id_rol_participacion")
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
public class RolParticipacion extends BaseCatalog {

    public RolParticipacion() {
    }

    public RolParticipacion(String nombre, String descripcion) {
        super(nombre, descripcion);
    }
}