package plenix.components.adapter;


/**
 * PipelineAdapter.
 */
public class PipelineAdapter implements Adapter {
    private Adapter[] adapters;

    /**
     * @see plenix.components.adapter.Adapter#adapt(java.lang.Object)
     */
    public Object adapt(Object source) throws AdapterException {
        for (int i = 0; i < this.getAdapters().length; i++) {
            source = ((Adapter) this.getAdapters()[i]).adapt(source);
        }
        return source;
    }

    public void setAdapters(Adapter[] adapters) {
        this.adapters = adapters;
    }

    public Adapter[] getAdapters() {
        return adapters;
    }
}
