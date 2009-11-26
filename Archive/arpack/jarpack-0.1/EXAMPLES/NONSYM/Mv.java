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



public class Mv {

// c 
// c=======================================================================
// c
// c     matrix vector multiplication subroutine
// c
// c
// c     Compute the matrix vector multiplication y<---M*x
// c     where M is mass matrix formed by using piecewise linear elements 

// c     on [0,1].
// c 

public static void mv (int n,
double [] v, int _v_offset,
double [] w, int _w_offset)  {

int j= 0;
double h= 0.0d;
w[(1-(1))+ _w_offset] = ((((4.0*v[(1-(1))+ _v_offset])+(1.0*v[(2-(1))+ _v_offset])))/6.0);
{
for (j = 2; j <= (n-1); j++) {
w[(j-(1))+ _w_offset] = (((((1.0*v[((j-1)-(1))+ _v_offset])+(4.0*v[(j-(1))+ _v_offset]))+(1.0*v[((j+1)-(1))+ _v_offset])))/6.0);
Dummy.label("Mv",10);
}              //  Close for() loop. 
}
w[(n-(1))+ _w_offset] = ((((1.0*v[((n-1)-(1))+ _v_offset])+(4.0*v[(n-(1))+ _v_offset])))/6.0);
// c
h = (1.0/(double)((n+1)));
org.netlib.blas.Dscal.dscal(n,h,w,_w_offset,1);
Dummy.go_to("Mv",999999);
Dummy.label("Mv",999999);
return;
   }
} // End class.
