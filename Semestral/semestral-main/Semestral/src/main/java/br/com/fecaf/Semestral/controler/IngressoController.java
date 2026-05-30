package br.com.fecaf.Semestral.controler;

import br.com.fecaf.Semestral.model.Ingresso;
import br.com.fecaf.Semestral.service.IngressoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingressos")
public class IngressoController {

    private final IngressoService ingressoService;

    public IngressoController(IngressoService ingressoService) {
        this.ingressoService = ingressoService;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Ingresso>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ingressoService.listarPorUsuario(usuarioId));
    }
}
