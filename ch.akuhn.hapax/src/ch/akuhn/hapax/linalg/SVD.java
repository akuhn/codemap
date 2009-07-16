package ch.akuhn.hapax.linalg;

import java.io.IOException;

import org.codemap.svdlib.Svdlib;
import org.codemap.svdlib.Svdlib.SMat;
import org.codemap.svdlib.Svdlib.SVDRec;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.AssociativeList;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ReadFromChunk;

public class SVD {

    private final double[] s;
    private final double[][] U; // terms
    private final double[][] V; // documents


    @ReadFromChunk("SVD")
    public SVD(ChunkInput chunk) throws IOException {
    	int m = chunk.readInt();
    	int n = chunk.readInt();
    	int k = chunk.readInt();
    	this.s = chunk.readDoubleArray(k);
    	this.U = chunk.readDoubleArray(k,m);
    	this.V = chunk.readDoubleArray(k,n);
        assert invariant();
    }

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
            U = new double[matrix.rowCount()][];
            V = new double[matrix.columnCount()][];
        }
        else {
	        SMat input = makeSMat(matrix);
	        SVDRec r = new Svdlib().svdLAS2(input, dimensions, 0, new double[] { -1e-30, 1e-30 }, 1e-6);
	        s = r.S;
	        U = t(r.Ut.value);
	        V = t(r.Vt.value);
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
    
    
	public SVD withAppendU(double[] pseudoU) {
    	assert pseudoU.length == s.length;
    	return new SVD(s, append(U, pseudoU), V);
    }

	public SVD withAppendV(double[] pseudoV) {
    	assert pseudoV.length == s.length;
    	return new SVD(s, U, append(V, pseudoV));
    }

    public int getRank() {
    	return s.length;
    }

    private double[] makePseudo(double[][] data, double[] weightings) {
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

    public double[] makePseudoU(double[] weightings) {
    	return makePseudo(V, weightings);
    }

    public double[] makePseudoV(double[] weightings) {
    	return makePseudo(U, weightings);
    }

    private SMat makeSMat(SparseMatrix matrix) {
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

    private double similarityS1(double[] a, double[] b) {
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
    
    
    private double similarityS2(double[] a, double[] b) {
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

    public double similarityU(int a, double[] pseudoU) {
    	if (s.length == 0) return Double.NaN;
        return similarityS2(U[a], pseudoU);
    }

    public double similarityUU(int a, int b) {
    	if (s.length == 0) return Double.NaN;
        return similarityS2(U[a], U[b]);
    }
    
    public double similarityUV(int a, int b) {
    	if (s.length == 0) return Double.NaN;
        return similarityS1(U[a], V[b]);
    }
    
    public double similarityV(int a, double[] pseudoV) {
    	if (s.length == 0) return Double.NaN;
        return similarityS2(V[a], pseudoV);
    }
    
    public double similarityVU(int a, int b) {
    	if (s.length == 0) return Double.NaN;
        return similarityS1(V[a], U[b]);
    }

    public double similarityVV(int a, int b) {
    	if (s.length == 0) return Double.NaN;
        return similarityS2(V[a], V[b]);
    }
    
    private double[][] t(double[][] array) {
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
    
    public SVD transposed() {
        return new SVD(s, V, U); // swap U and V
    }
    
    public SVD withReplaceU(int index, double[] values) {
    	assert values.length == s.length;
    	return new SVD(s, replace(U, index, values), V);
    }
    
    public SVD withReplaceV(int index, double[] values) {
    	assert values.length == s.length;
    	return new SVD(s, U, replace(V, index, values));
    }

	public int columnCount() {
		return V.length;
	}
 
	public int rowCount() {
		return U.length;
	}
	
    public SVD withSelectV(int[] indices) {
        double[][] selectV = new double[indices.length][s.length];
        for (int n = 0; n < indices.length; n++) {
            for (int k = 0; k < s.length; k++) {
            	selectV[n][k] = V[indices[n]][k];
            }
        }
        return new SVD(s, U, selectV);
    }	
	
}
