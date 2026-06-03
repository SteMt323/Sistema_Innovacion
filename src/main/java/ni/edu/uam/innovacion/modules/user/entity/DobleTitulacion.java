package ni.edu.uam.innovacion.modules.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.catalog.entity.Carrera;

@Entity
@Table(
    name = "doble_titulaciones",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_doble_titulaciones_estudiante_carrera",
            columnNames = {"id_estudiante", "id_carrera_secundaria"}
        )
    }
)
public class DobleTitulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doble_titulacion")
    private Long idDobleTitulacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private PerfilEstudiante perfilEstudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_carrera_secundaria", nullable = false)
    private Carrera carreraSecundaria;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoRegistro estado = EstadoRegistro.ACTIVO;

    @PrePersist
    void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }
        if (estado == null) {
            estado = EstadoRegistro.ACTIVO;
        }
    }

    public Long getIdDobleTitulacion() {
        return idDobleTitulacion;
    }

    public void setIdDobleTitulacion(Long idDobleTitulacion) {
        this.idDobleTitulacion = idDobleTitulacion;
    }

    public PerfilEstudiante getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(PerfilEstudiante perfilEstudiante) {
        this.perfilEstudiante = perfilEstudiante;
    }

    public Carrera getCarreraSecundaria() {
        return carreraSecundaria;
    }

    public void setCarreraSecundaria(Carrera carreraSecundaria) {
        this.carreraSecundaria = carreraSecundaria;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public EstadoRegistro getEstado() {
        return estado;
    }

    public void setEstado(EstadoRegistro estado) {
        this.estado = estado;
    }

    public boolean estaActiva() {
        return EstadoRegistro.ACTIVO.equals(estado);
    }
}
