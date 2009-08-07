package ch.akuhn.org.ggobi.plugins.ggvis;

import java.util.Arrays;

/** Symmetric matrix without diagonal.
 * <P>
 * This class is not thread-safe. 
 *
 * @author Adrian Kuhn
 *
 */
public class SMat {

    public final double[][] vals;

    public SMat(int size) {
        vals = new double[size][];
        for (int n = 0; n < vals.length; n++) vals[n] = new double[n];
    }

    public void fill(double value) {
        for (int n = 0; n < vals.length; n++) Arrays.fill(vals[n], value);
    }

}
