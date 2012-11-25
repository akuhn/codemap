/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: sndrv1.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Sndrv1 {

// c
// c
// c     Example program to illustrate the idea of reverse communication
// c     for a standard nonsymmetric eigenvalue problem.
// c
// c     We implement example one of ex-nonsym.doc in DOCUMENTS directory
// c
// c\Example-1
// c     ... Suppose we want to solve A*x = lambda*x in regular mode,
// c         where A is obtained from the standard central difference
// c         discretization of the convection-diffusion operator 
// c                 (Laplacian u) + rho*(du / dx)
// c         on the unit square [0,1]x[0,1] with zero Dirichlet boundary 
// c         condition.
// c
// c     ... OP = A  and  B = I.
// c
// c     ... Assume "call av (nx,x,y)" computes y = A*x.c
// c
// c     ... Use mode 1 of SNAUPD.
// c
// c\BeginLib
// c
// c\Routines called:
// c     snaupd  ARPACK reverse communication interface routine.
// c     sneupd  ARPACK routine that returns Ritz values and (optionally)
// c             Ritz vectors.
// c     slapy2  LAPACK routine to compute sqrt(x**2+y**2) carefully.
// c     saxpy   Level 1 BLAS that computes y <- alpha*x+y.
// c     snrm2   Level 1 BLAS that computes the norm of a vector.
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
// c FILE: ndrv1.F   SID: 2.5   DATE OF SID: 10/17/00   RELEASE: 2
// c
// c\Remarks
// c     1. None
// c
// c\EndLib
// c-----------------------------------------------------------------------
// c
// c     %-----------------------------%
// c     | Define maximum dimensions   |
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
// c     %--------------------------------------------------%
// c     | The number NX is the number of interior points   |
// c     | in the discretization of the 2-dimensional       |
// c     | convection-diffusion operator on the unit        |
// c     | square with zero Dirichlet boundary condition.   | 
// c     | The number N(=NX*NX) is the dimension of the     |
// c     | matrix.  A standard eigenvalue problem is        |
// c     | solved (BMAT = 'I').  NEV is the number of       |
// c     | eigenvalues to be approximated.  The user can    |
// c     | modify NX, NEV, NCV, WHICH to solve problems of  |
// c     | different sizes, and to get different parts of   |
// c     | the spectrum.  However, The following            |
// c     | conditions must be satisfied:                    |
// c     |                   N <= MAXN                      |
// c     |                 NEV <= MAXNEV                    |
// c     |           NEV + 2 <= NCV <= MAXNCV               | 
// c     %--------------------------------------------------% 
// c

public static void main (String [] args)  {

int [] iparam= new int[(11)];
int [] ipntr= new int[(14)];
boolean [] select= new boolean[(30)];
float [] ax= new float[(256)];
float [] d= new float[(30) * (3)];
float [] resid= new float[(256)];
float [] v= new float[(256) * (30)];
float [] workd= new float[((3*256))];
float [] workev= new float[((3*30))];
float [] workl= new float[((((3*30)*30)+(6*30)))];
String bmat= new String(" ");
String which= new String("  ");
intW ido= new intW(0);
int n= 0;
int nx= 0;
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
float sigmar= 0.0f;
float sigmai= 0.0f;
boolean first= false;
boolean rvec= false;
  java.util.Vector __io_vec = new java.util.Vector();
nx = 10;
n = (nx*nx);
nev.val = 4;
ncv = 20;
if ((n > 256))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _NDRV1: N is greater than MAXN "));
Util.f77write(null, __io_vec);
Dummy.go_to("Sndrv1",9000);
}
else if ((nev.val > 12))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _NDRV1: NEV is greater than MAXNEV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Sndrv1",9000);
}              // Close else if()
else if ((ncv > 30))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _NDRV1: NCV is greater than MAXNCV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Sndrv1",9000);
}              // Close else if()
bmat = "I";
which = "SM";
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
// c     | iterations allowed.  Mode 1 of SNAUPD is used     |
// c     | (IPARAM(7) = 1). All these options can be changed |
// c     | by the user. For details see the documentation in |
// c     | SNAUPD.                                           |
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
   Dummy.label("Sndrv1",10);
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
// c           | Perform matrix vector multiplication      |
// c           |                y <--- OP*x                |
// c           | The user should supply his/her own        |
// c           | matrix vector multiplication routine here |
// c           | that takes workd(ipntr(1)) as the input   |
// c           | vector, and return the matrix vector      |
// c           | product to workd(ipntr(2)).               | 
// c           %-------------------------------------------%
// c
Av.av(nx,workd,(ipntr[(1-(1))]-(1)),workd,(ipntr[(2-(1))]-(1)));
// c
// c           %-----------------------------------------%
// c           | L O O P   B A C K to call SNAUPD again. |
// c           %-----------------------------------------%
// c
Dummy.go_to("Sndrv1",10);
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
  __io_vec.addElement(new String(" Check the documentation of _naupd"));
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
org.netlib.arpack.Sneupd.sneupd(rvec,"A",select,0,d,0,d,(1-(1))+(2-(1)) * (30),v,0,256,sigmar,sigmai,workev,0,bmat,n,which,nev,tol.val,resid,0,ncv,v,0,256,iparam,0,ipntr,0,workd,0,workl,0,lworkl,ierr);
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
if ((d[(j-(1))+(2-(1)) * (30)] == 0.0f))  {
    // c
// c                  %--------------------%
// c                  | Ritz value is real |
// c                  %--------------------%
// c
Av.av(nx,v,(1-(1))+(j-(1)) * (256),ax,0);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(1-(1)) * (30)])),v,(1-(1))+(j-(1)) * (256),1,ax,0,1);
d[(j-(1))+(3-(1)) * (30)] = org.netlib.blas.Snrm2.snrm2(n,ax,0,1);
d[(j-(1))+(3-(1)) * (30)] = (d[(j-(1))+(3-(1)) * (30)]/Math.abs(d[(j-(1))+(1-(1)) * (30)]));
// c
}
else if (first)  {
    // c
// c                  %------------------------%
// c                  | Ritz value is complex. |
// c                  | Residual of one Ritz   |
// c                  | value of the conjugate |
// c                  | pair is computed.      | 
// c                  %------------------------%
// c        
Av.av(nx,v,(1-(1))+(j-(1)) * (256),ax,0);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(1-(1)) * (30)])),v,(1-(1))+(j-(1)) * (256),1,ax,0,1);
org.netlib.blas.Saxpy.saxpy(n,d[(j-(1))+(2-(1)) * (30)],v,(1-(1))+((j+1)-(1)) * (256),1,ax,0,1);
d[(j-(1))+(3-(1)) * (30)] = org.netlib.blas.Snrm2.snrm2(n,ax,0,1);
Av.av(nx,v,(1-(1))+((j+1)-(1)) * (256),ax,0);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(2-(1)) * (30)])),v,(1-(1))+(j-(1)) * (256),1,ax,0,1);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(1-(1)) * (30)])),v,(1-(1))+((j+1)-(1)) * (256),1,ax,0,1);
d[(j-(1))+(3-(1)) * (30)] = org.netlib.lapack.Slapy2.slapy2(d[(j-(1))+(3-(1)) * (30)],org.netlib.blas.Snrm2.snrm2(n,ax,0,1));
d[(j-(1))+(3-(1)) * (30)] = (d[(j-(1))+(3-(1)) * (30)]/org.netlib.lapack.Slapy2.slapy2(d[(j-(1))+(1-(1)) * (30)],d[(j-(1))+(2-(1)) * (30)]));
d[((j+1)-(1))+(3-(1)) * (30)] = d[(j-(1))+(3-(1)) * (30)];
first = false;
}              // Close else if()
else  {
  first = true;
}              //  Close else.
// c
Dummy.label("Sndrv1",20);
}              //  Close for() loop. 
}
// c
// c            %-----------------------------%
// c            | Display computed residuals. |
// c            %-----------------------------%
// c
org.netlib.arpack.Smout.smout(6,nconv,3,d,0,30,-6,"Ritz values (Real,Imag) and relative residuals");
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
  __io_vec.addElement(new String(" _NDRV1 "));
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
// c     | Done with program sndrv1. |
// c     %---------------------------%
// c
label9000:
   Dummy.label("Sndrv1",9000);
// c
Dummy.label("Sndrv1",999999);
return;
   }
} // End class.
