package evaluacionMomento2.evaluacion2.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENTE")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Cliente extends Usuario {

    @Min(value = 0, message = "Los puntos de fidelidad no pueden ser negativos")
    private Integer puntosFidelidad;

    // Un cliente puede tener muchas solicitudes
    @OneToMany(mappedBy = "cliente")
    @Builder.Default
    private List<Solicitud> solicitudes = new ArrayList<>();

    public Cliente(Long id, String nombre, String correo, Integer puntosFidelidad) {
        super(id, nombre, correo);
        this.puntosFidelidad = puntosFidelidad;
    }

    @Override
    public String obtenerDescripcion() {
        return "Cliente con puntos de fidelidad: " + puntosFidelidad;
    }
}
