package plenix.components.copying;

import plenix.components.logging.AbstractLogEnabled;


/**
 * CopierImpl.
 */
public class CopierImpl extends AbstractLogEnabled implements Copier {
    private ProducerFactory producerFactory;
    private ConsumerFactory consumerFactory;

    public void copy(Object context) throws CopyingException {
        Producer producer = this.producerFactory.newInstance(context);
        Consumer consumer = this.consumerFactory.newInstance(context);

        try {
            Object object;
            while ((object = producer.produce()) != null) {
                consumer.consume(object);
            }
            consumer.done();
        } catch (CopyingException e) {
            throw e;
        }
    }

    public void setConsumerFactory(ConsumerFactory sinkFactory) {
        this.consumerFactory = sinkFactory;
    }

    public ConsumerFactory getConsumerFactory() {
        return consumerFactory;
    }

    public void setProducerFactory(ProducerFactory sourceFactory) {
        this.producerFactory = sourceFactory;
    }

    public ProducerFactory getProducerFactory() {
        return producerFactory;
    }
}
