package ni.edu.uam.innovacion.modules.catalog.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;

/**
 * Entidad que representa el catálogo de roles dentro de un proyecto.
 *
 * Este catálogo permite definir qué función cumple un usuario
 * dentro de un proyecto, por ejemplo:
 * líder, integrante, desarrollador, diseñador, mentor de apoyo, entre otros.
 *
 * Se utilizará posteriormente en la entidad IntegranteProyecto
 * mediante la relación con id_rol_proyecto.
 */
@Entity
@Table(
        name = "roles_proyecto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_roles_proyecto_nombre",
                        columnNames = "nombre"
                )
        }
)
@AttributeOverrides({
        @AttributeOverride(
                name = "id",
                column = @Column(name = "id_rol_proyecto")
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
public class RolProyecto extends BaseCatalog {

    public RolProyecto() {
    }

    /**
     * Constructor útil para crear un rol de proyecto con nombre y descripción.
     *
     * El estado no se recibe aquí porque BaseCatalog lo asigna
     * automáticamente como ACTIVO por defecto.
     */
    public RolProyecto(String nombre, String descripcion) {
        super(nombre, descripcion);
    }
}