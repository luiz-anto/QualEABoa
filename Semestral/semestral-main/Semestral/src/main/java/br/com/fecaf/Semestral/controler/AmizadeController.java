package br.com.fecaf.Semestral.controler;

import br.com.fecaf.Semestral.dto.EnviarSolicitacaoRequest;
import br.com.fecaf.Semestral.model.Amizade;
import br.com.fecaf.Semestral.service.AmizadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amizades")
public class AmizadeController {

    private final AmizadeService amizadeService;

    public AmizadeController(AmizadeService amizadeService) {
        this.amizadeService = amizadeService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<Amizade> solicitar(@RequestBody EnviarSolicitacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(amizadeService.enviarSolicitacao(request.remetenteId(), request.destinatarioId()));
    }

    @PatchMapping("/{id}/aceitar")
    public ResponseEntity<Amizade> aceitar(@PathVariable Long id,
                                           @RequestParam Long usuarioId) {
        return ResponseEntity.ok(amizadeService.aceitar(id, usuarioId));
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<Amizade> recusar(@PathVariable Long id,
                                           @RequestParam Long usuarioId) {
        return ResponseEntity.ok(amizadeService.recusar(id, usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id,
                                        @RequestParam Long usuarioId) {
        amizadeService.remover(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/amigos/{userId}")
    public ResponseEntity<List<Amizade>> listarAmigos(@PathVariable Long userId) {
        return ResponseEntity.ok(amizadeService.listarAmigos(userId));
    }

    @GetMapping("/pendentes/{userId}")
    public ResponseEntity<List<Amizade>> listarPendentes(@PathVariable Long userId) {
        return ResponseEntity.ok(amizadeService.listarPendentes(userId));
    }

    @GetMapping("/status")
    public ResponseEntity<String> verificarStatus(@RequestParam Long u1, @RequestParam Long u2) {
        return ResponseEntity.ok(amizadeService.verificarStatus(u1, u2));
    }
}
