package ni.edu.uam.innovacion.modules.project.entity;

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
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.RolProyecto;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa a un integrante dentro de un proyecto.
 *
 * Esta tabla permite registrar qué usuarios forman parte de un proyecto,
 * qué rol cumplen dentro del equipo y qué administrador realizó el registro.
 *
 * Ejemplos de roles de proyecto:
 * - líder
 * - integrante
 * - desarrollador
 * - diseñador
 * - investigador
 *
 * Esta entidad es importante para el seguimiento de proyectos como
 * HartaZone, Eco-Scent, Plant Bee, AgroLab, Bioapósitos, SUI o Trayex.
 */
@Entity
@Table(
        name = "integrantes_proyecto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_integrantes_proyecto_proyecto_usuario",
                        columnNames = {"id_proyecto", "id_usuario"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_integrantes_proyecto_proyecto",
                        columnList = "id_proyecto"
                ),
                @Index(
                        name = "idx_integrantes_proyecto_usuario",
                        columnList = "id_usuario"
                ),
                @Index(
                        name = "idx_integrantes_proyecto_rol",
                        columnList = "id_rol_proyecto"
                ),
                @Index(
                        name = "idx_integrantes_proyecto_admin",
                        columnList = "registrado_por_admin_id"
                )
        }
)
public class IntegranteProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_integrante_proyecto")
    private Long idIntegranteProyecto;

    /**
     * Proyecto al que pertenece el integrante.
     *
     * Relación:
     * Proyecto 1 ---- N IntegranteProyecto
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    /**
     * Usuario que forma parte del proyecto.
     *
     * Puede ser estudiante, participante externo, docente u otro usuario
     * registrado dentro del sistema.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * Rol que cumple el usuario dentro del proyecto.
     *
     * Se toma desde el catálogo roles_proyecto.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rol_proyecto", nullable = false)
    private RolProyecto rolProyecto;

    /**
     * Fecha en la que el usuario fue vinculado al proyecto.
     */
    @Column(name = "fecha_vinculacion", nullable = false)
    private LocalDate fechaVinculacion;

    /**
     * Estado del integrante dentro del proyecto.
     *
     * Permite mantener historial sin eliminar físicamente el registro.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoRegistro estado = EstadoRegistro.ACTIVO;

    /**
     * Observaciones adicionales sobre la participación del integrante.
     */
    @Column(name = "observaciones", columnDefinition = "text")
    private String observaciones;

    /**
     * Administrador que registró la vinculación del integrante.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrado_por_admin_id", nullable = false)
    private PerfilAdministrador registradoPorAdmin;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    public IntegranteProyecto() {
    }

    @PrePersist
    protected void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();

        if (this.fechaVinculacion == null) {
            this.fechaVinculacion = LocalDate.now();
        }

        if (this.estado == null) {
            this.estado = EstadoRegistro.ACTIVO;
        }

        this.creadoEn = ahora;
        this.actualizadoEn = ahora;

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

    public boolean estaActivo() {
        return EstadoRegistro.ACTIVO.equals(this.estado);
    }

    public boolean estaInactivo() {
        return EstadoRegistro.INACTIVO.equals(this.estado);
    }

    public boolean estaArchivado() {
        return EstadoRegistro.ARCHIVADO.equals(this.estado);
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

    public Long getIdIntegranteProyecto() {
        return idIntegranteProyecto;
    }

    public void setIdIntegranteProyecto(Long idIntegranteProyecto) {
        this.idIntegranteProyecto = idIntegranteProyecto;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public RolProyecto getRolProyecto() {
        return rolProyecto;
    }

    public void setRolProyecto(RolProyecto rolProyecto) {
        this.rolProyecto = rolProyecto;
    }

    public LocalDate getFechaVinculacion() {
        return fechaVinculacion;
    }

    public void setFechaVinculacion(LocalDate fechaVinculacion) {
        this.fechaVinculacion = fechaVinculacion;
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
        this.observaciones = observaciones;
    }

    public PerfilAdministrador getRegistradoPorAdmin() {
        return registradoPorAdmin;
    }

    public void setRegistradoPorAdmin(PerfilAdministrador registradoPorAdmin) {
        this.registradoPorAdmin = registradoPorAdmin;
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