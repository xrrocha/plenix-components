package plenix.components.beans;

import java.beans.PropertyEditor;
import java.util.Map;

public class EnumPropertyEditorFactory extends TransformingPropertyEditorFactory {
    private Map<String, ? extends Object> values;

    public PropertyEditor newInstance() {
        return new TransformingPropertyEditor() {
            protected Object convert(String text) {
                Object value = getValues().get(text);
                if (value == null) {
                    throw new IllegalArgumentException("Enumeration value cannot be null");
                }
                return value;
            }
        };
    }

    public Map<String, ? extends Object> getValues() {
        return values;
    }

    public void setValues(Map<String, ? extends Object> values) {
        this.values = values;
    }
}
