/**
 * 
 */
package ch.akuhn.edu.mit.tedlab;

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

public class SMat {

    public int rows;
    public int cols;
    public int vals; /* Total non-zero entries. */
    public int[] pointr; /* For each col (plus 1), index of first non-zero entry. */
    public int[] rowind; /* For each nz entry, the row index. */
    public double[] value; /* For each nz entry, the value. */

    public SMat(int rows, int cols, int vals) {
        this.rows = rows;
        this.cols = cols;
        this.vals = vals;
        this.pointr = new int[cols + 1];
        this.rowind = new int[vals];
        this.value = new double[vals];
    }
}