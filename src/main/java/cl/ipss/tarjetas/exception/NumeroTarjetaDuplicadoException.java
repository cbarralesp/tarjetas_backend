package cl.ipss.tarjetas.exception;

/** Se lanza al registrar un número de tarjeta que ya existe (HTTP 409). */
public class NumeroTarjetaDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NumeroTarjetaDuplicadoException(String numero) {
        super("Ya existe una tarjeta registrada con el número " + numero);
    }
}
