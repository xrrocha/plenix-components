package plenix.components.scripting;

import java.util.Iterator;
import java.util.Map;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

/**
 * StateScript
 */
public class BSFScript {
    /*
     * BSFManager.registerScriptingEngine(
     *     "groovy", 
     *     "org.codehaus.groovy.bsf.GroovyEngine", 
     *     new String[] { "groovy", "gy" }
     * );
     */
    private String scriptText;
    private String languageName;
    private int lineNumber = 1;
    private int columnNumber = 1;
    private String scriptName = "eventHandler";
    
    public static Object eval(String languageName,
                              String scriptName,
                              String scriptText)
        throws Exception
    {
        return eval(languageName, scriptName, 1, 1, scriptText);
    }
    
    public static Object eval(String languageName,
                              String scriptName,
                              int lineNumber,
                              int columnNumber,
                              String scriptText)
        throws Exception
    {
        return new BSFManager().eval(languageName,
                                     scriptName,
                                     lineNumber,
                                     columnNumber,
                                     scriptText);
    }
    
    public Object execute(Map objectModel) throws Exception {
        BSFManager manager = new BSFManager();
        Iterator i = objectModel.keySet().iterator();
        while (i.hasNext()) {
            String name = (String) i.next();
            this.declareBean(manager, name, objectModel.get(name));
        }

        return manager.eval(this.languageName,
                            this.scriptName,
                            this.lineNumber,
                            this.columnNumber,
                            this.scriptText);
    }
    
    private void declareBean(BSFManager manager,
                             String name, Object bean)
        throws BSFException
    {
        if (bean != null) {
            manager.declareBean(name, bean, bean.getClass());
        }
    }
    /**
     * @param scriptText The scriptText to set.
     */
    public void setScriptText(String scriptText) {
        this.scriptText = scriptText;
    }

    /**
     * @return Returns the scriptText.
     */
    public String getScriptText() {
        return scriptText;
    }

    /**
     * @param languageName The languageName to set.
     */
    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    /**
     * @return Returns the languageName.
     */
    public String getLanguageName() {
        return languageName;
    }

    /**
     * @param scriptName The scriptName to set.
     */
    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    /**
     * @return Returns the scriptName.
     */
    public String getScriptName() {
        return scriptName;
    }

    /**
     * @param lineNumber The lineNumber to set.
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @return Returns the lineNumber.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @param columnNumber The columnNumber to set.
     */
    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    /**
     * @return Returns the columnNumber.
     */
    public int getColumnNumber() {
        return columnNumber;
    }
}
