/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: ssbdr5.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Ssbdr5 {

// c
// c     ... Construct the matrix A in LAPACK-style band form.
// c         The matrix A is the 1-dimensional discrete Laplacian on [0,1]

// c         with zero Dirichlet boundary condition, KG is the mass
// c         formed by using piecewise linear elements on [0,1]. 
// c
// c     ... Call SSBAND with Buckling mode to find eigenvalues LAMBDA 
// c         such that
// c                          A*x = M*x*LAMBDA.
// c
// c     ... Use mode 4 of SSAUPD.
// c
// c\BeginLib
// c
// c\Routines called:
// c     ssband  ARPACK banded eigenproblem solver.
// c     slapy2  LAPACK routine to compute sqrt(x**2+y**2) carefully.
// c     slaset  LAPACK routine to initialize a matrix to zero.
// c     saxpy   Level 1 BLAS that computes y <- alpha*x+y.
// c     snrm2   Level 1 BLAS that computes the norm of a vector.
// c     sgbmv   Level 2 BLAS that computes the band matrix vector product

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
// c FILE: sbdr5.F   SID: 2.5   DATE OF SID: 08/26/96   RELEASE: 2
// c
// c\Remarks
// c     1. None
// c
// c\EndLib
// c
// c----------------------------------------------------------------------

// c
// c     %-------------------------------------%
// c     | Define leading dimensions for all   |
// c     | arrays.                             |
// c     | MAXN   - Maximum size of the matrix |
// c     | MAXNEV - Maximum number of          |
// c     |          eigenvalues to be computed |
// c     | MAXNCV - Maximum number of Arnoldi  |
// c     |          vectors stored             | 
// c     | MAXBDW - Maximum bandwidth          |
// c     %-------------------------------------%
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
// c     | The number N is the dimension of the matrix.  A  |
// c     | generalized eigenvalue problem is solved         |
// c     | (BMAT = 'G').  NEV is the number of eigenvalues  |
// c     | to be approximated.  Since the Buckling mode is  |
// c     | is used, WHICH is set to 'LM'.  The user can     |
// c     | modify N, NEV, NCV and SIGMA to solve problems   |
// c     | of different sizes, and to get different parts   |
// c     | the spectrum.  However, the following conditions |
// c     | must be satisfied:                               |
// c     |                   N <= MAXN                      |
// c     |                 NEV <= MAXNEV                    |
// c     |           NEV + 1 <= NCV <= MAXNCV               | 
// c     %--------------------------------------------------% 
// c

public static void main (String [] args)  {

int lworkl= 0;
int [] iparam= new int[(11)];
int [] iwork= new int[(1000)];
boolean [] select= new boolean[(50)];
float [] a= new float[(50) * (1000)];
float [] m= new float[(50) * (1000)];
float [] rfac= new float[(50) * (1000)];
float [] workl= new float[(((50*50)+(8*50)))];
float [] workd= new float[((3*1000))];
float [] v= new float[(1000) * (50)];
float [] resid= new float[(1000)];
float [] d= new float[(50) * (2)];
float [] ax= new float[(1000)];
float [] mx= new float[(1000)];
String which= new String("  ");
String bmat= new String(" ");
intW nev= new intW(0);
int ncv= 0;
int kl= 0;
int ku= 0;
intW info= new intW(0);
int j= 0;
int ido= 0;
int n= 0;
int isub= 0;
int isup= 0;
int idiag= 0;
int maxitr= 0;
int mode= 0;
int nconv= 0;
floatW tol= new floatW(0.0f);
float h= 0.0f;
float sigma= 0.0f;
float r1= 0.0f;
float r2= 0.0f;
boolean rvec= false;
  java.util.Vector __io_vec = new java.util.Vector();
n = 100;
nev.val = 4;
ncv = 10;
if ((n > 1000))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SBDR5: N is greater than MAXN "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssbdr5",9000);
}
else if ((nev.val > 25))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SBDR5: NEV is greater than MAXNEV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssbdr5",9000);
}              // Close else if()
else if ((ncv > 50))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SBDR5: NCV is greater than MAXNCV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Ssbdr5",9000);
}              // Close else if()
bmat = "G";
which = "LM";
sigma = 1.0f;
// c
// c     %-----------------------------------------------------%
// c     | The work array WORKL is used in SSAUPD as           |
// c     | workspace.  Its dimension LWORKL is set as          |
// c     | illustrated below.  The parameter TOL determines    |
// c     | the stopping criterion. If TOL<=0, machine          |
// c     | precision is used.  The variable IDO is used for    |
// c     | reverse communication, and is initially set to 0.   |
// c     | Setting INFO=0 indicates that a random vector is    |
// c     | generated in SSAUPD to start the Arnoldi iteration. |
// c     %-----------------------------------------------------%
// c
lworkl = (((int)Math.pow(ncv, 2))+(8*ncv));
tol.val = 0.0f;
ido = 0;
info.val = 0;
// c
// c     %---------------------------------------------------%
// c     | IPARAM(3) specifies the maximum number of Arnoldi |
// c     | iterations allowed.  Mode 4 of SSAUPD is used     |
// c     | (IPARAM(7) = 4). All these options can be changed |
// c     | by the user. For details see the documentation in |
// c     | SSBAND.                                           |
// c     %---------------------------------------------------%
// c
maxitr = 300;
mode = 4;
// c
iparam[(3-(1))] = maxitr;
iparam[(7-(1))] = mode;
// c
// c     %----------------------------------------%
// c     | Construct the matrix A in LAPACK-style |
// c     | banded form.                           |
// c     %----------------------------------------%
// c
// c     %---------------------------------------------%
// c     | Zero out the workspace for banded matrices. |
// c     %---------------------------------------------%
// c
org.netlib.lapack.Slaset.slaset("A",50,n,0.0f,0.0f,a,0,50);
org.netlib.lapack.Slaset.slaset("A",50,n,0.0f,0.0f,m,0,50);
org.netlib.lapack.Slaset.slaset("A",50,n,0.0f,0.0f,rfac,0,50);
// c
// c     %-------------------------------------%
// c     | KU, KL are number of superdiagonals |
// c     | and subdiagonals within the band of |
// c     | matrices A and M.                   |
// c     %-------------------------------------%
// c
kl = 1;
ku = 1;
// c
// c     %---------------% 
// c     | Main diagonal |
// c     %---------------%
// c
h = (1.0f/(float)((n+1)));
r1 = (4.0f/6.0f);
idiag = ((kl+ku)+1);
{
for (j = 1; j <= n; j++) {
a[(idiag-(1))+(j-(1)) * (50)] = (2.0f/h);
m[(idiag-(1))+(j-(1)) * (50)] = (r1*h);
Dummy.label("Ssbdr5",30);
}              //  Close for() loop. 
}
// c 
// c     %-------------------------------------%
// c     | First subdiagonal and superdiagonal |
// c     %-------------------------------------%
// c 
r2 = (1.0f/6.0f);
isup = (kl+ku);
isub = ((kl+ku)+2);
{
for (j = 1; j <= (n-1); j++) {
a[(isup-(1))+((j+1)-(1)) * (50)] = (-((1.0f/h)));
a[(isub-(1))+(j-(1)) * (50)] = (-((1.0f/h)));
m[(isup-(1))+((j+1)-(1)) * (50)] = (r2*h);
m[(isub-(1))+(j-(1)) * (50)] = (r2*h);
Dummy.label("Ssbdr5",60);
}              //  Close for() loop. 
}
// c
// c     %-------------------------------------%
// c     | Call SSBAND to find eigenvalues and |
// c     | eigenvectors.  Eigenvalues are      |
// c     | returned in the first column of D.  |
// c     | Eigenvectors are returned in the    |
// c     | first NCONV (=IPARAM(5)) columns of |
// c     | V.                                  |
// c     %-------------------------------------%
// c
rvec = true;
Ssband.ssband(rvec,"A",select,0,d,0,v,0,1000,sigma,n,a,0,m,0,50,rfac,0,kl,ku,which,bmat,nev,tol,resid,0,ncv,v,0,1000,iparam,0,workd,0,workl,0,lworkl,iwork,0,info);
// c
if ((info.val == 0))  {
    // c
nconv = iparam[(5-(1))];
// c
// c        %-----------------------------------%
// c        | Print out convergence information |
// c        %-----------------------------------%
// c
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" _SBDR5 "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" ====== "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" The size of the matrix is "));
  __io_vec.addElement(new Integer(n));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Number of eigenvalue requested is "));
  __io_vec.addElement(new Integer(nev.val));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" The number of Lanczos vectors generated"));
  __io_vec.addElement(new String(" (NCV) is "));
  __io_vec.addElement(new Integer(ncv));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" The number of converged Ritz values is "));
  __io_vec.addElement(new Integer(nconv));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" What portion of the spectrum "));
  __io_vec.addElement(new String(which));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" The number of Implicit Arnoldi"));
  __io_vec.addElement(new String(" update taken is "));
  __io_vec.addElement(new Integer(iparam[(3-(1))]));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" The number of OP*x is "));
  __io_vec.addElement(new Integer(iparam[(9-(1))]));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" The convergence tolerance is "));
  __io_vec.addElement(new Float(tol.val));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
// c
// c        %----------------------------%
// c        | Compute the residual norm. |
// c        |    ||  A*x - lambda*x ||   |
// c        %----------------------------%
// c
{
for (j = 1; j <= nconv; j++) {
org.netlib.blas.Sgbmv.sgbmv("Notranspose",n,n,kl,ku,1.0f,a,((kl+1)-(1))+(1-(1)) * (50),50,v,(1-(1))+(j-(1)) * (1000),1,0.0f,ax,0,1);
org.netlib.blas.Sgbmv.sgbmv("Notranspose",n,n,kl,ku,1.0f,m,((kl+1)-(1))+(1-(1)) * (50),50,v,(1-(1))+(j-(1)) * (1000),1,0.0f,mx,0,1);
org.netlib.blas.Saxpy.saxpy(n,(-(d[(j-(1))+(1-(1)) * (50)])),mx,0,1,ax,0,1);
d[(j-(1))+(2-(1)) * (50)] = org.netlib.blas.Snrm2.snrm2(n,ax,0,1);
d[(j-(1))+(2-(1)) * (50)] = (d[(j-(1))+(2-(1)) * (50)]/Math.abs(d[(j-(1))+(1-(1)) * (50)]));
// c
Dummy.label("Ssbdr5",90);
}              //  Close for() loop. 
}
// 
org.netlib.arpack.Smout.smout(6,nconv,2,d,0,50,-6,"Ritz values and relative residuals");
}
else  {
  // c
// c        %-------------------------------------%
// c        | Either convergence failed, or there |
// c        | is error.  Check the documentation  |
// c        | for SSBAND.                         |
// c        %-------------------------------------%
// c
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Error with _sband, info= "));
  __io_vec.addElement(new Integer(info.val));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" Check the documentation of _sband "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" "));
Util.f77write(null, __io_vec);
// c
}              //  Close else.
// c
label9000:
   Dummy.label("Ssbdr5",9000);
Dummy.label("Ssbdr5",999999);
return;
   }
} // End class.
