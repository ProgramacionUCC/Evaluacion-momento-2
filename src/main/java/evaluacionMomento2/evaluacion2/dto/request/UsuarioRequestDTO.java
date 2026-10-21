package evaluacionMomento2.evaluacion2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// Un solo DTO para los tres roles: tipoUsuario decide cual se crea y se usan los campos propios de ese rol
@Getter
@Setter
public class UsuarioRequestDTO {

    @NotBlank(message = "El tipo de usuario es obligatorio (ADMINISTRADOR, EMPLEADO o CLIENTE)")
    private String tipoUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    // Solo ADMINISTRADOR
    private Integer nivelAcceso;

    // Solo EMPLEADO
    private String cargo;
    private Double salario;

    // Solo CLIENTE (si no se envia, empieza en 0)
    private Integer puntosFidelidad;
}
