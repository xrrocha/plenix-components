package plenix.components.beans;

import java.util.Map;

public class MapBasedPropertyEditorFactoryRegistry implements PropertyEditorFactoryRegistry {
    private Map<String, PropertyEditorFactory> factories;

    public PropertyEditorFactory getFactoryFor(Class clazz) {
        return  factories.get(clazz.getName());
    }

    public Map<String, PropertyEditorFactory> getFactories() {
        return factories;
    }

    public void setFactories(Map<String, PropertyEditorFactory> factories) {
        this.factories = factories;
    }
}
