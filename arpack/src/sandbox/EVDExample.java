package sandbox;

import org.netlib.arpack.ARPACK;
import org.netlib.util.doubleW;
import org.netlib.util.intW;

import ch.akuhn.util.Stopwatch;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.EVD;
import no.uib.cipr.matrix.NotConvergedException;
import no.uib.cipr.matrix.Vector;

public class EVDExample {

    public static void main(String[] args) throws NotConvergedException {
        
        DenseMatrix m = new DenseMatrix(800,800);
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (Math.random() > 0.7) continue;
                double value = Math.random()*i-Math.random()*j;
                m.set(i, j, value);
                m.set(j, i, value);
            }
        }
        
        Stopwatch.p();
        EVD evd = EVD.factorize(m);
        Stopwatch.p("with LAPACK");
        
        sandbox.Vector.from(evd.getRealEigenvalues()).sorted().reversed().p();
        
        // ------------
        
        Stopwatch.p();

        ARPACK arpack = ARPACK.getInstance();

        intW ido = new intW(0);
        String bmat = "I"; 
        int n = m.numColumns();
        String which = "LM";
        int nev = 4;
        doubleW tol = new doubleW(0); // uses machine precision
        double[] resid = new double[n];
        int ncv = nev * 3; // rule of thumb
        double[] v = new double[n * ncv];
        int[] iparam = new int[11];
        {
            int ishfts = 1;
            int maxitr = 50000;
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
            
            Vector vx = new DenseVector(m.numRows());
            Vector vy = new DenseVector(m.numColumns());
            
            for (int i = 0; i < vx.size(); i++) {
                vx.set(i, workd[x0+i]);
            }
            
            m.mult(vx, vy);
    
            for (int i = 0; i < vy.size(); i++) {
                workd[y0+i] = vy.get(i);
            }
            
        }

        if (info.val > 0) {
            System.out.println("dsaupd ERRNO = "+info.val);
        }
        
        boolean rvec = true; 
        boolean[] select = new boolean[ncv];
        double[] d = new double[ncv * 2];
        double sigma = 0; // ??
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
        System.out.println(ierr.val);

        for (int i = 0; i < nev * 2; i++) {
            System.out.println(d[i]);
        }
        
        System.out.println("done");
        
    }
    
}
