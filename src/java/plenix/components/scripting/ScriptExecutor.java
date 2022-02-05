package plenix.components.scripting;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * StateScript
 */
public class ScriptExecutor {
    private static Properties defaultAliases;
    
    // TODO Add log support!
    static {
        defaultAliases = new Properties();
        defaultAliases.setProperty("log","log");
    }

    private Map<String, Object> context;

    private Log log;
    private Properties aliases = defaultAliases;

    public Object execute(BSFScript script, Map<String, Object> objectModel) throws Exception {
        Map<String, Object> contextObjectModel = null;
        if (getContext() == null) {
            contextObjectModel = new HashMap<String, Object>();
        } else {
            contextObjectModel = new HashMap<String, Object>(getContext());
        }
        if (objectModel != null) {
            for (String name: objectModel.keySet()) {
                contextObjectModel.put(getAliasFor(name), objectModel.get(name));
            }
        }
        return script.execute(contextObjectModel);
    }
    
    private String getAliasFor(String objectName) {
        if (aliases.containsKey(objectName)) {
            return aliases.getProperty(objectName);
        }
        return objectName;
    }
    
    public void setAliases(Properties aliases) {
        this.aliases = aliases;
    }
    
    public Log getLog() {
        if (log == null) {
            log = LogFactory.getLog(this.getClass().getName());
        }
        return log;
    }
    
    public void setLog(Log log) {
        this.log = log;
    }

    /**
     * @param context The context to set.
     */
    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    /**
     * @return Returns the context.
     */
    public Map<String, Object> getContext() {
        return context;
    }
}
