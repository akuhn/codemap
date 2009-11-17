/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: dssimp.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Dssimp {

    // c
    // c     This example program is intended to illustrate the 
    // c     simplest case of using ARPACK in considerable detail.  
    // c     This code may be used to understand basic usage of ARPACK
    // c     and as a template for creating an interface to ARPACK.  
    // c   
    // c     This code shows how to use ARPACK to find a few eigenvalues 
    // c     (lambda) and corresponding eigenvectors (x) for the standard 
    // c     eigenvalue problem:
    // c          
    // c                        A*x = lambda*x
    // c 
    // c     where A is an n by n real symmetric matrix.
    // c
    // c     The main points illustrated here are 
    // c
    // c        1) How to declare sufficient memory to find NEV 
    // c           eigenvalues of largest magnitude.  Other options
    // c           are available.
    // c
    // c        2) Illustration of the reverse communication interface 
    // c           needed to utilize the top level ARPACK routine DSAUPD 
    // c           that computes the quantities needed to construct
    // c           the desired eigenvalues and eigenvectors(if requested).
    // c
    // c        3) How to extract the desired eigenvalues and eigenvectors
    // c           using the ARPACK routine DSEUPD.
    // c
    // c     The only thing that must be supplied in order to use this
    // c     routine on your problem is to change the array dimensions 
    // c     appropriately, to specify WHICH eigenvalues you want to compute 
    // c     and to supply a matrix-vector product
    // c
    // c                         w <-  Av
    // c
    // c     in place of the call to AV( ) below.
    // c
    // c     Once usage of this routine is understood, you may wish to explore

    // c     the other available options to improve convergence, to solve gener
    // c     problems, etc.  Look at the file ex-sym.doc in DOCUMENTS directory
    // c     This codes implements  
    // c
    // c\Example-1
    // c     ... Suppose we want to solve A*x = lambda*x in regular mode,
    // c         where A is derived from the central difference discretization

    // c         of the 2-dimensional Laplacian on the unit square with
    // c         zero Dirichlet boundary condition.
    // c     ... OP = A  and  B = I.
    // c     ... Assume "call av (n,x,y)" computes y = A*x
    // c     ... Use mode 1 of DSAUPD.
    // c
    // c\BeginLib
    // c
    // c\Routines called:
    // c     dsaupd  ARPACK reverse communication interface routine.
    // c     dseupd  ARPACK routine that returns Ritz values and (optionally)
    // c             Ritz vectors.
    // c     dnrm2   Level 1 BLAS that computes the norm of a vector.
    // c     daxpy   Level 1 BLAS that computes y <- alpha*x+y.
    // c
    // c\Author
    // c     Richard Lehoucq
    // c     Danny Sorensen
    // c     Chao Yang
    // c     Dept. of Computational &
    // c     Applied Mathematics
    // c     Rice University
    // c     Houston, Texas
    // c
    // c\SCCS Information: @(#)
    // c FILE: ssimp.F   SID: 2.6   DATE OF SID: 10/17/00   RELEASE: 2
    // c
    // c\Remarks
    // c     1. None
    // c
    // c\EndLib
    // c
    // c-----------------------------------------------------------------------
    // c
    // c     %------------------------------------------------------%
    // c     | Storage Declarations:                                |
    // c     |                                                      |
    // c     | The maximum dimensions for all arrays are            |
    // c     | set here to accommodate a problem size of            |
    // c     | N .le. MAXN                                          |
    // c     |                                                      |
    // c     | NEV is the number of eigenvalues requested.          |
    // c     |     See specifications for ARPACK usage below.       |
    // c     |                                                      |
    // c     | NCV is the largest number of basis vectors that will |
    // c     |     be used in the Implicitly Restarted Arnoldi      |
    // c     |     Process.  Work per major iteration is            |
    // c     |     proportional to N*NCV*NCV.                       |
    // c     |                                                      |
    // c     | You must set:                                        |
    // c     |                                                      |
    // c     | MAXN:   Maximum dimension of the A allowed.          |
    // c     | MAXNEV: Maximum NEV allowed.                         |
    // c     | MAXNCV: Maximum NCV allowed.                         |
    // c     %------------------------------------------------------%
    // c
    // c
    // c     %--------------%
    // c     | Local Arrays |
    // c     %--------------%
    // c
    // c
    // c     %---------------%
    // c     | Local Scalars |
    // c     %---------------%
    // c
    // c
    // c     %------------%
    // c     | Parameters |
    // c     %------------%
    // c
    // c  
    // c     %-----------------------------%
    // c     | BLAS & LAPACK routines used |
    // c     %-----------------------------%
    // c
    // c
    // c     %--------------------%
    // c     | Intrinsic function |
    // c     %--------------------%
    // c
    // c
    // c     %-----------------------%
    // c     | Executable Statements |
    // c     %-----------------------%
    // c
    // c     %-------------------------------------------------%
    // c     | The following include statement and assignments |
    // c     | initiate trace output from the internal         |
    // c     | actions of ARPACK.  See debug.doc in the        |
    // c     | DOCUMENTS directory for usage.  Initially, the  |
    // c     | most useful information will be a breakdown of  |
    // c     | time spent in the various stages of computation |
    // c     | given by setting msaupd = 1.                    |
    // c     %-------------------------------------------------%
    // c
    // debug.h
    // c
    // c\SCCS Information: @(#) 
    // c FILE: debug.h   SID: 2.3   DATE OF SID: 11/16/95   RELEASE: 2 
    // c
    // c     %---------------------------------%
    // c     | See debug.doc for documentation |
    // c     %---------------------------------%

    @SuppressWarnings("unchecked")
    public static void main (String [] args)  {

        double [] v= new double[(256) * (25)];
        double [] workl= new double[((25*((25+8))))];
        double [] workd= new double[((3*256))];
        double [] d= new double[(25) * (2)];
        double [] resid= new double[(256)];
        double [] ax= new double[(256)];
        boolean [] select= new boolean[(25)];
        int [] iparam= new int[(11)];
        int [] ipntr= new int[(11)];
        String bmat= new String(" ");
        String which= new String("  ");
        intW ido= new intW(0);
        int n= 0;
        intW nev= new intW(0);
        int ncv= 0;
        int lworkl= 0;
        intW info= new intW(0);
        intW ierr= new intW(0);
        int j= 0;
        int nx= 0;
        int ishfts= 0;
        int maxitr= 0;
        int mode1= 0;
        int nconv= 0;
        boolean rvec= false;
        doubleW tol= new doubleW(0.0d);
        double sigma= 0.0d;
        java.util.Vector __io_vec = new java.util.Vector();
        org.netlib.arpack.arpack_debug.ndigit.val = -3;
        org.netlib.arpack.arpack_debug.logfil.val = 6;
        org.netlib.arpack.arpack_debug.msgets.val = 0;
        org.netlib.arpack.arpack_debug.msaitr.val = 0;
        org.netlib.arpack.arpack_debug.msapps.val = 0;
        org.netlib.arpack.arpack_debug.msaupd.val = 0;
        org.netlib.arpack.arpack_debug.msaup2.val = 0;
        org.netlib.arpack.arpack_debug.mseigt.val = 0;
        org.netlib.arpack.arpack_debug.mseupd.val = 0;
        // c     
        // c     %-------------------------------------------------%
        // c     | The following sets dimensions for this problem. |
        // c     %-------------------------------------------------%
        // c
        nx = 10;
        n = (nx*nx);
        // c
        // c     %-----------------------------------------------%
        // c     |                                               | 
        // c     | Specifications for ARPACK usage are set       | 
        // c     | below:                                        |
        // c     |                                               |
        // c     |    1) NEV = 4  asks for 4 eigenvalues to be   |  
        // c     |       computed.                               | 
        // c     |                                               |
        // c     |    2) NCV = 20 sets the length of the Arnoldi |
        // c     |       factorization                           |
        // c     |                                               |
        // c     |    3) This is a standard problem              |
        // c     |         (indicated by bmat  = 'I')            |
        // c     |                                               |
        // c     |    4) Ask for the NEV eigenvalues of          |
        // c     |       largest magnitude                       |
        // c     |         (indicated by which = 'LM')           |
        // c     |       See documentation in DSAUPD for the     |
        // c     |       other options SM, LA, SA, LI, SI.       | 
        // c     |                                               |
        // c     | Note: NEV and NCV must satisfy the following  |
        // c     | conditions:                                   |
        // c     |              NEV <= MAXNEV                    |
        // c     |          NEV + 1 <= NCV <= MAXNCV             |
        // c     %-----------------------------------------------%
        // c
        nev.val = 4;
        ncv = 20;
        bmat = "I";
        which = "LM";
        // c
        if ((n > 256))  {
            __io_vec.clear();
            __io_vec.addElement(new String(" ERROR with _SSIMP: N is greater than MAXN "));
            Util.f77write(null, __io_vec);
            // Dummy.go_to("Dssimp",9000);
        }
        else if ((nev.val > 10))  {
            __io_vec.clear();
            __io_vec.addElement(new String(" ERROR with _SSIMP: NEV is greater than MAXNEV "));
            Util.f77write(null, __io_vec);
            // Dummy.go_to("Dssimp",9000);
        }              // Close else if()
        else if ((ncv > 25))  {
            __io_vec.clear();
            __io_vec.addElement(new String(" ERROR with _SSIMP: NCV is greater than MAXNCV "));
            Util.f77write(null, __io_vec);
            // Dummy.go_to("Dssimp",9000);
        }              // Close else if()
        // c
        // c     %-----------------------------------------------------%
        // c     |                                                     |
        // c     | Specification of stopping rules and initial         |
        // c     | conditions before calling DSAUPD                    |
        // c     |                                                     |
        // c     | TOL  determines the stopping criterion.             |
        // c     |                                                     |
        // c     |      Expect                                         |
        // c     |           abs(lambdaC - lambdaT) < TOL*abs(lambdaC) |
        // c     |               computed   true                       |
        // c     |                                                     |
        // c     |      If TOL .le. 0,  then TOL <- macheps            |
        // c     |           (machine precision) is used.              |
        // c     |                                                     |
        // c     | IDO  is the REVERSE COMMUNICATION parameter         |
        // c     |      used to specify actions to be taken on return  |
        // c     |      from DSAUPD. (See usage below.)                |
        // c     |                                                     |
        // c     |      It MUST initially be set to 0 before the first |
        // c     |      call to DSAUPD.                                | 
        // c     |                                                     |
        // c     | INFO on entry specifies starting vector information |
        // c     |      and on return indicates error codes            |
        // c     |                                                     |
        // c     |      Initially, setting INFO=0 indicates that a     | 
        // c     |      random starting vector is requested to         |
        // c     |      start the ARNOLDI iteration.  Setting INFO to  |
        // c     |      a nonzero value on the initial call is used    |
        // c     |      if you want to specify your own starting       |
        // c     |      vector (This vector must be placed in RESID.)  | 
        // c     |                                                     |
        // c     | The work array WORKL is used in DSAUPD as           | 
        // c     | workspace.  Its dimension LWORKL is set as          |
        // c     | illustrated below.                                  |
        // c     |                                                     |
        // c     %-----------------------------------------------------%
        // c
        lworkl = (ncv*((ncv+8)));
        tol.val = 0.0;
        info.val = 0;
        ido.val = 0;
        // c
        // c     %---------------------------------------------------%
        // c     | Specification of Algorithm Mode:                  |
        // c     |                                                   |
        // c     | This program uses the exact shift strategy        |
        // c     | (indicated by setting PARAM(1) = 1).              |
        // c     | IPARAM(3) specifies the maximum number of Arnoldi |
        // c     | iterations allowed.  Mode 1 of DSAUPD is used     |
        // c     | (IPARAM(7) = 1). All these options can be changed |
        // c     | by the user. For details see the documentation in |
        // c     | DSAUPD.                                           |
        // c     %---------------------------------------------------%
        // c
        ishfts = 1;
        maxitr = 300;
        mode1 = 1;
        // c
        iparam[(1-(1))] = ishfts;
        // c                
        iparam[(3-(1))] = maxitr;
        // c                  
        iparam[(7-(1))] = mode1;
        // c
        // c     %------------------------------------------------%
        // c     | M A I N   L O O P (Reverse communication loop) |
        // c     %------------------------------------------------%
        // c
        while (true) {
            // c
            // c        %---------------------------------------------%
            // c        | Repeatedly call the routine DSAUPD and take | 
            // c        | actions indicated by parameter IDO until    |
            // c        | either convergence is indicated or maxitr   |
            // c        | has been exceeded.                          |
            // c        %---------------------------------------------%
            // c
            org.netlib.arpack.Dsaupd.dsaupd(ido,bmat,n,which,nev.val,tol,resid,0,ncv,v,0,256,iparam,0,ipntr,0,workd,0,workl,0,lworkl,info);
            // c
            if (((ido.val == -1) || (ido.val == 1)))  {
                // c
                // c           %--------------------------------------%
                // c           | Perform matrix vector multiplication |
                // c           |              y <--- OP*x             |
                // c           | The user should supply his/her own   |
                // c           | matrix vector multiplication routine |
                // c           | here that takes workd(ipntr(1)) as   |
                // c           | the input, and return the result to  |
                // c           | workd(ipntr(2)).                     |
                // c           %--------------------------------------%
                // c
                Av.av(nx,workd,(ipntr[(1-(1))]-(1)),workd,(ipntr[(2-(1))]-(1)));
                // c
                // c           %-----------------------------------------%
                // c           | L O O P   B A C K to call DSAUPD again. |
                // c           %-----------------------------------------%
                // c
                // c
            } else break;
        }
        // c
        // c     %----------------------------------------%
        // c     | Either we have convergence or there is |
        // c     | an error.                              |
        // c     %----------------------------------------%
        // c
        if ((info.val < 0))  {
            // c
            // c        %--------------------------%
            // c        | Error message. Check the |
            // c        | documentation in DSAUPD. |
            // c        %--------------------------%
            // c
            __io_vec.clear();
            __io_vec.addElement(new String(" "));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" Error with _saupd, info = "));
            __io_vec.addElement(new Integer(info.val));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" Check documentation in _saupd "));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" "));
            Util.f77write(null, __io_vec);
            // c
        }
        else  {
            // c
            // c        %-------------------------------------------%
            // c        | No fatal errors occurred.                 |
            // c        | Post-Process using DSEUPD.                |
            // c        |                                           |
            // c        | Computed eigenvalues may be extracted.    |  
            // c        |                                           |
            // c        | Eigenvectors may be also computed now if  |
            // c        | desired.  (indicated by rvec = .true.)    | 
            // c        |                                           |
            // c        | The routine DSEUPD now called to do this  |
            // c        | post processing (Other modes may require  |
            // c        | more complicated post processing than     |
            // c        | mode1.)                                   |
            // c        |                                           |
            // c        %-------------------------------------------%
            // c           
            rvec = true;
            // c
            org.netlib.arpack.Dseupd.dseupd(rvec,"All",select,0,d,0,v,0,256,sigma,bmat,n,which,nev,tol.val,resid,0,ncv,v,0,256,iparam,0,ipntr,0,workd,0,workl,0,lworkl,ierr);
            // c
            // c         %----------------------------------------------%
            // c         | Eigenvalues are returned in the first column |
            // c         | of the two dimensional array D and the       |
            // c         | corresponding eigenvectors are returned in   |
            // c         | the first NCONV (=IPARAM(5)) columns of the  |
            // c         | two dimensional array V if requested.        |
            // c         | Otherwise, an orthogonal basis for the       |
            // c         | invariant subspace corresponding to the      |
            // c         | eigenvalues in D is returned in V.           |
            // c         %----------------------------------------------%
            // c
            if ((ierr.val != 0))  {
                // c
                // c            %------------------------------------%
                // c            | Error condition:                   |
                // c            | Check the documentation of DSEUPD. |
                // c            %------------------------------------%
                // c
                __io_vec.clear();
                __io_vec.addElement(new String(" "));
                Util.f77write(null, __io_vec);
                __io_vec.clear();
                __io_vec.addElement(new String(" Error with _seupd, info = "));
                __io_vec.addElement(new Integer(ierr.val));
                Util.f77write(null, __io_vec);
                __io_vec.clear();
                __io_vec.addElement(new String(" Check the documentation of _seupd. "));
                Util.f77write(null, __io_vec);
                __io_vec.clear();
                __io_vec.addElement(new String(" "));
                Util.f77write(null, __io_vec);
                // c
            }
            else  {
                // c
                nconv = iparam[(5-(1))];
                {
                    for (j = 1; j <= nconv; j++) {
                        // c
                        // c               %---------------------------%
                        // c               | Compute the residual norm |
                        // c               |                           |
                        // c               |   ||  A*x - lambda*x ||   |
                        // c               |                           |
                        // c               | for the NCONV accurately  |
                        // c               | computed eigenvalues and  |
                        // c               | eigenvectors.  (iparam(5) |
                        // c               | indicates how many are    |
                        // c               | accurate to the requested |
                        // c               | tolerance)                |
                        // c               %---------------------------%
                        // c
                        Av.av(nx,v,(1-(1))+(j-(1)) * (256),ax,0);
                        org.netlib.blas.Daxpy.daxpy(n,(-(d[(j-(1))+(1-(1)) * (25)])),v,(1-(1))+(j-(1)) * (256),1,ax,0,1);
                        d[(j-(1))+(2-(1)) * (25)] = org.netlib.blas.Dnrm2.dnrm2(n,ax,0,1);
                        d[(j-(1))+(2-(1)) * (25)] = (d[(j-(1))+(2-(1)) * (25)]/Math.abs(d[(j-(1))+(1-(1)) * (25)]));
                        // c
                        // Dummy.label("Dssimp",20);
                    }              //  Close for() loop. 
                }
                // c
                // c            %-----------------------------%
                // c            | Display computed residuals. |
                // c            %-----------------------------%
                // c
                //org.netlib.arpack.Dmout.dmout(6,nconv,2,d,0,25,-6,"Ritz values and relative residuals");
            }              //  Close else.
            // c
            // c         %-------------------------------------------%
            // c         | Print additional convergence information. |
            // c         %-------------------------------------------%
            // c
            if ((info.val == 1))  {
                __io_vec.clear();
                __io_vec.addElement(new String(" "));
                Util.f77write(null, __io_vec);
                __io_vec.clear();
                __io_vec.addElement(new String(" Maximum number of iterations reached."));
                Util.f77write(null, __io_vec);
                __io_vec.clear();
                __io_vec.addElement(new String(" "));
                Util.f77write(null, __io_vec);
            }
            else if ((info.val == 3))  {
                __io_vec.clear();
                __io_vec.addElement(new String(" "));
                Util.f77write(null, __io_vec);
                __io_vec.clear();
                __io_vec.addElement(new String(" No shifts could be applied during implicit"));
                __io_vec.addElement(new String(" Arnoldi update, try increasing NCV."));
                Util.f77write(null, __io_vec);
                __io_vec.clear();
                __io_vec.addElement(new String(" "));
                Util.f77write(null, __io_vec);
            }              // Close else if()
            // c
            __io_vec.clear();
            __io_vec.addElement(new String(" "));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" _SSIMP "));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" ====== "));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" "));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" Size of the matrix is "));
            __io_vec.addElement(new Integer(n));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" The number of Ritz values requested is "));
            __io_vec.addElement(new Integer(nev.val));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" The number of Arnoldi vectors generated"));
            __io_vec.addElement(new String(" (NCV) is "));
            __io_vec.addElement(new Integer(ncv));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" What portion of the spectrum: "));
            __io_vec.addElement(new String(which));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" The number of converged Ritz values is "));
            __io_vec.addElement(new Integer(nconv));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" The number of Implicit Arnoldi update"));
            __io_vec.addElement(new String(" iterations taken is "));
            __io_vec.addElement(new Integer(iparam[(3-(1))]));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" The number of OP*x is "));
            __io_vec.addElement(new Integer(iparam[(9-(1))]));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" The convergence criterion is "));
            __io_vec.addElement(new Double(tol.val));
            Util.f77write(null, __io_vec);
            __io_vec.clear();
            __io_vec.addElement(new String(" "));
            Util.f77write(null, __io_vec);
            // c
        }              //  Close else.
        // c
        // c     %---------------------------%
        // c     | Done with program dssimp. |
        // c     %---------------------------%
        // c
        label9000:
            // Dummy.label("Dssimp",9000);
            // c
            // Dummy.label("Dssimp",999999);
            return;
    }
} // End class.
