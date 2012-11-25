/*
 *  Produced by f2java.  f2java is part of the Fortran-
 *  -to-Java project at the University of Tennessee Netlib
 *  numerical software repository.
 *
 *  Original authorship for the BLAS and LAPACK numerical
 *  routines may be found in the Fortran source, available at
 *  http://www.netlib.org.
 *
 *  Fortran input file: sssimp.f
 *  f2java version: 0.8.1
 *
 */

import java.lang.*;
import org.netlib.util.*;



public class Av {

// c 
// c ------------------------------------------------------------------
// c     matrix vector subroutine
// c
// c     The matrix used is the 2 dimensional discrete Laplacian on unit
// c     square with zero Dirichlet boundary condition.
// c
// c     Computes w <--- OP*v, where OP is the nx*nx by nx*nx block 
// c     tridiagonal matrix
// c
// c                  | T -I          | 
// c                  |-I  T -I       |
// c             OP = |   -I  T       |
// c                  |        ...  -I|
// c                  |           -I T|
// c
// c     The subroutine TV is called to computed y<---T*x.
// c
// c

public static void av (int nx,
float [] v, int _v_offset,
float [] w, int _w_offset)  {

int j= 0;
int lo= 0;
int n2= 0;
float h2= 0.0f;
Tv.tv(nx,v,(1-(1))+ _v_offset,w,(1-(1))+ _w_offset);
org.netlib.blas.Saxpy.saxpy(nx,-1.0f,v,((nx+1)-(1))+ _v_offset,1,w,(1-(1))+ _w_offset,1);
// c
{
for (j = 2; j <= (nx-1); j++) {
lo = (((j-1))*nx);
Tv.tv(nx,v,((lo+1)-(1))+ _v_offset,w,((lo+1)-(1))+ _w_offset);
org.netlib.blas.Saxpy.saxpy(nx,-1.0f,v,(((lo-nx)+1)-(1))+ _v_offset,1,w,((lo+1)-(1))+ _w_offset,1);
org.netlib.blas.Saxpy.saxpy(nx,-1.0f,v,(((lo+nx)+1)-(1))+ _v_offset,1,w,((lo+1)-(1))+ _w_offset,1);
Dummy.label("Av",10);
}              //  Close for() loop. 
}
// c
lo = (((nx-1))*nx);
Tv.tv(nx,v,((lo+1)-(1))+ _v_offset,w,((lo+1)-(1))+ _w_offset);
org.netlib.blas.Saxpy.saxpy(nx,-1.0f,v,(((lo-nx)+1)-(1))+ _v_offset,1,w,((lo+1)-(1))+ _w_offset,1);
// c
// c     Scale the vector w by (1/h^2), where h is the mesh size
// c
n2 = (nx*nx);
h2 = (1.0f/(float)((((nx+1))*((nx+1)))));
org.netlib.blas.Sscal.sscal(n2,(1.0f/h2),w,_w_offset,1);
Dummy.go_to("Av",999999);
Dummy.label("Av",999999);
return;
   }
} // End class.
