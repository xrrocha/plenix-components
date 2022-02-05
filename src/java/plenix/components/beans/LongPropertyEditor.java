package plenix.components.beans;

import java.beans.PropertyEditorSupport;

public class LongPropertyEditor extends PropertyEditorSupport {
    public void setAsText(String text) {
        setValue(Long.valueOf(text));
    }
}
