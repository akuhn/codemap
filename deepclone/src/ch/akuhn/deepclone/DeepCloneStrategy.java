package ch.akuhn.deepclone;

public class DeepCloneStrategy {

    public static final DeepCloneStrategy IMMUTABLE = new DeepCloneStrategy(null);
    
    public DeepClone cloner;
    
    public boolean isImmutable() {
	return false;
    }
    
    public DeepCloneStrategy(DeepClone cloner) {
	this.cloner = cloner;
    }
    
    public Object perform(Object instance) throws Exception {
	return instance;
    }
    
}
