package plenix.components.copying;


/**
 * Producer.
 */
public interface Producer {
    /**
     * Produce a new discrete object.
     *
     * @return The newly produced object or <code>null</code> to indicate there
     * are no more objects to be returned.
     * @throws CopyingException
     */
    Object produce() throws CopyingException;
}
