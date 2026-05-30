package br.com.fecaf.Semestral.controler;

import br.com.fecaf.Semestral.model.Participacao;
import br.com.fecaf.Semestral.service.ParticipacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/participacoes")
public class ParticipacaoController {

    private final ParticipacaoService participacaoService;

    public ParticipacaoController(ParticipacaoService participacaoService) {
        this.participacaoService = participacaoService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Participacao>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(participacaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/ativos")
    public ResponseEntity<List<Participacao>> listarAtivos(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(participacaoService.listarAtivos(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/cancelados")
    public ResponseEntity<List<Participacao>> listarCancelados(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(participacaoService.listarCancelados(usuarioId));
    }

    @GetMapping("/verificar/{eventoId}/{usuarioId}")
    public ResponseEntity<Map<String, Object>> verificar(@PathVariable Long eventoId,
                                                         @PathVariable Long usuarioId) {
        Optional<Participacao> opt = participacaoService.verificar(eventoId, usuarioId);
        return ResponseEntity.ok(Map.of(
                "participando", opt.isPresent() && opt.get().getStatus().name().equals("ATIVO"),
                "participacaoId", opt.map(Participacao::getId).orElse(null)
        ));
    }

    @GetMapping("/evento/{eventoId}/contagem")
    public ResponseEntity<Map<String, Long>> contar(@PathVariable Long eventoId) {
        return ResponseEntity.ok(Map.of("total", participacaoService.contarParticipantes(eventoId)));
    }

    @PostMapping("/{eventoId}/participar")
    public ResponseEntity<Participacao> participar(@PathVariable Long eventoId,
                                                   @RequestParam Long usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(participacaoService.participar(eventoId, usuarioId));
    }

    @PatchMapping("/{eventoId}/cancelar")
    public ResponseEntity<Participacao> cancelar(@PathVariable Long eventoId,
                                                 @RequestParam Long usuarioId) {
        return ResponseEntity.ok(participacaoService.cancelar(eventoId, usuarioId));
    }
}
