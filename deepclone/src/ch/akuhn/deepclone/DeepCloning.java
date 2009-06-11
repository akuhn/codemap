package ch.akuhn.deepclone;

public class DeepCloning {

    public static final DeepCloning IMMUTABLE = new DeepCloning(null);
    
    public CloneFactory cloner;
    
    public DeepCloning(CloneFactory cloner) {
	this.cloner = cloner;
    }
    
    public Object perform(Object instance) throws Exception {
	return instance;
    }
    
}
