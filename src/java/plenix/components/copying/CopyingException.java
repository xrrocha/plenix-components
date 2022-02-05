package plenix.components.copying;

/**
 * CopyingException.
 */
public class CopyingException extends RuntimeException {

    /**
     * 
     */
    public CopyingException() {
        super();
    }

    /**
     * @param message
     */
    public CopyingException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public CopyingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public CopyingException(Throwable cause) {
        super(cause);
    }

}
