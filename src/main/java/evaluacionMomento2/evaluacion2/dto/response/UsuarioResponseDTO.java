package evaluacionMomento2.evaluacion2.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String tipoUsuario;
    private String descripcion;  // Resultado de obtenerDescripcion() (polimorfismo)
}
