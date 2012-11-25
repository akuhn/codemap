/**
 * 
 */
package ch.akuhn.edu.mit.tedlab;

public class DMat {
    public int rows;
    public int cols;
    public double[][] value; /*
     * Accessed by [row][col]. Free value[0] and value to
     * free.
     */

    public DMat(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.value = new double[rows][cols];
    }
}