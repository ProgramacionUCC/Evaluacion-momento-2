package evaluacionMomento2.evaluacion2.controller;

import evaluacionMomento2.evaluacion2.dto.request.SolicitudRequestDTO;
import evaluacionMomento2.evaluacion2.dto.response.PaginaResponseDTO;
import evaluacionMomento2.evaluacion2.dto.response.SolicitudResponseDTO;
import evaluacionMomento2.evaluacion2.service.SolicitudService;
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
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    // POST http://localhost:8080/solicitudes
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> crearSolicitud(@Valid @RequestBody SolicitudRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudService.crearSolicitud(dto));
    }

    // GET http://localhost:8080/solicitudes?pagina=0&tamano=10&ordenarPor=fechaCreacion&direccion=desc&estado=PENDIENTE
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<SolicitudResponseDTO>> listarSolicitudes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano,
            @RequestParam(defaultValue = "id") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direccion,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(solicitudService.listarSolicitudes(pagina, tamano, ordenarPor, direccion, estado));
    }

    // GET http://localhost:8080/solicitudes/1
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.buscarPorId(id));
    }
}
