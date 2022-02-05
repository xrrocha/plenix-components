package plenix.components.dao;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import plenix.components.logging.AbstractLogEnabled;

import java.io.Serializable;
import java.util.Iterator;

import static org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;

public class HibernateEntityInterceptor extends AbstractLogEnabled implements Interceptor, BeanFactoryAware {
    private AutowireCapableBeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
    }

    protected AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    private void autowire(Object bean) {
        getBeanFactory().autowireBeanProperties(bean, AUTOWIRE_BY_NAME, false);
    }

    private void validate(Object entity) {
        if (entity instanceof Validatable) {
            ((Validatable) entity).validate();
        }
    }

    protected Object getProperty(String propertyName, Object[] state, String[] propertyNames) {
        return state[indexFor(propertyName, propertyNames)];
    }

    protected void setProperty(String propertyName, Object propertyValue, Object[] state, String[] propertyNames) {
        state[indexFor(propertyName, propertyNames)] = propertyValue;
    }

    protected int indexFor(String name, String[] propertyNames) {
        for (int i = 0; i < propertyNames.length; i++) {
            if (propertyNames[i].equals(name)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid setProperty name: " + name);
    }

    /**
     * Called just before an object is initialized. The interceptor may change the <tt>state</tt>, which will
     * be propagated to the persistent object. Note that when this method is called, <tt>entity</tt> will be
     * an empty uninitialized instance of the class.
     *
     * @return <tt>true</tt> if the user modified the <tt>state</tt> in any way.
     */
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        autowire(entity);
        return false;
    }

    /**
     * Called before an object is saved. The interceptor may modify the <tt>state</tt>, which will be used for
     * the SQL <tt>INSERT</tt> and propagated to the persistent object.
     *
     * @return <tt>true</tt> if the user modified the <tt>state</tt> in any way.
     */
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        validate(entity);
        return false;
    }

    /**
     * Called when an object is detected to be dirty, during a flush. The interceptor may modify the detected
     * <tt>currentState</tt>, which will be propagated to both the database and the persistent object.
     * Note that not all flushes end in actual synchronization with the database, in which case the
     * new <tt>currentState</tt> will be propagated to the object, but not necessarily (immediately) to
     * the database. It is strongly recommended that the interceptor <b>not</b> modify the <tt>previousState</tt>.
     *
     * @return <tt>true</tt> if the user modified the <tt>currentState</tt> in any way.
     */
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        return false;
    }

    /**
     * Called before an object is deleted. It is not recommended that the interceptor modify the <tt>state</tt>.
     */
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
    }

    /**
     * Called before a flush
     */
    public void preFlush(Iterator entities) throws CallbackException {
    }

    /**
     * Called after a flush that actually ends in execution of the SQL statements required to synchronize
     * in-memory state with the database.
     */
    public void postFlush(Iterator entities) throws CallbackException {
    }

    /**
     * Called when a transient entity is passed to <tt>saveOrUpdate()</tt>. The return value determines
     * <ul>
     * <li><tt>Boolean.TRUE</tt> - the entity is passed to <tt>save()</tt>, resulting in an <tt>INSERT</tt>
     * <li><tt>Boolean.FALSE</tt> - the entity is passed to <tt>update()</tt>, resulting in an <tt>UPDATE</tt>
     * <li><tt>null</tt> - Hibernate uses the <tt>unsaved-value</tt> mapping to determine if the object is unsaved
     * </ul>
     *
     * @param entity a transient entity
     * @return Boolean or <tt>null</tt> to choose default behaviour
     */
    public Boolean isUnsaved(Object entity) {
        return null;
    }

    /**
     * Called from <tt>flush()</tt>. The return value determines whether the entity is updated
     * <ul>
     * <li>an array of setProperty indices - the entity is dirty
     * <li>and empty array - the entity is not dirty
     * <li><tt>null</tt> - use Hibernate's default dirty-checking algorithm
     * </ul>
     *
     * @param entity a persistent entity
     * @return array of dirty setProperty indices or <tt>null</tt> to choose default behaviour
     */
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return null;
    }

    /**
     * Instantiate the entity class. Return <tt>null</tt> to indicate that Hibernate should use
     * the default constructor of the class. The identifier setProperty of the returned instance
     * should be initialized with the given identifier.
     *
     * @param clazz a mapped class
     * @param id    the identifier of the new instance
     * @return an instance of the class, or <tt>null</tt> to choose default behaviour
     */
    public Object instantiate(Class clazz, Serializable id) throws CallbackException {
        return null;
    }

    @Override
    public void onCollectionRecreate(Object o, Serializable serializable) throws CallbackException {

    }

    @Override
    public void onCollectionRemove(Object o, Serializable serializable) throws CallbackException {

    }

    @Override
    public void onCollectionUpdate(Object o, Serializable serializable) throws CallbackException {

    }

    @Override
    public Boolean isTransient(Object o) {
        return null;
    }

    @Override
    public Object instantiate(String s, EntityMode entityMode, Serializable serializable) throws CallbackException {
        return null;
    }

    @Override
    public String getEntityName(Object o) throws CallbackException {
        return null;
    }

    @Override
    public Object getEntity(String s, Serializable serializable) throws CallbackException {
        return null;
    }

    @Override
    public void afterTransactionBegin(Transaction transaction) {

    }

    @Override
    public void beforeTransactionCompletion(Transaction transaction) {

    }

    @Override
    public void afterTransactionCompletion(Transaction transaction) {

    }

    @Override
    public String onPrepareStatement(String s) {
        return null;
    }
}
