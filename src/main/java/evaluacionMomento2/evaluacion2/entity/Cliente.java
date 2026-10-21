package evaluacionMomento2.evaluacion2.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CLIENTE")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Cliente extends Usuario {

    private Integer puntosFidelidad;

    public Cliente(Long id, String nombre, String correo, Integer puntosFidelidad) {
        super(id, nombre, correo);
        this.puntosFidelidad = puntosFidelidad;
    }

    @Override
    public String obtenerDescripcion() {
        return "Cliente con puntos de fidelidad: " + puntosFidelidad;
    }
}
