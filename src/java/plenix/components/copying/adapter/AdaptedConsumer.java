package plenix.components.copying.adapter;

import plenix.components.adapter.Adapter;
import plenix.components.copying.Consumer;
import plenix.components.copying.CopyingException;

/**
 * AdaptedConsumer.
 */
public class AdaptedConsumer implements Consumer {
    private Adapter adapter;
    private Consumer delegate;
    
    public AdaptedConsumer() {}
    
    public AdaptedConsumer(Adapter adapter, Consumer delegate) {
        this.setAdapter(adapter);
        this.setDelegate(delegate);
    }

    public void consume(Object object) throws CopyingException {
        try {
            object = this.adapter.adapt(object);
        } catch (Exception e) {
            throw new CopyingException("Error adapting object for consumer", e);
        }
        this.delegate.consume(object);
    }

    public void done() throws CopyingException {
        this.delegate.done();
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setDelegate(Consumer delegate) {
        this.delegate = delegate;
    }

    public Consumer getDelegate() {
        return delegate;
    }
}