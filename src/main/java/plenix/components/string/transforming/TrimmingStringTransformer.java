package plenix.components.string.transforming;

public class TrimmingStringTransformer implements StringTransformer {
    public String transform(String string) {
        if (string == null) {
            return "";
        }
        return string.trim();
    }
}
