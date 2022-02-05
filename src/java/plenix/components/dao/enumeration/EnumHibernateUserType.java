package plenix.components.dao.enumeration;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

public abstract class EnumHibernateUserType implements UserType {
    private static final int[] sqlTypes = { Types.VARCHAR };

    public int[] sqlTypes() {
        return sqlTypes;
    }

    public Class returnedClass() {
        return getReturnedClass();
    }

    public boolean equals(Object f, Object s) throws HibernateException {
        if (f == null || s == null) {
            return f == s;
        }
        return (f.toString().equals(s.toString()));
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String value = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);
        if (value == null) {
            return null;
        }
        for (String mnemonic: getValues().keySet()) {
            if (value.equals(mnemonic)) {
                return getValues().get(mnemonic);
            }
        }
        throw new HibernateException("Invalid mnemonic (" + value + ") for enumeration '"+ getName() + "'. Column name: " + names[0]);
    }

    public void nullSafeSet(PreparedStatement rs, Object obj, int idx) throws HibernateException, SQLException {
        if (obj != null) {
            for (String mnemonic: getValues().keySet()) {
                if (getValues().get(mnemonic).equals(obj)) {
                    Hibernate.STRING.nullSafeSet(rs, mnemonic, idx);
                    return;
                }
            }
            throw new HibernateException("Invalid value (" + obj + ") for enumeration '"+ getName() + "'. Column index: " + idx);
        }
    }

    public Object deepCopy(Object obj) throws HibernateException {
        return obj;
    }

    public boolean isMutable() {
        return false;
    }

    protected abstract String getName();
    protected abstract Class getReturnedClass();
    protected abstract Map<String, Object> getValues();
}
