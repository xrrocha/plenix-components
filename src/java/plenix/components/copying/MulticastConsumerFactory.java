package plenix.components.copying;

import plenix.components.logging.AbstractLogEnabled;

/**
 * MulticastConsumerFactory.
 */
public class MulticastConsumerFactory extends AbstractLogEnabled implements ConsumerFactory {
    private ConsumerFactory[] factories;
    
    /**
     * @see plenix.components.copying.ConsumerFactory#newInstance(java.lang.Object)
     */
    public Consumer newInstance(Object context) throws CopyingException {
        final Consumer[] consumers = new Consumer[factories.length];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = factories[i].newInstance(context);
        }
        
        // FIXME: What if an exception is thrown during multicasting?
        return new Consumer() {
            public void consume(Object object) throws CopyingException {

                for (int i = 0; i < consumers.length; i++) {
                    consumers[i].consume(object);
                }
            }
            
            public void done() throws CopyingException {
                for (int i = 0; i < consumers.length; i++) {
                    consumers[i].done();
                }
            }
        };
    }

    public void setFactories(ConsumerFactory[] factories) {
        this.factories = factories;
    }

    public ConsumerFactory[] getFactories() {
        return factories;
    }

}
