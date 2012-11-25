package ch.akuhn.matrix.eigenvalues;

import java.util.Random;

import ch.akuhn.matrix.Matrix;
import ch.akuhn.matrix.SparseMatrix;
import ch.akuhn.matrix.Vector;
import ch.akuhn.util.Out;
import ch.akuhn.util.Stopwatch;

public class SingularValues {

	public double[] value;
	public Vector[] vectorLeft;
	public Vector[] vectorRight;

	Matrix A;
	private int nev;

	public SingularValues(Matrix A, int nev) {
		this.A = A;
		this.nev = nev;
	}

	public SingularValues decompose() {
		Eigenvalues eigen = new FewEigenvalues(A.rowCount()) {
			@Override
			protected Vector callback(Vector v) {
				return A.mult(A.transposeMultiply(v));
			}
		}.greatest(nev).run();
		value = new double[nev];
		vectorLeft = eigen.vector;
		vectorRight = new Vector[nev];
		for (int i = 0; i < nev; i++) {
			value[i] = Math.sqrt(eigen.value[i]);
			vectorRight[i] = A.transposeMultiply(vectorLeft[i]);
			vectorRight[i].times(1.0 / vectorRight[i].norm());
		}
		return this;
	}

	public static void main(String... args) {
		Stopwatch.enter();
		Stopwatch.p();
		SparseMatrix A = Matrix.sparse(4000, 10000);
		Random rand = new Random(1);
		for (int i = 0; i < A.rowCount(); i++) {
			for (int j = 0; j < A.columnCount(); j++) {
				if (rand.nextDouble() > 0.2) continue;
				A.put(i, j, rand.nextDouble() * 23);
			}
		}
		SingularValues singular = new SingularValues(A, 10).decompose();
		Stopwatch.p();
		Out.puts(singular.value);
	}

}
