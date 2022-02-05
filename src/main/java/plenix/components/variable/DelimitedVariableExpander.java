package plenix.components.variable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * DelimitedVariableExpander.
 */
public class DelimitedVariableExpander extends AbstractVariableExpander {
    private String openingDelimiter = "${";
    private String closingDelimiter = "}";
    private VariableSpecInterpreter variableSpecInterpreter;

    public String expandVariables(String source, Map<String, ? extends Object> variables, Object defaultValue) {
        if (source == null) {
            return null;
        }
        if (variables == null) {
            return source;
            // variables = Collections.EMPTY_MAP;
        }
        int currentPosition = 0;
        int stringLength = source.length();
        StringBuffer buffer = new StringBuffer();
        while (currentPosition < stringLength) {
            int initialPosition =
                    source.indexOf(this.openingDelimiter, currentPosition);
            if (initialPosition == -1) {
                buffer.append(source.substring(currentPosition));
                break;
            }

            buffer.append(source.substring(currentPosition, initialPosition));
            currentPosition = initialPosition + this.openingDelimiter.length();
            int finalPosition =
                    source.indexOf(this.closingDelimiter, currentPosition);
            if (finalPosition == -1) {
                throw new IllegalArgumentException(
                        "Unclosed variable reference: " +
                                source.substring(currentPosition, currentPosition + 8) +
                                "...");
            }

            String name = source.substring(currentPosition, finalPosition);

            Object value = null;
            if (getVariableSpecInterpreter() == null) {
                value = variables.get(name);
            } else {
                value = getVariableSpecInterpreter().getValueFrom(name, variables);
            }

            if (value == null) {
                value = defaultValue;
            }
            if (value != null) {
                buffer.append(value);
            }
            currentPosition = finalPosition + this.closingDelimiter.length();
        }
        return buffer.toString();
    }

    public Set<String> variableReferences(String source) {
        if (source == null) {
            return Collections.EMPTY_SET;
        }

        int currentPosition = 0;
        int stringLength = source.length();
        Set<String> variableReferences = new HashSet<String>();

        while (currentPosition < stringLength) {
            int initialPosition =
                    source.indexOf(this.openingDelimiter, currentPosition);
            if (initialPosition == -1) {
                break;
            }
            currentPosition = initialPosition + this.openingDelimiter.length();
            int finalPosition =
                    source.indexOf(this.closingDelimiter, currentPosition);
            if (finalPosition == -1) {
                throw new IllegalArgumentException(
                        "Unclosed variable reference: " +
                                source.substring(currentPosition, currentPosition + 8) +
                                "...");
            }

            String name = source.substring(currentPosition, finalPosition);

            if (getVariableSpecInterpreter() != null) {
                name = getVariableSpecInterpreter().getNameFrom(name);
            }

            variableReferences.add(name);
            currentPosition = finalPosition + this.closingDelimiter.length();
        }

        return variableReferences;
    }

    public String getOpeningDelimiter() {
        return this.openingDelimiter;
    }

    public void setOpeningDelimiter(String openingDelimiter) {
        this.openingDelimiter = openingDelimiter;
    }

    public String getClosingDelimiter() {
        return this.closingDelimiter;
    }

    public void setClosingDelimiter(String closingDelimiter) {
        this.closingDelimiter = closingDelimiter;
    }

    public void setVariableSpecInterpreter(VariableSpecInterpreter variableSpecInterpreter) {
        this.variableSpecInterpreter = variableSpecInterpreter;
    }

    public VariableSpecInterpreter getVariableSpecInterpreter() {
        return variableSpecInterpreter;
    }
}
