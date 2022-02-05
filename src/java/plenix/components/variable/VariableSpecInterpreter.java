package plenix.components.variable;

import java.util.Map;


/**
 * VariableProcessor.
 */
public interface VariableSpecInterpreter {
    String getNameFrom(String spec);
    Object getValueFrom(String spec, Map<String, ?extends Object> variables);
}
