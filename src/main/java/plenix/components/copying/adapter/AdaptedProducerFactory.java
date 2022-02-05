package plenix.components.copying.adapter;

import plenix.components.adapter.Adapter;
import plenix.components.copying.CopyingException;
import plenix.components.copying.Producer;
import plenix.components.copying.ProducerFactory;

/**
 * AdaptedProducerFactory.
 */
public class AdaptedProducerFactory implements ProducerFactory {
    private Adapter adapter;
    private ProducerFactory delegate;

    public AdaptedProducerFactory() {
    }

    public AdaptedProducerFactory(Adapter adapter, ProducerFactory delegate) {
        this.setAdapter(adapter);
        this.setDelegate(delegate);
    }

    /**
     * @see plenix.components.copying.ProducerFactory#newInstance(java.lang.Object)
     */
    public Producer newInstance(Object context) throws CopyingException {
        return new AdaptedProducer(this.adapter, this.delegate.newInstance(context));
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setDelegate(ProducerFactory delegate) {
        this.delegate = delegate;
    }

    public ProducerFactory getDelegate() {
        return delegate;
    }

}
