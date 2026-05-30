package br.com.fecaf.Semestral.service;

import br.com.fecaf.Semestral.exception.NotFoundExeption;
import br.com.fecaf.Semestral.model.Evento;
import br.com.fecaf.Semestral.model.Notificacao;
import br.com.fecaf.Semestral.model.Usuario;
import br.com.fecaf.Semestral.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioId(usuarioId);
    }

    public void marcarComoLida(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Notificação não encontrada: " + id));
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }

    public Notificacao criarNotificacao(Usuario usuario, String icon, String titulo, String texto) {
        Notificacao n = new Notificacao();
        n.setIcon(icon);
        n.setTitulo(titulo);
        n.setTexto(texto);
        n.setHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        n.setLida(false);
        n.setUsuario(usuario);
        return notificacaoRepository.save(n);
    }

    public Notificacao criarParaEvento(Evento evento, Usuario usuario) {
        Notificacao notificacao = new Notificacao();
        notificacao.setIcon("🎉");
        notificacao.setTitulo("Evento publicado!");
        notificacao.setTexto("Seu evento \"" + evento.getNome() + "\" foi publicado com sucesso.");
        notificacao.setHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        notificacao.setLida(false);
        notificacao.setUsuario(usuario);
        return notificacaoRepository.save(notificacao);
    }
}
