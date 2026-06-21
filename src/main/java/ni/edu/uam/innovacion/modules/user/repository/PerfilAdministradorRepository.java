package ni.edu.uam.innovacion.modules.user.repository;

import ni.edu.uam.innovacion.modules.user.entity.PerfilAdministrador;
import ni.edu.uam.innovacion.modules.user.enums.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerfilAdministradorRepository extends JpaRepository<PerfilAdministrador, Long> {

    @Query("""
        select case when count(perfil) > 0 then true else false end
        from PerfilAdministrador perfil
        join perfil.usuario usuario
        join usuario.usuarioRoles usuarioRol
        where usuario.estado = :estado
          and usuarioRol.activo = true
          and lower(usuarioRol.rol.nombre) = lower(:nombreRol)
          and perfil.idUsuario <> :idUsuario
        """)
    boolean existsOtroPerfilAdministradorActivo(
        @Param("idUsuario") Long idUsuario,
        @Param("nombreRol") String nombreRol,
        @Param("estado") EstadoUsuario estado
    );
}
