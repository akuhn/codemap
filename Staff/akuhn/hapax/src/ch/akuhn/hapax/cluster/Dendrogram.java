package ch.akuhn.hapax.cluster;

public abstract class Dendrogram<E> {

    public static class Leaf<E>
            extends Dendrogram<E> {

        public final E element;

        public Leaf(E element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return element.toString();
        }

    }

    public static class Node<E>
            extends Dendrogram<E> {

        public final Dendrogram<E> left, right;
        public final double threshold;

        public Node(Dendrogram<E> left, Dendrogram<E> right, double threshold) {
            this.left = left;
            this.right = right;
            this.threshold = threshold;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", left, right);
        }

    }

    public Dendrogram<E> parent;

    public Dendrogram<E> merge(Dendrogram<E> dendro, double threshold) {
        return new Node<E>(this, dendro, threshold);
    }

}
