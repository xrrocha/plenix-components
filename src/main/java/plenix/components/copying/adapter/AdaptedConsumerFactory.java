package plenix.components.copying.adapter;

import plenix.components.adapter.Adapter;
import plenix.components.copying.Consumer;
import plenix.components.copying.ConsumerFactory;
import plenix.components.copying.CopyingException;

/**
 * AdaptedConsumerFactory.
 */
public class AdaptedConsumerFactory implements ConsumerFactory {
    private Adapter adapter;
    private ConsumerFactory delegate;

    /**
     * @see plenix.components.copying.ConsumerFactory#newInstance(java.lang.Object)
     */
    public Consumer newInstance(Object context) throws CopyingException {
        return new AdaptedConsumer(this.adapter, this.delegate.newInstance(context));
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setDelegate(ConsumerFactory delegate) {
        this.delegate = delegate;
    }

    public ConsumerFactory getDelegate() {
        return delegate;
    }

}
