package plenix.components.dao;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import static org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;

/**
 * EntityManager.
 */
public abstract class EntityManager implements BeanFactoryAware {
    private Dao dao;
    private AutowireCapableBeanFactory beanFactory;

    protected Object instantiate(Class clazz) {
        try {
            Object entity = (Object) clazz.newInstance();
            autowire(entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Instantiation error: " + e, e);
        }
    }

    protected void autowire(Object bean) {
        getBeanFactory().autowireBeanProperties(bean, AUTOWIRE_BY_NAME, false);
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
    }

    protected AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public Dao getDao() {
        return dao;
    }
}
