/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: sndrv2.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Sndrv2 {

// c
// c     Simple program to illustrate the idea of reverse communication
// c     in shift-invert mode for a standard nonsymmetric eigenvalue proble
// c
// c     We implement example two of ex-nonsym.doc in DOCUMENTS directory
// c
// c\Example-2
// c     ... Suppose we want to solve A*x = lambda*x in shift-invert mode,

// c         where A is derived from the centered difference discretization
// c         of the 1-dimensional convection-diffusion operator
// c                          (d^2u / dx^2) + rho*(du/dx) 
// c         on the interval [0,1] with zero Dirichlet boundary condition.

// c
// c     ... The shift sigma is a real number.
// c
// c     ... OP = inv[A-sigma*I] and  B = I.
// c
// c     ... Use mode 3 of SNAUPD.
// c
// c\BeginLib
// c
// c\Routines called:
// c     snaupd  ARPACK reverse communication interface routine.
// c     sneupd  ARPACK routine that returns Ritz values and (optionally)
// c             Ritz vectors.
// c     sgttrf  LAPACK tridiagonal factorization routine.
// c     sgttrs  LAPACK tridiagonal solve routine.
// c     slapy2  LAPACK routine to compute sqrt(x**2+y**2) carefully.
// c     saxpy   Level 1 BLAS that computes y <- alpha*x+y.
// c     scopy   Level 1 BLAS that copies one vector to another.
// c     sdot    Level 1 BLAS that computes the dot product of two vectors.
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
// c FILE: ndrv2.F   SID: 2.5   DATE OF SID: 10/17/00   RELEASE: 2
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
// c     | The number N is the dimension of the matrix.  A  |
// c     | standard eigenvalue problem is solved (BMAT =    |
// c     | 'I').  NEV is the number of eigenvalues (closest |
// c     | to the shift SIGMAR) to be approximated.  Since  |
// c     | the shift-invert mode is used, WHICH is set to   |
// c     | 'LM'.  The user can modify NEV, NCV, SIGMAR to   |
// c     | solve problems of different sizes, and to get    |
// c     | different parts of the spectrum.  However, The   |
// c     | following conditions must be satisfied:          |
// c     |                 N <= MAXN,                       | 
// c     |               NEV <= MAXNEV,                     |
// c     |           NEV + 2 <= NCV <= MAXNCV               | 
// c     %--------------------------------------------------%
// c

public static void main (String [] args)  {

int [] iparam= new int[(11)];
int [] ipntr= new int[(14)];
int [] ipiv= new int[(256)];
boolean [] select= new boolean[(25)];
float [] ax= new float[(256)];
float [] d= new float[(25) * (3)];
float [] resid= new float[(256)];
float [] v= new float[(256) * (25)];
float [] workd= new float[((3*256))];
float [] workev= new float[((3*25))];
float [] workl= new float[((((3*25)*25)+(6*25)))];
float [] dd= new float[(256)];
float [] dl= new float[(256)];
float [] du= new float[(256)];
float [] du2= new float[(256)];
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
floatW tol= new floatW(0.0f);
float h= 0.0f;
float s= 0.0f;
float sigmar= 0.0f;
float sigmai= 0.0f;
float s1= 0.0f;
float s2= 0.0f;
float s3= 0.0f;
boolean first= false;
boolean rvec= false;
  java.util.Vector __io_vec = new java.util.Vector();
n = 100;
nev.val = 4;
ncv = 20;
if ((n > 256))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _NDRV2: N is greater than MAXN "));
Util.f77write(null, __io_vec);
Dummy.go_to("Sndrv2",9000);
}
else if ((nev.val > 10))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _NDRV2: NEV is greater than MAXNEV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Sndrv2",9000);
}              // Close else if()
else if ((ncv > 25))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _NDRV2: NCV is greater than MAXNCV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Sndrv2",9000);
}              // Close else if()
bmat = "I";
which = "LM";
sigmar = 1.0E+0f;
sigmai = 0.0E+0f;
// c
// c     %----------------------------------------------------%
// c     | Construct C = A - SIGMA*I in real arithmetic, and  |
// c     | factor C in real arithmetic using LAPACK           |
// c     | subroutine sgttrf. The matrix A is chosen to be    |
// c     | the tridiagonal matrix derived from standard       |
// c     | central difference of the 1-d convection diffusion |
// c     | operator u" + rho*u' on the interval [0, 1] with   | 
// c     | zero Dirichlet boundary condition.                 |
// c     %----------------------------------------------------%
// c
sndrv2_convct.rho.val = 1.0E+1f;
h = (1.0f/(float)((n+1)));
s = ((sndrv2_convct.rho.val*h)/2.0f);
// c
s1 = (-1.0f-s);
s2 = (2.0f-sigmar);
s3 = (-1.0f+s);
// c
{
for (j = 1; j <= (n-1); j++) {
dl[(j-(1))] = s1;
dd[(j-(1))] = s2;
du[(j-(1))] = s3;
Dummy.label("Sndrv2",10);
}              //  Close for() loop. 
}
dd[(n-(1))] = s2;
// c 
org.netlib.lapack.Sgttrf.sgttrf(n,dl,0,dd,0,du,0,du2,0,ipiv,0,ierr);
if ((ierr.val != 0))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _gttrf in _NDRV2."));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
Dummy.go_to("Sndrv2",9000);
}
// c        
// c     %-----------------------------------------------------%
// c     | The work array WORKL is used in SNAUPD as           |
// c     | workspace.  Its dimension LWORKL is set as          |
// c     | illustrated below.  The parameter TOL determines    |
// c     | the stopping criterion. If TOL<=0, machine          |
// c     | precision is used.  The variable IDO is used for    |
// c     | reverse communication, and is initially set to 0.   |
// c     | Setting INFO=0 indicates that a random vector is    |
// c     | generated in SNAUPD to start the Arnoldi iteration. |
// c     %-----------------------------------------------------%
// c
lworkl = ((3*((int)Math.pow(ncv, 2)))+(6*ncv));
tol.val = 0.0f;
ido.val = 0;
info.val = 0;
// c
// c     %---------------------------------------------------%
// c     | This program uses exact shifts with respect to    |
// c     | the current Hessenberg matrix (IPARAM(1) = 1).    |
// c     | IPARAM(3) specifies the maximum number of Arnoldi |
// c     | iterations allowed. Mode 3 of SNAUPD is used      |
// c     | (IPARAM(7) = 3).  All these options can be        |
// c     | changed by the user. For details see the          |
// c     | documentation in SNAUPD.                          |
// c     %---------------------------------------------------%
// c
ishfts = 1;
maxitr = 300;
mode = 3;
// 
iparam[(1-(1))] = ishfts;
iparam[(3-(1))] = maxitr;
iparam[(7-(1))] = mode;
// c
// c     %-------------------------------------------%
// c     | M A I N   L O O P (Reverse communication) | 
// c     %-------------------------------------------%
// c
label20:
   Dummy.label("Sndrv2",20);
// c
// c        %---------------------------------------------%
// c        | Repeatedly call the routine SNAUPD and take | 
// c        | actions indicated by parameter IDO until    |
// c        | either convergence is indicated or maxitr   |
// c        | has been exceeded.                          |
// c        %---------------------------------------------%
// c
org.netlib.arpack.Snaupd.snaupd(ido,bmat,n,which,nev.val,tol,resid,0,ncv,v,0,256,iparam,0,ipntr,0,workd,0,workl,0,lworkl,info);
// c
if (((ido.val == -1) || (ido.val == 1)))  {
    // c
// c           %-------------------------------------------%
// c           | Perform  y <--- OP*x = inv[A-SIGMA*I]*x   |
// c           | The user should supply his/her own linear |
// c           | system solver here that takes             |
// c           | workd(ipntr(1)) as the input, and returns |
// c           | the result to workd(ipntr(2)).            |
// c           %-------------------------------------------%
// c
org.netlib.blas.Scopy.scopy(n,workd,(ipntr[(1-(1))]-(1)),1,workd,(ipntr[(2-(1))]-(1)),1);
// c
org.netlib.lapack.Sgttrs.sgttrs("N",n,1,dl,0,dd,0,du,0,du2,0,ipiv,0,workd,(ipntr[(2-(1))]-(1)),n,ierr);
if ((ierr.val != 0))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _gttrs in _NDRV2."));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
Dummy.go_to("Sndrv2",9000);
}
// c
// c           %-----------------------------------------%
// c           | L O O P   B A C K to call SNAUPD again. |
// c           %-----------------------------------------%
// c
Dummy.go_to("Sndrv2",20);
// c
}
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
// c        | documentation in SNAUPD. |
// c        %--------------------------%
// c
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _naupd, info = "));
  __io_vec.addElement(new Integer(info.val));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Check the documentation in _naupd."));
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
// c        | Post-Process using SNEUPD.                |
// c        |                                           |
// c        | Computed eigenvalues may be extracted.    |  
// c        |                                           |
// c        | Eigenvectors may also be computed now if  |
// c        | desired.  (indicated by rvec = .true.)    | 
// c        %-------------------------------------------%
// c 
rvec = true;
// c
org.netlib.arpack.Sneupd.sneupd(rvec,"A",select,0,d,0,d,(1-(1))+(2-(1)) * (25),v,0,256,sigmar,sigmai,workev,0,bmat,n,which,nev,tol.val,resid,0,ncv,v,0,256,iparam,0,ipntr,0,workd,0,workl,0,lworkl,ierr);
// c
// c        %-----------------------------------------------%
// c        | The real part of the eigenvalue is returned   |
// c        | in the first column of the two dimensional    |
// c        | array D, and the imaginary part is returned   |
// c        | in the second column of D.  The corresponding |
// c        | eigenvectors are returned in the first NEV    |
// c        | columns of the two dimensional array V if     |
// c        | requested.  Otherwise, an orthogonal basis    |
// c        | for the invariant subspace corresponding to   |
// c        | the eigenvalues in D is returned in V.        |
// c        %-----------------------------------------------%
// c
if ((ierr.val != 0))  {
    // c
// c           %------------------------------------%
// c           | Error condition:                   |
// c           | Check the documentation of SNEUPD. |
// c           %------------------------------------%
// c
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _neupd, info = "));
  __io_vec.addElement(new Integer(ierr.val));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Check the documentation of _neupd. "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
// c 
}
else  {
  // c
first = true;
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
if ((d[(j-(1))+(2-(1)) * (25)] == 0.0f))  {
    // c
// c                 %--------------------%
// c                 | Ritz value is real |
// c                 %--------------------%
// c
Av.av(n,v,(1-(1))+(j-(1)) * (256),ax,0);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(1-(1)) * (25)])),v,(1-(1))+(j-(1)) * (256),1,ax,0,1);
d[(j-(1))+(3-(1)) * (25)] = org.netlib.blas.Snrm2.snrm2(n,ax,0,1);
d[(j-(1))+(3-(1)) * (25)] = (d[(j-(1))+(3-(1)) * (25)]/Math.abs(d[(j-(1))+(1-(1)) * (25)]));
// c
}
else if (first)  {
    // c
// c                 %------------------------%
// c                 | Ritz value is complex  |
// c                 | Residual of one Ritz   |
// c                 | value of the conjugate |
// c                 | pair is computed.      | 
// c                 %------------------------%
// c 
Av.av(n,v,(1-(1))+(j-(1)) * (256),ax,0);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(1-(1)) * (25)])),v,(1-(1))+(j-(1)) * (256),1,ax,0,1);
org.netlib.blas.Saxpy.saxpy(n,d[(j-(1))+(2-(1)) * (25)],v,(1-(1))+((j+1)-(1)) * (256),1,ax,0,1);
d[(j-(1))+(3-(1)) * (25)] = org.netlib.blas.Snrm2.snrm2(n,ax,0,1);
Av.av(n,v,(1-(1))+((j+1)-(1)) * (256),ax,0);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(2-(1)) * (25)])),v,(1-(1))+(j-(1)) * (256),1,ax,0,1);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(1-(1)) * (25)])),v,(1-(1))+((j+1)-(1)) * (256),1,ax,0,1);
d[(j-(1))+(3-(1)) * (25)] = org.netlib.lapack.Slapy2.slapy2(d[(j-(1))+(3-(1)) * (25)],org.netlib.blas.Snrm2.snrm2(n,ax,0,1));
d[((j+1)-(1))+(3-(1)) * (25)] = d[(j-(1))+(3-(1)) * (25)];
first = false;
}              // Close else if()
else  {
  first = true;
}              //  Close else.
// c
Dummy.label("Sndrv2",30);
}              //  Close for() loop. 
}
// c
// c           %-----------------------------%
// c           | Display computed residuals. |
// c           %-----------------------------%
// c
org.netlib.arpack.Smout.smout(6,nconv,3,d,0,25,-6,"Ritz values (Real,Imag) and relative residuals");
// c
}              //  Close else.
// c
// c        %-------------------------------------------%
// c        | Print additional convergence information. |
// c        %-------------------------------------------%
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
  __io_vec.addElement(new String(" _NDRV2 "));
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
// c     | Done with program sndrv2. |
// c     %---------------------------%
// c
label9000:
   Dummy.label("Sndrv2",9000);
// c
Dummy.label("Sndrv2",999999);
return;
   }
} // End class.
