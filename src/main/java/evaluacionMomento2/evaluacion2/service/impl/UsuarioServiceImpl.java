package evaluacionMomento2.evaluacion2.service.impl;

import evaluacionMomento2.evaluacion2.dto.request.UsuarioRequestDTO;
import evaluacionMomento2.evaluacion2.dto.response.PaginaResponseDTO;
import evaluacionMomento2.evaluacion2.dto.response.UsuarioResponseDTO;
import evaluacionMomento2.evaluacion2.entity.Administrador;
import evaluacionMomento2.evaluacion2.entity.Cliente;
import evaluacionMomento2.evaluacion2.entity.Empleado;
import evaluacionMomento2.evaluacion2.entity.Usuario;
import evaluacionMomento2.evaluacion2.exception.RecursoNoEncontradoException;
import evaluacionMomento2.evaluacion2.exception.ReglaNegocioException;
import evaluacionMomento2.evaluacion2.repository.UsuarioRepository;
import evaluacionMomento2.evaluacion2.service.UsuarioService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private static final List<String> CAMPOS_ORDENABLES = List.of("id", "nombre", "correo");

    private final UsuarioRepository usuarioRepository;
    private final Validator validator;

    @Override
    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new ReglaNegocioException("El correo ya está registrado");
        }

        // switch: segun el tipo se arma la subclase correspondiente
        Usuario usuario;
        switch (dto.getTipoUsuario().trim().toUpperCase()) {
            case "ADMINISTRADOR" -> usuario = Administrador.builder()
                    .nombre(dto.getNombre())
                    .correo(dto.getCorreo())
                    .nivelAcceso(dto.getNivelAcceso())
                    .build();
            case "EMPLEADO" -> usuario = Empleado.builder()
                    .nombre(dto.getNombre())
                    .correo(dto.getCorreo())
                    .cargo(dto.getCargo())
                    .salario(dto.getSalario())
                    .build();
            case "CLIENTE" -> {
                int puntos = 0;
                if (dto.getPuntosFidelidad() != null) {
                    puntos = dto.getPuntosFidelidad();
                }
                usuario = Cliente.builder()
                        .nombre(dto.getNombre())
                        .correo(dto.getCorreo())
                        .puntosFidelidad(puntos)
                        .build();
            }
            default -> throw new ReglaNegocioException("Tipo de usuario no válido: " + dto.getTipoUsuario());
        }

        // Aplica las validaciones de la entidad (nivel 1-5, salario > 0...) antes de guardar
        validarEntidad(usuario);

        return convertir(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<UsuarioResponseDTO> listarUsuarios(int pagina, int tamano, String ordenarPor, String direccion) {
        PageRequest paginacion = crearPaginacion(pagina, tamano, ordenarPor, direccion);
        Page<Usuario> usuarios = usuarioRepository.findAll(paginacion);

        List<UsuarioResponseDTO> contenido = new ArrayList<>();
        for (Usuario usuario : usuarios.getContent()) {
            contenido.add(convertir(usuario));
        }

        return PaginaResponseDTO.<UsuarioResponseDTO>builder()
                .contenido(contenido)
                .pagina(usuarios.getNumber())
                .tamano(usuarios.getSize())
                .totalElementos(usuarios.getTotalElements())
                .totalPaginas(usuarios.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertir)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con id " + id));
    }

    private void validarEntidad(Usuario usuario) {
        Set<ConstraintViolation<Usuario>> errores = validator.validate(usuario);
        if (!errores.isEmpty()) {
            List<String> mensajes = new ArrayList<>();
            for (ConstraintViolation<Usuario> error : errores) {
                mensajes.add(error.getMessage());
            }
            throw new ReglaNegocioException(String.join("; ", mensajes));
        }
    }

    // Revisa los parametros de la URL y arma la pagina pedida (evita errores 500 por datos raros)
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

    private UsuarioResponseDTO convertir(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .tipoUsuario(usuario.getClass().getSimpleName().toUpperCase())
                .descripcion(usuario.obtenerDescripcion())
                .build();
    }
}
