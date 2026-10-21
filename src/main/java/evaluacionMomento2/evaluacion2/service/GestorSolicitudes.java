package evaluacionMomento2.evaluacion2.service;

import evaluacionMomento2.evaluacion2.entity.EstadoSolicitud;
import evaluacionMomento2.evaluacion2.entity.Solicitud;
import evaluacionMomento2.evaluacion2.exception.ReglaNegocioException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Maneja las solicitudes en memoria con tres estructuras de datos:
 *  - Lista (ArrayList): todas las solicitudes registradas.
 *  - Cola (Queue): las pendientes, se atienden en orden de llegada (FIFO).
 *  - Pila (Stack): las ya procesadas; la ultima en procesarse es la primera en deshacerse (LIFO).
 *
 * Es un solo objeto compartido por toda la app, por eso los metodos son synchronized.
 */
@Component
public class GestorSolicitudes {

    private final List<Solicitud> solicitudes = new ArrayList<>();
    private final Queue<Solicitud> colaPendientes = new LinkedList<>();
    private final Stack<Solicitud> historial = new Stack<>();

    // Toda solicitud nueva entra a la lista; si esta pendiente, tambien al final de la cola
    public synchronized void agregar(Solicitud solicitud) {
        solicitudes.add(solicitud);
        if (solicitud.getEstado() == EstadoSolicitud.PENDIENTE) {
            colaPendientes.offer(solicitud);
        }
    }

    // Saca de la cola la solicitud que lleva mas tiempo esperando y la marca como resuelta
    public synchronized Solicitud procesarSiguiente() {
        Solicitud solicitud = colaPendientes.poll();
        if (solicitud == null) {
            throw new ReglaNegocioException("No hay solicitudes pendientes en la cola");
        }
        solicitud.setEstado(EstadoSolicitud.RESUELTA);
        historial.push(solicitud);
        return solicitud;
    }

    // Procesa toda la cola, una por una, hasta dejarla vacia
    public synchronized List<Solicitud> procesarTodas() {
        List<Solicitud> procesadas = new ArrayList<>();
        while (!colaPendientes.isEmpty()) {
            procesadas.add(procesarSiguiente());
        }
        return procesadas;
    }

    // Saca de la pila la ultima solicitud procesada y la devuelve a la cola como pendiente
    public synchronized Solicitud deshacerUltimo() {
        if (historial.isEmpty()) {
            throw new ReglaNegocioException("No hay acciones para deshacer");
        }
        Solicitud solicitud = historial.pop();
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        colaPendientes.offer(solicitud);
        return solicitud;
    }

    public synchronized List<Solicitud> verCola() {
        return new ArrayList<>(colaPendientes);
    }

    // El historial se muestra empezando por la accion mas reciente (el tope de la pila)
    public synchronized List<Solicitud> verHistorial() {
        List<Solicitud> recientes = new ArrayList<>();
        for (int i = historial.size() - 1; i >= 0; i--) {
            recientes.add(historial.get(i));
        }
        return recientes;
    }

    // Busqueda lineal: recorre toda la lista comparando el texto con la descripcion
    public synchronized List<Solicitud> buscarPorTexto(String texto) {
        List<Solicitud> encontradas = new ArrayList<>();
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.getDescripcion().toLowerCase().contains(texto.toLowerCase())) {
                encontradas.add(solicitud);
            }
        }
        return encontradas;
    }

    // Busqueda binaria por id: parte la lista ordenada a la mitad en cada paso
    public synchronized Solicitud buscarPorId(Long id) {
        List<Solicitud> ordenadas = ordenar("id");
        int inicio = 0;
        int fin = ordenadas.size() - 1;
        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            Long idMedio = ordenadas.get(medio).getId();
            if (idMedio.equals(id)) {
                return ordenadas.get(medio);
            } else if (idMedio < id) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return null;
    }

    // Ordenamiento por insercion: va colocando cada solicitud en su lugar dentro de la parte ya ordenada
    public synchronized List<Solicitud> ordenar(String criterio) {
        Comparator<Solicitud> comparador = switch (criterio) {
            case "id" -> Comparator.comparing(Solicitud::getId);
            case "fecha" -> Comparator.comparing(Solicitud::getFechaCreacion);
            case "estado" -> Comparator.comparing(Solicitud::getEstado);
            default -> throw new ReglaNegocioException("Criterio no válido. Use: id, fecha o estado");
        };

        List<Solicitud> resultado = new ArrayList<>(solicitudes);
        for (int i = 1; i < resultado.size(); i++) {
            Solicitud actual = resultado.get(i);
            int j = i - 1;
            while (j >= 0 && comparador.compare(resultado.get(j), actual) > 0) {
                resultado.set(j + 1, resultado.get(j));
                j--;
            }
            resultado.set(j + 1, actual);
        }
        return resultado;
    }
}
