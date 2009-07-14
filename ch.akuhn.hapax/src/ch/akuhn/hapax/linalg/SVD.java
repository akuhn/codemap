package ch.akuhn.hapax.linalg;

import static ch.akuhn.util.Interval.range;

import java.io.IOException;

import org.codemap.svdlib.Svdlib;
import org.codemap.svdlib.Svdlib.SMat;
import org.codemap.svdlib.Svdlib.SVDRec;

import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ReadFromChunk;

public class SVD {

    public SVD(double[] s, double[][] Ut, double[][] Vt) {
        assert s.length == Ut.length;
        assert s.length == Vt.length;
        this.s = s;
        this.Ut = Ut;
        this.Vt = Vt; 
    }
    
    @ReadFromChunk("SVD")
    public SVD(ChunkInput chunk) throws IOException {
    	int m = chunk.readInt();
    	int n = chunk.readInt();
    	int k = chunk.readInt();
    	this.s = chunk.readDoubleArray(k);
    	this.Ut = chunk.readDoubleArray(k,m);
    	this.Vt = chunk.readDoubleArray(k,n);
    }
    

    private SVD decompose(SparseMatrix matrix, int dimensions) {
        if (matrix.rowCount() == 0 || matrix.columnCount() == 0) {
            s = new double[0];
            Ut = new double[0][];
            Vt = new double[0][];
            return this;
        }
        SMat input = makeSMat(matrix);
        SVDRec r = new Svdlib().svdLAS2(input, dimensions, 0, new double[] { -1e-30, 1e-30 }, 1e-6);
        s = r.S;
        Ut = r.Ut.value;
        Vt = r.Vt.value;
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

    public double[][] Ut;

    public double[][] Vt;

    public double similarityUU(int a, int b) {
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n: range(dim)) {
            sim += Ut[n][a] * Ut[n][b] * (s[n] * s[n]);
            suma += Ut[n][a] * Ut[n][a] * (s[n] * s[n]);
            sumb += Ut[n][b] * Ut[n][b] * (s[n] * s[n]);
        }
        return (sim / (Math.sqrt(suma) * Math.sqrt(sumb)));
    }

    public double similarityUV(int a, int b) {
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n: range(dim)) {
            sim += Ut[n][a] * Vt[n][b] * s[n];
            suma += Ut[n][a] * Ut[n][a] * s[n];
            sumb += Vt[n][b] * Vt[n][b] * s[n];
        }
        return sim / (Math.sqrt(suma) * Math.sqrt(sumb));
    }

    public double similarityV(int a, double[] pseudo) {
        assert pseudo.length == s.length;
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n: range(dim)) {
            sim += Vt[n][a] * pseudo[n] * s[n] * s[n];
            suma += Vt[n][a] * Vt[n][a] * s[n] * s[n];
            sumb += pseudo[n] * pseudo[n] * s[n] * s[n];
        }
        return sim / (Math.sqrt(suma) * Math.sqrt(sumb));
    }

    public double similarityVV(int a, int b) {
        assert Vt != null;
        assert s != null;
        assert a < Vt[0].length : a + " < " + Vt[0].length;
        assert b < Vt[0].length : b + " < " + Vt[0].length;
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n: range(dim)) {
            assert Vt[n] != null : n;
            sim += Vt[n][a] * Vt[n][b] * s[n] * s[n];
            suma += Vt[n][a] * Vt[n][a] * s[n] * s[n];
            sumb += Vt[n][b] * Vt[n][b] * s[n] * s[n];
        }
        return sim / (Math.sqrt(suma) * Math.sqrt(sumb));
    }
    
    public SVD(SparseMatrix matrix, int dimensions) {
        this.decompose(matrix, dimensions);
        assert s != null;
        assert Vt != null;
        assert Ut != null;
        assert Vt.length == s.length : Vt.length +"=="+ s.length;
        assert Ut.length == s.length;
    }

    public SVD transposed() {
        return new SVD(s, Vt, Ut); // swap Ut and Vt
    }

}