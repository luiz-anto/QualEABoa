package br.com.fecaf.Semestral.repository;

import br.com.fecaf.Semestral.model.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(u.handle) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Usuario> buscarPorNomeOuHandle(@Param("q") String q, Pageable pageable);
}
