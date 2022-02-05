package plenix.components.string.transforming;

public class FirstCharacterStringTransformer implements StringTransformer {
    public String transform(String string) {
        if (string == null) {
            return null;
        }
        return string.substring(0, 1);
    }
}
