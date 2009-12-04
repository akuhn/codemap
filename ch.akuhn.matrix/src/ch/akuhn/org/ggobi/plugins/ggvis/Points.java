package ch.akuhn.org.ggobi.plugins.ggvis;

import java.util.Arrays;

import ch.akuhn.matrix.Vector;


public class Points {

    public final double[] x, y;
    public double[] mean = new double[2];
    public double scl;

    public Points(double[] x, double[] y) {
        this.x = x;
        this.y = y;
    }
    
    public Points(int length, int ncols2) {
        this(length);
    }

    public Points(int length) {
        x = new double[length];
        y = new double[length];
    }

    public void clear() {
        Arrays.fill(x, 0.0);
        Arrays.fill(y, 0.0);
    }

    void get_center () {
        mean = new double[2];

        int n = 0;

        for (int i=0; i<x.length; i++) {
            // GGVIS excluded dragged points
            mean[0] += x[i];
            mean[1] += y[i];
            n++;
        }
        mean[0] /= n;
        mean[1] /= n;
    }

    void get_center_scale () {
        get_center();
        scl = 0.0;
        for(int i=0; i<x.length; i++) {
            // GGVIS excluded dragged points
            scl += ((x[i] - mean[0]) * (x[i] - mean[0]));
            scl += ((y[i] - mean[1]) * (y[i] - mean[1]));
        }
        scl = Math.sqrt(scl/x.length/2);
    }

    void ggv_center_scale_pos() {
        get_center_scale();
        for (int i=0; i<x.length; i++) {
            // GGVIS excluded dragged points
            x[i] = (x[i] - mean[0])/scl;
            y[i] = (y[i] - mean[1])/scl;
        }
    }

    public double[][] points() {
        return new double[][] { x, y };
    }

    double dot_prod(int a, int b) {
        double dsum = 0.0;
        dsum += (x[a] - mean[0]) * (x[b] - mean[0]);
        dsum += (y[a] - mean[1]) * (y[b] - mean[1]);
        return dsum;
    }

    double L2_norm(double x0, double y0) {
        double dsum = 0.0;
        dsum += (x0 - mean[0]) * (x0 - mean[0]);
        dsum += (y0 - mean[1]) * (y0 - mean[1]);
        return(dsum);
    }

    public void openVisualization(boolean[][] edges) {
        new PointsDatavis(this).withEdges(edges).open();
    }

    public void applyCentering() {
        Vector.wrap(x).applyCentering();
        Vector.wrap(y).applyCentering();
    }

	public int size() {
		return x.length;
	}

}