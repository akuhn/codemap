package org.codemap.graph;

import java.util.Arrays;

import ch.akuhn.util.Out;

public class Distances {

    public double[][] dist;

    public Distances(double[][] dist) {
        this.dist = dist;
    }

    public short[][] kayNearestNeighbours(int k, double threshold) {
        short[][] path = new short[dist.length][dist.length];
        for (int i = 0; i < path.length; i++) {
            Arrays.fill(path[i], Short.MAX_VALUE);
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
            double min = Math.max(minima[k], threshold);
            for (int j = k + 1; j < dist.length; j++) {
                path[i][j] = dist[i][j] <= min ? 1 : Short.MAX_VALUE;
            }
        }
        return path;
    }
    
    public static void main(String[] args) {
        new SwissRoll(1000).asDistanceMatrix().kayNearestNeighbours(5);
    }
    
}
