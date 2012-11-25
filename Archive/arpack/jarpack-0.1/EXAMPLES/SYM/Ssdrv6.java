/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: ssdrv6.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Ssdrv6 {

// c
// c     Program to illustrate the idea of reverse communication
// c     in Cayley mode for a generalized symmetric eigenvalue 
// c     problem.  The following program uses the two LAPACK subroutines 
// c     sgttrf.f and sgttrs.f to factor and solve a tridiagonal system of 
// c     equations.
// c
// c     We implement example six of ex-sym.doc in DOCUMENTS directory
// c
// c\Example-6
// c     ... Suppose we want to solve A*x = lambda*M*x in inverse mode,
// c         where A and M are obtained by the finite element of the
// c         1-dimensional discrete Laplacian
// c                             d^2u / dx^2
// c         on the interval [0,1] with zero Dirichlet boundary condition
// c         using piecewise linear elements.
// c
// c     ... OP = (inv[A-sigma*M])*(A+sigma*M)  and  B = M.
// c
// c     ... Use mode 5 of SSAUPD.
// c
// c\BeginLib
// c
// c\References:
// c  1. R.G. Grimes, J.G. Lewis and H.D. Simon, "A Shifted Block Lanczos 

// c     Algorithm for Solving Sparse Symmetric Generalized Eigenproblems",
// c     SIAM J. Matr. Anal. Apps.,  January (1993).
// c
// c\Routines called:
// c     ssaupd  ARPACK reverse communication interface routine.
// c     sseupd  ARPACK routine that returns Ritz values and (optionally)
// c             Ritz vectors.
// c     sgttrf  LAPACK tridiagonal factorization routine.
// c     sgttrs  LAPACK tridiagonal solve routine.
// c     saxpy   Level 1 BLAS that computes y <- alpha*x+y.
// c     scopy   Level 1 BLAS that copies one vector to another.
// c     sscal   Level 1 BLAS that scales a vector by a scalar.
// c     snrm2   Level 1 BLAS that computes the norm of a vector. 
// c     av      Matrix vector multiplication routine that computes A*x.
// c     mv      Matrix vector multiplication routine that computes M*x.
// c
// c\Author
// c     Danny Sorensen
// c     Richard Lehoucq
// c     Chao Yang
// c     Dept. of Computational &
// c     Applied Mathematics
// c     Rice University 
// c     Houston, Texas 
// c 
// c\SCCS Information: @(#)
// c FILE: sdrv6.F   SID: 2.5   DATE OF SID: 10/17/00   RELEASE: 2
// c
// c\Remarks
// c     1. None
// c
// c\EndLib
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
// c     character        bmat*1, which*2
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
// c     | Executable statements |
// c     %-----------------------%
// c
// c     %--------------------------------------------------%
// c     | The number N is the dimension of the matrix. A   |
// c     | generalized eigenvalue problem is solved (BMAT = |
// c     | 'G'.) NEV is the number of eigenvalues to be     |
// c     | approximated.  Since the Cayley mode is used,    |
// c     | WHICH is set to 'LM'.  The user can modify NEV,  |
// c     | NCV, SIGMA to solve problems of different sizes, |
// c     | and to get different parts of the spectrum.      |
// c     | However, The following conditions must be        |
// c     | satisfied:                                       |
// c     |                 N <= MAXN,                       | 
// c     |               NEV <= MAXNEV,                     |
// c     |           NEV + 1 <= NCV <= MAXNCV               | 
// c     %--------------------------------------------------%
// c

public static void main (String [] args)  {

float [] v= new float[(256) * (25)];
float [] workl= new float[((25*((25+8))))];
float [] workd= new float[((3*256))];
float [] d= new float[(25) * (2)];
float [] resid= new float[(256)];
float [] ad= new float[(256)];
float [] adl= new float[(256)];
float [] adu= new float[(256)];
float [] adu2= new float[(256)];
float [] temp= new float[(256)];
float [] ax= new float[(256)];
float [] mx= new float[(256)];
boolean [] select= new boolean[(25)];
int [] iparam= new int[(11)];
int [] ipntr= new int[(11)];
int [] ipiv= new int[(256)];
String bmat= new String(" ");
String which= new String("  ");
intW ido= new intW(0);
int n= 0;
intW nev= new intW(0);
int ncv= 0;
int lworkl= 0;
intW info= new intW(0);
int j= 0;
intW ierr= new intW(0);
int nconv= 0;
int maxitr= 0;
int ishfts= 0;
int mode= 0;
boolean rvec= false;
float sigma= 0.0f;
float r1= 0.0f;
float r2= 0.0f;
floatW tol= new floatW(0.0f);
float h= 0.0f;
  java.util.Vector __io_vec = new java.util.Vector();
n = 100;
nev.val = 4;
ncv = 20;
if ((n > 256))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SDRV6: N is greater than MAXN "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv6",9000);
}
else if ((nev.val > 10))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SDRV6: NEV is greater than MAXNEV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv6",9000);
}              // Close else if()
else if ((ncv > 25))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SDRV6: NCV is greater than MAXNCV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv6",9000);
}              // Close else if()
bmat = "G";
which = "LM";
sigma = 1.5E+2f;
// c
// c     %--------------------------------------------------%
// c     | The work array WORKL is used in SSAUPD as        |
// c     | workspace.  Its dimension LWORKL is set as       |
// c     | illustrated below.  The parameter TOL determines |
// c     | the stopping criterion.  If TOL<=0, machine      |
// c     | precision is used.  The variable IDO is used for |
// c     | reverse communication and is initially set to 0. |
// c     | Setting INFO=0 indicates that a random vector is |
// c     | generated in SSAUPD to start the Arnoldi         |
// c     | iteration.                                       |
// c     %--------------------------------------------------%
// c
lworkl = (ncv*((ncv+8)));
tol.val = 0.0f;
ido.val = 0;
info.val = 0;
// c
// c     %---------------------------------------------------%
// c     | This program uses exact shifts with respect to    |
// c     | the current Hessenberg matrix (IPARAM(1) = 1).    |
// c     | IPARAM(3) specifies the maximum number of Arnoldi |
// c     | iterations allowed.  Mode 5 specified in the      |
// c     | documentation of SSAUPD is used (IPARAM(7) = 5).  |
// c     | All these options may be changed by the user. For |
// c     | details, see the documentation in SSAUPD.         |
// c     %---------------------------------------------------%
// c
ishfts = 1;
maxitr = 300;
mode = 5;
// c
iparam[(1-(1))] = ishfts;
iparam[(3-(1))] = maxitr;
iparam[(7-(1))] = mode;
// c
// c     %------------------------------------------------------%
// c     | Call LAPACK routine to factor (A-sigma*M).  The      |
// c     | stiffness matrix A is the 1-d discrete Laplacian.    |
// c     | The mass matrix M is the associated mass matrix      |
// c     | arising from using piecewise linear finite elements  |
// c     | on the interval [0, 1].                              |
// c     %------------------------------------------------------%
// c
h = (1.0f/(float)((n+1)));
r1 = (((4.0f/6.0f))*h);
r2 = (((1.0f/6.0f))*h);
{
for (j = 1; j <= n; j++) {
ad[(j-(1))] = ((2.0f/h)-(sigma*r1));
adl[(j-(1))] = ((-((1.0f/h)))-(sigma*r2));
Dummy.label("Ssdrv6",20);
}              //  Close for() loop. 
}
org.netlib.blas.Scopy.scopy(n,adl,0,1,adu,0,1);
org.netlib.lapack.Sgttrf.sgttrf(n,adl,0,ad,0,adu,0,adu2,0,ipiv,0,ierr);
if ((ierr.val != 0))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _gttrf in _SDRV6."));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv6",9000);
}
// c
// c     %-------------------------------------------%
// c     | M A I N   L O O P (Reverse communication) |
// c     %-------------------------------------------%
// c
label10:
   Dummy.label("Ssdrv6",10);
// c
// c        %---------------------------------------------%
// c        | Repeatedly call the routine SSAUPD and take |
// c        | actions indicated by parameter IDO until    |
// c        | either convergence is indicated or maxitr   |
// c        | has been exceeded.                          |
// c        %---------------------------------------------%
// c
org.netlib.arpack.Ssaupd.ssaupd(ido,bmat,n,which,nev.val,tol,resid,0,ncv,v,0,256,iparam,0,ipntr,0,workd,0,workl,0,lworkl,info);
// c
if ((ido.val == -1))  {
    // c
// c           %-------------------------------------------------------%
// c           | Perform  y <--- OP*x = (inv[A-SIGMA*M])*(A+SIGMA*M)*x |
// c           | to force starting vector into the range of OP.  The   |
// c           | user should provide his/her matrix vector (A*x, M*x)  |
// c           | multiplication routines and a linear system solver    |
// c           | here.  The matrix vector multiplication routine takes |
// c           | workd(ipntr(1)) as the input vector.  The final       |
// c           | result is returned to workd(ipntr(2)).                | 
// c           %-------------------------------------------------------%
// c
Av.av(n,workd,(ipntr[(1-(1))]-(1)),workd,(ipntr[(2-(1))]-(1)));
Mv.mv(n,workd,(ipntr[(1-(1))]-(1)),temp,0);
org.netlib.blas.Saxpy.saxpy(n,sigma,temp,0,1,workd,(ipntr[(2-(1))]-(1)),1);
// c
org.netlib.lapack.Sgttrs.sgttrs("Notranspose",n,1,adl,0,ad,0,adu,0,adu2,0,ipiv,0,workd,(ipntr[(2-(1))]-(1)),n,ierr);
if ((ierr.val != 0))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _gttrs in _SDRV6."));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv6",9000);
}
// c
// c           %-----------------------------------------%
// c           | L O O P   B A C K to call SSAUPD again. |
// c           %-----------------------------------------%
// c
Dummy.go_to("Ssdrv6",10);
// c
}
else if ((ido.val == 1))  {
    // c
// c           %----------------------------------------------------%
// c           | Perform y <-- OP*x = inv[A-SIGMA*M]*(A+SIGMA*M)*x. |
// c           | M*x has been saved in workd(ipntr(3)).  The user   |
// c           | need the matrix vector multiplication (A*x)        |
// c           | routine and a linear system solver here.  The      |
// c           | matrix vector multiplication routine takes         |
// c           | workd(ipntr(1)) as the input, and the result is    |
// c           | combined with workd(ipntr(3)) to form the input    |
// c           | for the linear system solver.  The final result is |
// c           | returned to workd(ipntr(2)).                       | 
// c           %----------------------------------------------------%
// c
Av.av(n,workd,(ipntr[(1-(1))]-(1)),workd,(ipntr[(2-(1))]-(1)));
org.netlib.blas.Saxpy.saxpy(n,sigma,workd,(ipntr[(3-(1))]-(1)),1,workd,(ipntr[(2-(1))]-(1)),1);
org.netlib.lapack.Sgttrs.sgttrs("Notranspose",n,1,adl,0,ad,0,adu,0,adu2,0,ipiv,0,workd,(ipntr[(2-(1))]-(1)),n,ierr);
if ((ierr.val != 0))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _gttrs in _SDRV6. "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv6",9000);
}
// c
// c           %-----------------------------------------%
// c           | L O O P   B A C K to call SSAUPD again. |
// c           %-----------------------------------------%
// c
Dummy.go_to("Ssdrv6",10);
// c
}              // Close else if()
else if ((ido.val == 2))  {
    // c
// c           %--------------------------------------------%
// c           |             Perform  y <--- M*x.           |
// c           | Need matrix vector multiplication routine  |
// c           | here that takes workd(ipntr(1)) as input   |
// c           | and returns the result to workd(ipntr(2)). |
// c           %--------------------------------------------%
// c
Mv.mv(n,workd,(ipntr[(1-(1))]-(1)),workd,(ipntr[(2-(1))]-(1)));
// c
// c           %-----------------------------------------%
// c           | L O O P   B A C K to call SSAUPD again. |
// c           %-----------------------------------------%
// c
Dummy.go_to("Ssdrv6",10);
// c
}              // Close else if()
// c
// c     %-----------------------------------------%
// c     | Either we have convergence, or there is |
// c     | an error.                               |
// c     %-----------------------------------------%
// c
if ((info.val < 0))  {
    // c
// c        %--------------------------%
// c        | Error message, check the |
// c        | documentation in SSAUPD  |
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
  __io_vec.addElement(new String(" Check the documentation of _saupd. "));
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
// c        | Post-Process using SSEUPD.                |
// c        |                                           |
// c        | Computed eigenvalues may be extracted.    |
// c        |                                           |
// c        | Eigenvectors may also be computed now if  |
// c        | desired.  (indicated by rvec = .true.)    |
// c        %-------------------------------------------%
// c
rvec = true;
// c
org.netlib.arpack.Sseupd.sseupd(rvec,"All",select,0,d,0,v,0,256,sigma,bmat,n,which,nev,tol.val,resid,0,ncv,v,0,256,iparam,0,ipntr,0,workd,0,workl,0,lworkl,ierr);
// c
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
// c           %------------------------------------%
// c           | Error condition:                   |
// c           | Check the documentation of SSEUPD. |
// c           %------------------------------------%
// c
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _seupd, info = "));
  __io_vec.addElement(new Integer(ierr.val));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Check the documentation of _seupd "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
// c
}
else  {
  // c
// c           %---------------------------%
// c           | Compute the residual norm |
// c           |                           |
// c           |   ||  A*x - lambda*x ||   |
// c           |                           |
// c           | for the NCONV accurately  |
// c           | computed eigenvalues and  |
// c           | eigenvectors.  (iparam(5) |
// c           | indicates how many are    |
// c           | accurate to the requested |
// c           | tolerance)                |
// c           %---------------------------%
// c  
nconv = iparam[(5-(1))];
{
for (j = 1; j <= nconv; j++) {
Av.av(n,v,(1-(1))+(j-(1)) * (256),ax,0);
Mv.mv(n,v,(1-(1))+(j-(1)) * (256),mx,0);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(1-(1)) * (25)])),mx,0,1,ax,0,1);
d[(j-(1))+(2-(1)) * (25)] = org.netlib.blas.Snrm2.snrm2(n,ax,0,1);
d[(j-(1))+(2-(1)) * (25)] = (d[(j-(1))+(2-(1)) * (25)]/Math.abs(d[(j-(1))+(1-(1)) * (25)]));
Dummy.label("Ssdrv6",30);
}              //  Close for() loop. 
}
// c
org.netlib.arpack.Smout.smout(6,nconv,2,d,0,25,-6,"Ritz values and relative residuals");
// c
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
  __io_vec.addElement(new String(" _SDRV6 "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" ====== "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Size of the matrix is"));
  __io_vec.addElement(new Integer(n));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" The number of Ritz values requested is"));
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
  __io_vec.addElement(new String(" iterations taken is"));
  __io_vec.addElement(new Integer(iparam[(3-(1))]));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" The number of OP*x is "));
  __io_vec.addElement(new Integer(iparam[(9-(1))]));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" The convergence criterion is "));
  __io_vec.addElement(new Float(tol.val));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
// c
}              //  Close else.
// c
// c     %---------------------------%
// c     | Done with program ssdrv6. |
// c     %---------------------------%
// c
label9000:
   Dummy.label("Ssdrv6",9000);
// c
Dummy.label("Ssdrv6",999999);
return;
   }
} // End class.
