package ch.akuhn.values;

public class Arguments {

    private int index = 0;
    private Value<?>[] values;
    
    public Arguments(Value<?>[] values) {
        this.index = 0;
        this.values = values;
    }
    
    
    @SuppressWarnings("unchecked")
    public <A> A next() {
        if (index >= values.length) return null;
        return (A) values[index++].getValue();
    }
    
}
