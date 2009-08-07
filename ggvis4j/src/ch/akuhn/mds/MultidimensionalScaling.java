package ch.akuhn.mds;

import java.io.PrintStream;
import java.util.EventListener;

import ch.akuhn.org.ggobi.plugins.ggvis.Mds;

/** Multidimensional scaling, based on GGobi/GGvis.
 * 
 * @author Adrian Kuhn, 2009
 *
 */
public class MultidimensionalScaling {

	double[][] fdistances;
	int fiterations = 100;
	private PointsListener flistener;
	private PrintStream fout;
	
	public MultidimensionalScaling dissimilarities(double[][] matrix) {
		for (double[] each: matrix) assert each.length == matrix.length;
		fdistances = matrix;
		return this;
	}
	
	public MultidimensionalScaling listener(PointsListener listener) {
		this.flistener = listener;
		return this;
	}
	
	public MultidimensionalScaling maxIterations(int iterations) {
		this.fiterations = iterations;
		return this;
	}
	
	public double[][] run() {
		if (fdistances.length == 0) return new double[0][2];
		Mds mds = new Mds();
		mds.init(fdistances);
		int len = fdistances.length;
		fdistances = null;
		while (fiterations > 0) {
			if (flistener != null) flistener.update(mds.points());
			long t = System.nanoTime();
			for (int n = 0; n < 10; n++) mds.mds_once(true);
			if (fout != null) fout.printf("%d\n", (int) (1e9 * 10 / (System.nanoTime() - t)));
			fiterations -= 10;
		}
		double[][] points = mds.points();
		assert points.length == len;
		for (double[] each: points) assert each.length == 2;
		mds = null;
		return points;
	}
	
	public MultidimensionalScaling similarities(double[][] matrix) {
		for (double[] each: matrix) assert each.length == matrix.length;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = Math.sqrt(Math.max(0, 1 -matrix[i][j]));
			}
		}
		return this.dissimilarities(matrix);
	}
	
	public MultidimensionalScaling verbose() {
		fout = System.out;
		return this;
	}
	
	public interface PointsListener extends EventListener {
		public void update(double[][] points);
	}
	
}
