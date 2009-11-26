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



public class Av {

// c 
// c ------------------------------------------------------------------
// c     matrix vector subroutines
// c
// c     The matrix A is derived from the simplest finite difference 
// c     discretization of the integral operator 
// c
// c                     f(s) = integral(K(s,t)x(t)dt).
// c      
// c     Thus, the matrix A is a discretization of the 2-dimensional kernel
// c     K(s,t)dt, where
// c
// c                 K(s,t) =  s(t-1)   if 0 .le. s .le. t .le. 1,
// c                           t(s-1)   if 0 .le. t .lt. s .le. 1.
// c
// c     Thus A is an m by n matrix with entries
// c
// c                 A(i,j) = k*(si)*(tj - 1)  if i .le. j,
// c                          k*(tj)*(si - 1)  if i .gt. j
// c
// c     where si = i/(m+1)  and  tj = j/(n+1)  and k = 1/(n+1).
// c      
// c-------------------------------------------------------------------
// c
// c
// c     computes  w <- A*x
// c
// c

public static void av (int m,
int n,
double [] x, int _x_offset,
double [] w, int _w_offset)  {

int i= 0;
int j= 0;
double h= 0.0d;
double k= 0.0d;
double s= 0.0d;
double t= 0.0d;
h = (1.0/(double)((m+1)));
k = (1.0/(double)((n+1)));
{
for (i = 1; i <= m; i++) {
w[(i-(1))+ _w_offset] = 0.0;
Dummy.label("Av",5);
}              //  Close for() loop. 
}
t = 0.0;
// c      
{
for (j = 1; j <= n; j++) {
t = (t+k);
s = 0.0;
{
for (i = 1; i <= j; i++) {
s = (s+h);
w[(i-(1))+ _w_offset] = (w[(i-(1))+ _w_offset]+(((k*s)*((t-1.0)))*x[(j-(1))+ _x_offset]));
Dummy.label("Av",10);
}              //  Close for() loop. 
}
{
for (i = (j+1); i <= m; i++) {
s = (s+h);
w[(i-(1))+ _w_offset] = (w[(i-(1))+ _w_offset]+(((k*t)*((s-1.0)))*x[(j-(1))+ _x_offset]));
Dummy.label("Av",20);
}              //  Close for() loop. 
}
Dummy.label("Av",30);
}              //  Close for() loop. 
}
// c
Dummy.go_to("Av",999999);
Dummy.label("Av",999999);
return;
   }
} // End class.
