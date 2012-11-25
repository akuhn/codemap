/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: dndrv4.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Av {

// c------------------------------------------------------------------
// c
// c     Compute the matrix vector multiplication y<---A*x
// c     where A is obtained from the finite element discretization of the

// c     1-dimensional convection diffusion operator
// c                     d^u/dx^2 + rho*(du/dx)
// c     on the interval [0,1] with zero Dirichlet boundary condition
// c     using linear elements.
// c     This routine is only used in residual calculation.
// c

public static void av (int n,
double [] v, int _v_offset,
double [] w, int _w_offset)  {

int j= 0;
double dd= 0.0d;
double dl= 0.0d;
double du= 0.0d;
double s= 0.0d;
double h= 0.0d;
h = (1.0/(double)((n+1)));
s = (dndrv4_convct.rho.val/2.0);
dd = (2.0/h);
dl = ((-((1.0/h)))-s);
du = ((-((1.0/h)))+s);
// c
w[(1-(1))+ _w_offset] = ((dd*v[(1-(1))+ _v_offset])+(du*v[(2-(1))+ _v_offset]));
{
for (j = 2; j <= (n-1); j++) {
w[(j-(1))+ _w_offset] = (((dl*v[((j-1)-(1))+ _v_offset])+(dd*v[(j-(1))+ _v_offset]))+(du*v[((j+1)-(1))+ _v_offset]));
Dummy.label("Av",10);
}              //  Close for() loop. 
}
w[(n-(1))+ _w_offset] = ((dl*v[((n-1)-(1))+ _v_offset])+(dd*v[(n-(1))+ _v_offset]));
Dummy.go_to("Av",999999);
Dummy.label("Av",999999);
return;
   }
} // End class.
