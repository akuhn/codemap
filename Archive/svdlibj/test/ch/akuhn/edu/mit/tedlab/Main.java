package ch.akuhn.edu.mit.tedlab;

import ch.akuhn.edu.mit.tedlab.DMat;
import ch.akuhn.edu.mit.tedlab.SMat;
import ch.akuhn.edu.mit.tedlab.SVDRec;
import ch.akuhn.edu.mit.tedlab.Svdlib;

// # CAUTION DO NOT USE !!!
// 
// This implementation of SVDLIB is not correct!
// 
// For correct code please refer to [1]
// 
// This code has been manually translated the code from C to Java. We have been 
// reported that results different from the original code for some problem sets. 
// We have a better implementation of SVD in Java, based on a version of ARPACK 
// that was automatically transcoded from the Fortran sources using f2j [2] it
// is faster, works on offline matrices and results should be accurate since the
// translation to Java was automated.
// 
// [1] http://bender.unibe.ch/svn/codemap/ch.akuhn.matrix/src/ch/akuhn/matrix/eigenvalues/
// [2] http://icl.cs.utk.edu/f2j
//
// --Nov 2011

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
        DMat D = new DMat(12, 9);
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
