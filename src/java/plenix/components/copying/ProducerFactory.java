package plenix.components.copying;


/**
 * ProducerFactory.
 */
public interface ProducerFactory {
    Producer newInstance(Object context) throws CopyingException;
}
