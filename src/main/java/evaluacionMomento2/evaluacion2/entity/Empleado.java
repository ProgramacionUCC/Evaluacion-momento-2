package evaluacionMomento2.evaluacion2.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("EMPLEADO")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Empleado extends Usuario {

    @NotBlank(message = "El cargo es obligatorio")
    private String cargo;

    @NotNull(message = "El salario es obligatorio")
    @Positive(message = "El salario debe ser mayor que cero")
    private Double salario;

    public Empleado(Long id, String nombre, String correo, String cargo, Double salario) {
        super(id, nombre, correo);
        this.cargo = cargo;
        this.salario = salario;
    }

    @Override
    public String obtenerDescripcion() {
        return "Empleado con cargo: " + cargo + " y salario: " + salario;
    }
}
