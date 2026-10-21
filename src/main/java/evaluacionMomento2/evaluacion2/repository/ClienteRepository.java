package evaluacionMomento2.evaluacion2.repository;

import evaluacionMomento2.evaluacion2.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
