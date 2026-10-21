package evaluacionMomento2.evaluacion2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudRequestDTO {

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El id del cliente es obligatorio")
    private Long clienteId;

    // Opcional: la solicitud puede crearse sin empleado asignado
    private Long empleadoId;
}
