package evaluacionMomento2.evaluacion2.service;

import evaluacionMomento2.evaluacion2.dto.request.SolicitudRequestDTO;
import evaluacionMomento2.evaluacion2.dto.response.PaginaResponseDTO;
import evaluacionMomento2.evaluacion2.dto.response.SolicitudResponseDTO;

import java.util.List;

public interface SolicitudService {

    SolicitudResponseDTO crearSolicitud(SolicitudRequestDTO dto);

    // estado es opcional: si es null se listan todas
    PaginaResponseDTO<SolicitudResponseDTO> listarSolicitudes(int pagina, int tamano, String ordenarPor,
                                                              String direccion, String estado);

    SolicitudResponseDTO buscarPorId(Long id);

    // Estructuras de datos (GestorSolicitudes)

    List<SolicitudResponseDTO> verCola();

    List<SolicitudResponseDTO> verHistorial();

    SolicitudResponseDTO procesarSiguiente();

    List<SolicitudResponseDTO> procesarTodas();

    SolicitudResponseDTO deshacerUltimo();

    List<SolicitudResponseDTO> buscarPorTexto(String texto);

    SolicitudResponseDTO buscarEnMemoria(Long id);

    List<SolicitudResponseDTO> ordenar(String criterio);
}
