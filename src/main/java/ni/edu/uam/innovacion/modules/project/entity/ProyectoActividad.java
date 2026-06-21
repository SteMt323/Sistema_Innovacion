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
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.project.enums.TipoVinculoProyectoActividad;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;

/**
 * Entidad que representa la relación entre un proyecto
 * y una actividad registrada en el sistema.
 *
 * Esta entidad permite saber si un proyecto nació, fue presentado,
 * recibió seguimiento, participó en un concurso, recibió formación
 * o tuvo mentoría dentro de una actividad específica.
 *
 * Reglas importantes que luego deben reforzarse desde el service:
 * - Un mismo proyecto no debe vincularse dos veces con la misma actividad.
 * - Un proyecto no debería tener más de una actividad marcada como origen.
 * - No se deberían agregar vínculos a proyectos archivados.
 * - La relación debe ser registrada por un administrador.
 */
@Entity
@Table(
        name = "proyecto_actividades",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_proyecto_actividades_proyecto_actividad",
                        columnNames = {"id_proyecto", "id_actividad"}
                )
        },
        indexes = {
                @Index(name = "idx_proyecto_actividades_id_proyecto", columnList = "id_proyecto"),
                @Index(name = "idx_proyecto_actividades_id_actividad", columnList = "id_actividad"),
                @Index(name = "idx_proyecto_actividades_tipo_vinculo", columnList = "tipo_vinculo"),
                @Index(name = "idx_proyecto_actividades_actividad_origen", columnList = "es_actividad_origen"),
                @Index(name = "idx_proyecto_actividades_admin_registro", columnList = "registrado_por_admin_id")
        }
)
public class ProyectoActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto_actividad")
    private Long idProyectoActividad;

    /**
     * Proyecto que será vinculado con una actividad.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    /**
     * Actividad relacionada con el proyecto.
     *
     * Ejemplos:
     * - Hackathon Nicaragua
     * - Programa PIA
     * - Rally Nacional de Innovación
     * - Taller de validación
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;

    /**
     * Tipo de relación entre el proyecto y la actividad.
     */
    @Convert(converter = TipoVinculoProyectoActividadConverter.class)
    @Column(name = "tipo_vinculo", nullable = false, length = 30)
    private TipoVinculoProyectoActividad tipoVinculo;

    /**
     * Indica si esta actividad fue el origen del proyecto.
     *
     * Ejemplo:
     * Si un proyecto nació dentro del Hackathon Nicaragua,
     * ese vínculo puede marcarse como actividad origen.
     */
    @Column(name = "es_actividad_origen", nullable = false)
    private Boolean esActividadOrigen = Boolean.FALSE;

    @Column(name = "fecha_vinculacion", nullable = false)
    private LocalDateTime fechaVinculacion;

    /**
     * Administrador que registró la vinculación entre el proyecto
     * y la actividad.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrado_por_admin_id", nullable = false)
    private PerfilAdministrador registradoPorAdmin;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @PrePersist
    void prePersist() {
        if (fechaVinculacion == null) {
            fechaVinculacion = LocalDateTime.now();
        }

        if (esActividadOrigen == null) {
            esActividadOrigen = Boolean.FALSE;
        }

        normalizarTexto();
    }

    @PreUpdate
    void preUpdate() {
        if (esActividadOrigen == null) {
            esActividadOrigen = Boolean.FALSE;
        }

        normalizarTexto();
    }

    private void normalizarTexto() {
        observaciones = limpiar(observaciones);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public boolean esActividadOrigen() {
        return Boolean.TRUE.equals(esActividadOrigen);
    }

    public Long getIdProyectoActividad() {
        return idProyectoActividad;
    }

    public void setIdProyectoActividad(Long idProyectoActividad) {
        this.idProyectoActividad = idProyectoActividad;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public TipoVinculoProyectoActividad getTipoVinculo() {
        return tipoVinculo;
    }

    public void setTipoVinculo(TipoVinculoProyectoActividad tipoVinculo) {
        this.tipoVinculo = tipoVinculo;
    }

    public Boolean getEsActividadOrigen() {
        return esActividadOrigen;
    }

    public void setEsActividadOrigen(Boolean esActividadOrigen) {
        this.esActividadOrigen = esActividadOrigen;
    }

    public LocalDateTime getFechaVinculacion() {
        return fechaVinculacion;
    }

    public void setFechaVinculacion(LocalDateTime fechaVinculacion) {
        this.fechaVinculacion = fechaVinculacion;
    }

    public PerfilAdministrador getRegistradoPorAdmin() {
        return registradoPorAdmin;
    }

    public void setRegistradoPorAdmin(PerfilAdministrador registradoPorAdmin) {
        this.registradoPorAdmin = registradoPorAdmin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = limpiar(observaciones);
    }
}