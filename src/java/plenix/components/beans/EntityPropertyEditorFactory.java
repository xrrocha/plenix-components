package plenix.components.beans;

import plenix.components.dao.Dao;

import java.beans.PropertyEditor;
import java.io.Serializable;

public class EntityPropertyEditorFactory extends TransformingPropertyEditorFactory {
    private Dao dao;
    private Class entityClass;

    private PropertyEditorFactory idPropertyEditorFactory;

    public PropertyEditor newInstance() {
        return new TransformingPropertyEditor() {
            protected Object convert(String text) {
                Serializable id = text;
                if (getIdPropertyEditorFactory() != null) {
                    PropertyEditor idPropertyEditor = getIdPropertyEditorFactory().newInstance();
                    idPropertyEditor.setAsText(text);
                    id = (Serializable) idPropertyEditor.getValue();
                }
                return getDao().retrieve(getEntityClass(), id);
            }
        };
    }

    public PropertyEditorFactory getIdPropertyEditorFactory() {
        return idPropertyEditorFactory;
    }

    public void setIdPropertyEditorFactory(PropertyEditorFactory idPropertyEditorFactory) {
        this.idPropertyEditorFactory = idPropertyEditorFactory;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }
}
