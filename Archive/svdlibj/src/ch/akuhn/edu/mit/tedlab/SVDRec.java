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

public class SVDRec {
    public int d; /* Dimensionality (rank) */
    public DMat Ut; /*
     * Transpose of left singular vectors. (d by m) The vectors are
     * the rows of Ut.
     */
    public double[] S; /* Array of singular values. (length d) */
    public DMat Vt; /*
     * Transpose of right singular vectors. (d by n) The vectors are
     * the rows of Vt.
     */

    public SVDRec() {
    }

}