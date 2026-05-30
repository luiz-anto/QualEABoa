package br.com.fecaf.Semestral.repository;

import br.com.fecaf.Semestral.model.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    @Query("SELECT i FROM Ingresso i LEFT JOIN FETCH i.usuario LEFT JOIN FETCH i.evento WHERE i.usuario.id = :usuarioId")
    List<Ingresso> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
