/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: ssdrv6.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Av {

// c
// c-----------------------------------------------------------------------
// c     Matrix vector subroutine
// c     where the matrix is the stiffness matrix obtained from the 
// c     finite element discretization of the 1-dimensional discrete Laplac
// c     on the interval [0,1] with zero Dirichlet boundary condition
// c     using piecewise linear elements.
// c
// c

public static void av (int n,
float [] v, int _v_offset,
float [] w, int _w_offset)  {

int j= 0;
float h= 0.0f;
w[(1-(1))+ _w_offset] = ((2.0f*v[(1-(1))+ _v_offset])-v[(2-(1))+ _v_offset]);
{
for (j = 2; j <= (n-1); j++) {
w[(j-(1))+ _w_offset] = (((-(v[((j-1)-(1))+ _v_offset]))+(2.0f*v[(j-(1))+ _v_offset]))-v[((j+1)-(1))+ _v_offset]);
Dummy.label("Av",100);
}              //  Close for() loop. 
}
j = n;
w[(j-(1))+ _w_offset] = ((-(v[((j-1)-(1))+ _v_offset]))+(2.0f*v[(j-(1))+ _v_offset]));
// c     
// c     Scale the vector w by (1/h).
// c
h = (1.0f/(float)((n+1)));
org.netlib.blas.Sscal.sscal(n,(1.0f/h),w,_w_offset,1);
Dummy.go_to("Av",999999);
Dummy.label("Av",999999);
return;
   }
} // End class.
