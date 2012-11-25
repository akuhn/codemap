/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: dsvd.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Dsvd {

// c
// c     This example program is intended to illustrate the 
// c     the use of ARPACK to compute the Singular Value Decomposition.
// c   
// c     This code shows how to use ARPACK to find a few of the
// c     largest singular values(sigma) and corresponding right singular 
// c     vectors (v) for the the matrix A by solving the symmetric problem:
// c          
// c                        (A'*A)*v = sigma*v
// c 
// c     where A is an m by n real matrix.
// c
// c     This code may be easily modified to estimate the 2-norm
// c     condition number  largest(sigma)/smallest(sigma) by setting
// c     which = 'BE' below.  This will ask for a few of the smallest
// c     and a few of the largest singular values simultaneously.
// c     The condition number could then be estimated by taking
// c     the ratio of the largest and smallest singular values.
// c
// c     This formulation is appropriate when  m  .ge.  n.
// c     Reverse the roles of A and A' in the case that  m .le. n.
// c
// c     The main points illustrated here are 
// c
// c        1) How to declare sufficient memory to find NEV 
// c           largest singular values of A .  
// c
// c        2) Illustration of the reverse communication interface 
// c           needed to utilize the top level ARPACK routine DSAUPD 
// c           that computes the quantities needed to construct
// c           the desired singular values and vectors(if requested).
// c
// c        3) How to extract the desired singular values and vectors
// c           using the ARPACK routine DSEUPD.
// c
// c        4) How to construct the left singular vectors U from the 
// c           right singular vectors V to obtain the decomposition
// c
// c                        A*V = U*S
// c
// c           where S = diag(sigma_1, sigma_2, ..., sigma_k).
// c
// c     The only thing that must be supplied in order to use this
// c     routine on your problem is to change the array dimensions 
// c     appropriately, to specify WHICH singular values you want to 
// c     compute and to supply a the matrix-vector products 
// c
// c                         w <-  Ax
// c                         y <-  A'w
// c
// c     in place of the calls  to AV( ) and ATV( ) respectively below.  
// c
// c     Further documentation is available in the header of DSAUPD
// c     which may be found in the SRC directory.
// c
// c     This codes implements
// c
// c\Example-1
// c     ... Suppose we want to solve A'A*v = sigma*v in regular mode,
// c         where A is derived from the simplest finite difference 
// c         discretization of the 2-dimensional kernel  K(s,t)dt  where
// c
// c                 K(s,t) =  s(t-1)   if 0 .le. s .le. t .le. 1,
// c                           t(s-1)   if 0 .le. t .lt. s .le. 1. 
// c
// c         See subroutines AV  and ATV for details.
// c     ... OP = A'*A  and  B = I.
// c     ... Assume "call av (n,x,y)" computes y = A*x
// c     ... Assume "call atv (n,y,w)" computes w = A'*y
// c     ... Assume exact shifts are used
// c     ...
// c
// c\BeginLib
// c
// c\Routines called:
// c     dsaupd  ARPACK reverse communication interface routine.
// c     dseupd  ARPACK routine that returns Ritz values and (optionally)
// c             Ritz vectors.
// c     dnrm2   Level 1 BLAS that computes the norm of a vector.
// c     daxpy   Level 1 BLAS that computes y <- alpha*x+y.
// c     dscal   Level 1 BLAS thst computes x <- x*alpha.
// c     dcopy   Level 1 BLAS thst computes y <- x.
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
// c FILE: svd.F   SID: 2.4   DATE OF SID: 10/17/00   RELEASE: 2
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
// c     | It is assumed that A is M by N with M .ge. N.        |
// c     |                                                      |
// c     | The maximum dimensions for all arrays are            |
// c     | set here to accommodate a problem size of            |
// c     | M .le. MAXM  and  N .le. MAXN                        |
// c     |                                                      |
// c     | The NEV right singular vectors will be computed in   |
// c     | the N by NCV array V.                                |
// c     |                                                      |
// c     | The NEV left singular vectors will be computed in    |
// c     | the M by NEV array U.                                |
// c     |                                                      |
// c     | NEV is the number of singular values requested.      |
// c     |     See specifications for ARPACK usage below.       |
// c     |                                                      |
// c     | NCV is the largest number of basis vectors that will |
// c     |     be used in the Implicitly Restarted Arnoldi      |
// c     |     Process.  Work per major iteration is            |
// c     |     proportional to N*NCV*NCV.                       |
// c     |                                                      |
// c     | You must set:                                        |
// c     |                                                      |
// c     | MAXM:   Maximum number of rows of the A allowed.     |
// c     | MAXN:   Maximum number of columns of the A allowed.  |
// c     | MAXNEV: Maximum NEV allowed                          |
// c     | MAXNCV: Maximum NCV allowed                          |
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

public static void main (String [] args)  {

double [] v= new double[(250) * (25)];
double [] u= new double[(500) * (10)];
double [] workl= new double[((25*((25+8))))];
double [] workd= new double[((3*250))];
double [] s= new double[(25) * (2)];
double [] resid= new double[(250)];
double [] ax= new double[(500)];
boolean [] select= new boolean[(25)];
int [] iparam= new int[(11)];
int [] ipntr= new int[(11)];
String bmat= new String(" ");
String which= new String("  ");
intW ido= new intW(0);
int m= 0;
int n= 0;
intW nev= new intW(0);
int ncv= 0;
int lworkl= 0;
intW info= new intW(0);
intW ierr= new intW(0);
int j= 0;
int ishfts= 0;
int maxitr= 0;
int mode1= 0;
int nconv= 0;
boolean rvec= false;
doubleW tol= new doubleW(0.0d);
double sigma= 0.0d;
double temp= 0.0d;
  java.util.Vector __io_vec = new java.util.Vector();
org.netlib.arpack.arpack_debug.ndigit.val = -3;
org.netlib.arpack.arpack_debug.logfil.val = 6;
org.netlib.arpack.arpack_debug.msgets.val = 0;
org.netlib.arpack.arpack_debug.msaitr.val = 0;
org.netlib.arpack.arpack_debug.msapps.val = 0;
org.netlib.arpack.arpack_debug.msaupd.val = 1;
org.netlib.arpack.arpack_debug.msaup2.val = 0;
org.netlib.arpack.arpack_debug.mseigt.val = 0;
org.netlib.arpack.arpack_debug.mseupd.val = 0;
// c
// c     %-------------------------------------------------%
// c     | The following sets dimensions for this problem. |
// c     %-------------------------------------------------%
// c
m = 500;
n = 100;
// c
// c     %------------------------------------------------%
// c     | Specifications for ARPACK usage are set        | 
// c     | below:                                         |
// c     |                                                |
// c     |    1) NEV = 4 asks for 4 singular values to be |  
// c     |       computed.                                | 
// c     |                                                |
// c     |    2) NCV = 20 sets the length of the Arnoldi  |
// c     |       factorization                            |
// c     |                                                |
// c     |    3) This is a standard problem               |
// c     |         (indicated by bmat  = 'I')             |
// c     |                                                |
// c     |    4) Ask for the NEV singular values of       |
// c     |       largest magnitude                        |
// c     |         (indicated by which = 'LM')            |
// c     |       See documentation in DSAUPD for the      |
// c     |       other options SM, BE.                    | 
// c     |                                                |
// c     | Note: NEV and NCV must satisfy the following   |
// c     |       conditions:                              |
// c     |                 NEV <= MAXNEV,                 |
// c     |             NEV + 1 <= NCV <= MAXNCV           |
// c     %------------------------------------------------%
// c
nev.val = 4;
ncv = 10;
bmat = "I";
which = "LM";
// c
if ((n > 250))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SVD: N is greater than MAXN "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsvd",9000);
}
else if ((m > 500))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SVD: M is greater than MAXM "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsvd",9000);
}              // Close else if()
else if ((nev.val > 10))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SVD: NEV is greater than MAXNEV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsvd",9000);
}              // Close else if()
else if ((ncv > 25))  {
    __io_vec.clear();
  __io_vec.addElement(new String(" ERROR with _SVD: NCV is greater than MAXNCV "));
Util.f77write(null, __io_vec);
Dummy.go_to("Dsvd",9000);
}              // Close else if()
// c
// c     %-----------------------------------------------------%
// c     | Specification of stopping rules and initial         |
// c     | conditions before calling DSAUPD                    |
// c     |                                                     |
// c     |           abs(sigmaC - sigmaT) < TOL*abs(sigmaC)    |
// c     |               computed   true                       |
// c     |                                                     |
// c     |      If TOL .le. 0,  then TOL <- macheps            |
// c     |              (machine precision) is used.           |
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
// c     | (indicated by setting IPARAM(1) = 1.)             |
// c     | IPARAM(3) specifies the maximum number of Arnoldi |
// c     | iterations allowed.  Mode 1 of DSAUPD is used     |
// c     | (IPARAM(7) = 1). All these options can be changed |
// c     | by the user. For details see the documentation in |
// c     | DSAUPD.                                           |
// c     %---------------------------------------------------%
// c
ishfts = 1;
maxitr = n;
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
label10:
   Dummy.label("Dsvd",10);
// c
// c        %---------------------------------------------%
// c        | Repeatedly call the routine DSAUPD and take | 
// c        | actions indicated by parameter IDO until    |
// c        | either convergence is indicated or maxitr   |
// c        | has been exceeded.                          |
// c        %---------------------------------------------%
// c
org.netlib.arpack.Dsaupd.dsaupd(ido,bmat,n,which,nev.val,tol,resid,0,ncv,v,0,250,iparam,0,ipntr,0,workd,0,workl,0,lworkl,info);
// c
if (((ido.val == -1) || (ido.val == 1)))  {
    // c
// c           %---------------------------------------%
// c           | Perform matrix vector multiplications |
// c           |              w <--- A*x       (av())  |
// c           |              y <--- A'*w      (atv()) |
// c           | The user should supply his/her own    |
// c           | matrix vector multiplication routines |
// c           | here that takes workd(ipntr(1)) as    |
// c           | the input, and returns the result in  |
// c           | workd(ipntr(2)).                      |
// c           %---------------------------------------%
// c
Av.av(m,n,workd,(ipntr[(1-(1))]-(1)),ax,0);
Atv.atv(m,n,ax,0,workd,(ipntr[(2-(1))]-(1)));
// c
// c           %-----------------------------------------%
// c           | L O O P   B A C K to call DSAUPD again. |
// c           %-----------------------------------------%
// c
Dummy.go_to("Dsvd",10);
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
// c        %--------------------------------------------%
// c        | No fatal errors occurred.                  |
// c        | Post-Process using DSEUPD.                 |
// c        |                                            |
// c        | Computed singular values may be extracted. |  
// c        |                                            |
// c        | Singular vectors may also be computed now  |
// c        | if desired.  (indicated by rvec = .true.)  | 
// c        |                                            |
// c        | The routine DSEUPD now called to do this   |
// c        | post processing                            | 
// c        %--------------------------------------------%
// c           
rvec = true;
// c
org.netlib.arpack.Dseupd.dseupd(rvec,"All",select,0,s,0,v,0,250,sigma,bmat,n,which,nev,tol.val,resid,0,ncv,v,0,250,iparam,0,ipntr,0,workd,0,workl,0,lworkl,ierr);
// c
// c        %-----------------------------------------------%
// c        | Singular values are returned in the first     |
// c        | column of the two dimensional array S         |
// c        | and the corresponding right singular vectors  | 
// c        | are returned in the first NEV columns of the  |
// c        | two dimensional array V as requested here.    |
// c        %-----------------------------------------------%
// c
if ((ierr.val != 0))  {
    // c
// c           %------------------------------------%
// c           | Error condition:                   |
// c           | Check the documentation of DSEUPD. |
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
s[(j-(1))+(1-(1)) * (25)] = Math.sqrt(s[(j-(1))+(1-(1)) * (25)]);
// c
// c              %-----------------------------%
// c              | Compute the left singular   |
// c              | vectors from the formula    |
// c              |                             |
// c              |     u = Av/sigma            |
// c              |                             |
// c              | u should have norm 1 so     |
// c              | divide by norm(Av) instead. |
// c              %-----------------------------%
// c
Av.av(m,n,v,(1-(1))+(j-(1)) * (250),ax,0);
org.netlib.blas.Dcopy.dcopy(m,ax,0,1,u,(1-(1))+(j-(1)) * (500),1);
temp = (1.0/org.netlib.blas.Dnrm2.dnrm2(m,u,(1-(1))+(j-(1)) * (500),1));
org.netlib.blas.Dscal.dscal(m,temp,u,(1-(1))+(j-(1)) * (500),1);
// c
// c              %---------------------------%
// c              |                           |
// c              | Compute the residual norm |
// c              |                           |
// c              |   ||  A*v - sigma*u ||    |
// c              |                           |
// c              | for the NCONV accurately  |
// c              | computed singular values  |
// c              | and vectors.  (iparam(5)  |
// c              | indicates how many are    |
// c              | accurate to the requested |
// c              | tolerance).               |
// c              | Store the result in 2nd   |
// c              | column of array S.        |
// c              %---------------------------%
// c
org.netlib.blas.Daxpy.daxpy(m,(-(s[(j-(1))+(1-(1)) * (25)])),u,(1-(1))+(j-(1)) * (500),1,ax,0,1);
s[(j-(1))+(2-(1)) * (25)] = org.netlib.blas.Dnrm2.dnrm2(m,ax,0,1);
// c
Dummy.label("Dsvd",20);
}              //  Close for() loop. 
}
// c
// c           %-------------------------------%
// c           | Display computed residuals    |
// c           %-------------------------------%
// c
org.netlib.arpack.Dmout.dmout(6,nconv,2,s,0,25,-6,"Singular values and direct residuals");
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
  __io_vec.addElement(new String(" _SVD "));
Util.f77write(null, __io_vec);
__io_vec.clear();
  __io_vec.addElement(new String(" ==== "));
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
// c     %-------------------------%
// c     | Done with program dsvd. |
// c     %-------------------------%
// c
label9000:
   Dummy.label("Dsvd",9000);
// c
Dummy.label("Dsvd",999999);
return;
   }
} // End class.
