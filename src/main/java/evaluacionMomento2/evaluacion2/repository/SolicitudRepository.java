package evaluacionMomento2.evaluacion2.repository;

import evaluacionMomento2.evaluacion2.entity.EstadoSolicitud;
import evaluacionMomento2.evaluacion2.entity.Solicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findByClienteId(Long clienteId);

    Page<Solicitud> findByEstado(EstadoSolicitud estado, Pageable pageable);
}
