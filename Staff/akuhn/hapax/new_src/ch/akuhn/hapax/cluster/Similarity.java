package ch.akuhn.hapax.cluster;

public interface Similarity<T> {

    /**
     * Returns the similarity between two objects.
     * 
     * @return a value between <tt>-1.0</tt> and <tt>+1.0</tt>.
     */
    public double similarity(T a, T b);

}
