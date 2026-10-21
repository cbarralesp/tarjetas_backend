package cl.ipss.tarjetas.exception;

/** Se lanza cuando no existe la tarjeta pedida (HTTP 404). */
public class TarjetaNoEncontradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TarjetaNoEncontradaException(Long id) {
        super("No existe una tarjeta con id " + id);
    }
}
