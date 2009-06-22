package org.codemap.svdlib;

import org.codemap.svdlib.Svdlib.DMat;
import org.codemap.svdlib.Svdlib.SMat;
import org.codemap.svdlib.Svdlib.SVDRec;

public class Main {

	public static void main(String[] args) {
		
		Svdlib svdlib = new Svdlib();
		SMat A = makeSMat();
		int dimensions = 9;
		int iterations = 100;
		double[] end = new double[] { -1e-30, 1e-30 };
		double kappa = 1e-6;
		long time = System.nanoTime();
		SVDRec R = svdlib.svdLAS2(A, dimensions, iterations, end, kappa);
		System.out.println(System.nanoTime() - time);
		for (int n = 0; n < R.d; n++) {
			System.out.println(R.S[n]);
		}
		
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
		D.value[8] = new double[] { 0, 1, 0, 0, 0, 0, 0, 0, 1 };
		D.value[9] = new double[] { 0, 0, 0, 0, 0, 1, 1, 1, 0 };
		D.value[10] = new double[] { 0, 0, 0, 0, 0, 0, 1, 1, 1 };
		D.value[11] = new double[] { 0, 0, 0, 0, 0, 0, 0, 1, 1 };
		return new Svdlib().svdConvertDtoS(D);
	}
	
}
