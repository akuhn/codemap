package ch.akuhn.values;

public class IntegerValue extends AbstractValue<Integer> {

    private int intValue;

    @Override
    public Throwable getError() {
        return null;
    }

    @Override
    public Integer getValue() {
        return intValue;
    }
    
    public int intValue() {
        return intValue;
    }
    
    @Override
    public void setValue(Integer value) {
        if (value == null) throw new IllegalArgumentException();
        this.setValue(value.intValue());
    }

    public void setValue(int intValue) {
        int oldValue = this.intValue;
        this.intValue = intValue;
        if (oldValue != this.intValue) changed();
    }
    
}
