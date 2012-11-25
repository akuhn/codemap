/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: dsbdr1.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Dsbdr1 {

// c
// c     ... Construct the matrix A in LAPACK-style band form.
// c         The matrix A is derived from the discretization of
// c         the 2-dimensional Laplacian on the unit square with 
// c         zero Dirichlet boundary condition using standard 
// c         central difference.
// c
// c     ... Call DSBAND  to find eigenvalues LAMBDA such that
// c                          A*x = x*LAMBDA.
// c       
// c     ... Use mode 1 of DSAUPD .
// c
// c\BeginLib
// c
// c\Routines called:
// c     dsband   ARPACK banded eigenproblem solver.
// c     dlapy2   LAPACK routine to compute sqrt(x**2+y**2) carefully.
// c     dlaset   LAPACK routine to initialize a matrix to zero.
// c     daxpy    Level 1 BLAS that computes y <- alpha*x+y.
// c     dnrm2    Level 1 BLAS that computes the norm of a vector.
// c     dgbmv    Level 2 BLAS that computes the band matrix vector product
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
// c FILE: sbdr1.F   SID: 2.5   DATE OF SID: 08/26/96   RELEASE: 2
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
// c     %-------------------------------------------------%
// c     | The number NX is the number of interior points  |
// c     | in the discretization of the 2-dimensional      |
// c     | Laplacian operator on the unit square with zero |
// c     | Dirichlet boundary condition. The number        |
// c     | N(=NX*NX) is the dimension of the matrix.  A    |
// c     | standard eigenvalue problem is solved           |
// c     | (BMAT = 'I').  NEV is the number of eigenvalues |
// c     | to be approximated. The user can modify NX,NEV, |
// c     | NCV and WHICH to solve problems of different    |
// c     | sizes, and to get different parts the spectrum. |
// c     | However, the following conditions must be       |
// c     | satisfied:                                      |
// c     |                   N <= MAXN                     |
// c     |                 NEV <= MAXNEV                   |
// c     |           NEV + 1 <= NCV <= MAXNCV              | 
// c     %-------------------------------------------------% 
// c

public static void main (String [] args)  {

int lworkl= 0;
int [] iparam= new int[(11)];
int [] iwork= new int[(1000)];
boolean [] select= new boolean[(50)];
double [] a= new double[(50) * (1000)];
double [] m= new double[(50) * (1000)];
double [] rfac= new double[(50) * (1000)];
double [] workl= new double[(((50*50)+(8*50)))];
double [] workd= new double[((3*1000))];
double [] v= new double[(1000) * (50)];
double [] resid= new double[(1000)];
double [] d= new double[(50) * (2)];
double [] ax= new double[(1000)];
String which= new String("  ");
String bmat= new String(" ");
intW nev= new intW(0);
int ncv= 0;
int ku= 0;
int kl= 0;
intW info= new intW(0);
int i= 0;
int j= 0;
int ido= 0;
int n= 0;
int nx= 0;
int lo= 0;
int isub= 0;
int isup= 0;
int idiag= 0;
int maxitr= 0;
int mode= 0;
int nconv= 0;
doubleW tol= new doubleW(0.0d);
double sigma= 0.0d;
double h2= 0.0d;
boolean rvec= false;
  java.util.Vector __io_vec = new java.util.Vector();
nx = 10;
n = (nx*nx);
nev.val = 4;
ncv = 10;
if ((n > 1000))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SBDR1: N is greater than MAXN "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsbdr1",9000);
}
else if ((nev.val > 25))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SBDR1: NEV is greater than MAXNEV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsbdr1",9000);
}              // Close else if()
else if ((ncv > 50))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SBDR1: NCV is greater than MAXNCV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsbdr1",9000);
}              // Close else if()
bmat = "I";
which = "LM";
// c
// c     %-----------------------------------------------------%
// c     | The work array WORKL is used in DSAUPD  as           |
// c     | workspace.  Its dimension LWORKL is set as          |
// c     | illustrated below.  The parameter TOL determines    |
// c     | the stopping criterion. If TOL<=0, machine          |
// c     | precision is used.  The variable IDO is used for    |
// c     | reverse communication, and is initially set to 0.   |
// c     | Setting INFO=0 indicates that a random vector is    |
// c     | generated in DSAUPD  to start the Arnoldi iteration. |
// c     %-----------------------------------------------------%
// c
lworkl = (((int)Math.pow(ncv, 2))+(8*ncv));
tol.val = 0.0;
ido = 0;
info.val = 0;
// c
// c     %---------------------------------------------------%
// c     | IPARAM(3) specifies the maximum number of Arnoldi |
// c     | iterations allowed.  Mode 1 of DSAUPD  is used     |
// c     | (IPARAM(7) = 1). All these options can be changed |
// c     | by the user. For details see the documentation in |
// c     | DSBAND .                                           |
// c     %---------------------------------------------------%
// c
maxitr = 300;
mode = 1;
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
org.netlib.lapack.Dlaset.dlaset("A",50,n,0.0,0.0,a,0,50);
org.netlib.lapack.Dlaset.dlaset("A",50,n,0.0,0.0,m,0,50);
org.netlib.lapack.Dlaset.dlaset("A",50,n,0.0,0.0,rfac,0,50);
// c
// c     %-------------------------------------%
// c     | KU, KL are number of superdiagonals |
// c     | and subdiagonals within the band of |
// c     | matrices A and M.                   |
// c     %-------------------------------------%
// c
kl = nx;
ku = nx;
// c
// c     %---------------% 
// c     | Main diagonal |
// c     %---------------%
// c
h2 = (1.0/((((nx+1))*((nx+1)))));
idiag = ((kl+ku)+1);
{
for (j = 1; j <= n; j++) {
a[(idiag-(1))+(j-(1)) * (50)] = (4.0e+0/h2);
Dummy.label("Dsbdr1",30);
}              //  Close for() loop. 
}
// c 
// c     %-------------------------------------%
// c     | First subdiagonal and superdiagonal |
// c     %-------------------------------------%
// c 
isup = (kl+ku);
isub = ((kl+ku)+2);
{
for (i = 1; i <= nx; i++) {
lo = (((i-1))*nx);
{
for (j = (lo+1); j <= ((lo+nx)-1); j++) {
a[(isup-(1))+((j+1)-(1)) * (50)] = (-((1.0/h2)));
a[(isub-(1))+(j-(1)) * (50)] = (-((1.0/h2)));
Dummy.label("Dsbdr1",40);
}              //  Close for() loop. 
}
Dummy.label("Dsbdr1",50);
}              //  Close for() loop. 
}
// c
// c     %------------------------------------%
// c     | KL-th subdiagonal and KU-th super- |
// c     | diagonal.                          |
// c     %------------------------------------%
// c
isup = (kl+1);
isub = (((2*kl)+ku)+1);
{
for (i = 1; i <= (nx-1); i++) {
lo = (((i-1))*nx);
{
for (j = (lo+1); j <= (lo+nx); j++) {
a[(isup-(1))+((nx+j)-(1)) * (50)] = (-((1.0/h2)));
a[(isub-(1))+(j-(1)) * (50)] = (-((1.0/h2)));
Dummy.label("Dsbdr1",70);
}              //  Close for() loop. 
}
Dummy.label("Dsbdr1",80);
}              //  Close for() loop. 
}
// c
// c     %-------------------------------------%
// c     | Call DSBAND  to find eigenvalues and |
// c     | eigenvectors.  Eigenvalues are      |
// c     | returned in the first column of D.  |
// c     | Eigenvectors are returned in the    |
// c     | first NCONV (=IPARAM(5)) columns of |
// c     | V.                                  |
// c     %-------------------------------------% 
// c
rvec = true;
Dsband.dsband(rvec,"A",select,0,d,0,v,0,1000,sigma,n,a,0,m,0,50,rfac,0,kl,ku,which,bmat,nev,tol,resid,0,ncv,v,0,1000,iparam,0,workd,0,workl,0,lworkl,iwork,0,info);
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
  __io_vec.addElement(new String(" _SBDR1 "));
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
  __io_vec.addElement(new Double(tol.val));
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
org.netlib.blas.Dgbmv.dgbmv("Notranspose",n,n,kl,ku,1.0,a,((kl+1)-(1))+(1-(1)) * (50),50,v,(1-(1))+(j-(1)) * (1000),1,0.0,ax,0,1);
org.netlib.blas.Daxpy.daxpy(n,(-(d[(j-(1))+(1-(1)) * (50)])),v,(1-(1))+(j-(1)) * (1000),1,ax,0,1);
d[(j-(1))+(2-(1)) * (50)] = org.netlib.blas.Dnrm2.dnrm2(n,ax,0,1);
d[(j-(1))+(2-(1)) * (50)] = (d[(j-(1))+(2-(1)) * (50)]/Math.abs(d[(j-(1))+(1-(1)) * (50)]));
// c
Dummy.label("Dsbdr1",90);
}              //  Close for() loop. 
}
// 
org.netlib.arpack.Dmout.dmout(6,nconv,2,d,0,50,-6,"Ritz values and relative residuals");
}
else  {
  // c
// c        %-------------------------------------%
// c        | Either convergence failed, or there |
// c        | is error.  Check the documentation  |
// c        | for DSBAND .                         |
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
   Dummy.label("Dsbdr1",9000);
Dummy.label("Dsbdr1",999999);
return;
   }
} // End class.
