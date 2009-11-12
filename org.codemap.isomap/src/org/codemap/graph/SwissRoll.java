package org.codemap.graph;

public class SwissRoll {

    public double[] x, y, z;
    
    public SwissRoll(int n) {
        x = new double[n];
        y = new double[n];
        z = new double[n];
        for (int i = 0; i < n; i++) {
            double len = Math.PI * (Math.random() * 3 + 1);
            x[i] = Math.cos(len) * (1 + len / 4);
            y[i] = Math.sin(len) * (1 + len / 4);
            z[i] = Math.random() * 4 - 2;
        }
    }
    
    public Distances asDistanceMatrix() {
        double[][] dist = new double[x.length][x.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                dist[i][j] = dist(i,j);
            }
        }
        return new Distances(dist);
    }

    private double dist(int i, int j) {
        return Math.sqrt(
                (x[i] - x[j]) * (x[i] - x[j]) +
                (y[i] - y[j]) * (y[i] - y[j]) +
                (z[i] - z[j]) * (z[i] - z[j]));
    }
    
}
