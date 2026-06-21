package ni.edu.uam.innovacion.modules.participation.entity;

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
import ni.edu.uam.innovacion.modules.catalog.entity.RolParticipacion;
import ni.edu.uam.innovacion.modules.enrollment.entity.Inscripcion;
import ni.edu.uam.innovacion.modules.participation.enums.EstadoParticipacion;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;

/**
 * Entidad que representa la participación real de un usuario en una actividad.
 *
 * La participación se registra a partir de una inscripción existente, pero no
 * debe confundirse con la inscripción. Una persona puede estar inscrita en una
 * actividad y aun así no haber participado realmente.
 *
 * Esta entidad permite validar asistencia, cumplimiento o involucramiento real
 * dentro de una actividad, indicando también el rol desempeñado.
 *
 * Reglas principales:
 * - Una inscripción solo debe tener una participación asociada.
 * - La participación inicia en estado pendiente.
 * - Solo una participación validada debería permitir asignar puntos.
 * - La validación debe quedar asociada a un administrador.
 */
@Entity
@Table(
        name = "participaciones",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_participaciones_inscripcion",
                        columnNames = "id_inscripcion"
                )
        },
        indexes = {
                @Index(
                        name = "idx_participaciones_inscripcion",
                        columnList = "id_inscripcion"
                ),
                @Index(
                        name = "idx_participaciones_rol",
                        columnList = "id_rol_participacion"
                ),
                @Index(
                        name = "idx_participaciones_estado",
                        columnList = "estado"
                ),
                @Index(
                        name = "idx_participaciones_admin",
                        columnList = "validado_por_admin_id"
                ),
                @Index(
                        name = "idx_participaciones_fecha_validacion",
                        columnList = "fecha_validacion"
                )
        }
)
public class Participacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participacion")
    private Long idParticipacion;

    /**
     * Inscripción desde la cual se valida la participación.
     *
     * Relación:
     * Inscripcion 1 ---- 0..1 Participacion
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_inscripcion", nullable = false)
    private Inscripcion inscripcion;

    /**
     * Rol que tuvo el usuario dentro de la actividad.
     *
     * Ejemplos:
     * - Participante
     * - Mentor
     * - Jurado
     * - Expositor
     * - Facilitador
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rol_participacion", nullable = false)
    private RolParticipacion rolParticipacion;

    /**
     * Estado de la participación.
     *
     * Valores:
     * - pendiente
     * - validada
     * - no_validada
     * - anulada
     */
    @Convert(converter = EstadoParticipacionConverter.class)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoParticipacion estado = EstadoParticipacion.PENDIENTE;

    /**
     * Fecha en que el administrador validó, no validó o anuló la participación.
     */
    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    /**
     * Administrador que realizó la validación.
     *
     * Puede ser null mientras la participación esté pendiente.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validado_por_admin_id")
    private PerfilAdministrador validadoPorAdmin;

    /**
     * Observaciones internas sobre la participación.
     */
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    public Participacion() {
    }

    @PrePersist
    protected void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();

        if (estado == null) {
            estado = EstadoParticipacion.PENDIENTE;
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

    public boolean estaPendiente() {
        return EstadoParticipacion.PENDIENTE.equals(this.estado);
    }

    public boolean estaValidada() {
        return EstadoParticipacion.VALIDADA.equals(this.estado);
    }

    public boolean estaNoValidada() {
        return EstadoParticipacion.NO_VALIDADA.equals(this.estado);
    }

    public boolean estaAnulada() {
        return EstadoParticipacion.ANULADA.equals(this.estado);
    }

    public void dejarPendiente() {
        this.estado = EstadoParticipacion.PENDIENTE;
        this.fechaValidacion = null;
        this.validadoPorAdmin = null;
    }

    public void validar(PerfilAdministrador administrador) {
        this.estado = EstadoParticipacion.VALIDADA;
        this.fechaValidacion = LocalDateTime.now();
        this.validadoPorAdmin = administrador;
    }

    public void noValidar(PerfilAdministrador administrador) {
        this.estado = EstadoParticipacion.NO_VALIDADA;
        this.fechaValidacion = LocalDateTime.now();
        this.validadoPorAdmin = administrador;
    }

    public void anular(PerfilAdministrador administrador) {
        this.estado = EstadoParticipacion.ANULADA;
        this.fechaValidacion = LocalDateTime.now();
        this.validadoPorAdmin = administrador;
    }

    public Long getIdParticipacion() {
        return idParticipacion;
    }

    public void setIdParticipacion(Long idParticipacion) {
        this.idParticipacion = idParticipacion;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public RolParticipacion getRolParticipacion() {
        return rolParticipacion;
    }

    public void setRolParticipacion(RolParticipacion rolParticipacion) {
        this.rolParticipacion = rolParticipacion;
    }

    public EstadoParticipacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoParticipacion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public PerfilAdministrador getValidadoPorAdmin() {
        return validadoPorAdmin;
    }

    public void setValidadoPorAdmin(PerfilAdministrador validadoPorAdmin) {
        this.validadoPorAdmin = validadoPorAdmin;
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