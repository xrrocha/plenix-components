package plenix.components.string.transforming;

import java.util.Collection;

public class CompositeStringTransformer implements StringTransformer {
    public Collection<StringTransformer> transformers;

    public String transform(String string) {
        for (StringTransformer transformer: transformers) {
            string = transformer.transform(string);
        }
        return string;
    }
}
