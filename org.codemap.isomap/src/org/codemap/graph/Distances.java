package org.codemap.graph;

import java.util.Arrays;

import ch.akuhn.util.Out;

public class Distances {

    public double[][] dist;

    public Distances(double[][] dist) {
        this.dist = dist;
    }

    public double[][] kayNearestNeighbours(int k) {
        double[][] path = new double[dist.length][dist.length];
        for (int i = 0; i < path.length; i++) {
            Arrays.fill(path[i], Double.POSITIVE_INFINITY);
        }
        for (int i = 0; i < dist.length; i++) {
            double[] minima = Arrays.copyOf(dist[i], k + 1);
            Arrays.sort(minima);
            for (int j = k + 1; j < dist.length; j++) {
                if (dist[i][j] >= minima[k]) continue;
                int n = -Arrays.binarySearch(minima, dist[i][j])-1;
                System.arraycopy(minima, n, minima, n+1, k-n);
                minima[n] = dist[i][j];
            }
            for (int j = k + 1; j < dist.length; j++) {
                path[i][j] = dist[i][j] <= minima[k] ? 1 : Double.POSITIVE_INFINITY;
            }
        }
        return path;
    }
    
    public static void main(String[] args) {
        new SwissRoll(1000).asDistanceMatrix().kayNearestNeighbours(5);
    }
    
}
