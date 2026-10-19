package evaluacionMomento2.evaluacion2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Cliente extends Usuario {
    private Integer puntosFidelidad;

    public Cliente(Integer id, String nombre, String correo, Integer puntosFidelidad) {
        super(id, nombre, correo);
        this.puntosFidelidad = puntosFidelidad;
    }

    @Override
    public String obtenerDescripcion() {
        return "Cliente con puntos de fidelidad: " + puntosFidelidad;
    }
}
