package ch.akuhn.hapax.linalg;

import org.codemap.svdlib.Svdlib;
import org.codemap.svdlib.Svdlib.SMat;
import org.codemap.svdlib.Svdlib.SVDRec;

/** Singular value decomposition of matrix A.
 *<P> 
 * Given a term-document matrix A, with dimension m on n, the decomposition consists of
 * S = a diagonal matrix with the k largest singular values, 
 * U = m left singular vectors of length k, one for each document, and
 * V = n right singular vectors of length k, one for each term.
 * The matrix A' = U * S * V^T is the closest rank k approximation of of matrix A
 * [Eckard and Young].
 *<P>
 * Instances of this class are immutable. 
 * 
 * @author Adrian Kuhn, 2008-2009
 *
 */
public class SVD {

    private final double[] s;
    private final double[][] U; // terms
    private final double[][] V; // documents


    public SVD(double[] s, double[][] U, double[][] V) {
        this.s = s;
        this.U = U;
        this.V = V; 
        assert invariant();
    }

    private boolean invariant() {
        if (s == null || U == null || V == null ) return false;
        if (s.length > U.length || s.length > V.length) return false;
        for (int n = 0; n < U.length; n++) if (s.length != U[n].length) return false;
        for (int n = 0; n < V.length; n++) if (s.length != V[n].length) return false;
        return true;
    }


    public SVD(SparseMatrix matrix, int dimensions) {
        if (matrix.rowCount() == 0 || matrix.columnCount() == 0) {
            s = new double[0];
            U = new double[matrix.rowCount()][0];
            V = new double[matrix.columnCount()][0];
        }
        else {
            SMat input = makeSMat(matrix);
            SVDRec r = new Svdlib().svdLAS2(input, dimensions, 0, new double[] { -1e-30, 1e-30 }, 1e-6);
            s = r.S;
            U = transpose(r.Ut.value);
            V = transpose(r.Vt.value);
        }
        assert invariant();
    }

    /*default*/ static final double[][] append(double[][] data, double[] values) {
        assert data.length == 0 || data[0].length == values.length;
        double[][] result = new double[data.length + 1][];
        System.arraycopy(data, 0, result, 0, data.length);
        result[data.length] = values;
        return result;
    }

    /*default*/ static final double[][] replace(double[][] data, int index, double[] values) {
        assert 0 <= index && index < data.length;
        assert data[index].length == values.length;
        double[][] result = new double[data.length][];
        System.arraycopy(data, 0, result, 0, data.length);
        result[index] = values;
        return result;
    }

    /*default*/ static final double[][] remove(double[][] data, int index) {
        assert 0 <= index && index < data.length;
        double[][] result = new double[data.length - 1][];
        System.arraycopy(data, 0, result, 0, index);
        System.arraycopy(data, index + 1, result, index, data.length - index - 1);
        return result;
    }


    /** Returns a copy with additional term.
     * 
     * @param u new term vector, length k.
     * @return new copy of this instance.
     */
    public SVD withAppendU(double[] u) {
        assert u.length == s.length;
        return new SVD(s, append(U, u), V);
    }

    /** Returns a copy with additional document.
     * 
     * @param v new document vector, length k.
     * @return new copy of this instance.
     */
    public SVD withAppendV(double[] v) {
        assert v.length == s.length;
        return new SVD(s, U, append(V, v));
    }

    /** Returns the rank of this singular value decomposition.
     * 
     * @return a positive integer.
     */
    public int getRank() {
        return s.length;
    }

    private double[] project(double[][] data, double[] weightings) {
        if (s.length == 0) return new double[] { };
        assert weightings.length == data.length;
        double[] result = new double[s.length];
        for (int n = 0; n < weightings.length; n++) {
            double weight = weightings[n];
            if (weight == 0) continue;
            double[] dn = data[n];
            for (int k = 0; k < s.length; k++) {
                result[k] += weight * dn[k];
            }
        }
        for (int k = 0; k < s.length; k++) {
            result[k] /= s[k];
        }
        return result;
    }

    /** Places a term at the weighted sum of its documents.
     * 
     * @param weightings document weight vector, length = m.
     * @return pseudo term vector.
     */
    public double[] makePseudoU(double[] weightings) {
        return project(V, weightings);
    }

    /** Places a document at the weighted sum of its terms.
     * 
     * @param weightings
     * @return
     */
    public double[] makePseudoV(double[] weightings) {
        return project(U, weightings);
    }

    /*default*/ static final SMat makeSMat(SparseMatrix matrix) {
        SMat S = new Svdlib().new SMat(matrix.rowCount(), matrix.columnCount(), matrix.used());
        for (int j = 0, n = 0; j < matrix.columnCount(); j++) {
            S.pointr[j] = n;
            for (int i = 0; i < matrix.rowCount(); i++)
                if (matrix.get(i, j) != 0) {
                    S.rowind[n] = i;
                    S.value[n] = matrix.get(i, j);
                    n++;
                }
        }
        S.pointr[S.cols] = S.vals;
        return S;
    }

    private double similaritySqrt(double[] a, double[] b) {
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int k = 0; k < b.length; k++) {
            double s2 = s[k]; 
            sim += a[k] * b[k] * s2;
            suma += a[k] * a[k] * s2;
            sumb += b[k] * b[k] * s2;
        }
        return (sim / (Math.sqrt(suma) * Math.sqrt(sumb)));
    }


    private double similarity(double[] a, double[] b) {
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int k = 0; k < b.length; k++) {
            double s2 = s[k] * s[k]; 
            sim += a[k] * b[k] * s2;
            suma += a[k] * a[k] * s2;
            sumb += b[k] * b[k] * s2;
        }
        return (sim / (Math.sqrt(suma) * Math.sqrt(sumb)));
    }

    /** Computes the similarity between i-th term and pseudo-term t.
     * 
     * @param i index of a term, range 0..m.
     * @param t a pseudo-term vector, length k
     * @return a cosine value, typically greater then zero.
     */
    public double similarityU(int i, double[] t) {
        if (s.length == 0) return Double.NaN;
        return similarity(U[i], t);
    }

    /** Computes the similarity between a-th and b-th term.
     * 
     * @param a index of a term, range 0..m.
     * @param b index of a term, range 0..m.
     * @return a cosine value, typically greater then zero.
     */
    public double similarityUU(int a, int b) {
        if (s.length == 0) return Double.NaN;
        return similarity(U[a], U[b]);
    }

    /** Computes the similarity between i-th term and j-th document.
     * 
     * @param i index of a term, range 0..m.
     * @param j index of a document, range 0..n.
     * @return a cosine value, typically greater than zero.
     */
    public double similarityUV(int i, int j) {
        if (s.length == 0) return Double.NaN;
        return similaritySqrt(U[i], V[j]);
    }

    /** Computes the similarity between j-th document and pseudo-document d.
     * 
     * @param j index of a document, range 0..n. 
     * @param d a pseudo-document vector, length k.
     * @return a cosine value, typically greater than zero.
     */
    public double similarityV(int j, double[] d) {
        if (s.length == 0) return Double.NaN;
        return similarity(V[j], d);
    }

    /** Computes the similarity between a-th and b-th document.
     * 
     * @param a index of a document, range 0..n.
     * @param b index of a document, range 0..n.
     * @return a cosine value, typically greater then zero.
     */
    public double similarityVV(int a, int b) {
        if (s.length == 0) return Double.NaN;
        return similarity(V[a], V[b]);
    }

    /*default*/ static final double[][] transpose(double[][] array) {
        if (array.length == 0) return new double[][] {{}};
        int width = array[0].length, height = array.length;
        double[][] t = new double[width][height];
        for (int n = 0; n < width; n++) {
            for (int m = 0; m < height; m++) {
                t[n][m] = array[m][n];
            }
        }
        return t;
    }

    /** Returns the decomposition of matrix A^T.
     * 
     * @return new copy of this instance.
     */
    public SVD transposed() {
        return new SVD(s, V, U); // swap U and V
    }

    /** Returns a copy, where one term is updated.
     * 
     * @param index index of a term, range 0..m.
     * @param values new term vector, length k.
     * @return new copy of this instance.
     */
    public SVD withReplaceU(int index, double[] values) {
        assert values.length == s.length;
        return new SVD(s, replace(U, index, values), V);
    }

    /** Returns a copy, where one document is updated.
     * 
     * @param index index of a document, range 0..n.
     * @param values new document vector, length k.
     * @return new copy of this instance.
     */
    public SVD withReplaceV(int index, double[] values) {
        assert values.length == s.length;
        return new SVD(s, U, replace(V, index, values));
    }

    /** Returns the number of right singular vectors in V,
     * that is the number of columns of A (m x n).
     * Given a term-document matrix, this is the number of documents.
     * 
     * @return the value of n.
     */
    public int columnCount() {
        return V.length;
    }

    /** Returns the number of left singular vectors in U,
     * that is the number of rows of A (m x n).
     * Given a term-document matrix, this is the number of terms.
     * 
     * @return the value of m.
     */
    public int rowCount() {
        return U.length;
    }

    /** Returns a copy, with selected documents only.
     * 
     * @param indices list of indices of documents, range 0..n.
     * @return a new copy of this instance.
     * 
     */
    public SVD withSelectV(int[] indices) {
        double[][] selectV = new double[indices.length][s.length];
        for (int n = 0; n < indices.length; n++) {
            for (int k = 0; k < s.length; k++) {
                selectV[n][k] = V[indices[n]][k];
            }
        }
        return new SVD(s, U, selectV);
    }

    /** Returns copy without given document.
     * 
     * @param doc index of a document, range 0..n.
     * @return new copy of this instance.
     */
    public SVD withoutV(int doc) {
        return new SVD(s, U, remove(V, doc));
    }


}
