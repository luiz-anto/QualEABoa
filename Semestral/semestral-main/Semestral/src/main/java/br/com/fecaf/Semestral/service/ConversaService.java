package br.com.fecaf.Semestral.service;

import br.com.fecaf.Semestral.exception.NotFoundExeption;
import br.com.fecaf.Semestral.model.*;
import br.com.fecaf.Semestral.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ConversaService {

    private final ConversaRepository conversaRepository;
    private final MensagemPrivadaRepository mensagemPrivadaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AmizadeRepository amizadeRepository;

    public ConversaService(ConversaRepository conversaRepository,
                           MensagemPrivadaRepository mensagemPrivadaRepository,
                           UsuarioRepository usuarioRepository,
                           AmizadeRepository amizadeRepository) {
        this.conversaRepository       = conversaRepository;
        this.mensagemPrivadaRepository = mensagemPrivadaRepository;
        this.usuarioRepository         = usuarioRepository;
        this.amizadeRepository         = amizadeRepository;
    }

    public List<Conversa> listarConversas(Long userId) {
        return conversaRepository.findByUsuarioId(userId);
    }

    public List<MensagemPrivada> listarMensagens(Long conversaId) {
        return mensagemPrivadaRepository.findByConversaIdOrderByDataEnvioAsc(conversaId);
    }

    @Transactional
    public MensagemPrivada enviarMensagem(Long remetenteId, Long destinatarioId, String texto) {
        // Validates friendship
        amizadeRepository.findAmizadeEntreUsuarios(remetenteId, destinatarioId)
                .filter(a -> a.getStatus() == AmizadeStatus.ACEITA)
                .orElseThrow(() -> new IllegalStateException("Vocês precisam ser amigos para trocar mensagens."));

        Usuario remetente    = buscarUsuario(remetenteId);
        Usuario destinatario = buscarUsuario(destinatarioId);

        // Get or create conversation
        Conversa conversa = conversaRepository.findByUsuarios(remetenteId, destinatarioId)
                .orElseGet(() -> {
                    Conversa nova = new Conversa();
                    nova.setUsuario1(remetente);
                    nova.setUsuario2(destinatario);
                    return conversaRepository.save(nova);
                });

        String agora = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        MensagemPrivada msg = new MensagemPrivada();
        msg.setConversa(conversa);
        msg.setRemetente(remetente);
        msg.setTexto(texto);
        msg.setDataEnvio(agora);
        msg.setLida(false);

        MensagemPrivada salva = mensagemPrivadaRepository.save(msg);

        // Update conversation metadata
        conversa.setUltimaMensagem(texto.length() > 80 ? texto.substring(0, 80) + "…" : texto);
        conversa.setDataUltimaMensagem(agora);
        conversaRepository.save(conversa);

        return salva;
    }

    @Transactional
    public Conversa iniciarConversa(Long u1, Long u2) {
        return conversaRepository.findByUsuarios(u1, u2)
                .orElseGet(() -> {
                    Usuario usuario1 = buscarUsuario(u1);
                    Usuario usuario2 = buscarUsuario(u2);
                    Conversa nova = new Conversa();
                    nova.setUsuario1(usuario1);
                    nova.setUsuario2(usuario2);
                    return conversaRepository.save(nova);
                });
    }

    // ── helper ──────────────────────────────────────────────

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Usuário não encontrado: " + id));
    }
}
