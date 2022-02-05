package plenix.components.copying;


/**
 * Consumer.
 */
public interface Consumer {
    void consume(Object object) throws CopyingException;

    void done() throws CopyingException;
}
