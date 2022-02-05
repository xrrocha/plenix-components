package plenix.components.copying.adapter;

import plenix.components.adapter.Adapter;
import plenix.components.copying.CopyingException;
import plenix.components.copying.Producer;

/**
 * AdaptedProducer.
 */
public class AdaptedProducer implements Producer {
    private Adapter adapter;
    private Producer delegate;

    public AdaptedProducer() {
    }

    public AdaptedProducer(Adapter adapter, Producer delegate) {
        this.setAdapter(adapter);
        this.setDelegate(delegate);
    }

    /**
     * @see plenix.components.copying.Producer#produce()
     */
    public Object produce() throws CopyingException {
        try {
            return this.adapter.adapt(this.delegate.produce());
        } catch (Exception e) {
            throw new CopyingException("Error adapting produced object", e);
        }
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setDelegate(Producer delegate) {
        this.delegate = delegate;
    }

    public Producer getDelegate() {
        return delegate;
    }

}
