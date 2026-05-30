package br.com.fecaf.Semestral.service;

import br.com.fecaf.Semestral.exception.NotFoundExeption;
import br.com.fecaf.Semestral.model.Mensagem;
import br.com.fecaf.Semestral.repository.MensagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemService {

    private final MensagemRepository mensagemRepository;

    public MensagemService(MensagemRepository mensagemRepository) {
        this.mensagemRepository = mensagemRepository;
    }

    public List<Mensagem> listarPorUsuario(Long usuarioId) {
        return mensagemRepository.findByUsuarioId(usuarioId);
    }

    public Mensagem marcarComoLida(Long id) {
        Mensagem mensagem = mensagemRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Mensagem não encontrada: " + id));
        mensagem.setLida(true);
        return mensagemRepository.save(mensagem);
    }
}
