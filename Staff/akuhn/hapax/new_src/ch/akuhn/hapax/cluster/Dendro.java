package ch.akuhn.hapax.cluster;

public abstract class Dendro<E> {

    public static class Leaf<E>
            extends Dendro<E> {

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
            extends Dendro<E> {

        public final Dendro<E> left, right;
        public final double threshold;

        public Node(Dendro<E> left, Dendro<E> right, double threshold) {
            this.left = left;
            this.right = right;
            this.threshold = threshold;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", left, right);
        }

    }

    public Dendro<E> parent;

    public Dendro<E> merge(Dendro<E> dendro, double threshold) {
        return new Node<E>(this, dendro, threshold);
    }

}
