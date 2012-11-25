/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: ssdrv1.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Tv {

// c
// c-------------------------------------------------------------------
// c
// c
// c
// c     Compute the matrix vector multiplication y<---T*x
// c     where T is a nx by nx tridiagonal matrix with DD on the 
// c     diagonal, DL on the subdiagonal, and DU on the superdiagonal.
// c     
// c

public static void tv (int nx,
float [] x, int _x_offset,
float [] y, int _y_offset)  {

int j= 0;
float dd= 0.0f;
float dl= 0.0f;
float du= 0.0f;
dd = 4.0E+0f;
dl = -1.0f;
du = -1.0f;
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
