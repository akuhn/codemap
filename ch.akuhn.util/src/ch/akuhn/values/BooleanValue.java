package ch.akuhn.values;

public class BooleanValue extends AbstractValue<Boolean> {
    
    private boolean booleanValue;
    
    public BooleanValue(boolean initial) {
        booleanValue = initial;
    }

    @Override
    public Throwable getError() {
        return null;
    }

    @Override
    public Boolean getValue() {
        return booleanValue;
    }
    
    @Override
    public void setValue(Boolean value) {
        if (value == null) throw new IllegalArgumentException();
        this.setValue(value.booleanValue());
    }
    
    public void setValue(boolean bool) {
        boolean oldValue = booleanValue;
        booleanValue = bool;
        if (oldValue != booleanValue) changed();
    }
}
