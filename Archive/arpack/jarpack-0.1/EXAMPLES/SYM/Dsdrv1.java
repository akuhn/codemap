/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: dsdrv1.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Dsdrv1 {

// c
// c     Simple program to illustrate the idea of reverse communication
// c     in regular mode for a standard symmetric eigenvalue problem.
// c
// c     We implement example one of ex-sym.doc in SRC directory
// c
// c\Example-1
// c     ... Suppose we want to solve A*x = lambda*x in regular mode,
// c         where A is derived from the central difference discretization

// c         of the 2-dimensional Laplacian on the unit square [0,1]x[0,1]

// c         with zero Dirichlet boundary condition.
// c
// c     ... OP = A  and  B = I.
// c
// c     ... Assume "call av (n,x,y)" computes y = A*x.
// c
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
// c     av      Matrix vector multiplication routine that computes A*x.
// c     tv      Matrix vector multiplication routine that computes T*x, 
// c             where T is a tridiagonal matrix.  It is used in routine
// c             av.
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
// c FILE: sdrv1.F   SID: 2.5   DATE OF SID: 10/17/00   RELEASE: 2
// c
// c\Remarks
// c     1. None
// c
// c\EndLib
// c
// c-----------------------------------------------------------------------
// c
// c     %-----------------------------%
// c     | Define leading dimensions   |
// c     | for all arrays.             |
// c     | MAXN:   Maximum dimension   |
// c     |         of the A allowed.   |
// c     | MAXNEV: Maximum NEV allowed |
// c     | MAXNCV: Maximum NCV allowed |
// c     %-----------------------------%
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
// c     %----------------------------------------------------%
// c     | The number NX is the number of interior points     |
// c     | in the discretization of the 2-dimensional         |
// c     | Laplacian on the unit square with zero Dirichlet   |
// c     | boundary condition.  The number N(=NX*NX) is the   |
// c     | dimension of the matrix.  A standard eigenvalue    |
// c     | problem is solved (BMAT = 'I'). NEV is the number  |
// c     | of eigenvalues to be approximated.  The user can   |
// c     | modify NEV, NCV, WHICH to solve problems of        |
// c     | different sizes, and to get different parts of the |
// c     | spectrum.  However, The following conditions must  |
// c     | be satisfied:                                      |
// c     |                   N <= MAXN,                       | 
// c     |                 NEV <= MAXNEV,                     |
// c     |             NEV + 1 <= NCV <= MAXNCV               | 
// c     %----------------------------------------------------% 
// c

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
int nconv= 0;
int maxitr= 0;
int mode= 0;
int ishfts= 0;
boolean rvec= false;
doubleW tol= new doubleW(0.0d);
double sigma= 0.0d;
  java.util.Vector __io_vec = new java.util.Vector();
nx = 10;
n = (nx*nx);
nev.val = 4;
ncv = 10;
if ((n > 256))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SDRV1: N is greater than MAXN "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsdrv1",9000);
}
else if ((nev.val > 10))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SDRV1: NEV is greater than MAXNEV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsdrv1",9000);
}              // Close else if()
else if ((ncv > 25))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SDRV1: NCV is greater than MAXNCV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsdrv1",9000);
}              // Close else if()
bmat = "I";
which = "SM";
// c
// c     %--------------------------------------------------%
// c     | The work array WORKL is used in DSAUPD as        |
// c     | workspace.  Its dimension LWORKL is set as       |
// c     | illustrated below.  The parameter TOL determines |
// c     | the stopping criterion.  If TOL<=0, machine      |
// c     | precision is used.  The variable IDO is used for |
// c     | reverse communication and is initially set to 0. |
// c     | Setting INFO=0 indicates that a random vector is |
// c     | generated in DSAUPD to start the Arnoldi         |
// c     | iteration.                                       |
// c     %--------------------------------------------------%
// c
lworkl = (ncv*((ncv+8)));
tol.val = 0.0;
info.val = 0;
ido.val = 0;
// c
// c     %---------------------------------------------------%
// c     | This program uses exact shifts with respect to    |
// c     | the current Hessenberg matrix (IPARAM(1) = 1).    |
// c     | IPARAM(3) specifies the maximum number of Arnoldi |
// c     | iterations allowed.  Mode 1 of DSAUPD is used     |
// c     | (IPARAM(7) = 1).  All these options may be        |
// c     | changed by the user. For details, see the         |
// c     | documentation in DSAUPD.                          |
// c     %---------------------------------------------------%
// c
ishfts = 1;
maxitr = 300;
mode = 1;
// c      
iparam[(1-(1))] = ishfts;
iparam[(3-(1))] = maxitr;
iparam[(7-(1))] = mode;
// c
// c     %-------------------------------------------%
// c     | M A I N   L O O P (Reverse communication) |
// c     %-------------------------------------------%
// c
label10:
   Dummy.label("Dsdrv1",10);
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
Dummy.go_to("Dsdrv1",10);
// c
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
// c        | Eigenvectors may also be computed now if  |
// c        | desired.  (indicated by rvec = .true.)    | 
// c        %-------------------------------------------%
// c           
rvec = true;
// c
org.netlib.arpack.Dseupd.dseupd(rvec,"All",select,0,d,0,v,0,256,sigma,bmat,n,which,nev,tol.val,resid,0,ncv,v,0,256,iparam,0,ipntr,0,workd,0,workl,0,lworkl,ierr);
// 
// c        %----------------------------------------------%
// c        | Eigenvalues are returned in the first column |
// c        | of the two dimensional array D and the       |
// c        | corresponding eigenvectors are returned in   |
// c        | the first NEV columns of the two dimensional |
// c        | array V if requested.  Otherwise, an         |
// c        | orthogonal basis for the invariant subspace  |
// c        | corresponding to the eigenvalues in D is     |
// c        | returned in V.                               |
// c        %----------------------------------------------%
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
Dummy.label("Dsdrv1",20);
}              //  Close for() loop. 
}
// c
// c            %-------------------------------%
// c            | Display computed residuals    |
// c            %-------------------------------%
// c
org.netlib.arpack.Dmout.dmout(6,nconv,2,d,0,25,-6,"Ritz values and relative residuals");
}              //  Close else.
// c
// c        %------------------------------------------%
// c        | Print additional convergence information |
// c        %------------------------------------------%
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
  __io_vec.addElement(new String(" _SDRV1 "));
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
// c     | Done with program dsdrv1. |
// c     %---------------------------%
// c
label9000:
   Dummy.label("Dsdrv1",9000);
// c
Dummy.label("Dsdrv1",999999);
return;
   }
} // End class.
