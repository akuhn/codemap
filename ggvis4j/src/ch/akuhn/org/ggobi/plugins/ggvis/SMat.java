package ch.akuhn.org.ggobi.plugins.ggvis;

import java.util.Arrays;

public class SMat {

    double[][] vals;

    public SMat(int size) {
        vals = new double[size][];
        for (int n = 0; n < vals.length; n++) vals[n] = new double[n];
    }

    public void fill(double value) {
        for (int n = 0; n < vals.length; n++) Arrays.fill(vals[n], value);
    }

}
