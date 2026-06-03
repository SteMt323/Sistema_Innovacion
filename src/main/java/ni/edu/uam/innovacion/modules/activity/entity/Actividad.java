package ni.edu.uam.innovacion.modules.activity.entity;

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
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.activity.enums.EstadoActividad;
import ni.edu.uam.innovacion.modules.activity.enums.ModalidadActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.AmbitoActividad;
import ni.edu.uam.innovacion.modules.catalog.entity.CategoriaDIEM;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.entity.Usuario;

@Entity
@Table(
    name = "actividades",
    indexes = {
        @Index(name = "idx_actividades_id_ambito_actividad", columnList = "id_ambito_actividad"),
        @Index(name = "idx_actividades_id_categoria_diem", columnList = "id_categoria_diem"),
        @Index(name = "idx_actividades_estado_fecha_inicio", columnList = "estado, fecha_inicio"),
        @Index(name = "idx_actividades_id_administrador_creador", columnList = "id_administrador_creador")
    }
)
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad")
    private Long idActividad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ambito_actividad", nullable = false)
    private AmbitoActividad ambitoActividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria_diem")
    private CategoriaDIEM categoriaDiem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_administrador_creador", nullable = false)
    private PerfilAdministrador administradorCreador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsable_usuario")
    private Usuario responsableUsuario;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Convert(converter = ModalidadActividadConverter.class)
    @Column(name = "modalidad", nullable = false, length = 30)
    private ModalidadActividad modalidad;

    @Convert(converter = EstadoActividadConverter.class)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoActividad estado = EstadoActividad.BORRADOR;

    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    @Column(name = "ubicacion", length = 255)
    private String ubicacion;

    @Column(name = "responsable_nombre", length = 150)
    private String responsableNombre;

    @Column(name = "puntos_base", nullable = false)
    private Integer puntosBase = 0;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @PrePersist
    void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        if (creadoEn == null) {
            creadoEn = ahora;
        }
        if (estado == null) {
            estado = EstadoActividad.BORRADOR;
        }
        if (puntosBase == null) {
            puntosBase = 0;
        }
        normalizarTexto();
    }

    @PreUpdate
    void preUpdate() {
        actualizadoEn = LocalDateTime.now();
        if (puntosBase == null) {
            puntosBase = 0;
        }
        normalizarTexto();
    }

    private void normalizarTexto() {
        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);
        ubicacion = limpiar(ubicacion);
        responsableNombre = limpiar(responsableNombre);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public Long getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Long idActividad) {
        this.idActividad = idActividad;
    }

    public AmbitoActividad getAmbitoActividad() {
        return ambitoActividad;
    }

    public void setAmbitoActividad(AmbitoActividad ambitoActividad) {
        this.ambitoActividad = ambitoActividad;
    }

    public CategoriaDIEM getCategoriaDiem() {
        return categoriaDiem;
    }

    public void setCategoriaDiem(CategoriaDIEM categoriaDiem) {
        this.categoriaDiem = categoriaDiem;
    }

    public PerfilAdministrador getAdministradorCreador() {
        return administradorCreador;
    }

    public void setAdministradorCreador(PerfilAdministrador administradorCreador) {
        this.administradorCreador = administradorCreador;
    }

    public Usuario getResponsableUsuario() {
        return responsableUsuario;
    }

    public void setResponsableUsuario(Usuario responsableUsuario) {
        this.responsableUsuario = responsableUsuario;
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

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public ModalidadActividad getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadActividad modalidad) {
        this.modalidad = modalidad;
    }

    public EstadoActividad getEstado() {
        return estado;
    }

    public void setEstado(EstadoActividad estado) {
        this.estado = estado;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = limpiar(ubicacion);
    }

    public String getResponsableNombre() {
        return responsableNombre;
    }

    public void setResponsableNombre(String responsableNombre) {
        this.responsableNombre = limpiar(responsableNombre);
    }

    public Integer getPuntosBase() {
        return puntosBase;
    }

    public void setPuntosBase(Integer puntosBase) {
        this.puntosBase = puntosBase;
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
