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
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import ni.edu.uam.innovacion.modules.project.enums.FasePIA;
import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;

/**
 * Entidad que representa el historial de fases de un proyecto
 * dentro del Programa PIA.
 *
 * Esta entidad permite conservar la trazabilidad de las fases por las que
 * ha pasado un proyecto PIA, por ejemplo:
 * - prospecto
 * - preincubacion
 * - incubacion
 * - aceleracion
 * - seguimiento
 * - graduado
 *
 * Reglas importantes que luego deben reforzarse desde el service:
 * - Todo historial debe pertenecer a un ProyectoPIA existente.
 * - Toda fase registrada debe ser válida dentro del enum FasePIA.
 * - La fecha de inicio es obligatoria.
 * - La fecha de fin no puede ser anterior a la fecha de inicio.
 * - El historial debe registrar qué administrador realizó el cambio.
 * - La información histórica no debería eliminarse, solo consultarse.
 */
@Entity
@Table(
        name = "historial_fases_pia",
        indexes = {
                @Index(name = "idx_historial_fases_pia_id_proyecto_pia", columnList = "id_proyecto_pia"),
                @Index(name = "idx_historial_fases_pia_fase", columnList = "fase"),
                @Index(name = "idx_historial_fases_pia_fecha_inicio", columnList = "fecha_inicio"),
                @Index(name = "idx_historial_fases_pia_admin_registro", columnList = "registrado_por_admin_id")
        }
)
public class HistorialFasePIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial_fase")
    private Long idHistorialFase;

    /**
     * Proyecto PIA al que pertenece el historial de fase.
     *
     * Un proyecto PIA puede tener varios registros en el historial,
     * ya que puede avanzar por diferentes fases durante el programa.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proyecto_pia", nullable = false)
    private ProyectoPIA proyectoPIA;

    /**
     * Fase registrada en el historial.
     *
     * Ejemplo:
     * - PROSPECTO
     * - PREINCUBACION
     * - INCUBACION
     * - ACELERACION
     * - SEGUIMIENTO
     * - GRADUADO
     */
    @Convert(converter = FasePIAConverter.class)
    @Column(name = "fase", nullable = false, length = 30)
    private FasePIA fase;

    /**
     * Fecha en que inició esta fase dentro del Programa PIA.
     */
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    /**
     * Fecha en que finalizó esta fase.
     *
     * Puede ser null mientras la fase esté vigente.
     */
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    /**
     * Administrador que registró el cambio o movimiento de fase.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrado_por_admin_id", nullable = false)
    private PerfilAdministrador registradoPorAdmin;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    void prePersist() {
        if (fechaInicio == null) {
            fechaInicio = LocalDate.now();
        }

        if (creadoEn == null) {
            creadoEn = LocalDateTime.now();
        }

        normalizarTexto();
    }

    private void normalizarTexto() {
        observaciones = limpiar(observaciones);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public boolean faseEstaVigente() {
        return fechaFin == null;
    }

    public boolean tieneFechaFin() {
        return fechaFin != null;
    }

    public Long getIdHistorialFase() {
        return idHistorialFase;
    }

    public void setIdHistorialFase(Long idHistorialFase) {
        this.idHistorialFase = idHistorialFase;
    }

    public ProyectoPIA getProyectoPIA() {
        return proyectoPIA;
    }

    public void setProyectoPIA(ProyectoPIA proyectoPIA) {
        this.proyectoPIA = proyectoPIA;
    }

    public FasePIA getFase() {
        return fase;
    }

    public void setFase(FasePIA fase) {
        this.fase = fase;
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
}