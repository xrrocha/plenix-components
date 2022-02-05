package plenix.components.beans;

import java.beans.PropertyEditor;

public interface PropertyEditorFactory {
    PropertyEditor newInstance();
}
