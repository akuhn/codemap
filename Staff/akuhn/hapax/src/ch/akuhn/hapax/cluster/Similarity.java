package ch.akuhn.hapax.cluster;

public abstract class Similarity<T> implements Distance<T> {

    @Override
    public double dist(T a, T b) {
        return 1 - similarity(a, b);
    }

    /**
     * Returns the similarity between two objects.
     * 
     * @return a value between <tt>-1.0</tt> and <tt>+1.0</tt>.
     */
    public abstract double similarity(T a, T b);

}
