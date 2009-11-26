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



public class Mv {

// c
// c-----------------------------------------------------------------------
// c     Matrix vector subroutine
// c     where the matrix used is the 1 dimensional mass matrix
// c     arising from using the piecewise linear finite element
// c     on the interval [0,1].
// c
// c

public static void mv (int n,
float [] v, int _v_offset,
float [] w, int _w_offset)  {

int j= 0;
float h= 0.0f;
w[(1-(1))+ _w_offset] = ((4.0f*v[(1-(1))+ _v_offset])+v[(2-(1))+ _v_offset]);
{
for (j = 2; j <= (n-1); j++) {
w[(j-(1))+ _w_offset] = ((v[((j-1)-(1))+ _v_offset]+(4.0f*v[(j-(1))+ _v_offset]))+v[((j+1)-(1))+ _v_offset]);
Dummy.label("Mv",100);
}              //  Close for() loop. 
}
j = n;
w[(j-(1))+ _w_offset] = (v[((j-1)-(1))+ _v_offset]+(4.0f*v[(j-(1))+ _v_offset]));
// c
// c     Scale the vector w by h.
// c
h = (1.0f/((6.0f*(float)((n+1)))));
org.netlib.blas.Sscal.sscal(n,h,w,_w_offset,1);
Dummy.go_to("Mv",999999);
Dummy.label("Mv",999999);
return;
   }
} // End class.
