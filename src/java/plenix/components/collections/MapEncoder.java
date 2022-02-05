package plenix.components.collections;

import java.util.Map;

/**
 * MapEncoder.
 */
public interface MapEncoder {
    // if map == null then return null
    public String encode(Map<String, ?extends Object> map);
    // if string == null then return null
    // if string == null then return null
    public Map<String, ?extends Object> decode(String string);
}
