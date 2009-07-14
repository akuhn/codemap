package ch.akuhn.hapax.linalg;

import static ch.akuhn.util.Interval.range;

import java.io.IOException;

import org.codemap.svdlib.Svdlib;
import org.codemap.svdlib.Svdlib.SMat;
import org.codemap.svdlib.Svdlib.SVDRec;

import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ReadFromChunk;

public class SVD {

    public SVD(double[] s, double[][] U, double[][] V) {
        assert s.length == U[0].length;
        assert s.length == V[0].length;
        this.s = s;
        this.U = U;
        this.V = V; 
    }
    
    @ReadFromChunk("SVD")
    public SVD(ChunkInput chunk) throws IOException {
    	int m = chunk.readInt();
    	int n = chunk.readInt();
    	int k = chunk.readInt();
    	this.s = chunk.readDoubleArray(k);
    	this.U = chunk.readDoubleArray(k,m);
    	this.V = chunk.readDoubleArray(k,n);
    }
    

    private SVD decompose(SparseMatrix matrix, int dimensions) {
        if (matrix.rowCount() == 0 || matrix.columnCount() == 0) {
            s = new double[0];
            U = new double[0][];
            V = new double[0][];
            return this;
        }
        SMat input = makeSMat(matrix);
        SVDRec r = new Svdlib().svdLAS2(input, dimensions, 0, new double[] { -1e-30, 1e-30 }, 1e-6);
        s = r.S;
        U = t(r.Ut.value);
        V = t(r.Vt.value);
        return this;
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

    public double[] s;

    public double time;

    public double[][] U;

    public double[][] V;

	private int modeCount;

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
	
	
    public double similarityUU(int a, int b) {
    	if (s.length == 0) return Double.NaN;
        return similarityS2(U[a], U[b]);
    }

    public double similarityVV(int a, int b) {
    	if (s.length == 0) return Double.NaN;
        return similarityS2(V[a], V[b]);
    }

    public double similarityUV(int a, int b) {
    	if (s.length == 0) return Double.NaN;
        return similarityS1(U[a], V[b]);
    }

    public double similarityVU(int a, int b) {
    	if (s.length == 0) return Double.NaN;
        return similarityS1(V[a], U[b]);
    }

    public double similarityV(int a, double[] pseudoV) {
    	if (s.length == 0) return Double.NaN;
        return similarityS2(V[a], pseudoV);
    }

    public double similarityU(int a, double[] pseudoU) {
    	if (s.length == 0) return Double.NaN;
        return similarityS2(U[a], pseudoU);
    }
    
    
    public SVD(SparseMatrix matrix, int dimensions) {
        this.decompose(matrix, dimensions);
        assert s != null;
        assert V != null;
        assert U != null;
        if (s.length == 0) {
        	assert V.length == 0;
        	assert U.length == 0;
        }
        else {
        	assert V[0].length == s.length : V[0].length +"=="+ s.length;
        	assert U[0].length == s.length;
        }
    }

    public SVD transposed() {
        return new SVD(s, V, U); // swap U and V
    }

    public double[] makePseudoV(double[] weightings) {
    	return makePseudo(U, weightings);
    }
    
    public double[] makePseudoU(double[] weightings) {
    	return makePseudo(V, weightings);
    }
    
    private double[] makePseudo(double[][] data, double[] weightings) {
    	if (s.length == 0) return new double[] { };
    	assert weightings.length == data[0].length;
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
    
    public void updateU(int index, double[] values) {
    	assert values.length == s.length;
    	U[index] = values;
    	modeCount++;
    }

    public void updateV(int index, double[] values) {
    	assert values.length == s.length;
    	V[index] = values;
    	modeCount++;
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
    
}