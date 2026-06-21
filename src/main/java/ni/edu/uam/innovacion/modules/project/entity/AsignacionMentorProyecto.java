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
import ni.edu.uam.innovacion.modules.project.enums.EstadoAsignacion;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;

import java.time.LocalDateTime;

/**
 * Entidad que representa la asignación de un mentor a un proyecto.
 *
 * Esta tabla permite registrar qué mentor acompaña a un proyecto,
 * cuándo fue asignado, cuál es el estado de esa asignación y qué
 * administrador realizó el registro.
 *
 * Es útil para dar seguimiento al acompañamiento de proyectos dentro
 * del Programa PIA, hackathones, rallies, semilleros u otras actividades
 * de innovación y emprendimiento.
 */
@Entity
@Table(
        name = "asignaciones_mentor_proyecto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_asignaciones_mentor_proyecto_proyecto_mentor",
                        columnNames = {"id_proyecto", "id_mentor"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_asignaciones_mentor_proyecto",
                        columnList = "id_proyecto"
                ),
                @Index(
                        name = "idx_asignaciones_mentor_mentor",
                        columnList = "id_mentor"
                ),
                @Index(
                        name = "idx_asignaciones_mentor_admin",
                        columnList = "id_admin_registro"
                ),
                @Index(
                        name = "idx_asignaciones_mentor_estado",
                        columnList = "estado"
                )
        }
)
public class AsignacionMentorProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion_mentor")
    private Long idAsignacionMentor;

    /**
     * Proyecto que recibirá el acompañamiento del mentor.
     *
     * Relación:
     * Proyecto 1 ---- N AsignacionMentorProyecto
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    /**
     * Mentor asignado al proyecto.
     *
     * Se relaciona con perfiles_mentor, cuyo identificador también
     * corresponde al id_usuario del mentor.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_mentor", nullable = false)
    private PerfilMentor mentor;

    /**
     * Administrador que registró la asignación del mentor.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_admin_registro", nullable = false)
    private PerfilAdministrador administradorRegistro;

    /**
     * Fecha y hora en que el mentor fue asignado al proyecto.
     */
    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    /**
     * Estado de la asignación.
     *
     * Puede ser:
     * - activa
     * - finalizada
     * - cancelada
     */
    @Convert(converter = EstadoAsignacionConverter.class)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoAsignacion estado = EstadoAsignacion.ACTIVA;

    /**
     * Observaciones administrativas sobre la asignación.
     */
    @Column(name = "observaciones", columnDefinition = "text")
    private String observaciones;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    public AsignacionMentorProyecto() {
    }

    @PrePersist
    protected void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();

        if (this.fechaAsignacion == null) {
            this.fechaAsignacion = ahora;
        }

        if (this.estado == null) {
            this.estado = EstadoAsignacion.ACTIVA;
        }

        if (this.creadoEn == null) {
            this.creadoEn = ahora;
        }

        normalizarDatos();
    }

    @PreUpdate
    protected void preUpdate() {
        this.actualizadoEn = LocalDateTime.now();
        normalizarDatos();
    }

    private void normalizarDatos() {
        if (this.observaciones != null) {
            this.observaciones = this.observaciones.trim();
        }
    }

    public boolean estaActiva() {
        return EstadoAsignacion.ACTIVA.equals(this.estado);
    }

    public boolean estaFinalizada() {
        return EstadoAsignacion.FINALIZADA.equals(this.estado);
    }

    public boolean estaCancelada() {
        return EstadoAsignacion.CANCELADA.equals(this.estado);
    }

    public void activar() {
        this.estado = EstadoAsignacion.ACTIVA;
    }

    public void finalizar() {
        this.estado = EstadoAsignacion.FINALIZADA;
    }

    public void cancelar() {
        this.estado = EstadoAsignacion.CANCELADA;
    }

    public Long getIdAsignacionMentor() {
        return idAsignacionMentor;
    }

    public void setIdAsignacionMentor(Long idAsignacionMentor) {
        this.idAsignacionMentor = idAsignacionMentor;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public PerfilMentor getMentor() {
        return mentor;
    }

    public void setMentor(PerfilMentor mentor) {
        this.mentor = mentor;
    }

    public PerfilAdministrador getAdministradorRegistro() {
        return administradorRegistro;
    }

    public void setAdministradorRegistro(PerfilAdministrador administradorRegistro) {
        this.administradorRegistro = administradorRegistro;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public EstadoAsignacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignacion estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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