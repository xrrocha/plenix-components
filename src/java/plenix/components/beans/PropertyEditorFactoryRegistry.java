package plenix.components.beans;

public interface PropertyEditorFactoryRegistry {
    PropertyEditorFactory getFactoryFor(Class clazz);
}
