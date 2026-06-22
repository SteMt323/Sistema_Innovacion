package ni.edu.uam.innovacion.modules.mentorship.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.activity.entity.Actividad;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;

@Entity
@Table(
    name = "actividad_colaboradores",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_actividad_colaboradores_actividad_usuario_rol",
            columnNames = {"id_actividad", "id_usuario", "rol_colaborador"}
        )
    },
    indexes = {
        @Index(name = "idx_actividad_colaboradores_actividad", columnList = "id_actividad"),
        @Index(name = "idx_actividad_colaboradores_usuario", columnList = "id_usuario"),
        @Index(name = "idx_actividad_colaboradores_admin", columnList = "agregado_por_admin_id"),
        @Index(name = "idx_actividad_colaboradores_estado", columnList = "estado")
    }
)
public class MentoriaActividad {

    public static final String ROL_COLABORADOR_MENTOR = "mentor";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador")
    private Long idColaborador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private PerfilMentor mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agregado_por_admin_id")
    private PerfilAdministrador agregadoPorAdmin;

    @Column(name = "rol_colaborador", nullable = false, length = 80)
    private String rolColaborador = ROL_COLABORADOR_MENTOR;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoRegistro estado = EstadoRegistro.ACTIVO;

    @Column(name = "observaciones", columnDefinition = "text")
    private String observaciones;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @PrePersist
    void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        if (fechaAsignacion == null) {
            fechaAsignacion = ahora;
        }
        if (estado == null) {
            estado = EstadoRegistro.ACTIVO;
        }
        if (rolColaborador == null || rolColaborador.isBlank()) {
            rolColaborador = ROL_COLABORADOR_MENTOR;
        }
        if (creadoEn == null) {
            creadoEn = ahora;
        }
        actualizadoEn = ahora;
        normalizar();
    }

    @PreUpdate
    void preUpdate() {
        actualizadoEn = LocalDateTime.now();
        normalizar();
    }

    private void normalizar() {
        rolColaborador = limpiar(rolColaborador);
        observaciones = limpiar(observaciones);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public boolean estaActivo() {
        return EstadoRegistro.ACTIVO.equals(estado);
    }

    public boolean estaArchivado() {
        return EstadoRegistro.ARCHIVADO.equals(estado);
    }

    public void activar() {
        estado = EstadoRegistro.ACTIVO;
    }

    public void inactivar() {
        estado = EstadoRegistro.INACTIVO;
    }

    public void archivar() {
        estado = EstadoRegistro.ARCHIVADO;
    }

    public Long getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Long idColaborador) {
        this.idColaborador = idColaborador;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public PerfilMentor getMentor() {
        return mentor;
    }

    public void setMentor(PerfilMentor mentor) {
        this.mentor = mentor;
    }

    public PerfilAdministrador getAgregadoPorAdmin() {
        return agregadoPorAdmin;
    }

    public void setAgregadoPorAdmin(PerfilAdministrador agregadoPorAdmin) {
        this.agregadoPorAdmin = agregadoPorAdmin;
    }

    public String getRolColaborador() {
        return rolColaborador;
    }

    public void setRolColaborador(String rolColaborador) {
        this.rolColaborador = limpiar(rolColaborador);
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public EstadoRegistro getEstado() {
        return estado;
    }

    public void setEstado(EstadoRegistro estado) {
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
