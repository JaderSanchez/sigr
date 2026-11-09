package co.edu.tecnologicadeloriente.sigr.servicio;

/** Error de negocio: la petición es válida en forma pero viola una regla del restaurante. */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) { super(mensaje); }
}
