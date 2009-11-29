package ch.akuhn.org.netlib.arpack;

import java.util.Arrays;

import org.netlib.arpack.ARPACK;
import org.netlib.util.doubleW;
import org.netlib.util.intW;

import ch.akuhn.linalg.Matrix;
import ch.akuhn.linalg.Vector;

/** Finds the largest eigenvalues of a matrix. 
 *<P> 
 * This class use ARPACK to find a few eigenvalues (&lambda;) and corresponding
 * eigenvectors (<b>x</b>) for the standard eigenvalue problem:
 *<PRE>A<b>x</b> = &lambda;<b>x</b></PRE>          
 * where <code>A</code> is an <code>n</code> &times; <code>n</code> real symmetric matrix.
 *<P>
 * The only thing that must be supplied in order to use this
 * routine on your problem is to change the array dimensions 
 * appropriately, to specify <em>which</em> eigenvalues you want to compute 
 * and to supply a matrix-vector product
 *<PRE><b>w</b> &larr; A<b>v</b></PRE>
 * in the {@link #callback(Vector)} method.
 *<P> 
 * <b>Example:</b>
 *<PRE>
 * Matrix A = <i>&hellip;square matrix&hellip;</i>;
 * Eigenvalues eigen = Eigenvalues.of(A).largest(4);
 * eigen.run();
 * double[] l = eigen.values;
 * Vector[] x = eigen.vectors;
 *</PRE> 
 * @author Adrian Kuhn (Java)
 * @author Richard Lehoucq, Danny Sorensen, Chao Yang (Fortran)
 *
 */
public abstract class Eigenvalues implements Runnable {

	private enum Which { LA, SA, LM, SM, BE };
	
	/** Problem size. */
	private int n;
	
	/** The number of eigenvalues requested. */
	private int nev = 4;
	
	/** The kind of eigenvalues requests. */
    private Which which = Which.LM;
    
    public double[] value;
    public Vector[] vector;

    public static Eigenvalues of(final Matrix matrix) {
    	assert matrix.isSquare();
    	return new Eigenvalues(matrix.columnCount()) {
			@Override
			protected Vector callback(Vector vector) {
				return matrix.mult(vector);
			}
    	};
    }
    
    public Eigenvalues(int n) {
        this.n = n;
    }
    
    private Eigenvalues which(Which which, int nev) {
    	this.which = which;
    	this.nev = nev;
    	return this;
    }

    /** Compute the largest algebraic eigenvalues. */
    public Eigenvalues largest(int nev) {
    	return which(Which.LA, nev);
    }
    
	/** Compute the smallest algebraic eigenvalues. */
    public Eigenvalues smallest(int nev) {
    	return which(Which.SA, nev);
    }

	/** Compute the largest eigenvalues in magnitude. */
    public Eigenvalues greatest(int nev) {
    	return which(Which.LM, nev);
    }

	/** Compute the smallest eigenvalues in magnitude. */
    public Eigenvalues lowest(int nev) {
    	return which(Which.SM, nev);
    }

	/** Compute eigenvalues from both end of the spectrum.
	 * When the <CODE>nev</CODE> is odd, compute one more from the
	 * high end than from the low end.
	 */
    public Eigenvalues fromBothEnds(int nev) {
    	return which(Which.BE, nev);
    }

    /** Runs the eigenvalue decomposition,
     * using an implicitly restarted Arnoldi process (IRAP).
     * Please refer to the ARPACK guide for more information.
     * 
     */
    public void run() {
    	ARPACK arpack = ARPACK.getInstance();
    	/*
    	 * Setting up parameters for DSAUPD call.
    	 * 
    	 */
        intW ido = new intW(0); // must start zero
        String bmat = "I"; // standard problem
        doubleW tol = new doubleW(0); // uses machine precision
        intW info = new intW(0); // request random starting vector
        double[] resid = new double[n]; // allocate starting vector
        /*
         * NVC is the largest number of basis vectors that will
         * be used in the Implicitly Restarted Arnoldi Process.
         * Work per major iteration is proportional to N*NCV*NCV.  
         */
        int ncv = nev * 2; // rule of thumb use twice nev
        double[] v = new double[n * ncv]; 
        double[] workd = new double[3*n];
        double[] workl = new double[ncv*(ncv+8)];
        // Parameters
        int[] iparam = new int[11]; 
        {
            int ishfts = 1; // use the exact shift strategy
            int maxitr = 300; // max number of Arnoldi iterations
            int mode = 1; // use "mode 1"
            iparam[1-1] = ishfts;
            iparam[3-1] = maxitr;
            iparam[7-1] = mode;
        }
        int[] ipntr = new int[11];
        // debug setting
        org.netlib.arpack.arpack_debug.ndigit.val = -3;
        org.netlib.arpack.arpack_debug.logfil.val = 6;
        org.netlib.arpack.arpack_debug.msgets.val = 0;
        org.netlib.arpack.arpack_debug.msaitr.val = 0;
        org.netlib.arpack.arpack_debug.msapps.val = 0;
        org.netlib.arpack.arpack_debug.msaupd.val = 0;
        org.netlib.arpack.arpack_debug.msaup2.val = 0;
        org.netlib.arpack.arpack_debug.mseigt.val = 0;
        org.netlib.arpack.arpack_debug.mseupd.val = 0;
        /*
         * Main loop (reverse communication loop)
         * 
         * Repeatedly calls the routine DSAUPD and takes
         * actions indicated by parameter IDO until either
         * convergence is indicated or maxitr has been
         * exceeded.
         * 
         */
        assert n > 0;
        assert nev > 0;
        assert ncv > nev && ncv <= n;
        while (true) {
            arpack.dsaupd(
                    ido, // reverse communication parameter
                    bmat, // "I" = standard problem
                    n, // problem size
                    which.name(), // which values are requested?
                    nev, // who many values?
                    tol, // 0 = use machine precision
                    resid, 
                    ncv,
                    v, 
                    n, 
                    iparam,
                    ipntr,
                    workd,
                    workl,
                    workl.length,
                    info );
            if (!(ido.val == 1 || ido.val == -1)) break;
            /* Perform matrix vector multiplication
             *
             *  	y <--- OP*x
             * 
             * The user should supply his own matrix-
             * vector multiplication routine here that
             * takes workd(ipntr(1)) as the input, and
             * return the result to workd(ipntr(2)).   
             * 
             */
            int x0 = ipntr[1-1]-1; // Fortran is off-by-one compared to Java!
            int y0 = ipntr[2-1]-1;
            Vector x = Vector.copy(workd, x0, n);
            Vector y = this.callback(x);
            assert y.size() == n;
            y.storeOn(workd, y0);   
        }
        /*
         * Either we have convergence or there is an error.
         *   
         */
        if (info.val < 0) throw new Error("dsaupd ERRNO = "+info.val);
        /*
         * Post-Process using DSEUPD.
         * 
         * Computed eigenvalues may be extracted.
         * 
         * The routine DSEUPD now called to do this
         * post processing (other modes may require
         * more complicated post processing than
         * "mode 1").
         * 
         */
        boolean rvec = true; // request vectors
        boolean[] select = new boolean[ncv];
        double[] d = new double[ncv * 2];
        double sigma = 0; // not used in "mode 1"
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
                which.name(),
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
        if (ierr.val < 0) throw new Error("dseupd ERRNO = "+info.val);
        /* 
         * Eigenvalues are returned in the first column
         * of the two dimensional array D and the
         * corresponding eigenvectors are returned in
         * the first NCONV (=IPARAM(5)) columns of the 
         * two dimensional array V if requested.
         * Otherwise, an orthogonal basis for the 
         * invariant subspace corresponding to the 
         * eigenvalues in D is returned in V.        
 		 *
 		 */
        int nconv = iparam[5-1];
        value = Arrays.copyOf(d, nconv);
        vector = new Vector[nconv];    
        for (int i = 0; i < value.length; i++) {
        	vector[i] = Vector.copy(v, i*n, n);
        }
    }
    
    protected abstract Vector callback(Vector vector);
    
}
