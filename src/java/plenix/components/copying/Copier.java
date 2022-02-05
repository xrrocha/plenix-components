package plenix.components.copying;

/**
 * Copier.
 */
public interface Copier {
    public void copy(Object context) throws CopyingException;
}