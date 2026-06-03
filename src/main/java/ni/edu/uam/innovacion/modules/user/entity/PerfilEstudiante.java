package ni.edu.uam.innovacion.modules.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "perfiles_estudiante",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_perfiles_estudiante_cif", columnNames = "cif"),
        @UniqueConstraint(name = "uk_perfiles_estudiante_correo_institucional", columnNames = "correo_institucional")
    }
)
public class PerfilEstudiante {

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "cif", nullable = false, length = 30)
    private String cif;

    @Column(name = "correo_institucional", length = 150)
    private String correoInstitucional;

    @Column(name = "id_carrera_principal", nullable = false)
    private Long idCarreraPrincipal;

    @Column(name = "doble_titular", nullable = false)
    private Boolean dobleTitular = Boolean.FALSE;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public Long getIdCarreraPrincipal() {
        return idCarreraPrincipal;
    }

    public void setIdCarreraPrincipal(Long idCarreraPrincipal) {
        this.idCarreraPrincipal = idCarreraPrincipal;
    }

    public Boolean getDobleTitular() {
        return dobleTitular;
    }

    public void setDobleTitular(Boolean dobleTitular) {
        this.dobleTitular = dobleTitular;
    }
}
