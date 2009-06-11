package ch.akuhn.deepclone;

public interface DeepCloneStrategy {

    public static final DeepCloneStrategy IMMUTABLE = new DeepCloneStrategy() {
        @Override
        public Object makeClone(Object original, CloneFactory delegate) throws Exception {
            return original;
        }
    };

    public Object makeClone(Object original, CloneFactory delegate) throws Exception;
    
}
