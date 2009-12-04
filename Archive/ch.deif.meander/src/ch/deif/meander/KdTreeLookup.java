package ch.deif.meander;

import ch.deif.meander.kdtree.KDTree;
import ch.deif.meander.kdtree.KeySizeException;

public class KdTreeLookup {

    private KDTree<Location> tree;
    private int width;

    public KdTreeLookup(KDTree<Location> kdTree, int width) {
        this.tree = kdTree;
        this.width = width;
    }

    public Location getResult(int px, int py) {
        try {
            return tree.nearest(asDoubleCoordinates(px, py));
        } catch (KeySizeException e) {
            throw new RuntimeException(e);
        }
    }

    private double[] asDoubleCoordinates(int px, int py) {
        double x = (double) px / width;
        double y = (double) py / width;
        return new double[] { x, y };
    }

}
