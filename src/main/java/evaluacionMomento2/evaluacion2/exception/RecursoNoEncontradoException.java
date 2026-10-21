package evaluacionMomento2.evaluacion2.exception;

// Se lanza cuando un id no existe en la base de datos; se responde con 404
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
