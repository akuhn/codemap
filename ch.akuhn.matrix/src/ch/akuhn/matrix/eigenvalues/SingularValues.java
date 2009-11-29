package ch.akuhn.matrix.eigenvalues;

import ch.akuhn.linalg.Matrix;
import ch.akuhn.linalg.SVD;
import ch.akuhn.linalg.SparseMatrix;
import ch.akuhn.linalg.Vector;
import ch.akuhn.util.Out;
import ch.akuhn.util.Stopwatch;

public class SingularValues {

	public double[] value;
	public Vector[] vectorLeft;
	public Vector[] vectorRight;
	
	private Matrix A;
	private int nev;
	
	public SingularValues(Matrix A, int nev) {
		this.A = A;
		this.nev = nev;
	}
	
	public SingularValues decompose() {
		final Stopwatch stopwatch = new Stopwatch();
		Eigenvalues eigen = new FewEigenvalues(A.rowCount()) {
			@Override
			protected Vector callback(Vector v) {
				stopwatch.on();
				return stopwatch.off(A.mult(A.transposeMultiply(v)));
			}
		}.greatest(nev).run();
		stopwatch.total("ATV+AV");
		value = new double[nev];
		vectorLeft = eigen.vector;
		vectorRight = new Vector[nev];
		for (int i = 0; i < nev; i++) {
			value[i] = Math.sqrt(eigen.value[i]);
			vectorRight[i] = A.transposeMultiply(vectorLeft[i]);
			vectorRight[i].times(1.0/vectorRight[i].norm());
		}
		return this;
	}
	
	public static void main(String... args) {
		Stopwatch.p();
		SparseMatrix A = Matrix.sparse(400,5000);
		for (int i = 0; i < A.rowCount(); i++) {
			for (int j = 0; j < A.columnCount(); j++) {
				if (Math.random() > 0.2) continue;
				A.put(i, j, Math.random() * 23);
			}
		} 
		Stopwatch.p();
		SingularValues singular = new SingularValues(A, 10).decompose();
		Stopwatch.p("ARPACK");
		Out.puts(singular.value);
		Stopwatch.p();
		SVD svd = new SVD(A, 10);
		Stopwatch.p("SVDLIBC");
		Out.puts(svd.s);
	}
	
}
