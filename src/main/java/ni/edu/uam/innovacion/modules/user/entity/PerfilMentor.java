package ni.edu.uam.innovacion.modules.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import ni.edu.uam.innovacion.modules.user.enums.GradoAcademico;

@Entity
@Table(name = "perfiles_mentor")
public class PerfilMentor {

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "area_experiencia", length = 120)
    private String areaExperiencia;

    @Column(name = "especialidad", length = 120)
    private String especialidad;

    @Column(name = "institucion", length = 150)
    private String institucion;

    @Column(name = "tipo_acompanamiento", length = 100)
    private String tipoAcompanamiento;

    @Convert(converter = GradoAcademicoConverter.class)
    @Column(name = "grado_academico", length = 30)
    private GradoAcademico gradoAcademico;

    @Column(name = "titulo_universitario", length = 150)
    private String tituloUniversitario;

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

    public String getAreaExperiencia() {
        return areaExperiencia;
    }

    public void setAreaExperiencia(String areaExperiencia) {
        this.areaExperiencia = areaExperiencia;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getTipoAcompanamiento() {
        return tipoAcompanamiento;
    }

    public void setTipoAcompanamiento(String tipoAcompanamiento) {
        this.tipoAcompanamiento = tipoAcompanamiento;
    }

    public GradoAcademico getGradoAcademico() {
        return gradoAcademico;
    }

    public void setGradoAcademico(GradoAcademico gradoAcademico) {
        this.gradoAcademico = gradoAcademico;
    }

    public String getTituloUniversitario() {
        return tituloUniversitario;
    }

    public void setTituloUniversitario(String tituloUniversitario) {
        this.tituloUniversitario = tituloUniversitario;
    }
}
