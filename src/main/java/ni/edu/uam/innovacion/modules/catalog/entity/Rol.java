package ni.edu.uam.innovacion.modules.catalog.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import ni.edu.uam.innovacion.common.entity.BaseCatalog;

/**
 * Entidad que representa el catálogo de roles del sistema.
 *
 * Un rol define el tipo de participación o nivel de acceso que puede tener
 * un usuario dentro del sistema.
 */
@Entity

//unique key
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_roles_nombre", columnNames = "nombre")
        }
)
@AttributeOverrides({
        @AttributeOverride(
                name = "id",
                column = @Column(name = "id_rol")
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
public class Rol extends BaseCatalog {


    public Rol() {
    }

    /**
     * Constructor útil para crear un rol con nombre y descripción.
     *
     * El estado no se recibe aquí porque BaseCatalog lo asigna
     * automáticamente como ACTIVO por defecto.
     */
    public Rol(String nombre, String descripcion) {
        super(nombre, descripcion);
    }
}