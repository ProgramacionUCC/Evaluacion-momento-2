package evaluacionMomento2.evaluacion2.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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

    private String cargo;
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
