package ni.edu.uam.innovacion.modules.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.catalog.entity.FuenteProyecto;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyecto;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;

/**
 * Entidad que representa un proyecto de innovación o emprendimiento.
 *
 * Un proyecto puede originarse desde una fuente específica, por ejemplo:
 * - Programa PIA
 * - Hackathon Nicaragua
 * - Rally Nacional de Innovación
 * - Rally Latinoamericano de Innovación
 * - Actividad externa
 *
 * Esta entidad funciona como base para otros procesos del módulo project,
 * como ProyectoPIA, integrantes, actividades vinculadas y mentorías.
 */
@Entity
@Table(
        name = "proyectos",
        indexes = {
                @Index(name = "idx_proyectos_id_fuente_proyecto", columnList = "id_fuente_proyecto"),
                @Index(name = "idx_proyectos_id_admin_registro", columnList = "id_admin_registro"),
                @Index(name = "idx_proyectos_estado", columnList = "estado"),
                @Index(name = "idx_proyectos_fecha_registro", columnList = "fecha_registro")
        }
)
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Long idProyecto;

    /**
     * Fuente desde donde nace o se vincula el proyecto.
     *
     * Ejemplos:
     * - Programa PIA
     * - Hackathon Nicaragua
     * - Rally Nacional de Innovación
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_fuente_proyecto", nullable = false)
    private FuenteProyecto fuenteProyecto;

    /**
     * Administrador que registra el proyecto en el sistema.
     *
     * Se utiliza PerfilAdministrador porque solo un usuario administrador
     * debe poder registrar, actualizar o cambiar el estado de proyectos.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_admin_registro", nullable = false)
    private PerfilAdministrador administradorRegistro;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Convert(converter = EstadoProyectoConverter.class)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoProyecto estado = EstadoProyecto.ACTIVO;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @PrePersist
    void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();

        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }

        if (estado == null) {
            estado = EstadoProyecto.ACTIVO;
        }

        if (creadoEn == null) {
            creadoEn = ahora;
        }

        normalizarTexto();
    }

    @PreUpdate
    void preUpdate() {
        actualizadoEn = LocalDateTime.now();
        normalizarTexto();
    }

    private void normalizarTexto() {
        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public boolean estaActivo() {
        return EstadoProyecto.ACTIVO.equals(estado);
    }

    public boolean estaArchivado() {
        return EstadoProyecto.ARCHIVADO.equals(estado);
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public FuenteProyecto getFuenteProyecto() {
        return fuenteProyecto;
    }

    public void setFuenteProyecto(FuenteProyecto fuenteProyecto) {
        this.fuenteProyecto = fuenteProyecto;
    }

    public PerfilAdministrador getAdministradorRegistro() {
        return administradorRegistro;
    }

    public void setAdministradorRegistro(PerfilAdministrador administradorRegistro) {
        this.administradorRegistro = administradorRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = limpiar(nombre);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = limpiar(descripcion);
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(LocalDateTime actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }
}