package br.com.fecaf.Semestral.repository;

import br.com.fecaf.Semestral.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    @Query("SELECT m FROM Mensagem m WHERE m.usuario.id = :usuarioId ORDER BY m.hora DESC")
    List<Mensagem> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
