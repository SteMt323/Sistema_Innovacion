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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.project.enums.EstadoProyectoPIA;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;

/**
 * Entidad que representa la participación de un proyecto
 * dentro del Programa PIA.
 *
 * El Programa PIA contempla fases como:
 * - prospecto
 * - preincubacion
 * - incubacion
 * - aceleracion
 * - seguimiento
 * - graduado
 *
 * Esta entidad funciona como una extensión del proyecto base,
 * permitiendo controlar la fase actual, estado dentro del programa,
 * fecha de ingreso y administrador que registró el proceso.
 *
 * Reglas importantes que luego deben reforzarse desde el service:
 * - Un proyecto solo debe tener un registro activo dentro de ProyectoPIA.
 * - No se debe registrar un proyecto archivado dentro del Programa PIA.
 * - La fase inicial debe ser coherente con el proceso del programa.
 * - Los cambios de fase deben conservar historial en HistorialFasePIA.
 * - Solo un administrador debe poder registrar o modificar este proceso.
 */
@Entity
@Table(
        name = "proyectos_pia",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_proyectos_pia_proyecto",
                        columnNames = "id_proyecto"
                )
        },
        indexes = {
                @Index(name = "idx_proyectos_pia_id_proyecto", columnList = "id_proyecto"),
                @Index(name = "idx_proyectos_pia_fase_actual", columnList = "fase_actual"),
                @Index(name = "idx_proyectos_pia_estado", columnList = "estado"),
                @Index(name = "idx_proyectos_pia_fecha_ingreso", columnList = "fecha_ingreso"),
                @Index(name = "idx_proyectos_pia_admin_registro", columnList = "registrado_por_admin_id")
        }
)
public class ProyectoPIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto_pia")
    private Long idProyectoPIA;

    /**
     * Proyecto base que ingresa al Programa PIA.
     *
     * Se usa relación OneToOne porque un proyecto no debería tener
     * más de un registro principal dentro del Programa PIA.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    /**
     * Fase actual del proyecto dentro del Programa PIA.
     */
    @Convert(converter = FasePIAConverter.class)
    @Column(name = "fase_actual", nullable = false, length = 30)
    private FasePIA faseActual = FasePIA.PROSPECTO;

    /**
     * Fecha en que el proyecto ingresó al Programa PIA.
     */
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    /**
     * Estado del proyecto dentro del Programa PIA.
     */
    @Convert(converter = EstadoProyectoPIAConverter.class)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoProyectoPIA estado = EstadoProyectoPIA.ACTIVO;

    /**
     * Administrador que registró el proyecto dentro del Programa PIA.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrado_por_admin_id", nullable = false)
    private PerfilAdministrador registradoPorAdmin;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @PrePersist
    void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();

        if (faseActual == null) {
            faseActual = FasePIA.PROSPECTO;
        }

        if (fechaIngreso == null) {
            fechaIngreso = LocalDate.now();
        }

        if (estado == null) {
            estado = EstadoProyectoPIA.ACTIVO;
        }

        if (creadoEn == null) {
            creadoEn = ahora;
        }

        normalizarTexto();
    }

    @PreUpdate
    void preUpdate() {
        actualizadoEn = LocalDateTime.now();

        if (faseActual == null) {
            faseActual = FasePIA.PROSPECTO;
        }

        if (estado == null) {
            estado = EstadoProyectoPIA.ACTIVO;
        }

        normalizarTexto();
    }

    private void normalizarTexto() {
        observaciones = limpiar(observaciones);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public boolean estaActivo() {
        return EstadoProyectoPIA.ACTIVO.equals(estado);
    }

    public boolean estaPausado() {
        return EstadoProyectoPIA.PAUSADO.equals(estado);
    }

    public boolean estaFinalizado() {
        return EstadoProyectoPIA.FINALIZADO.equals(estado);
    }

    public boolean estaRetirado() {
        return EstadoProyectoPIA.RETIRADO.equals(estado);
    }

    public boolean puedeModificarse() {
        return !estaFinalizado() && !estaRetirado();
    }

    public Long getIdProyectoPIA() {
        return idProyectoPIA;
    }

    public void setIdProyectoPIA(Long idProyectoPIA) {
        this.idProyectoPIA = idProyectoPIA;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public FasePIA getFaseActual() {
        return faseActual;
    }

    public void setFaseActual(FasePIA faseActual) {
        this.faseActual = faseActual;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public EstadoProyectoPIA getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyectoPIA estado) {
        this.estado = estado;
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