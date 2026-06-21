package ni.edu.uam.innovacion.modules.enrollment.entity;

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
import ni.edu.uam.innovacion.modules.enrollment.enums.EstadoInscripcion;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

/**
 * Entidad que representa la inscripción de un usuario a una actividad.
 *
 * Esta tabla permite controlar qué usuarios se registraron en cada actividad,
 * evitando duplicidad de inscripciones y separando el concepto de inscripción
 * del concepto de participación validada.
 *
 * Reglas principales:
 * - Un usuario no puede inscribirse dos veces en la misma actividad.
 * - Una inscripción no significa participación real.
 * - La participación se validará posteriormente desde el módulo de participaciones.
 */
@Entity
@Table(
        name = "inscripciones",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_inscripciones_usuario_actividad",
                        columnNames = {"id_usuario", "id_actividad"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_inscripciones_usuario",
                        columnList = "id_usuario"
                ),
                @Index(
                        name = "idx_inscripciones_actividad",
                        columnList = "id_actividad"
                ),
                @Index(
                        name = "idx_inscripciones_estado",
                        columnList = "estado"
                ),
                @Index(
                        name = "idx_inscripciones_fecha_inscripcion",
                        columnList = "fecha_inscripcion"
                )
        }
)
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Long idInscripcion;

    /**
     * Usuario inscrito en la actividad.
     *
     * Puede ser estudiante, participante externo, docente, mentor
     * u otro usuario registrado en el sistema.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * Actividad a la que se inscribe el usuario.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;

    /**
     * Fecha y hora en que se realizó la inscripción.
     */
    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;

    /**
     * Estado actual de la inscripción.
     *
     * Valores:
     * - registrada
     * - pendiente
     * - confirmada
     * - cancelada
     * - rechazada
     */
    @Convert(converter = EstadoInscripcionConverter.class)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoInscripcion estado = EstadoInscripcion.REGISTRADA;

    /**
     * Observaciones adicionales sobre la inscripción.
     */
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    public Inscripcion() {
    }

    @PrePersist
    protected void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();

        if (fechaInscripcion == null) {
            fechaInscripcion = ahora;
        }

        if (estado == null) {
            estado = EstadoInscripcion.REGISTRADA;
        }

        creadoEn = ahora;
        actualizadoEn = ahora;

        normalizarDatos();
    }

    @PreUpdate
    protected void preUpdate() {
        actualizadoEn = LocalDateTime.now();
        normalizarDatos();
    }

    private void normalizarDatos() {
        observaciones = limpiar(observaciones);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public boolean estaRegistrada() {
        return EstadoInscripcion.REGISTRADA.equals(this.estado);
    }

    public boolean estaPendiente() {
        return EstadoInscripcion.PENDIENTE.equals(this.estado);
    }

    public boolean estaConfirmada() {
        return EstadoInscripcion.CONFIRMADA.equals(this.estado);
    }

    public boolean estaCancelada() {
        return EstadoInscripcion.CANCELADA.equals(this.estado);
    }

    public boolean estaRechazada() {
        return EstadoInscripcion.RECHAZADA.equals(this.estado);
    }

    public void registrar() {
        this.estado = EstadoInscripcion.REGISTRADA;
    }

    public void dejarPendiente() {
        this.estado = EstadoInscripcion.PENDIENTE;
    }

    public void confirmar() {
        this.estado = EstadoInscripcion.CONFIRMADA;
    }

    public void cancelar() {
        this.estado = EstadoInscripcion.CANCELADA;
    }

    public void rechazar() {
        this.estado = EstadoInscripcion.RECHAZADA;
    }

    public Long getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public EstadoInscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoInscripcion estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = limpiar(observaciones);
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