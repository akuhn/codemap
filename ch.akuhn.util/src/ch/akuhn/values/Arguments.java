package ch.akuhn.values;

public class Arguments {

    private int index = 0;
    private Value<?>[] values;
    
    public Arguments(Value<?>[] arguments) {
        this.index = 0;
        this.values = arguments;
    }
    
    @SuppressWarnings("unchecked")
    public <A> A next() {
        if (index >= values.length) return null;
        return (A) values[index++].getValue();
    }
    
}
