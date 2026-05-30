package br.com.fecaf.Semestral.repository;

import br.com.fecaf.Semestral.model.MensagemPrivada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensagemPrivadaRepository extends JpaRepository<MensagemPrivada, Long> {

    @Query("SELECT m FROM MensagemPrivada m WHERE m.conversa.id = :conversaId ORDER BY m.dataEnvio ASC")
    List<MensagemPrivada> findByConversaIdOrderByDataEnvioAsc(@Param("conversaId") Long conversaId);
}
