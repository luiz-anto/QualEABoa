package br.com.fecaf.Semestral.controler;

import br.com.fecaf.Semestral.dto.CancelarEventoRequest;
import br.com.fecaf.Semestral.dto.CriarEventoRequest;
import br.com.fecaf.Semestral.model.Evento;
import br.com.fecaf.Semestral.service.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public ResponseEntity<List<Evento>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long usuarioId) {
        return ResponseEntity.ok(eventoService.listar(status, usuarioId));
    }

    @PostMapping
    public ResponseEntity<Evento> criar(@RequestBody CriarEventoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.criar(request));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Evento> cancelar(@PathVariable Long id,
                                           @RequestBody CancelarEventoRequest request) {
        return ResponseEntity.ok(eventoService.cancelar(id, request.motivo()));
    }

    @GetMapping("/privado/{id}")
    public ResponseEntity<Evento> buscarPrivado(@PathVariable Long id,
                                                @RequestParam Long usuarioId) {
        return ResponseEntity.ok(eventoService.buscarPrivadoPorId(id, usuarioId));
    }
}
