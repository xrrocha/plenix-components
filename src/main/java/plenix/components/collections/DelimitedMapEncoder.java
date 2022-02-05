package plenix.components.collections;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * DelimitedMapEncoder.
 */
public class DelimitedMapEncoder implements MapEncoder {
    private String valueDelimiter = "=";
    private String variableDelimiter = ",";
    private PropertyEditor propertyEditor;

    public String encode(Map<String, ? extends Object> map) {
        if (map == null) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();

        for (Object name : map.keySet()) {
            Object value = map.get(name);
            if (buffer.length() > 0) {
                buffer.append(getVariableDelimiter());
            }
            buffer.append(name);
            buffer.append(getValueDelimiter());
            buffer.append(value);
        }

        return buffer.toString();
    }

    public Map<String, ? extends Object> decode(String string) {
        if (string == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        StringTokenizer vst = new StringTokenizer(string, getVariableDelimiter());
        while (vst.hasMoreTokens()) {
            String variable = vst.nextToken();
            StringTokenizer lst = new StringTokenizer(variable, getValueDelimiter());
            String key = lst.nextToken();
            String valueString = lst.nextToken();

            Object value = valueString;
            if (getPropertyEditor() != null) {
                getPropertyEditor().setAsText(valueString);
                value = getPropertyEditor().getValue();
            }

            result.put(key, value);
        }
        return result;
    }

    public void setValueDelimiter(String delimitadorValor) {
        this.valueDelimiter = delimitadorValor;
    }

    public String getValueDelimiter() {
        return valueDelimiter;
    }

    public void setVariableDelimiter(String delimitadorVariables) {
        this.variableDelimiter = delimitadorVariables;
    }

    public String getVariableDelimiter() {
        return variableDelimiter;
    }

    public void setPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }

    public PropertyEditor getPropertyEditor() {
        return propertyEditor;
    }
}
