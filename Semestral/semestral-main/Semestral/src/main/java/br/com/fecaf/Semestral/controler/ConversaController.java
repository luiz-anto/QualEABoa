package br.com.fecaf.Semestral.controler;

import br.com.fecaf.Semestral.dto.EnviarMensagemRequest;
import br.com.fecaf.Semestral.model.Conversa;
import br.com.fecaf.Semestral.model.MensagemPrivada;
import br.com.fecaf.Semestral.service.ConversaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversas")
public class ConversaController {

    private final ConversaService conversaService;

    public ConversaController(ConversaService conversaService) {
        this.conversaService = conversaService;
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Conversa>> listarConversas(@PathVariable Long userId) {
        return ResponseEntity.ok(conversaService.listarConversas(userId));
    }

    @GetMapping("/{conversaId}/mensagens")
    public ResponseEntity<List<MensagemPrivada>> listarMensagens(@PathVariable Long conversaId) {
        return ResponseEntity.ok(conversaService.listarMensagens(conversaId));
    }

    @PostMapping("/mensagem")
    public ResponseEntity<MensagemPrivada> enviarMensagem(@RequestBody EnviarMensagemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conversaService.enviarMensagem(
                        request.remetenteId(),
                        request.destinatarioId(),
                        request.texto()));
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Conversa> iniciarConversa(@RequestParam Long u1, @RequestParam Long u2) {
        return ResponseEntity.ok(conversaService.iniciarConversa(u1, u2));
    }
}
