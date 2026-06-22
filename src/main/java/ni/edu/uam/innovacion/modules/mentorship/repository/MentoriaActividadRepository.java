package ni.edu.uam.innovacion.modules.mentorship.repository;

import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.common.enums.EstadoRegistro;
import ni.edu.uam.innovacion.modules.mentorship.entity.MentoriaActividad;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MentoriaActividadRepository
    extends JpaRepository<MentoriaActividad, Long>, JpaSpecificationExecutor<MentoriaActividad> {

    @Override
    @EntityGraph(attributePaths = {
        "actividad",
        "mentor",
        "mentor.usuario",
        "agregadoPorAdmin",
        "agregadoPorAdmin.usuario"
    })
    Optional<MentoriaActividad> findById(Long idColaborador);

    @EntityGraph(attributePaths = {
        "actividad",
        "mentor",
        "mentor.usuario",
        "agregadoPorAdmin",
        "agregadoPorAdmin.usuario"
    })
    Optional<MentoriaActividad> findByActividadIdActividadAndMentorIdUsuarioAndRolColaboradorIgnoreCase(
        Long idActividad,
        Long idMentor,
        String rolColaborador
    );

    @Override
    @EntityGraph(attributePaths = {
        "actividad",
        "mentor",
        "mentor.usuario",
        "agregadoPorAdmin",
        "agregadoPorAdmin.usuario"
    })
    List<MentoriaActividad> findAll(Specification<MentoriaActividad> specification, Sort sort);

    long countByRolColaboradorIgnoreCaseAndEstado(String rolColaborador, EstadoRegistro estado);

    long countByMentorIdUsuarioAndRolColaboradorIgnoreCaseAndEstado(
        Long idMentor,
        String rolColaborador,
        EstadoRegistro estado
    );

    @Query("""
        select count(distinct mentoria.actividad.idActividad)
        from MentoriaActividad mentoria
        where lower(mentoria.rolColaborador) = lower(:rolColaborador)
          and mentoria.estado = :estado
        """)
    long countActividadesDistintasPorRolYEstado(
        @Param("rolColaborador") String rolColaborador,
        @Param("estado") EstadoRegistro estado
    );

    @Query("""
        select mentoria.mentor.idUsuario as idMentor,
               count(mentoria) as totalActivas
        from MentoriaActividad mentoria
        where lower(mentoria.rolColaborador) = lower(:rolColaborador)
          and mentoria.estado = :estado
        group by mentoria.mentor.idUsuario
        """)
    List<MentorMentoriaActivaProjection> contarActivasPorMentor(
        @Param("rolColaborador") String rolColaborador,
        @Param("estado") EstadoRegistro estado
    );
}
