package example;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ch.akuhn.hapax.cluster.ClusterMatrix;
import ch.akuhn.hapax.cluster.Similarity;

public class ClusterExample {

    public static void main(String[] args) {

        Similarity<Point> sim = new Similarity<Point>() {
            @Override
            public double similarity(Point a, Point b) {
                return -a.distance(b);
            }
        };

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(2, 3));
        points.add(new Point(2, 5));
        points.add(new Point(2, 6));
        points.add(new Point(7, 8));

        ClusterMatrix<Point> clusty = new ClusterMatrix<Point>(points, sim);
        System.out.println(clusty.cluster());
    }

}
