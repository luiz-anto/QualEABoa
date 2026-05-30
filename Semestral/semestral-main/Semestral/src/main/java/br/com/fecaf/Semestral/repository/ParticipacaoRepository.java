package br.com.fecaf.Semestral.repository;

import br.com.fecaf.Semestral.model.Participacao;
import br.com.fecaf.Semestral.model.ParticipacaoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {

    @Query("SELECT p FROM Participacao p LEFT JOIN FETCH p.evento LEFT JOIN FETCH p.usuario WHERE p.usuario.id = :usuarioId ORDER BY p.dataParticipacao DESC")
    List<Participacao> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Participacao p LEFT JOIN FETCH p.evento LEFT JOIN FETCH p.usuario WHERE p.usuario.id = :usuarioId AND p.status = br.com.fecaf.Semestral.model.ParticipacaoStatus.ATIVO ORDER BY p.dataParticipacao DESC")
    List<Participacao> findByUsuarioIdAtivos(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Participacao p LEFT JOIN FETCH p.evento LEFT JOIN FETCH p.usuario WHERE p.usuario.id = :usuarioId AND p.status = br.com.fecaf.Semestral.model.ParticipacaoStatus.CANCELADO ORDER BY p.dataParticipacao DESC")
    List<Participacao> findByUsuarioIdCancelados(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Participacao p LEFT JOIN FETCH p.evento LEFT JOIN FETCH p.usuario WHERE p.evento.id = :eventoId")
    List<Participacao> findByEventoId(@Param("eventoId") Long eventoId);

    @Query("SELECT p FROM Participacao p LEFT JOIN FETCH p.evento LEFT JOIN FETCH p.usuario WHERE p.usuario.id = :usuarioId AND p.evento.id = :eventoId")
    Optional<Participacao> findByUsuarioIdAndEventoId(@Param("usuarioId") Long usuarioId, @Param("eventoId") Long eventoId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Participacao p WHERE p.usuario.id = :usuarioId AND p.evento.id = :eventoId AND p.status = :status")
    boolean existsByUsuarioIdAndEventoIdAndStatus(@Param("usuarioId") Long usuarioId, @Param("eventoId") Long eventoId, @Param("status") ParticipacaoStatus status);

    @Query("SELECT COUNT(p) FROM Participacao p WHERE p.evento.id = :eventoId AND p.status = :status")
    long countByEventoIdAndStatus(@Param("eventoId") Long eventoId, @Param("status") ParticipacaoStatus status);
}
