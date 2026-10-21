package evaluacionMomento2.evaluacion2.service.impl;

import evaluacionMomento2.evaluacion2.dto.request.SolicitudRequestDTO;
import evaluacionMomento2.evaluacion2.dto.response.PaginaResponseDTO;
import evaluacionMomento2.evaluacion2.dto.response.SolicitudResponseDTO;
import evaluacionMomento2.evaluacion2.entity.Cliente;
import evaluacionMomento2.evaluacion2.entity.Empleado;
import evaluacionMomento2.evaluacion2.entity.EstadoSolicitud;
import evaluacionMomento2.evaluacion2.entity.Solicitud;
import evaluacionMomento2.evaluacion2.exception.RecursoNoEncontradoException;
import evaluacionMomento2.evaluacion2.exception.ReglaNegocioException;
import evaluacionMomento2.evaluacion2.repository.ClienteRepository;
import evaluacionMomento2.evaluacion2.repository.EmpleadoRepository;
import evaluacionMomento2.evaluacion2.repository.SolicitudRepository;
import evaluacionMomento2.evaluacion2.service.GestorSolicitudes;
import evaluacionMomento2.evaluacion2.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl implements SolicitudService {

    private static final List<String> CAMPOS_ORDENABLES = List.of("id", "fechaCreacion", "estado");

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final GestorSolicitudes gestor;

    @Override
    @Transactional
    public SolicitudResponseDTO crearSolicitud(SolicitudRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un cliente con id " + dto.getClienteId()));

        Empleado empleado = null;
        if (dto.getEmpleadoId() != null) {
            empleado = empleadoRepository.findById(dto.getEmpleadoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe un empleado con id " + dto.getEmpleadoId()));
        }

        Solicitud solicitud = Solicitud.builder()
                .descripcion(dto.getDescripcion())
                .cliente(cliente)
                .empleado(empleado)
                .build();

        Solicitud guardada = solicitudRepository.save(solicitud);
        gestor.agregar(guardada);
        return convertir(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<SolicitudResponseDTO> listarSolicitudes(int pagina, int tamano, String ordenarPor,
                                                                      String direccion, String estado) {
        PageRequest paginacion = crearPaginacion(pagina, tamano, ordenarPor, direccion);

        Page<Solicitud> solicitudes;
        if (estado == null || estado.isBlank()) {
            solicitudes = solicitudRepository.findAll(paginacion);
        } else {
            solicitudes = solicitudRepository.findByEstado(convertirEstado(estado), paginacion);
        }

        List<SolicitudResponseDTO> contenido = new ArrayList<>();
        for (Solicitud solicitud : solicitudes.getContent()) {
            contenido.add(convertir(solicitud));
        }

        return PaginaResponseDTO.<SolicitudResponseDTO>builder()
                .contenido(contenido)
                .pagina(solicitudes.getNumber())
                .tamano(solicitudes.getSize())
                .totalElementos(solicitudes.getTotalElements())
                .totalPaginas(solicitudes.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudResponseDTO buscarPorId(Long id) {
        return solicitudRepository.findById(id)
                .map(this::convertir)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una solicitud con id " + id));
    }

    @Override
    public List<SolicitudResponseDTO> verCola() {
        return convertirLista(gestor.verCola());
    }

    @Override
    public List<SolicitudResponseDTO> verHistorial() {
        return convertirLista(gestor.verHistorial());
    }

    // Lo que el gestor marca como resuelta (o pendiente al deshacer) tambien se guarda en la base de datos
    @Override
    @Transactional
    public SolicitudResponseDTO procesarSiguiente() {
        Solicitud solicitud = gestor.procesarSiguiente();
        solicitudRepository.save(solicitud);
        return convertir(solicitud);
    }

    @Override
    @Transactional
    public List<SolicitudResponseDTO> procesarTodas() {
        List<Solicitud> procesadas = gestor.procesarTodas();
        solicitudRepository.saveAll(procesadas);
        return convertirLista(procesadas);
    }

    @Override
    @Transactional
    public SolicitudResponseDTO deshacerUltimo() {
        Solicitud solicitud = gestor.deshacerUltimo();
        solicitudRepository.save(solicitud);
        return convertir(solicitud);
    }

    @Override
    public List<SolicitudResponseDTO> buscarPorTexto(String texto) {
        return convertirLista(gestor.buscarPorTexto(texto));
    }

    @Override
    public SolicitudResponseDTO buscarEnMemoria(Long id) {
        Solicitud solicitud = gestor.buscarPorId(id);
        if (solicitud == null) {
            throw new RecursoNoEncontradoException("No existe una solicitud con id " + id + " en memoria");
        }
        return convertir(solicitud);
    }

    @Override
    public List<SolicitudResponseDTO> ordenar(String criterio) {
        return convertirLista(gestor.ordenar(criterio));
    }

    private List<SolicitudResponseDTO> convertirLista(List<Solicitud> solicitudes) {
        List<SolicitudResponseDTO> respuesta = new ArrayList<>();
        for (Solicitud solicitud : solicitudes) {
            respuesta.add(convertir(solicitud));
        }
        return respuesta;
    }

    private PageRequest crearPaginacion(int pagina, int tamano, String ordenarPor, String direccion) {
        if (pagina < 0 || tamano < 1) {
            throw new ReglaNegocioException("La página debe ser >= 0 y el tamaño >= 1");
        }
        if (!CAMPOS_ORDENABLES.contains(ordenarPor)) {
            throw new ReglaNegocioException("Solo se puede ordenar por: " + CAMPOS_ORDENABLES);
        }
        Sort.Direction orden = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(direccion)) {
            orden = Sort.Direction.DESC;
        } else if (!"asc".equalsIgnoreCase(direccion)) {
            throw new ReglaNegocioException("La dirección debe ser asc o desc");
        }
        return PageRequest.of(pagina, tamano, Sort.by(orden, ordenarPor));
    }

    private EstadoSolicitud convertirEstado(String estado) {
        try {
            return EstadoSolicitud.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("Estado no válido. Use: PENDIENTE, EN_PROCESO, RESUELTA o CANCELADA");
        }
    }

    private SolicitudResponseDTO convertir(Solicitud solicitud) {
        Long empleadoId = null;
        if (solicitud.getEmpleado() != null) {
            empleadoId = solicitud.getEmpleado().getId();
        }
        return SolicitudResponseDTO.builder()
                .id(solicitud.getId())
                .descripcion(solicitud.getDescripcion())
                .estado(solicitud.getEstado().name())
                .fechaCreacion(solicitud.getFechaCreacion())
                .clienteId(solicitud.getCliente().getId())
                .clienteNombre(solicitud.getCliente().getNombre())
                .empleadoId(empleadoId)
                .build();
    }
}
