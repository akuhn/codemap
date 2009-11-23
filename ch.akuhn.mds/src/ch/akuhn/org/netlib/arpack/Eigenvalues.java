package ch.akuhn.org.netlib.arpack;

import java.util.Arrays;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;

import org.netlib.arpack.ARPACK;
import org.netlib.util.doubleW;
import org.netlib.util.intW;

import ch.akuhn.util.Stopwatch;

public class Eigenvalues {

    public final int n;
    public final int nev;
    private DenseMatrix matrix;
    
    public double[] value;
    public double[][] vector;
    
    public Eigenvalues(int n, int nev) {
        this.n = n;
        this.nev = nev;
        System.out.println("nev = " + nev);
    }
    
    public Eigenvalues(DenseMatrix matrix, int nev) {
        assert matrix.isSquare();
        this.n = matrix.numRows();
        this.nev = nev;
        this.matrix = matrix;
    }
    
    public void run() {
        
        ARPACK arpack = ARPACK.getInstance();

        intW ido = new intW(0);
        String bmat = "I"; 
        String which = "LA";
        doubleW tol = new doubleW(0); // uses machine precision
        double[] resid = new double[n];
        int ncv = nev * 2; // rule of thumb would be nev * 2
        double[] v = new double[n * ncv];
        int[] iparam = new int[11];
        {
            int ishfts = 1;
            int maxitr = 300;
            int mode1 = 1;
            iparam[1-1] = ishfts;
            iparam[3-1] = maxitr;
            iparam[7-1] = mode1;
        }
        int[] ipntr = new int[11];
        double[] workd = new double[3*n];
        double[] workl = new double[ncv*(ncv+8)];
        intW info = new intW(0);

        org.netlib.arpack.arpack_debug.ndigit.val = -3;
        org.netlib.arpack.arpack_debug.logfil.val = 6;
        org.netlib.arpack.arpack_debug.msgets.val = 0;
        org.netlib.arpack.arpack_debug.msaitr.val = 0;
        org.netlib.arpack.arpack_debug.msapps.val = 0;
        org.netlib.arpack.arpack_debug.msaupd.val = 0;
        org.netlib.arpack.arpack_debug.msaup2.val = 0;
        org.netlib.arpack.arpack_debug.mseigt.val = 0;
        org.netlib.arpack.arpack_debug.mseupd.val = 0;
        
        while (true) {
        
            arpack.dsaupd(
                    ido,
                    bmat,
                    n,
                    which,
                    nev,
                    tol,
                    resid,
                    ncv,
                    v,
                    n,
                    iparam,
                    ipntr,
                    workd,
                    workl,
                    workl.length,
                    info);
    
            if (ido.val != 1 && ido.val != -1) break;
            
            int x0 = ipntr[1-1]-1;
            int y0 = ipntr[2-1]-1;
            
            double[] vector = new double[n];
            System.arraycopy(workd, x0, vector, 0, n);
            vector = this.callback(vector);
            System.arraycopy(vector, 0, workd, y0, n);
            
        }

        if (info.val < 0) {
            throw new Error("dsaupd ERRNO = "+info.val);
        }
        
        boolean rvec = true; 
        boolean[] select = new boolean[ncv];
        double[] d = new double[ncv * 2];
        double sigma = 0; // not used in this mode
        intW ierr = new intW(0);
        intW nevW = new intW(nev);
        
        arpack.dseupd(
                rvec, 
                "All",
                select,
                d,
                v,
                n,
                sigma,
                bmat,
                n,
                which,
                nevW,
                tol.val,
                resid,
                ncv,
                v,
                n,
                iparam,
                ipntr,
                workd,
                workl,
                workl.length,
                ierr );
        
        Stopwatch.p("with ARPACK");

        if (ierr.val < 0) {
            throw new Error("dseupd ERRNO = "+info.val);
        }
 
        int nconv = iparam[5-1];
        
        value = Arrays.copyOf(d, nconv);
        vector = new double[nconv][];    
        for (int i = 0; i < value.length; i++) {
            vector[i] = Arrays.copyOfRange(v, i*n, (i+1)*n);
        }
        
        sandbox.Vector.from(value).p();
        sandbox.Vector.from(vector[0]).p();
        
        System.out.println("done");

        
    }
    
    protected double[] callback(double[] vector) {
        DenseVector vx = new DenseVector(vector);
        DenseVector vy = new DenseVector(vector.length);
        matrix.mult(vx, vy);
        return vy.getData();
    }
    
}
