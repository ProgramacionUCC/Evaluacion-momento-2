package evaluacionMomento2.evaluacion2.repository;

import evaluacionMomento2.evaluacion2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreo(String correo);
}
