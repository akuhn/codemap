package ch.akuhn.hapax.cluster;

public class Dendrogram<T> {

    private final T element;
    private final Similarity<? super T> sim;

    public Dendrogram(T element, Similarity<? super T> sim) {
        this.element = element;
        this.sim = sim;
    }
    
    public double sim(Dendrogram<T> other) {
        return 0.0;
    }

    public Dendrogram<T> merge(Dendrogram<T> other) {
        return this;
    }
    
}
