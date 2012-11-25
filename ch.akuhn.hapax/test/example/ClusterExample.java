package example;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ch.akuhn.hapax.cluster.ClusterEngine;
import ch.akuhn.hapax.cluster.Distance;

public class ClusterExample {

    public static void main(String[] args) {

        Distance<Point> sim = new Distance<Point>() {
            //@Override
            public double dist(Point a, Point b) {
                return a.distance(b);
            }
        };

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(2, 3));
        points.add(new Point(2, 5));
        points.add(new Point(2, 6));
        points.add(new Point(7, 8));

        ClusterEngine<Point> clusty = new ClusterEngine<Point>(points, sim);
        System.out.println(clusty.dendrogram());
    }

}
