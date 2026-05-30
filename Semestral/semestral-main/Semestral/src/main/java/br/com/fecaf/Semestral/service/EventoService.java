package br.com.fecaf.Semestral.service;

import br.com.fecaf.Semestral.dto.CriarEventoRequest;
import br.com.fecaf.Semestral.exception.NotFoundExeption;
import br.com.fecaf.Semestral.model.Evento;
import br.com.fecaf.Semestral.model.Usuario;
import br.com.fecaf.Semestral.repository.EventoRepository;
import br.com.fecaf.Semestral.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final IngressoService ingressoService;
    private final NotificacaoService notificacaoService;

    public EventoService(EventoRepository eventoRepository,
                         UsuarioRepository usuarioRepository,
                         IngressoService ingressoService,
                         NotificacaoService notificacaoService) {
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.ingressoService = ingressoService;
        this.notificacaoService = notificacaoService;
    }

    public List<Evento> listar(String status, Long usuarioId) {
        if (usuarioId != null) {
            return eventoRepository.findVisibleByStatus(
                    status != null ? status : "active", usuarioId);
        }
        if (status != null) {
            return eventoRepository.findByStatusAndPrivadoFalse(status);
        }
        return eventoRepository.findByStatusAndPrivadoFalse("active");
    }

    public Evento buscarPrivadoPorId(Long id, Long usuarioId) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Evento não encontrado: " + id));
        if (Boolean.TRUE.equals(evento.getPrivado())) {
            boolean ehCriador = evento.getUsuarioId() != null && evento.getUsuarioId().equals(usuarioId);
            if (!ehCriador) {
                throw new NotFoundExeption("Você não tem permissão para visualizar este evento privado.");
            }
        }
        return evento;
    }

    @Transactional
    public Evento criar(CriarEventoRequest req) {
        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new NotFoundExeption("Usuário não encontrado: " + req.usuarioId()));

        Evento evento = new Evento();
        evento.setNome(req.nome());
        evento.setDescricao(req.descricao());
        evento.setLocal(req.local());
        evento.setData(req.data());
        evento.setCategoria(req.categoria());
        evento.setVagas(req.vagas());
        evento.setPreco(req.preco());
        if (req.coords() != null && req.coords().size() == 2) {
            evento.setCoordLng(req.coords().get(0));
            evento.setCoordLat(req.coords().get(1));
        }
        evento.setImgUrl(req.imgUrl());
        evento.setStatus("active");
        evento.setPrivado(req.privado() != null && req.privado());
        evento.setTotalParticipantes(1); // creator counts as first participant
        evento.setCriadoEm(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        evento.setOrganizadorNome(usuario.getNome());
        evento.setOrganizadorEmail(usuario.getEmail());
        evento.setUsuario(usuario);

        Evento salvo = eventoRepository.save(evento);

        ingressoService.criarParaEvento(salvo, usuario);
        notificacaoService.criarParaEvento(salvo, usuario);

        return salvo;
    }

    @Transactional
    public Evento cancelar(Long id, String motivo) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Evento não encontrado: " + id));
        evento.setStatus("cancelled");
        evento.setMotivoCancelamento(motivo);
        return eventoRepository.save(evento);
    }
}
