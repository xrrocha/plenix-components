package plenix.components.copying;


/**
 * ConsumerFactory.
 */
public interface ConsumerFactory {
    Consumer newInstance(Object context) throws CopyingException;
}
