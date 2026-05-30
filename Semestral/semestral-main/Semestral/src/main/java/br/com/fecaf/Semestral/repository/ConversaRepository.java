package br.com.fecaf.Semestral.repository;

import br.com.fecaf.Semestral.model.Conversa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversaRepository extends JpaRepository<Conversa, Long> {

    @Query("SELECT c FROM Conversa c WHERE c.usuario1.id = :userId OR c.usuario2.id = :userId ORDER BY c.dataUltimaMensagem DESC NULLS LAST")
    List<Conversa> findByUsuarioId(@Param("userId") Long userId);

    @Query("SELECT c FROM Conversa c WHERE (c.usuario1.id = :u1 AND c.usuario2.id = :u2) OR (c.usuario1.id = :u2 AND c.usuario2.id = :u1)")
    Optional<Conversa> findByUsuarios(@Param("u1") Long u1, @Param("u2") Long u2);
}
