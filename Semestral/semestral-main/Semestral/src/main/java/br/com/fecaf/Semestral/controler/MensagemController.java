package br.com.fecaf.Semestral.controler;

import br.com.fecaf.Semestral.model.Mensagem;
import br.com.fecaf.Semestral.service.MensagemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensagens")
public class MensagemController {

    private final MensagemService mensagemService;

    public MensagemController(MensagemService mensagemService) {
        this.mensagemService = mensagemService;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Mensagem>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(mensagemService.listarPorUsuario(usuarioId));
    }

    @PatchMapping("/{id}/lida")
    public ResponseEntity<Mensagem> marcarLida(@PathVariable Long id) {
        return ResponseEntity.ok(mensagemService.marcarComoLida(id));
    }
}
