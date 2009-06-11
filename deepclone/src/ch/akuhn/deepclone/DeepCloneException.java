package ch.akuhn.deepclone;

public class DeepCloneException extends RuntimeException {

    private static final long serialVersionUID = 167984378038815842L;

    public DeepCloneException(Throwable ex) {
	super(ex);
    }

}
