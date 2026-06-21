package ni.edu.uam.innovacion.modules.points.repository;

import java.util.List;
import java.util.Optional;
import ni.edu.uam.innovacion.modules.points.entity.PuntoInnovacion;
import ni.edu.uam.innovacion.modules.points.enums.EstadoPuntos;
import ni.edu.uam.innovacion.modules.points.enums.TipoMovimientoPuntos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PuntoInnovacionRepository
        extends JpaRepository<PuntoInnovacion, Long>, JpaSpecificationExecutor<PuntoInnovacion> {

    Optional<PuntoInnovacion> findByParticipacionIdParticipacionAndTipoMovimiento(
        Long idParticipacion,
        TipoMovimientoPuntos tipoMovimiento
    );

    List<PuntoInnovacion> findByUsuarioIdUsuarioOrderByFechaAsignacionDesc(Long idUsuario);

    List<PuntoInnovacion> findTop10ByOrderByFechaAsignacionDesc();

    long countByEstado(EstadoPuntos estado);

    @Query("""
        select coalesce(sum(punto.cantidad), 0)
        from PuntoInnovacion punto
        where punto.estado = :estado
        """)
    Long sumarCantidadPorEstado(@Param("estado") EstadoPuntos estado);

    @Query("""
        select punto.usuario.idUsuario as idUsuario,
               punto.usuario.nombreCompleto as nombreUsuario,
               sum(punto.cantidad) as totalPuntos
        from PuntoInnovacion punto
        where punto.estado = :estado
        group by punto.usuario.idUsuario, punto.usuario.nombreCompleto
        having sum(punto.cantidad) > 0
        order by sum(punto.cantidad) desc
        """)
    List<TopUsuarioPuntosProjection> obtenerRanking(
        @Param("estado") EstadoPuntos estado,
        Pageable pageable
    );
}
