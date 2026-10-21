package evaluacionMomento2.evaluacion2.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SolicitudResponseDTO {
    private Long id;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaCreacion;
    private Long clienteId;
    private String clienteNombre;
    private Long empleadoId;  // null si todavia no tiene empleado asignado
}
