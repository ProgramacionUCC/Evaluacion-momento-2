package evaluacionMomento2.evaluacion2.service;

import evaluacionMomento2.evaluacion2.dto.request.UsuarioRequestDTO;
import evaluacionMomento2.evaluacion2.dto.response.PaginaResponseDTO;
import evaluacionMomento2.evaluacion2.dto.response.UsuarioResponseDTO;

public interface UsuarioService {

    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto);

    PaginaResponseDTO<UsuarioResponseDTO> listarUsuarios(int pagina, int tamano, String ordenarPor, String direccion);

    UsuarioResponseDTO buscarPorId(Long id);
}
