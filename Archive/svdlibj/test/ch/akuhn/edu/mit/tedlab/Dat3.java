package ch.akuhn.edu.mit.tedlab;

import java.io.File;
import java.io.FileNotFoundException;

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

public class Dat3 {

    public static void main(String... args) throws FileNotFoundException {
        Svdlib svdlib = new Svdlib();
        SMat A = Svdlib.svdLoadSparseTextHBFile(new File("lib/dat3.txt"));
        int dimensions = Math.min(A.cols, A.rows);
        int iterations = 100;
        double[] end = new double[] { -1e-30, 1e-30 };
        double kappa = 1e-6;
        long time = System.currentTimeMillis();
        SVDRec R = svdlib.svdLAS2(A, dimensions, iterations, end, kappa);
        System.out.println("-----------" + (System.currentTimeMillis() - time));
        for (int n = 0; n < R.d; n++) {
            System.out.println(R.S[n]);
        }
    }

}

