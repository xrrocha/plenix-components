package plenix.components.jdbc;

/**
 * ColumnValue.
 */
public class ColumnValue {
    private int type;
    private Object value;

    public ColumnValue() {
    }

    public ColumnValue(int type, Object value) {
        this.setType(type);
        this.setValue(value);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
