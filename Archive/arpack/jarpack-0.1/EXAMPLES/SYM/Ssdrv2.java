/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: ssdrv2.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Ssdrv2 {

// c
// c     Program to illustrate the idea of reverse communication
// c     in shift and invert mode for a standard symmetric eigenvalue
// c     problem.  The following program uses the two LAPACK subroutines 
// c     sgttrf.f and sgttrs.f to factor and solve a tridiagonal system of 
// c     equations.
// c
// c     We implement example two of ex-sym.doc in DOCUMENTS directory
// c
// c\Example-2
// c     ... Suppose we want to solve A*x = lambda*x in shift-invert mode,

// c         where A is derived from the central difference discretization

// c         of the 1-dimensional Laplacian on [0,1]  with zero Dirichlet
// c         boundary condition.
// c     ... OP = (inv[A - sigma*I]) and  B = I.
// c     ... Use mode 3 of SSAUPD.
// c
// c\BeginLib
// c
// c\Routines called:
// c     ssaupd  ARPACK reverse communication interface routine.
// c     sseupd  ARPACK routine that returns Ritz values and (optionally)
// c             Ritz vectors.
// c     sgttrf  LAPACK tridiagonal factorization routine.
// c     sgttrs  LAPACK tridiagonal solve routine.
// c     saxpy   saxpy   Level 1 BLAS that computes y <- alpha*x+y.
// c     snrm2   Level 1 BLAS that computes the norm of a vector.     
// c     av      Matrix vector multiplication routine that computes A*x.
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
// c FILE: sdrv2.F   SID: 2.5   DATE OF SID: 10/17/00   RELEASE: 2
// c
// c\Remarks
// c     1. None
// c
// c\EndLib
// c----------------------------------------------------------------------

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
// c     | Executable Statements |
// c     %-----------------------%
// c
// c     %----------------------------------------------------%
// c     | The number N is the dimension of the matrix.  A    |
// c     | standard eigenvalue problem is solved (BMAT = 'I'. |
// c     | NEV is the number of eigenvalues (closest to       |
// c     | SIGMA) to be approximated.  Since the shift-invert |
// c     | mode is used, WHICH is set to 'LM'.  The user can  |
// c     | modify NEV, NCV, SIGMA to solve problems of        | 
// c     | different sizes, and to get different parts of the |
// c     | spectrum.  However, The following conditions must  |
// c     | be satisfied:                                      |
// c     |                   N <= MAXN,                       | 
// c     |                 NEV <= MAXNEV,                     |
// c     |             NEV + 1 <= NCV <= MAXNCV               | 
// c     %----------------------------------------------------% 
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
float [] ax= new float[(256)];
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
floatW tol= new floatW(0.0f);
float h2= 0.0f;
  java.util.Vector __io_vec = new java.util.Vector();
n = 100;
nev.val = 4;
ncv = 10;
if ((n > 256))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SDRV2: N is greater than MAXN "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv2",9000);
}
else if ((nev.val > 10))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SDRV2: NEV is greater than MAXNEV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv2",9000);
}              // Close else if()
else if ((ncv > 25))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SDRV2: NCV is greater than MAXNCV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv2",9000);
}              // Close else if()
// c
bmat = "I";
which = "LM";
sigma = 0.0f;
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
// c     | iterations allowed.  Mode 3 of SSAUPD is used     |
// c     | (IPARAM(7) = 3).  All these options may be        |
// c     | changed by the user. For details, see the         |
// c     | documentation in SSAUPD.                          |
// c     %---------------------------------------------------%
// c
ishfts = 1;
maxitr = 300;
mode = 3;
// c
iparam[(1-(1))] = ishfts;
iparam[(3-(1))] = maxitr;
iparam[(7-(1))] = mode;
// c
// c     %-----------------------------------------------------%
// c     | Call LAPACK routine to factor (A-SIGMA*I), where A  |
// c     | is the 1-d Laplacian.                               | 
// c     %-----------------------------------------------------%
// c
h2 = (1.0f/(float)((((n+1))*((n+1)))));
{
for (j = 1; j <= n; j++) {
ad[(j-(1))] = ((2.0f/h2)-sigma);
adl[(j-(1))] = (-((1.0f/h2)));
Dummy.label("Ssdrv2",20);
}              //  Close for() loop. 
}
org.netlib.blas.Scopy.scopy(n,adl,0,1,adu,0,1);
org.netlib.lapack.Sgttrf.sgttrf(n,adl,0,ad,0,adu,0,adu2,0,ipiv,0,ierr);
if ((ierr.val != 0))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _gttrf in SDRV2."));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv2",9000);
}
// c
// c     %-------------------------------------------%
// c     | M A I N   L O O P (Reverse communication) |
// c     %-------------------------------------------%
// c
label10:
   Dummy.label("Ssdrv2",10);
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
if (((ido.val == -1) || (ido.val == 1)))  {
    // c
// c           %----------------------------------------%
// c           | Perform y <-- OP*x = inv[A-sigma*I]*x. |
// c           | The user only need the linear system   |
// c           | solver here that takes workd(ipntr(1)) |
// c           | as input, and returns the result to    |
// c           | workd(ipntr(2)).                       |
// c           %----------------------------------------%
// c
org.netlib.blas.Scopy.scopy(n,workd,(ipntr[(1-(1))]-(1)),1,workd,(ipntr[(2-(1))]-(1)),1);
// c
org.netlib.lapack.Sgttrs.sgttrs("Notranspose",n,1,adl,0,ad,0,adu,0,adu2,0,ipiv,0,workd,(ipntr[(2-(1))]-(1)),n,ierr);
if ((ierr.val != 0))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _gttrs in _SDRV2. "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssdrv2",9000);
}
// c
// c           %-----------------------------------------%
// c           | L O O P   B A C K to call SSAUPD again. |
// c           %-----------------------------------------%
// c
Dummy.go_to("Ssdrv2",10);
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
// c        %----------------------------%
// c        | Error message.  Check the  |
// c        | documentation in SSAUPD    |
// c        %----------------------------%
// c
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _saupd, info = "));
  __io_vec.addElement(new Integer(info.val));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Check documentation of _saupd "));
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
// 
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
nconv = iparam[(5-(1))];
{
for (j = 1; j <= nconv; j++) {
// c
// c              %---------------------------%
// c              | Compute the residual norm |
// c              |                           |
// c              |   ||  A*x - lambda*x ||   |
// c              |                           |
// c              | for the NCONV accurately  |
// c              | computed eigenvalues and  |
// c              | eigenvectors.  (iparam(5) |
// c              | indicates how many are    |
// c              | accurate to the requested |
// c              | tolerance)                |
// c              %---------------------------%
// c
Av.av(n,v,(1-(1))+(j-(1)) * (256),ax,0);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(1-(1)) * (25)])),v,(1-(1))+(j-(1)) * (256),1,ax,0,1);
d[(j-(1))+(2-(1)) * (25)] = org.netlib.blas.Snrm2.snrm2(n,ax,0,1);
d[(j-(1))+(2-(1)) * (25)] = (d[(j-(1))+(2-(1)) * (25)]/Math.abs(d[(j-(1))+(1-(1)) * (25)]));
// c
Dummy.label("Ssdrv2",30);
}              //  Close for() loop. 
}
// c
// c           %-------------------------------%
// c           | Display computed residuals    |
// c           %-------------------------------%
// c
org.netlib.arpack.Smout.smout(6,nconv,2,d,0,25,-6,"Ritz values and relative residuals");
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
  __io_vec.addElement(new String(" _SDRV2 "));
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
  __io_vec.addElement(new Float(tol.val));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
// c
}              //  Close else.
// c
// c     %---------------------------%
// c     | Done with program ssdrv2. |
// c     %---------------------------%
// c
label9000:
   Dummy.label("Ssdrv2",9000);
// c
Dummy.label("Ssdrv2",999999);
return;
   }
} // End class.
