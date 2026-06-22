package ni.edu.uam.innovacion.modules.user.repository;

import java.util.List;
import ni.edu.uam.innovacion.modules.user.entity.PerfilMentor;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilMentorRepository extends JpaRepository<PerfilMentor, Long> {

    @Override
    @EntityGraph(attributePaths = {"usuario"})
    List<PerfilMentor> findAll();

    @Override
    @EntityGraph(attributePaths = {"usuario"})
    java.util.Optional<PerfilMentor> findById(Long idUsuario);

    @EntityGraph(attributePaths = {"usuario"})
    List<PerfilMentor> findAllByOrderByUsuarioNombreCompletoAsc();

    @EntityGraph(attributePaths = {"usuario"})
    List<PerfilMentor> findByUsuarioEstadoOrderByUsuarioNombreCompletoAsc(EstadoUsuario estado);

    long countByUsuarioEstado(EstadoUsuario estado);
}
