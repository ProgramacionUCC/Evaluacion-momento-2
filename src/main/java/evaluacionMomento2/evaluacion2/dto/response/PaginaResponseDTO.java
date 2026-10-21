package evaluacionMomento2.evaluacion2.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PaginaResponseDTO<T> {
    private List<T> contenido;
    private int pagina;           // Empieza en 0
    private int tamano;
    private long totalElementos;
    private int totalPaginas;
}
