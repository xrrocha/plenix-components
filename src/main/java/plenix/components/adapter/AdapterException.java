package plenix.components.adapter;

/**
 * AdapterException.
 */
public class AdapterException extends Exception {

    /**
     *
     */
    public AdapterException() {
        super();
    }

    /**
     * @param message
     */
    public AdapterException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public AdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public AdapterException(Throwable cause) {
        super(cause);
    }

}
