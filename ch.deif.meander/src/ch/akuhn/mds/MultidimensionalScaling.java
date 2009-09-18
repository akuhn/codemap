package ch.akuhn.mds;

import java.io.PrintStream;
import java.util.EventListener;

import ch.akuhn.org.ggobi.plugins.ggvis.Function;
import ch.akuhn.org.ggobi.plugins.ggvis.Mds;
import ch.akuhn.org.ggobi.plugins.ggvis.Points;
import ch.akuhn.org.ggobi.plugins.ggvis.SMat;

/** Multidimensional scaling, based on GGobi/GGvis.
 * 
 * @author Adrian Kuhn, 2009
 *
 */
public class MultidimensionalScaling {

    private Points fInitialConfiguration;
    private SMat fdistances;
    private int fiterations = 100;
    private MultidimensionalScalingListener fListener;
    private PrintStream fOut;
    private double fThreshold = 1e-6;

    public MultidimensionalScaling initialConfiguration(double[] x, double[] y) {
        fInitialConfiguration = new Points(x, y);
        return this;
    }

    public MultidimensionalScaling dissimilarities(double[][] matrix) {
        fdistances = new SMat(matrix);
        return this;
    }

    public MultidimensionalScaling listener(MultidimensionalScalingListener listener) {
        this.fListener = listener;
        return this;
    }

    public MultidimensionalScaling iterations(int iterations) {
        this.fiterations = iterations;
        return this;
    }

    public double[][] run() {
        if (fdistances.vals.length == 0) return new double[0][2];
        Mds mds = new Mds(fdistances, fInitialConfiguration, Function.IDENTITY, Function.IDENTITY, Function.IDENTITY);
        int len = fdistances.vals.length;
        fdistances = null;
        fInitialConfiguration = null;
        loop: while (fiterations > 0) {
            if (fListener != null) fListener.update(mds);
            long t = System.nanoTime();
            double prev = 1.0;
            for (int n = 0; n < 5; n++) {
                mds.mds_once(true);
                mds.mds_once(true);
                double stress = mds.stress;
                if (prev - stress < fThreshold) break loop;
                prev = stress;
            }
            if (fOut != null) fOut.printf("%d, %d (stress=%f)\n",
                    (int) (1e9 * 10 / (System.nanoTime() - t)), 
                    (int) (1e9 * 10 * len / (System.nanoTime() - t)),
                    prev);
            fiterations -= 10;
        }
        if (fListener != null) fListener.update(mds);
        return mds.points();
    }

    public MultidimensionalScaling similarities(double[][] matrix) {
        SMat d = new SMat(matrix);
        d.apply(Function.COSINE_TO_DISSIMILARITY);
        fdistances = d;
        return this;
    }

    public MultidimensionalScaling verbose() {
        fOut = System.out;
        return this;
    }

    public interface MultidimensionalScalingListener extends EventListener {
        public void update(Mds mds);
    }

}
