package plenix.components.dao;

/**
 * EntityException.
 */
public class EntityException extends RuntimeException {
    /**
     *
     */
    public EntityException() {
        super();
    }

    /**
     * @param message
     */
    public EntityException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public EntityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public EntityException(Throwable cause) {
        super(cause);
    }
}
