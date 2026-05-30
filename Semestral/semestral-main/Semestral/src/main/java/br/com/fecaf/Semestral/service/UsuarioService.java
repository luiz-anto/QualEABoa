package br.com.fecaf.Semestral.service;

import br.com.fecaf.Semestral.dto.UsuarioBuscaDTO;
import br.com.fecaf.Semestral.exception.NotFoundExeption;
import br.com.fecaf.Semestral.model.Amizade;
import br.com.fecaf.Semestral.model.AmizadeStatus;
import br.com.fecaf.Semestral.model.Usuario;
import br.com.fecaf.Semestral.repository.AmizadeRepository;
import br.com.fecaf.Semestral.repository.UsuarioRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AmizadeRepository amizadeRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, AmizadeRepository amizadeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.amizadeRepository = amizadeRepository;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Usuário não encontrado: " + id));
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario criar(Usuario usuario) {
        usuario.setCriadoEm(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return usuarioRepository.save(usuario);
    }

    public Usuario login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundExeption("Credenciais inválidas"));
        if (!usuario.getSenha().equals(senha)) {
            throw new NotFoundExeption("Credenciais inválidas");
        }
        return usuario;
    }

    public List<UsuarioBuscaDTO> buscarUsuarios(String q, Long usuarioAtualId) {
        List<Usuario> encontrados = usuarioRepository.buscarPorNomeOuHandle(q, PageRequest.of(0, 20));
        return encontrados.stream()
                .filter(u -> !u.getId().equals(usuarioAtualId))
                .map(u -> {
                    Optional<Amizade> amizadeOpt = amizadeRepository.findAmizadeEntreUsuarios(usuarioAtualId, u.getId());
                    String status = "NENHUMA";
                    Long amizadeId = null;
                    if (amizadeOpt.isPresent()) {
                        Amizade a = amizadeOpt.get();
                        amizadeId = a.getId();
                        if (a.getStatus() == AmizadeStatus.ACEITA) {
                            status = "ACEITA";
                        } else if (a.getStatus() == AmizadeStatus.PENDENTE) {
                            status = a.getRemetente().getId().equals(usuarioAtualId) ? "PENDENTE_ENVIADA" : "PENDENTE_RECEBIDA";
                        } else {
                            status = a.getStatus().name();
                        }
                    }
                    return new UsuarioBuscaDTO(u.getId(), u.getNome(), u.getHandle(), u.getEmail(), status, amizadeId);
                })
                .collect(Collectors.toList());
    }
}
