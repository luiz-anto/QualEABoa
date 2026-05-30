package br.com.fecaf.Semestral.service;

import br.com.fecaf.Semestral.model.Evento;
import br.com.fecaf.Semestral.model.Ingresso;
import br.com.fecaf.Semestral.model.Usuario;
import br.com.fecaf.Semestral.repository.IngressoRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class IngressoService {

    private final IngressoRepository ingressoRepository;

    public IngressoService(IngressoRepository ingressoRepository) {
        this.ingressoRepository = ingressoRepository;
    }

    public List<Ingresso> listarPorUsuario(Long usuarioId) {
        return ingressoRepository.findByUsuarioId(usuarioId);
    }

    public Ingresso criarParaEvento(Evento evento, Usuario usuario) {
        Ingresso ingresso = new Ingresso();
        ingresso.setEventNome(evento.getNome());
        ingresso.setEventData(evento.getData());
        ingresso.setEventLocal(evento.getLocal());
        ingresso.setStatus("valid");
        ingresso.setEvento(evento);
        ingresso.setUsuario(usuario);

        Ingresso salvo = ingressoRepository.save(ingresso);

        String codigo = String.format("EVT-%d-%05d-A", Year.now().getValue(), salvo.getId());
        salvo.setCodigo(codigo);
        return ingressoRepository.save(salvo);
    }
}
