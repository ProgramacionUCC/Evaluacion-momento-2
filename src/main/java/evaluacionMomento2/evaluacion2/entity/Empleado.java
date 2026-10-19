package evaluacionMomento2.evaluacion2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "empleados")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Empleado extends Usuario {
    private String cargo;
    private Double salario;

    public Empleado(Integer id, String nombre, String correo, String cargo, Double salario) {
        super(id, nombre, correo);
        this.cargo = cargo;
        this.salario = salario;
    }

    @Override
    public String obtenerDescripcion() {
        return "Empleado con cargo: " + cargo + " y salario: " + salario;
    }
}
