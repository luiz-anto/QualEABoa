package br.com.fecaf.Semestral.service;

import br.com.fecaf.Semestral.exception.NotFoundExeption;
import br.com.fecaf.Semestral.model.*;
import br.com.fecaf.Semestral.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipacaoService {

    private final ParticipacaoRepository participacaoRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final IngressoService ingressoService;

    public ParticipacaoService(ParticipacaoRepository participacaoRepository,
                                EventoRepository eventoRepository,
                                UsuarioRepository usuarioRepository,
                                IngressoService ingressoService) {
        this.participacaoRepository = participacaoRepository;
        this.eventoRepository       = eventoRepository;
        this.usuarioRepository      = usuarioRepository;
        this.ingressoService        = ingressoService;
    }

    public List<Participacao> listarPorUsuario(Long usuarioId) {
        return participacaoRepository.findByUsuarioId(usuarioId);
    }

    public List<Participacao> listarAtivos(Long usuarioId) {
        return participacaoRepository.findByUsuarioIdAtivos(usuarioId);
    }

    public List<Participacao> listarCancelados(Long usuarioId) {
        return participacaoRepository.findByUsuarioIdCancelados(usuarioId);
    }

    public Optional<Participacao> verificar(Long eventoId, Long usuarioId) {
        return participacaoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId);
    }

    public long contarParticipantes(Long eventoId) {
        return participacaoRepository.countByEventoIdAndStatus(eventoId, ParticipacaoStatus.ATIVO);
    }

    @Transactional
    public Participacao participar(Long eventoId, Long usuarioId) {
        Evento evento   = buscarEvento(eventoId);
        Usuario usuario = buscarUsuario(usuarioId);

        Optional<Participacao> existente = participacaoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId);

        if (existente.isPresent()) {
            Participacao p = existente.get();
            if (p.getStatus() == ParticipacaoStatus.ATIVO) {
                throw new IllegalStateException("Você já está participando deste evento.");
            }
            // Reactivate cancelled participation
            p.setStatus(ParticipacaoStatus.ATIVO);
            p.setDataParticipacao(agora());
            Participacao salva = participacaoRepository.save(p);
            atualizarContagem(evento);
            return salva;
        }

        Participacao nova = new Participacao();
        nova.setUsuario(usuario);
        nova.setEvento(evento);
        nova.setStatus(ParticipacaoStatus.ATIVO);
        nova.setDataParticipacao(agora());

        Participacao salva = participacaoRepository.save(nova);

        // Generate ingresso for this participant
        ingressoService.criarParaEvento(evento, usuario);

        atualizarContagem(evento);
        return salva;
    }

    @Transactional
    public Participacao cancelar(Long eventoId, Long usuarioId) {
        Participacao p = participacaoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId)
                .orElseThrow(() -> new NotFoundExeption("Participação não encontrada."));
        p.setStatus(ParticipacaoStatus.CANCELADO);
        Participacao salva = participacaoRepository.save(p);
        atualizarContagem(p.getEvento());
        return salva;
    }

    // ── helpers ─────────────────────────────────────────────

    private void atualizarContagem(Evento evento) {
        long total = participacaoRepository.countByEventoIdAndStatus(evento.getId(), ParticipacaoStatus.ATIVO);
        evento.setTotalParticipantes((int) total);
        eventoRepository.save(evento);
    }

    private Evento buscarEvento(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Evento não encontrado: " + id));
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Usuário não encontrado: " + id));
    }

    private String agora() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
