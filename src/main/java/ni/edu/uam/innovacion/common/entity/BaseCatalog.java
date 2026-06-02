package ni.edu.uam.innovacion.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;

/**
 * Clase base para los catálogos del sistema.
 *
 * Un catálogo es una tabla de datos reutilizables, como facultades,
 * carreras, roles, categorías o fuentes de proyecto.
 *
 * Esta clase evita repetir atributos comunes en cada catálogo.
 *
 * No representa una tabla propia, por eso usa @MappedSuperclass.
 * Sus atributos serán heredados por las entidades hijas.
 */

@MappedSuperclass
public abstract class BaseCatalog extends BaseModel {

    @NotBlank(message = "El nombre del catálogo es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Size(max = 400, message = "La descripción no puede superar los 400 caracteres")
    @Column(name = "descripcion", length = 255)
    private String descripcion;


    /**
     * Estado general del registro.
     *
     * Permite saber si el dato está activo, inactivo o archivado.
     * Esto evita eliminar físicamente información que puede servir
     * para historial o reportes.
     */

    @NotNull(message = "El estado del catálogo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoRegistro estado = EstadoRegistro.ACTIVO;

    public BaseCatalog() {
    }

    public BaseCatalog(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = EstadoRegistro.ACTIVO;
    }

    @PrePersist
    @PreUpdate
    protected void normalizarDatos() {
        if (this.nombre != null) {
            this.nombre = this.nombre.trim();
        }

        if (this.descripcion != null) {
            this.descripcion = this.descripcion.trim();
        }

        if (this.estado == null) {
            this.estado = EstadoRegistro.ACTIVO;
        }
    }

    public boolean estaActivo() {
        return EstadoRegistro.ACTIVO.equals(this.estado);
    }

    public void activar() {
        this.estado = EstadoRegistro.ACTIVO;
    }

    public void inactivar() {
        this.estado = EstadoRegistro.INACTIVO;
    }

    public void archivar() {
        this.estado = EstadoRegistro.ARCHIVADO;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoRegistro getEstado() {
        return estado;
    }

    public void setEstado(EstadoRegistro estado) {
        this.estado = estado;
    }
}