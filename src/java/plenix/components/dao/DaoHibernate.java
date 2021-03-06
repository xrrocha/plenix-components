package plenix.components.dao;

import java.io.Serializable;
import java.util.List;

import net.sf.hibernate.SessionFactory;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * DaoHibernate.
 */
public class DaoHibernate implements Dao {
    private HibernateDaoSupport hibernate = new HibernateDaoSupport() {};

    public void store(Object entity) {
        hibernate.getHibernateTemplate().saveOrUpdateCopy(entity);
    }

    public void remove(Object entity) {
        hibernate.getHibernateTemplate().delete(entity);
    }

    public List retrieve(Class claseEntidad) {
        return hibernate.getHibernateTemplate().find("FROM " + claseEntidad.getName());
    }

    public Object retrieve(Class claseEntidad, Serializable id) {
        return (Object) hibernate.getHibernateTemplate().load(claseEntidad, id);
    }

    public List retrieve(String nombreConsulta,
                            String[] nombresParametros,
                            Object[] valoresParametros)
    {
        return hibernate.getHibernateTemplate().findByNamedQueryAndNamedParam(nombreConsulta, nombresParametros, valoresParametros);
    }
    
    public HibernateDaoSupport getHibernate() { return hibernate; }
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        hibernate.setSessionFactory(sessionFactory);
    }
    
    public SessionFactory getSessionFactory() {
        return hibernate.getSessionFactory();
    }
}
