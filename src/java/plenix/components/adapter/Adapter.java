package plenix.components.adapter;

/**
 * Adapter.
 */
public interface Adapter {
    Object adapt(Object source) throws AdapterException;
}
