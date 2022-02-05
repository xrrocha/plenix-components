package plenix.components.beans;

import java.beans.PropertyEditor;

public class LongPropertyEditorFactory extends TransformingPropertyEditorFactory {
    public PropertyEditor newInstance() {
        return new TransformingPropertyEditor() {
          protected Object convert(String text) {
              return Long.valueOf(text);    
          }
        };
    }
}
