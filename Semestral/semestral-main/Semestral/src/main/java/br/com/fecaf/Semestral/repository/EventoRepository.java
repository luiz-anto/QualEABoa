package br.com.fecaf.Semestral.repository;

import br.com.fecaf.Semestral.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    // Public active events
    @Query("SELECT e FROM Evento e WHERE e.status = :status AND e.privado = false")
    List<Evento> findByStatusAndPrivadoFalse(@Param("status") String status);

    // Visible events for a user (public OR own private)
    @Query("SELECT e FROM Evento e WHERE e.status = :status AND (e.privado = false OR e.usuario.id = :userId)")
    List<Evento> findVisibleByStatus(@Param("status") String status, @Param("userId") Long userId);

    // All events created by a specific user
    @Query("SELECT e FROM Evento e WHERE e.usuario.id = :usuarioId")
    List<Evento> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Events by status created by a specific user
    @Query("SELECT e FROM Evento e WHERE e.status = :status AND e.usuario.id = :usuarioId")
    List<Evento> findByStatusAndUsuarioId(@Param("status") String status, @Param("usuarioId") Long usuarioId);

    // Search by ID (for private events)
    Optional<Evento> findById(Long id);

    // Combined filter
    @Query("SELECT e FROM Evento e WHERE (:status IS NULL OR e.status = :status) AND (:usuarioId IS NULL OR e.usuario.id = :usuarioId) AND (e.privado = false OR e.usuario.id = :usuarioId)")
    List<Evento> findComFiltro(@Param("status") String status, @Param("usuarioId") Long usuarioId);
}
