package svdlib;

import svdlib.Svdlib.DMat;
import svdlib.Svdlib.SMat;

public class Main {

	public static void main(String[] args) {
		
		Svdlib svdlib = new Svdlib();
		SMat A = makeSMat();
		int dimensions = 3;
		int iterations = Math.min(A.cols, A.rows);
		double[] end = new double[] { -1e-30, 1e-30 };
		double kappa = 1e-6;
		svdlib.svdLAS2(A, dimensions, iterations, end, kappa);
		
	}

	private static SMat makeSMat() {
		DMat D = new Svdlib().new DMat(12, 9);
		D.value[0] = new double[] { 1, 0, 0, 1, 0, 0, 0, 0, 0 };
		D.value[1] = new double[] { 1, 0, 1, 0, 0, 0, 0, 0, 0 };
		D.value[2] = new double[] { 1, 1, 0, 0, 0, 0, 0, 0, 0 };
		D.value[3] = new double[] { 0, 1, 1, 0, 1, 0, 0, 0, 0 };
		D.value[4] = new double[] { 0, 1, 1, 2, 0, 0, 0, 0, 0 };
		D.value[5] = new double[] { 0, 1, 0, 0, 1, 0, 0, 0, 0 };
		D.value[6] = new double[] { 0, 1, 0, 0, 1, 0, 0, 0, 0 };
		D.value[7] = new double[] { 0, 0, 1, 1, 0, 0, 0, 0, 0 };
		D.value[8] = new double[] { 	0, 1, 0, 0, 0, 0, 0, 0, 1 };
		D.value[9] = new double[] { 	0, 0, 0, 0, 0, 1, 1, 1, 0 };
		D.value[10] = new double[] { 0, 0, 0, 0, 0, 0, 1, 1, 1 };
		D.value[10] = new double[] { 0, 0, 0, 0, 0, 0, 0, 1, 1 };
		return new Svdlib().svdConvertDtoS(D);
	}
	
}
