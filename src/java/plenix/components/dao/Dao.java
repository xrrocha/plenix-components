package plenix.components.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Dao.
 */
public interface Dao {
    void store(Object entity);
    void remove(Object entity);

    List retrieve(Class entityClass);
    Object retrieve(Class entityClass, Serializable id);
    List retrieve(String queryName,
                  String[] paramNames,
                  Object[] paramValues);
}
