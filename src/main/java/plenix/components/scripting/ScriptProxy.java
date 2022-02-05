package plenix.components.scripting;

import java.util.HashMap;
import java.util.Map;

/**
 * ScriptProxy.
 */
public class ScriptProxy {
    private BSFScript script;
    private ScriptExecutor scriptExecutor;
    private String[] argumentNames;

    public Object execute(Object[] args) throws Exception {
        if (args.length != getArgumentNames().length) {
            throw new IllegalArgumentException("Argument count mismatch");
        }
        Map<String, Object> objectModel = new HashMap<String, Object>();
        for (int i = 0; i < args.length; i++) {
            objectModel.put(getArgumentNames()[i], args[i]);
        }
        return getScriptExecutor().execute(getScript(), objectModel);
    }

    /**
     * @param script The script to set.
     */
    public void setScript(BSFScript script) {
        this.script = script;
    }

    /**
     * @return Returns the script.
     */
    public BSFScript getScript() {
        return script;
    }

    /**
     * @param scriptExecutor The scriptExecutor to set.
     */
    public void setScriptExecutor(ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
    }

    /**
     * @return Returns the scriptExecutor.
     */
    public ScriptExecutor getScriptExecutor() {
        return scriptExecutor;
    }

    /**
     * @param argumentNames The argumentNames to set.
     */
    public void setArgumentNames(String[] argumentNames) {
        this.argumentNames = argumentNames;
    }

    /**
     * @return Returns the argumentNames.
     */
    public String[] getArgumentNames() {
        return argumentNames;
    }
}
