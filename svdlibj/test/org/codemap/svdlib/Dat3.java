package org.codemap.svdlib;

import java.io.File;
import java.io.FileNotFoundException;



public class Dat3 {

    public static void main(String... args) throws FileNotFoundException {
        Svdlib svdlib = new Svdlib();
        SMat A = svdlib.svdLoadSparseTextHBFile(new File("lib/dat3.txt"));
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

