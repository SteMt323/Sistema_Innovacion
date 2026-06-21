package ni.edu.uam.innovacion.modules.points.entity;

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
import ni.edu.uam.innovacion.modules.participation.entity.Participacion;
import ni.edu.uam.innovacion.modules.points.enums.EstadoPuntos;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

@Entity
@Table(
    name = "puntos_innovacion",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_puntos_participacion_tipo",
            columnNames = {"id_participacion", "tipo_movimiento"}
        )
    },
    indexes = {
        @Index(name = "idx_puntos_innovacion_id_usuario", columnList = "id_usuario"),
        @Index(name = "idx_puntos_innovacion_id_participacion", columnList = "id_participacion"),
        @Index(name = "idx_puntos_innovacion_id_admin_ajuste", columnList = "id_admin_ajuste"),
        @Index(name = "idx_puntos_innovacion_fecha_asignacion", columnList = "fecha_asignacion"),
        @Index(name = "idx_puntos_innovacion_estado", columnList = "estado")
    }
)
public class PuntoInnovacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_punto")
    private Long idPunto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participacion")
    private Participacion participacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_ajuste")
    private PerfilAdministrador adminAjuste;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Convert(converter = TipoMovimientoPuntosConverter.class)
    @Column(name = "tipo_movimiento", nullable = false, length = 30)
    private TipoMovimientoPuntos tipoMovimiento;

    @Column(name = "motivo", length = 255)
    private String motivo;

    @Column(name = "origen", length = 100)
    private String origen;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Convert(converter = EstadoPuntosConverter.class)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPuntos estado = EstadoPuntos.ACTIVO;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        if (fechaAsignacion == null) {
            fechaAsignacion = ahora;
        }
        if (creadoEn == null) {
            creadoEn = ahora;
        }
        if (estado == null) {
            estado = EstadoPuntos.ACTIVO;
        }
        normalizarTexto();
    }

    @PreUpdate
    void preUpdate() {
        normalizarTexto();
    }

    private void normalizarTexto() {
        motivo = limpiar(motivo);
        origen = limpiar(origen);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public boolean estaActivo() {
        return EstadoPuntos.ACTIVO.equals(estado);
    }

    public void activar() {
        estado = EstadoPuntos.ACTIVO;
        fechaAsignacion = LocalDateTime.now();
    }

    public void anular() {
        estado = EstadoPuntos.ANULADO;
    }

    public Long getIdPunto() {
        return idPunto;
    }

    public void setIdPunto(Long idPunto) {
        this.idPunto = idPunto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Participacion getParticipacion() {
        return participacion;
    }

    public void setParticipacion(Participacion participacion) {
        this.participacion = participacion;
    }

    public PerfilAdministrador getAdminAjuste() {
        return adminAjuste;
    }

    public void setAdminAjuste(PerfilAdministrador adminAjuste) {
        this.adminAjuste = adminAjuste;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public TipoMovimientoPuntos getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimientoPuntos tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = limpiar(motivo);
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = limpiar(origen);
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public EstadoPuntos getEstado() {
        return estado;
    }

    public void setEstado(EstadoPuntos estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
