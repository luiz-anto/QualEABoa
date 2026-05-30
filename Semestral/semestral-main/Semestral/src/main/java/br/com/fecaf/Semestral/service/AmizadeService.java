package br.com.fecaf.Semestral.service;

import br.com.fecaf.Semestral.exception.NotFoundExeption;
import br.com.fecaf.Semestral.model.*;
import br.com.fecaf.Semestral.repository.AmizadeRepository;
import br.com.fecaf.Semestral.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AmizadeService {

    private final AmizadeRepository amizadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoService notificacaoService;

    public AmizadeService(AmizadeRepository amizadeRepository,
                          UsuarioRepository usuarioRepository,
                          NotificacaoService notificacaoService) {
        this.amizadeRepository = amizadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoService = notificacaoService;
    }

    @Transactional
    public Amizade enviarSolicitacao(Long remetenteId, Long destinatarioId) {
        if (remetenteId.equals(destinatarioId)) {
            throw new IllegalArgumentException("Você não pode adicionar a si mesmo.");
        }
        amizadeRepository.findAmizadeEntreUsuarios(remetenteId, destinatarioId).ifPresent(a -> {
            throw new IllegalStateException("Já existe uma solicitação entre esses usuários.");
        });

        Usuario remetente    = buscarUsuario(remetenteId);
        Usuario destinatario = buscarUsuario(destinatarioId);

        Amizade amizade = new Amizade();
        amizade.setRemetente(remetente);
        amizade.setDestinatario(destinatario);
        amizade.setStatus(AmizadeStatus.PENDENTE);
        amizade.setCriadoEm(agora());

        Amizade salva = amizadeRepository.save(amizade);

        notificacaoService.criarNotificacao(destinatario, "👋",
                "Pedido de amizade",
                remetente.getNome() + " quer ser seu amigo!");

        return salva;
    }

    @Transactional
    public Amizade aceitar(Long amizadeId, Long usuarioId) {
        Amizade amizade = buscarAmizade(amizadeId);
        if (!amizade.getDestinatario().getId().equals(usuarioId)) {
            throw new IllegalStateException("Sem permissão para aceitar esta solicitação.");
        }
        amizade.setStatus(AmizadeStatus.ACEITA);
        Amizade salva = amizadeRepository.save(amizade);

        notificacaoService.criarNotificacao(amizade.getRemetente(), "✅",
                "Amizade aceita",
                amizade.getDestinatario().getNome() + " aceitou seu pedido de amizade!");

        return salva;
    }

    @Transactional
    public Amizade recusar(Long amizadeId, Long usuarioId) {
        Amizade amizade = buscarAmizade(amizadeId);
        if (!amizade.getDestinatario().getId().equals(usuarioId)) {
            throw new IllegalStateException("Sem permissão para recusar esta solicitação.");
        }
        amizade.setStatus(AmizadeStatus.RECUSADA);
        return amizadeRepository.save(amizade);
    }

    @Transactional
    public void remover(Long amizadeId, Long usuarioId) {
        Amizade amizade = buscarAmizade(amizadeId);
        boolean pertence = amizade.getRemetente().getId().equals(usuarioId)
                || amizade.getDestinatario().getId().equals(usuarioId);
        if (!pertence) throw new IllegalStateException("Sem permissão.");
        amizadeRepository.delete(amizade);
    }

    public List<Amizade> listarAmigos(Long userId) {
        return amizadeRepository.findAmigosAceitos(userId);
    }

    public List<Amizade> listarPendentes(Long userId) {
        return amizadeRepository.findSolicitacoesPendentes(userId);
    }

    public String verificarStatus(Long u1, Long u2) {
        return amizadeRepository.findAmizadeEntreUsuarios(u1, u2)
                .map(a -> a.getStatus().name())
                .orElse("NENHUMA");
    }

    // ── helpers ──────────────────────────────────────────────

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Usuário não encontrado: " + id));
    }

    private Amizade buscarAmizade(Long id) {
        return amizadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Amizade não encontrada: " + id));
    }

    private String agora() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
