package plenix.components.variable;

import java.util.Map;
import java.util.Set;

/**
 * VariableExpander.
 */
public interface VariableExpander {
    // if source == null then return empty set
    public Set<String> variableReferences(String source);

    // if source == null then return null
    // if variables == null then return source unchanged
    public String expandVariables(String source, Map<String, ? extends Object> variables, Object defaultValue);

    public String expandVariables(String source, Map<String, ? extends Object> variables);  // Convenience method
} 
