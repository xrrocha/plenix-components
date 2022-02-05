package plenix.components.string.transforming;

public class UpperCaseStringTransformer implements StringTransformer {
    public String transform(String string) {
        if (string == null) {
            return null;
        }
        return string.toUpperCase();
    }
}
