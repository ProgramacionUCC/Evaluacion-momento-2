package evaluacionMomento2.evaluacion2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "administradores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Administrador extends Usuario {
    private Integer nivelAcceso;

    public Administrador(Integer id, String nombre, String correo, Integer nivelAcceso) {
        super(id, nombre, correo);
        this.nivelAcceso = nivelAcceso;
    }

    @Override
    public String obtenerDescripcion() {
        return "Administrador con nivel de acceso: " + nivelAcceso;
    }
}
