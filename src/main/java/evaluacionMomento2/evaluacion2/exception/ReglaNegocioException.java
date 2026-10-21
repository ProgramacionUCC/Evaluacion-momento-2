package evaluacionMomento2.evaluacion2.exception;

// Se lanza cuando no se cumple una regla de negocio; el GlobalExceptionHandler la responde con 400
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
