package ch.akuhn.hapax.cluster;

public interface Distance<T> {

    public static final double INFINITY = Double.POSITIVE_INFINITY;

    /**
     * Returns the distance between two objects.
     * 
     * @return a non-negative value between <tt>0.0</tt> and {@link INFINITY};
     */
    public double dist(T a, T b);

}
