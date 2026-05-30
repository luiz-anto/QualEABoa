package br.com.fecaf.Semestral.repository;

import br.com.fecaf.Semestral.model.Amizade;
import br.com.fecaf.Semestral.model.AmizadeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AmizadeRepository extends JpaRepository<Amizade, Long> {

    @Query("SELECT a FROM Amizade a WHERE (a.remetente.id = :u1 AND a.destinatario.id = :u2) OR (a.remetente.id = :u2 AND a.destinatario.id = :u1)")
    Optional<Amizade> findAmizadeEntreUsuarios(@Param("u1") Long u1, @Param("u2") Long u2);

    @Query("SELECT a FROM Amizade a WHERE (a.remetente.id = :userId OR a.destinatario.id = :userId) AND a.status = 'ACEITA'")
    List<Amizade> findAmigosAceitos(@Param("userId") Long userId);

    @Query("SELECT a FROM Amizade a WHERE a.destinatario.id = :userId AND a.status = 'PENDENTE'")
    List<Amizade> findSolicitacoesPendentes(@Param("userId") Long userId);

    @Query("SELECT a FROM Amizade a WHERE a.remetente.id = :userId AND a.status = 'PENDENTE'")
    List<Amizade> findSolicitacoesEnviadas(@Param("userId") Long userId);
}
