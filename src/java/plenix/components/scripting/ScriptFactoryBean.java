package plenix.components.scripting;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;

/**
 * ScriptFactoryBean.
 */
public class ScriptFactoryBean implements FactoryBean {
    private Class[] interfaces;
    private Map<String, ScriptProxy> scripts;

    public Object getObject() throws Exception {
        InvocationHandler invocationHandler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ScriptProxy script = getScripts().get(method.getName());
                if (script != null) {
                    return script.execute(args);
                }
                throw new IllegalStateException("Unknown script: " + method.getName());
            }
        };
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                                      getInterfaces(), invocationHandler);
    }

    public Class getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * @param interfaces The interfaces to set.
     */
    public void setInterfaces(Class[] interfaces) {
        this.interfaces = interfaces;
    }

    /**
     * @return Returns the interfaces.
     */
    public Class[] getInterfaces() {
        return interfaces;
    }

    /**
     * @param scripts The scripts to set.
     */public void setScripts(Map<String, ScriptProxy> scripts){this.scripts=scripts;}

    /**
     * @return Returns the scripts.
     */public Map<String, ScriptProxy> getScripts(){return scripts;}
}
