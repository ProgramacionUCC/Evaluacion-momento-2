package evaluacionMomento2.evaluacion2.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
