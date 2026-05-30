package br.com.fecaf.Semestral.repository;

import br.com.fecaf.Semestral.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    @Query("SELECT n FROM Notificacao n WHERE n.usuario.id = :usuarioId ORDER BY n.hora DESC")
    List<Notificacao> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
