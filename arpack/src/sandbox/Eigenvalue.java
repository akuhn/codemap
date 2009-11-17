package sandbox;

import org.netlib.arpack.ARPACK;
import org.netlib.util.doubleW;
import org.netlib.util.intW;

public class Eigenvalue {

    public static void main(String[] args) {

        ARPACK arpack = ARPACK.getInstance();

        intW ido = new intW(0);
        String bmat = "I"; 
        int n = 10;
        String which = "LM";
        int nev = 4;
        doubleW tol = new doubleW(0); // uses machine precision
        double[] resid = new double[n];
        int ncv = nev * 2; // rule of thumb
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
                v.length,
                iparam,
                ipntr,
                workd,
                workl,
                workl.length,
                info);

        System.out.println(ido.val);
        
        int x0 = ipntr[1-1]-1;
        int y0 = ipntr[2-1]-1;
        
        System.out.println(x0);
        System.out.println(y0);
        
        //Av.av(nx,workd,(ipntr[(1-(1))]-(1)),workd,(ipntr[(2-(1))]-(1)));

    }

}
