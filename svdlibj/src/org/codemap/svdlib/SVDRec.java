/**
 * 
 */
package org.codemap.svdlib;

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