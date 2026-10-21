package evaluacionMomento2.evaluacion2.controller;

import evaluacionMomento2.evaluacion2.dto.request.SolicitudRequestDTO;
import evaluacionMomento2.evaluacion2.dto.response.PaginaResponseDTO;
import evaluacionMomento2.evaluacion2.dto.response.SolicitudResponseDTO;
import evaluacionMomento2.evaluacion2.service.SolicitudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

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

    // GET http://localhost:8080/solicitudes/cola  -> pendientes en orden de llegada
    @GetMapping("/cola")
    public ResponseEntity<List<SolicitudResponseDTO>> verCola() {
        return ResponseEntity.ok(solicitudService.verCola());
    }

    // GET http://localhost:8080/solicitudes/historial  -> procesadas, la mas reciente primero
    @GetMapping("/historial")
    public ResponseEntity<List<SolicitudResponseDTO>> verHistorial() {
        return ResponseEntity.ok(solicitudService.verHistorial());
    }

    // POST http://localhost:8080/solicitudes/procesar  -> atiende la primera de la cola
    @PostMapping("/procesar")
    public ResponseEntity<SolicitudResponseDTO> procesarSiguiente() {
        return ResponseEntity.ok(solicitudService.procesarSiguiente());
    }

    // POST http://localhost:8080/solicitudes/procesar-todas  -> vacia la cola
    @PostMapping("/procesar-todas")
    public ResponseEntity<List<SolicitudResponseDTO>> procesarTodas() {
        return ResponseEntity.ok(solicitudService.procesarTodas());
    }

    // POST http://localhost:8080/solicitudes/deshacer  -> devuelve la ultima procesada a la cola
    @PostMapping("/deshacer")
    public ResponseEntity<SolicitudResponseDTO> deshacerUltimo() {
        return ResponseEntity.ok(solicitudService.deshacerUltimo());
    }

    // GET http://localhost:8080/solicitudes/buscar?texto=cuenta
    @GetMapping("/buscar")
    public ResponseEntity<List<SolicitudResponseDTO>> buscarPorTexto(@RequestParam String texto) {
        return ResponseEntity.ok(solicitudService.buscarPorTexto(texto));
    }

    // GET http://localhost:8080/solicitudes/ordenadas?criterio=fecha  (id, fecha o estado)
    @GetMapping("/ordenadas")
    public ResponseEntity<List<SolicitudResponseDTO>> ordenar(@RequestParam(defaultValue = "id") String criterio) {
        return ResponseEntity.ok(solicitudService.ordenar(criterio));
    }

    // GET http://localhost:8080/solicitudes/memoria/1  -> busqueda binaria en la lista en memoria
    @GetMapping("/memoria/{id}")
    public ResponseEntity<SolicitudResponseDTO> buscarEnMemoria(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.buscarEnMemoria(id));
    }

    // GET http://localhost:8080/solicitudes/1
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.buscarPorId(id));
    }
}
