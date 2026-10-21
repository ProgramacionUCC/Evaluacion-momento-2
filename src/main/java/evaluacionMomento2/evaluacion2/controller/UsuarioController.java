package evaluacionMomento2.evaluacion2.controller;

import evaluacionMomento2.evaluacion2.dto.request.UsuarioRequestDTO;
import evaluacionMomento2.evaluacion2.dto.response.PaginaResponseDTO;
import evaluacionMomento2.evaluacion2.dto.response.UsuarioResponseDTO;
import evaluacionMomento2.evaluacion2.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // POST http://localhost:8080/usuarios
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(dto));
    }

    // GET http://localhost:8080/usuarios?pagina=0&tamano=10&ordenarPor=nombre&direccion=asc
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<UsuarioResponseDTO>> listarUsuarios(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano,
            @RequestParam(defaultValue = "id") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direccion) {
        return ResponseEntity.ok(usuarioService.listarUsuarios(pagina, tamano, ordenarPor, direccion));
    }

    // GET http://localhost:8080/usuarios/1
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}
