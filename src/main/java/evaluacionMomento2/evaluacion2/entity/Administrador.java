package evaluacionMomento2.evaluacion2.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ADMINISTRADOR")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Administrador extends Usuario {

    @NotNull(message = "El nivel de acceso es obligatorio")
    @Min(value = 1, message = "El nivel de acceso debe ser mínimo 1")
    @Max(value = 5, message = "El nivel de acceso debe ser máximo 5")
    private Integer nivelAcceso;

    public Administrador(Long id, String nombre, String correo, Integer nivelAcceso) {
        super(id, nombre, correo);
        this.nivelAcceso = nivelAcceso;
    }

    @Override
    public String obtenerDescripcion() {
        return "Administrador con nivel de acceso: " + nivelAcceso;
    }
}
