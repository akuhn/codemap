package ch.akuhn.deepclone;

public class DeepCloning {

    public static final DeepCloning IMMUTABLE = new DeepCloning(null);
    
    public DeepClone cloner;
    
    public boolean isImmutable() {
	return false;
    }
    
    public DeepCloning(DeepClone cloner) {
	this.cloner = cloner;
    }
    
    public Object perform(Object instance) throws Exception {
	return instance;
    }
    
}
