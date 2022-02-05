package plenix.components.beans;

import plenix.components.string.transforming.StringTransformer;

import java.beans.PropertyEditorSupport;

public abstract class TransformingPropertyEditorFactory implements PropertyEditorFactory {
    private StringTransformer transformer;

    abstract class TransformingPropertyEditor extends PropertyEditorSupport {
        public void setAsText(String text) {
            if (getTransformer() != null) {
                text = getTransformer().transform(text);
            }
            setValue(convert(text));
        }
        protected abstract Object convert(String text);
    }

    public StringTransformer getTransformer() {
        return transformer;
    }

    public void setTransformer(StringTransformer transformer) {
        this.transformer = transformer;
    }
}
