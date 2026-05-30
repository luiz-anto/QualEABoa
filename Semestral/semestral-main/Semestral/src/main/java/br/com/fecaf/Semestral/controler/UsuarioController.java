package br.com.fecaf.Semestral.controler;

import br.com.fecaf.Semestral.dto.LoginRequest;
import br.com.fecaf.Semestral.dto.UsuarioBuscaDTO;
import br.com.fecaf.Semestral.model.Usuario;
import br.com.fecaf.Semestral.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request.email(), request.senha()));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioBuscaDTO>> buscar(@RequestParam String q,
                                                         @RequestParam Long usuarioId) {
        return ResponseEntity.ok(usuarioService.buscarUsuarios(q, usuarioId));
    }
}
