/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: dndrv1.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Tv {

// c=======================================================================
// c
// c
// c
// c     Compute the matrix vector multiplication y<---T*x
// c     where T is a nx by nx tridiagonal matrix with DD on the 
// c     diagonal, DL on the subdiagonal, and DU on the superdiagonal.
// c
// c     When rho*h/2 <= 1, the discrete convection-diffusion operator 
// c     has real eigenvalues.  When rho*h/2 > 1, it has COMPLEX 
// c     eigenvalues.
// c

public static void tv (int nx,
double [] x, int _x_offset,
double [] y, int _y_offset)  {

int j= 0;
double h= 0.0d;
double h2= 0.0d;
double dd= 0.0d;
double dl= 0.0d;
double du= 0.0d;
h = (1.0/(double)((nx+1)));
h2 = (h*h);
dd = (4.0e+0/h2);
dl = ((-((1.0/h2)))-((5.0e-1*0.0)/h));
du = ((-((1.0/h2)))+((5.0e-1*0.0)/h));
// c 
y[(1-(1))+ _y_offset] = ((dd*x[(1-(1))+ _x_offset])+(du*x[(2-(1))+ _x_offset]));
{
for (j = 2; j <= (nx-1); j++) {
y[(j-(1))+ _y_offset] = (((dl*x[((j-1)-(1))+ _x_offset])+(dd*x[(j-(1))+ _x_offset]))+(du*x[((j+1)-(1))+ _x_offset]));
Dummy.label("Tv",10);
}              //  Close for() loop. 
}
y[(nx-(1))+ _y_offset] = ((dl*x[((nx-1)-(1))+ _x_offset])+(dd*x[(nx-(1))+ _x_offset]));
Dummy.go_to("Tv",999999);
Dummy.label("Tv",999999);
return;
   }
} // End class.
