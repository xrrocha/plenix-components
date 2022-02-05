package plenix.components.collections;

import plenix.components.adapter.Adapter;
import plenix.components.adapter.AdapterException;

import java.util.Map;

/**
 * Map2ArrayAdapter.
 */
public class Map2ArrayAdapter implements Adapter {
    private Object[] keys;

    /**
     * @see plenix.components.adapter.Adapter#adapt(java.lang.Object)
     */
    public Object adapt(Object source) throws AdapterException {
        Map map = (Map) source;
        Object[] result = new Object[this.keys.length];
        for (int i = 0; i < this.keys.length; i++) {
            result[i] = map.get(this.keys[i]);
        }
        return result;
    }

    public void setKeys(Object[] keys) {
        this.keys = keys;
    }

    public Object[] getKeys() {
        return keys;
    }

}
