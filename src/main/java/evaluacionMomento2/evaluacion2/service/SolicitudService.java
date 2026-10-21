package evaluacionMomento2.evaluacion2.service;

import evaluacionMomento2.evaluacion2.dto.request.SolicitudRequestDTO;
import evaluacionMomento2.evaluacion2.dto.response.PaginaResponseDTO;
import evaluacionMomento2.evaluacion2.dto.response.SolicitudResponseDTO;

public interface SolicitudService {

    SolicitudResponseDTO crearSolicitud(SolicitudRequestDTO dto);

    // estado es opcional: si es null se listan todas
    PaginaResponseDTO<SolicitudResponseDTO> listarSolicitudes(int pagina, int tamano, String ordenarPor,
                                                              String direccion, String estado);

    SolicitudResponseDTO buscarPorId(Long id);
}
