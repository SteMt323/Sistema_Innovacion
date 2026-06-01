package ni.edu.uam.innovacion.common.entity;


//Lo que se está haciendo aquí es crear una clase padre común para todas las entidades que necesiten un identificador.
// En vez de escribir esto en todas las clases:
/**
 * Identificador único de cada registro en la base de datos.
 *
 * Se usa Long porque en la base de datos los identificadores principales
 * están definidos como bigint en la mayoría de tablas.
 *
 * Ejemplo:
 * id_usuario bigint
 * id_facultad bigint
 * id_carrera bigint
 * id_actividad bigint
 */

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

//lesta anoacion ayuda pra saber que esto no sera una tbla, sino para  saeber q estos campos se pasan a la entidad hija
@MappedSuperclass
public abstract class BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public BaseModel() {
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
}