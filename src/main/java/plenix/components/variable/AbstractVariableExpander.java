package plenix.components.variable;

import java.util.Map;

/**
 * AbstractVariableExpander.
 */
public abstract class AbstractVariableExpander implements VariableExpander {
    public String expandVariables(String source, Map<String, ? extends Object> variables) {
        return this.expandVariables(source, variables, null);
    }
}
