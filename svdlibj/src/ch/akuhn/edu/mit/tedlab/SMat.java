/**
 * 
 */
package ch.akuhn.edu.mit.tedlab;

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