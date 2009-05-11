package svdlib;

public class Svdlib {

	long[] svd_longArray(int size, boolean empty, String name) {
		return new long[size];
	}

	double[] svd_doubleArray(int size, boolean empty, String name) {
		return new double[size];
	}

	void svd_beep() {
		System.err.print((char) 10);
	}

	void svd_debug(String fmt, Object... args) {
		System.err.printf(fmt, args);
	}

	void svd_error(String fmt, Object... args) {
		svd_beep();
		System.err.print("ERROR: ");
		System.err.printf(fmt, args);
		System.err.println();
	}

	void svd_fatalError(String fmt, Object... args) {
		svd_error(fmt, args);
		System.exit(1);
	}

	boolean stringEndsIn(String s, String t) {
		return s.endsWith(t);
	}

	/**************************************************************
	 * returns |a| if b is positive; else fsign returns -|a| *
	 **************************************************************/
	double svd_fsign(double a, double b) {
		if ((a >= 0.0 && b >= 0.0) || (a < 0.0 && b < 0.0))
			return a;
		else
			return -a;
	}

	/**************************************************************
	 * returns the larger of two double precision numbers *
	 **************************************************************/
	double svd_dmax(double a, double b) {
		return (a > b) ? a : b;
	}

	/**************************************************************
	 * returns the smaller of two double precision numbers *
	 **************************************************************/
	double svd_dmin(double a, double b) {
		return (a < b) ? a : b;
	}

	/**************************************************************
	 * returns the larger of two integers *
	 **************************************************************/
	long svd_imax(long a, long b) {
		return (a > b) ? a : b;
	}

	/**************************************************************
	 * returns the smaller of two integers *
	 **************************************************************/
	long svd_imin(long a, long b) {
		return (a < b) ? a : b;
	}

	/**************************************************************
	 * Function scales a vector by a constant. * Based on Fortran-77 routine
	 * from Linpack by J. Dongarra *
	 **************************************************************/
	void svd_dscal(long n, double da, double[] dx, long incx) {
		throw null;
		// long i;
		//	  
		// if (n <= 0 || incx == 0) return;
		// if (incx < 0) dx += (-n+1) * incx;
		// for (i=0; i < n; i++) {
		// *dx *= da;
		// dx += incx;
		// }
		// return;
	}

	/**************************************************************
	 * function scales a vector by a constant. * Based on Fortran-77 routine
	 * from Linpack by J. Dongarra *
	 **************************************************************/
	void svd_datx(long n, double da, double[] dx, long incx, double[] dy,
			long incy) {
		throw null;
		// long i;
		//	  
		// if (n <= 0 || incx == 0 || incy == 0 || da == 0.0) return;
		// if (incx == 1 && incy == 1)
		// for (i=0; i < n; i++) *dy++ = da * (*dx++);
		//	  
		// else {
		// if (incx < 0) dx += (-n+1) * incx;
		// if (incy < 0) dy += (-n+1) * incy;
		// for (i=0; i < n; i++) {
		// *dy = da * (*dx);
		// dx += incx;
		// dy += incy;
		// }
		// }
		// return;
	}

	/**************************************************************
	 * Function copies a vector x to a vector y * Based on Fortran-77 routine
	 * from Linpack by J. Dongarra *
	 **************************************************************/
	void svd_dcopy(long n, double[] dx, long incx, double[] dy, long incy) {
		throw null;
		// long i;
		//	  
		// if (n <= 0 || incx == 0 || incy == 0) return;
		// if (incx == 1 && incy == 1)
		// for (i=0; i < n; i++) *dy++ = *dx++;
		//	  
		// else {
		// if (incx < 0) dx += (-n+1) * incx;
		// if (incy < 0) dy += (-n+1) * incy;
		// for (i=0; i < n; i++) {
		// *dy = *dx;
		// dx += incx;
		// dy += incy;
		// }
		// }
		// return;
	}

	/**************************************************************
	 * Function forms the dot product of two vectors. * Based on Fortran-77
	 * routine from Linpack by J. Dongarra *
	 **************************************************************/
	double svd_ddot(long n, double[] dx, long incx, double[] dy, long incy) {
		throw null;
		// long i;
		// double dot_product;
		//	  
		// if (n <= 0 || incx == 0 || incy == 0) return(0.0);
		// dot_product = 0.0;
		// if (incx == 1 && incy == 1)
		// for (i=0; i < n; i++) dot_product += (*dx++) * (*dy++);
		// else {
		// if (incx < 0) dx += (-n+1) * incx;
		// if (incy < 0) dy += (-n+1) * incy;
		// for (i=0; i < n; i++) {
		// dot_product += (*dx) * (*dy);
		// dx += incx;
		// dy += incy;
		// }
		// }
		// return(dot_product);
	}

	/**************************************************************
	 * Constant times a vector plus a vector * Based on Fortran-77 routine from
	 * Linpack by J. Dongarra *
	 **************************************************************/
	void svd_daxpy(long n, double da, double[] dx, long incx, double[] dy,
			long incy) {
		throw null;
		// long i;
		//	  
		// if (n <= 0 || incx == 0 || incy == 0 || da == 0.0) return;
		// if (incx == 1 && incy == 1)
		// for (i=0; i < n; i++) {
		// *dy += da * (*dx++);
		// dy++;
		// }
		// else {
		// if (incx < 0) dx += (-n+1) * incx;
		// if (incy < 0) dy += (-n+1) * incy;
		// for (i=0; i < n; i++) {
		// *dy += da * (*dx);
		// dx += incx;
		// dy += incy;
		// }
		// }
		// return;
	}

	/*********************************************************************
	 * Function sorts array1 and array2 into increasing order for array1 *
	 *********************************************************************/
	void svd_dsort2(long igap, long n, double[] array1, double[] array2) {
		throw null;
		// double temp;
		// long i, j, index;
		//	  
		// if (!igap) return;
		// else {
		// for (i = igap; i < n; i++) {
		// j = i - igap;
		// index = i;
		// while (j >= 0 && array1[j] > array1[index]) {
		// temp = array1[j];
		// array1[j] = array1[index];
		// array1[index] = temp;
		// temp = array2[j];
		// array2[j] = array2[index];
		// array2[index] = temp;
		// j -= igap;
		// index = j + igap;
		// }
		// }
		// }
		// svd_dsort2(igap/2,n,array1,array2);
	}

	/**************************************************************
	 * Function interchanges two vectors * Based on Fortran-77 routine from
	 * Linpack by J. Dongarra *
	 **************************************************************/
	void svd_dswap(long n, double[] dx, long incx, double[] dy, long incy) {
		throw null;
		// long i;
		// double dtemp;
		//	  
		// if (n <= 0 || incx == 0 || incy == 0) return;
		// if (incx == 1 && incy == 1) {
		// for (i=0; i < n; i++) {
		// dtemp = *dy;
		// *dy++ = *dx;
		// *dx++ = dtemp;
		// }
		// }
		// else {
		// if (incx < 0) dx += (-n+1) * incx;
		// if (incy < 0) dy += (-n+1) * incy;
		// for (i=0; i < n; i++) {
		// dtemp = *dy;
		// *dy = *dx;
		// *dx = dtemp;
		// dx += incx;
		// dy += incy;
		// }
		// }
	}

	/*****************************************************************
	 * Function finds the index of element having max. absolute value* based on
	 * FORTRAN 77 routine from Linpack by J. Dongarra *
	 *****************************************************************/
	long svd_idamax(long n, double[] dx, long incx) {
		throw null;
		// long ix,i,imax;
		// double dtemp, dmax;
		//	  
		// if (n < 1) return(-1);
		// if (n == 1) return(0);
		// if (incx == 0) return(-1);
		//	  
		// if (incx < 0) ix = (-n+1) * incx;
		// else ix = 0;
		// imax = ix;
		// dx += ix;
		// dmax = fabs(*dx);
		// for (i=1; i < n; i++) {
		// ix += incx;
		// dx += incx;
		// dtemp = fabs(*dx);
		// if (dtemp > dmax) {
		// dmax = dtemp;
		// imax = ix;
		// }
		// }
		// return(imax);
	}

	/* Row-major dense matrix. Rows are consecutive vectors. */
	class DMat {
		int rows;
		int cols;
		double[][] value; /*
						 * Accessed by [row][col]. Free value[0] and value to
						 * free.
						 */

		DMat(int rows, int cols) {
			int i;
			this.rows = rows;
			this.cols = cols;

			this.value = new double[rows][cols];
		}
	}

	/* Harwell-Boeing sparse matrix. */
	class SMat {

		int rows;
		int cols;
		int vals; /* Total non-zero entries. */
		int[] pointr; /* For each col (plus 1), index of first non-zero entry. */
		int[] rowind; /* For each nz entry, the row index. */
		double[] value; /* For each nz entry, the value. */

		SMat(int rows, int cols, int vals) {
			this.rows = rows;
			this.cols = cols;
			this.vals = vals;
			this.pointr = new int[cols + 1];
			this.rowind = new int[vals];
			this.value = new double[vals];
		}
	}

	class SVDRec {
		int d; /* Dimensionality (rank) */
		DMat Ut; /*
				 * Transpose of left singular vectors. (d by m) The vectors are
				 * the rows of Ut.
				 */
		double[] S; /* Array of singular values. (length d) */
		DMat Vt; /*
				 * Transpose of right singular vectors. (d by n) The vectors are
				 * the rows of Vt.
				 */

		SVDRec() {
		}

	}

	/**************************************************************
	 * multiplication of matrix B by vector x, where B = A'A, * and A is nrow by
	 * ncol (nrow >> ncol). Hence, B is of order * n = ncol (y stores product
	 * vector). *
	 **************************************************************/
	void svd_opb(SMat A, double[] x, double[] y, double[] temp) {
		throw null;
		// long i, j, end;
		// long *pointr = A->pointr, *rowind = A->rowind;
		// double *value = A->value;
		// long n = A->cols;
		//
		// SVDCount[SVD_MXV] += 2;
		// memset(y, 0, n * sizeof(double));
		// for (i = 0; i < A->rows; i++) temp[i] = 0.0;
		//	  
		// for (i = 0; i < A->cols; i++) {
		// end = pointr[i+1];
		// for (j = pointr[i]; j < end; j++)
		// temp[rowind[j]] += value[j] * (*x);
		// x++;
		// }
		// for (i = 0; i < A->cols; i++) {
		// end = pointr[i+1];
		// for (j = pointr[i]; j < end; j++)
		// *y += value[j] * temp[rowind[j]];
		// y++;
		// }
		// return;
	}

	/***********************************************************
	 * multiplication of matrix A by vector x, where A is * nrow by ncol (nrow
	 * >> ncol). y stores product vector. *
	 ***********************************************************/
	void svd_opa(SMat A, double[] x, double[] y) {
		throw null;
		// long end, i, j;
		// long *pointr = A->pointr, *rowind = A->rowind;
		// double *value = A->value;
		//	   
		// SVDCount[SVD_MXV]++;
		// memset(y, 0, A->rows * sizeof(double));
		//	  
		// for (i = 0; i < A->cols; i++) {
		// end = pointr[i+1];
		// for (j = pointr[i]; j < end; j++)
		// y[rowind[j]] += value[j] * x[i];
		// }
		// return;
	}

	/***********************************************************************
	 * * random() * (double precision) *
	 ***********************************************************************/
	/***********************************************************************
	 * Description -----------
	 * 
	 * This is a translation of a Fortran-77 uniform random number generator.
	 * The code is based on theory and suggestions given in D. E. Knuth (1969),
	 * vol 2. The argument to the function should be initialized to an arbitrary
	 * integer prior to the first call to random. The calling program should not
	 * alter the value of the argument between subsequent calls to random.
	 * Random returns values within the interval (0,1).
	 * 
	 * 
	 * Arguments ---------
	 * 
	 * (input) iy an integer seed whose value must not be altered by the caller
	 * between subsequent calls
	 * 
	 * (output) random a double precision random number between (0,1)
	 ***********************************************************************/
	double svd_random2(long[] iy) {
		throw null;
		// static long m2 = 0;
		// static long ia, ic, mic;
		// static double halfm, s;
		//
		// /* If first entry, compute (max int) / 2 */
		// if (!m2) {
		// m2 = 1 << (8 * (int)sizeof(int) - 2);
		// halfm = m2;
		//
		// /* compute multiplier and increment for linear congruential
		// * method */
		// ia = 8 * (long)(halfm * atan(1.0) / 8.0) + 5;
		// ic = 2 * (long)(halfm * (0.5 - sqrt(3.0)/6.0)) + 1;
		// mic = (m2-ic) + m2;
		//
		// /* s is the scale factor for converting to floating point */
		// s = 0.5 / halfm;
		// }
		//
		// /* compute next random number */
		// *iy = *iy * ia;
		//
		// /* for computers which do not allow integer overflow on addition */
		// if (*iy > mic) *iy = (*iy - m2) - m2;
		//
		// *iy = *iy + ic;
		//
		// /* for computers whose word length for addition is greater than
		// * for multiplication */
		// if (*iy / 2 > m2) *iy = (*iy - m2) - m2;
		//	  
		// /* for computers whose integer overflow affects the sign bit */
		// if (*iy < 0) *iy = (*iy + m2) + m2;
		//
		// return((double)(*iy) * s);
	}

	/**************************************************************
	 * * Function finds sqrt(a^2 + b^2) without overflow or * destructive
	 * underflow. * *
	 **************************************************************/
	/**************************************************************
	 * Funtions used -------------
	 * 
	 * UTILITY dmax, dmin
	 **************************************************************/
	double svd_pythag(double a, double b) {
		double p, r, s, t, u, temp;

		p = svd_dmax(Math.abs(a), Math.abs(b));
		if (p != 0.0) {
			temp = svd_dmin(Math.abs(a), Math.abs(b)) / p;
			r = temp * temp;
			t = 4.0 + r;
			while (t != 4.0) {
				s = r / t;
				u = 1.0 + 2.0 * s;
				p *= u;
				temp = s / u;
				r *= temp * temp;
				t = 4.0 + r;
			}
		}
		return p;
	}

	String SVDVersion = "1.34";
	long SVDVerbosity = 1;

	void svdResetCounters() {
		throw null;
	}

	/**************************** Conversion *************************************/

	/* Converts a sparse matrix to a dense one (without affecting the former) */
	DMat svdConvertStoD(SMat S) {
		throw null;
		// int i, c;
		// DMat D = svdNewDMat(S->rows, S->cols);
		// if (!D) {
		// svd_error("svdConvertStoD: failed to allocate D");
		// return NULL;
		// }
		// for (i = 0, c = 0; i < S->vals; i++) {
		// while (S->pointr[c + 1] <= i) c++;
		// D->value[S->rowind[i]][c] = S->value[i];
		// }
		// return D;
	}

	/* Converts a dense matrix to a sparse one (without affecting the dense one) */
	SMat svdConvertDtoS(DMat D) {
		throw null;
		// SMat S;
		// int i, j, n;
		// for (i = 0, n = 0; i < D->rows; i++)
		// for (j = 0; j < D->cols; j++)
		// if (D->value[i][j] != 0) n++;
		//	  
		// S = svdNewSMat(D->rows, D->cols, n);
		// if (!S) {
		// svd_error("svdConvertDtoS: failed to allocate S");
		// return NULL;
		// }
		// for (j = 0, n = 0; j < D->cols; j++) {
		// S->pointr[j] = n;
		// for (i = 0; i < D->rows; i++)
		// if (D->value[i][j] != 0) {
		// S->rowind[n] = i;
		// S->value[n] = D->value[i][j];
		// n++;
		// }
		// }
		// S->pointr[S->cols] = S->vals;
		// return S;
	}

	/* Transposes a dense matrix. */
	DMat svdTransposeD(DMat D) {
		int r, c;
		DMat N = new DMat(D.cols, D.rows);
		for (r = 0; r < D.rows; r++)
			for (c = 0; c < D.cols; c++)
				N.value[c][r] = D.value[r][c];
		return N;
	}

	/* Efficiently transposes a sparse matrix. */
	SMat svdTransposeS(SMat S) {
		int r, c, i, j;
		SMat N = new SMat(S.cols, S.rows, S.vals);
		/* Count number nz in each row. */
		for (i = 0; i < S.vals; i++)
			N.pointr[S.rowind[i]]++;
		/* Fill each cell with the starting point of the previous row. */
		N.pointr[S.rows] = S.vals - N.pointr[S.rows - 1];
		for (r = S.rows - 1; r > 0; r--)
			N.pointr[r] = N.pointr[r + 1] - N.pointr[r - 1];
		N.pointr[0] = 0;
		/* Assign the new columns and values. */
		for (c = 0, i = 0; c < S.cols; c++) {
			for (; i < S.pointr[c + 1]; i++) {
				r = S.rowind[i];
				j = N.pointr[r + 1]++;
				N.rowind[j] = c;
				N.value[j] = S.value[i];
			}
		}
		return N;
	}
	
	/*************************************************************************
    (c) Copyright 2003
       Douglas Rohde

adapted from SVDPACKC, which is

    (c) Copyright 1993
 University of Tennessee
   All Rights Reserved                          
*************************************************************************/

static int MAXLL = 2;

enum storeVals {STORQ, RETRQ, STORP, RETRP};

static String[] error_msg = {  /* error messages used by function    *
        * check_parameters                   */
null,
"",
"ENDL MUST BE LESS THAN ENDR",
"REQUESTED DIMENSIONS CANNOT EXCEED NUM ITERATIONS",
"ONE OF YOUR DIMENSIONS IS LESS THAN OR EQUAL TO ZERO",
"NUM ITERATIONS (NUMBER OF LANCZOS STEPS) IS INVALID",
"REQUESTED DIMENSIONS (NUMBER OF EIGENPAIRS DESIRED) IS INVALID",
"6*N+4*ITERATIONS+1 + ITERATIONS*ITERATIONS CANNOT EXCEED NW",
"6*N+4*ITERATIONS+1 CANNOT EXCEED NW", null};

double[][] LanStore;
double[] OPBTemp;
double eps, eps1, reps, eps34;
long ierr;
/*
double rnm, anorm, tol;
FILE *fp_out1, *fp_out2;
*/

/***********************************************************************
*                                                                     *
*                        main()                                       *
* Sparse SVD(A) via Eigensystem of A'A symmetric Matrix 	       *
*                  (double precision)                                 *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

This sample program uses landr to compute singular triplets of A via
the equivalent symmetric eigenvalue problem                         

B x = lambda x, where x' = (u',v'), lambda = sigma**2,
where sigma is a singular value of A,
                                              
B = A'A , and A is m (nrow) by n (ncol) (nrow >> ncol),                
                                          
so that {u,sqrt(lambda),v} is a singular triplet of A.        
(A' = transpose of A)                                      
                                     
User supplied routines: svd_opa, opb, store, timer              
                                 
svd_opa(     x,y) takes an n-vector x and returns A*x in y.
svd_opb(ncol,x,y) takes an n-vector x and returns B*x in y.
                                           
Based on operation flag isw, store(n,isw,j,s) stores/retrieves 
to/from storage a vector of length n in s.                   
                                        
User should edit timer() with an appropriate call to an intrinsic
timing routine that returns elapsed user time.                      


External parameters 
-------------------

Defined and documented in las2.h


Local parameters 
----------------

(input)
endl     left end of interval containing unwanted eigenvalues of B
endr     right end of interval containing unwanted eigenvalues of B
kappa    relative accuracy of ritz values acceptable as eigenvalues
of B
vectors is not equal to 1
r        work array
n	    dimension of the eigenproblem for matrix B (ncol)
dimensions   upper limit of desired number of singular triplets of A
iterations   upper limit of desired number of Lanczos steps
nnzero   number of nonzeros in A
vectors  1 indicates both singular values and singular vectors are 
wanted and they can be found in output file lav2;
0 indicates only singular values are wanted 

(output)
ritz	    array of ritz values
bnd      array of error bounds
d        array of singular values
memory   total memory allocated in bytes to solve the B-eigenproblem


Functions used
--------------

BLAS		svd_daxpy, svd_dscal, svd_ddot
USER		svd_opa, svd_opb, timer
MISC		write_header, check_parameters
LAS2		landr


Precision
---------

All floating-point calculations are done in double precision;
variables are declared as long and double.


LAS2 development
----------------

LAS2 is a C translation of the Fortran-77 LAS2 from the SVDPACK
library written by Michael W. Berry, University of Tennessee,
Dept. of Computer Science, 107 Ayres Hall, Knoxville, TN, 37996-1301

31 Jan 1992:  Date written 

Theresa H. Do
University of Tennessee
Dept. of Computer Science
107 Ayres Hall
Knoxville, TN, 37996-1301
internet: tdo@cs.utk.edu

***********************************************************************/

/***********************************************************************
*								       *
*		      check_parameters()			       *
*								       *
***********************************************************************/
/***********************************************************************

Description
-----------
Function validates input parameters and returns error code (long)  

Parameters 
----------
(input)
dimensions   upper limit of desired number of eigenpairs of B           
iterations   upper limit of desired number of lanczos steps             
n        dimension of the eigenproblem for matrix B               
endl     left end of interval containing unwanted eigenvalues of B
endr     right end of interval containing unwanted eigenvalues of B
vectors  1 indicates both eigenvalues and eigenvectors are wanted 
and they can be found in lav2; 0 indicates eigenvalues only
nnzero   number of nonzero elements in input matrix (matrix A)      
                                               
***********************************************************************/

long check_parameters(SMat A, long dimensions, long iterations, 
		double endl, double endr, long vectors) {
	long error_index;
	error_index = 0;

	if (endl >/*=*/ endr)  error_index = 2;
	else if (dimensions > iterations) error_index = 3;
	else if (A.cols <= 0 || A.rows <= 0) error_index = 4;
	/*else if (n > A->cols || n > A->rows) error_index = 1;*/
	else if (iterations <= 0 || iterations > A.cols || iterations > A.rows)
		error_index = 5;
	else if (dimensions <= 0 || dimensions > iterations) error_index = 6;
	if (error_index) 
		svd_error("svdLAS2 parameter error: %s\n", error_msg[error_index]);
	return(error_index);
}

/***********************************************************************
*								       *
*			  write_header()			       *
*   Function writes out header of output file containing ritz values  *
*								       *
***********************************************************************/

void write_header(long iterations, long dimensions, double endl, double endr, 
		long vectors, double kappa, long nrow, long ncol, 
		long vals) {
	printf("SOLVING THE [A^TA] EIGENPROBLEM\n");
	printf("NO. OF ROWS               = %6ld\n", nrow);
	printf("NO. OF COLUMNS            = %6ld\n", ncol);
	printf("NO. OF NON-ZERO VALUES    = %6ld\n", vals);
	printf("MATRIX DENSITY            = %6.2f%%\n", 
			((float) vals / nrow) * 100 / ncol);
	/* printf("ORDER OF MATRIX A         = %5ld\n", n); */
	printf("MAX. NO. OF LANCZOS STEPS = %6ld\n", iterations);
	printf("MAX. NO. OF EIGENPAIRS    = %6ld\n", dimensions);
	printf("LEFT  END OF THE INTERVAL = %9.2E\n", endl);
	printf("RIGHT END OF THE INTERVAL = %9.2E\n", endr);
	printf("KAPPA                     = %9.2E\n", kappa);
	/* printf("WANT S-VECTORS?   [T/F]   =     %c\n", (vectors) ? 'T' : 'F'); */
	printf("\n");
	return;
}

void printf(String fmt, Object ... args) {
	System.out.printf(fmt, args);
}


/***********************************************************************
*                                                                     *
*				landr()				       *
*        Lanczos algorithm with selective orthogonalization           *
*                    Using Simon's Recurrence                         *
*                       (double precision)                            *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

landr() is the LAS2 driver routine that, upon entry,
(1)  checks for the validity of input parameters of the 
B-eigenproblem 
(2)  determines several machine constants
(3)  makes a Lanczos run
(4)  calculates B-eigenvectors (singular vectors of A) if requested 
by user


arguments
---------

(input)
n        dimension of the eigenproblem for A'A
iterations   upper limit of desired number of Lanczos steps
dimensions   upper limit of desired number of eigenpairs
nnzero   number of nonzeros in matrix A
endl     left end of interval containing unwanted eigenvalues of B
endr     right end of interval containing unwanted eigenvalues of B
vectors  1 indicates both eigenvalues and eigenvectors are wanted
and they can be found in output file lav2; 
0 indicates only eigenvalues are wanted
kappa    relative accuracy of ritz values acceptable as eigenvalues
of B (singular values of A)
r        work array

(output)
j        number of Lanczos steps actually taken                     
neig     number of ritz values stabilized                           
ritz     array to hold the ritz values                              
bnd      array to hold the error bounds


External parameters
-------------------

Defined and documented in las2.h


local parameters
-------------------

ibeta    radix for the floating-point representation
it       number of base ibeta digits in the floating-point significand
irnd     floating-point addition rounded or chopped
machep   machine relative precision or round-off error
negeps   largest negative integer
wptr	    array of pointers each pointing to a work space


Functions used
--------------

MISC         svd_dmax, machar, check_parameters
LAS2         ritvec, lanso

***********************************************************************/

void fake_memset_127(double[] a) {
	double d = Double.longBitsToDouble(0x7f7f7f7f7f7f7f7fL);
	for (int n = 0; n < a.length; a++) {
		a[n] = d;
	}
}

SVDRec svdLAS2A(SMat A, long dimensions) {
	double end[2] = {-1.0e-30, 1.0e-30};
	double kappa = 1e-6;
	if (A == null) {
		svd_error("svdLAS2A called with NULL array\n");
		return null;
	}
	return svdLAS2(A, dimensions, 0, end, kappa);
}

SVDRec svdLAS2(SMat A, long dimensions, long iterations, double[] end, 
		double kappa) {
	char transpose = FALSE;
	long ibeta, it, irnd, machep, negep, n, i, steps, nsig, neig, m;
	double[][] wptr = new double[10][];
	double[] ritz;
	double[] bnd;
	SVDRec R = null;

	svdResetCounters();

	m = svd_imin(A.rows, A.cols);
	if (dimensions <= 0 || dimensions > m)
		dimensions = m;
	if (iterations <= 0 || iterations > m)
		iterations = m;
	if (iterations < dimensions) iterations = dimensions;

	/* Write output header */
	if (SVDVerbosity > 0)
		write_header(iterations, dimensions, end[0], end[1], true, kappa, A.rows, 
				A.cols, A.vals);

	/* Check parameters */
	if (check_parameters(A, dimensions, iterations, end[0], end[1], true))
		return null;

	/* If A is wide, the SVD is computed on its transpose for speed. */
	if (A.cols >= A.rows * 1.2) {
		if (SVDVerbosity > 0) printf("TRANSPOSING THE MATRIX FOR SPEED\n");
		transpose = true;
		A = svdTransposeS(A);
	}

	n = A.cols;

	/* BEGIN Compute machine precision */ 
	long[] machar_result = machar(/* &ibeta, &it, &irnd, &machep, &negep */);
	ibeta = machar_result[0];
	it = machar_result[1];
	irnd = machar_result[2];
	machep = machar_result[3];
	negep = machar_result[4];
	/* END Compute machine precision */
	
	eps1 = eps * Math.sqrt((double) n);
	reps = Math.sqrt(eps);
	eps34 = reps * Math.sqrt(reps);

	/* Allocate temporary space. */
	wptr[0] = new double[n];
	wptr[1] = new double[n];
	wptr[2] = new double[n];
	wptr[3] = new double[n];
	wptr[4] = new double[n];
	wptr[5] = new double[n];
	wptr[6] = new double[iterations];
	wptr[7] = new double[iterations];
	wptr[8] = new double[iterations];
	wptr[9] = new double[iterations + 1];

	ritz = new double[iterations + 1];
	bnd = new double[iterations + 1]
	fake_memset_127(bnd);

	LanStore = new double[iterations + MAXLL][];
	OPBTemp = svd_doubleArray(A.rows, false, "las2: OPBTemp");

	/* Actually run the lanczos thing: */
	double[] ref_neig = new double[] { neig }; // XXX wrap neig
	steps = lanso(A, iterations, dimensions, end[0], end[1], ritz, bnd, wptr, 
			ref_neig, n);
	neig = ref_neig[0]; // XXX unwrap neig
	
	/* Print some stuff. */
	if (SVDVerbosity > 0) {
		printf("NUMBER OF LANCZOS STEPS   = %6ld\n"
				"RITZ VALUES STABILIZED    = %6ld\n", steps + 1, neig);
	}
	if (SVDVerbosity > 2) {
		printf("\nCOMPUTED RITZ VALUES  (ERROR BNDS)\n");
		for (i = 0; i <= steps; i++)
			printf("%3ld  %22.14E  (%11.2E)\n", i + 1, ritz[i], bnd[i]);
	}

	wptr[0] = null;
	wptr[1] = null;
	wptr[2] = null;
	wptr[3] = null;
	wptr[4] = null;
	wptr[7] = null;
	wptr[8] = null;

	/* Compute eigenvectors */
	kappa = svd_dmax(fabs(kappa), eps34);

	R = new SVDRec();
	R.d  = /*svd_imin(nsig, dimensions)*/dimensions;
	R.Ut = new DMat(R.d, A.rows);
	R.S  = svd_doubleArray(R.d, true, "las2: R->s");
	R.Vt = new NewDMat(R.d, A.cols);

	nsig = ritvec(n, A, R, kappa, ritz, bnd, wptr[6], wptr[9], wptr[5], steps, 
			neig);

	if (SVDVerbosity > 1) {
		printf("\nSINGULAR VALUES: ");
		svdWriteDenseArray(R.S, R.d, "-", false);

		if (SVDVerbosity > 2) {
			printf("\nLEFT SINGULAR VECTORS (transpose of U): ");
			svdWriteDenseMatrix(R.Ut, "-", SVD_F_DT);

			printf("\nRIGHT SINGULAR VECTORS (transpose of V): ");
			svdWriteDenseMatrix(R.Vt, "-", SVD_F_DT);
		}
	} else if (SVDVerbosity > 0)
		printf("SINGULAR VALUES FOUND     = %6d\n", R.d);

	/* This swaps and transposes the singular matrices if A was transposed. */
	if (transpose) {
		DMat swap = R.Ut;
		R.Ut = R.Vt;
		R.Vt = swap;
	}

	return R;
}


/***********************************************************************
*                                                                     *
*                        ritvec()                                     *
* 	    Function computes the singular vectors of matrix A	       *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

This function is invoked by landr() only if eigenvectors of the A'A
eigenproblem are desired.  When called, ritvec() computes the 
singular vectors of A and writes the result to an unformatted file.


Parameters
----------

(input)
nrow       number of rows of A
steps      number of Lanczos iterations performed
fp_out2    pointer to unformatted output file
n	      dimension of matrix A
kappa      relative accuracy of ritz values acceptable as 
eigenvalues of A'A
ritz       array of ritz values
bnd        array of error bounds
alf        array of diagonal elements of the tridiagonal matrix T
bet        array of off-diagonal elements of T
w1, w2     work space

(output)
xv1        array of eigenvectors of A'A (right singular vectors of A)
ierr	      error code
0 for normal return from imtql2()
k if convergence did not occur for k-th eigenvalue in
imtql2()
nsig       number of accepted ritz values based on kappa

(local)
s	      work array which is initialized to the identity matrix
of order (j + 1) upon calling imtql2().  After the call,
s contains the orthonormal eigenvectors of the symmetric 
tridiagonal matrix T

Functions used
--------------

BLAS		svd_dscal, svd_dcopy, svd_daxpy
USER		store
imtql2

***********************************************************************/

void rotateArray(double[] a, int size, int x) {
	int i, j, n, start;
	double t1, t2;
	if (x == 0) return;
	j = start = 0;
	t1 = a[0];
	for (i = 0; i < size; i++) {
		n = (j >= x) ? j - x : j + size - x;
		t2 = a[n];
		a[n] = t1;
		t1 = t2;
		j = n;
		if (j == start) {
			start = ++j;
			t1 = a[j];
		}
	}
}

long ritvec(long n, SMat A, SVDRec R, double kappa, double[] ritz, double[] bnd, 
		double[] alf, double[] bet, double[] w2, long steps, long neig) {
	long js, jsq, i, k, /*size,*/ id2, tmp, nsig, x;
	double[] s;
	double[] xv2;
	double tmp0, tmp1, xnorm;
	double[] w1 = R.Vt.value[0];

	js = steps + 1;
	jsq = js * js;
	/*size = sizeof(double) * n;*/

	s = svd_doubleArray(jsq, true, "ritvec: s");
	xv2 = svd_doubleArray(n, false, "ritvec: xv2");

	/* initialize s to an identity matrix */
	for (i = 0; i < jsq; i+= (js+1)) { 
		s[i] = 1.0;
	}
	svd_dcopy(js, alf, 1, w1, -1); // TODO
	svd_dcopy(steps, &bet[1], 1, &w2[1], -1); // TODO start dcopy at index 1 of both arrays

	/* on return from imtql2(), w1 contains eigenvalues in ascending 
	 * order and s contains the corresponding eigenvectors */
	imtql2(js, js, w1, w2, s);
	if (ierr) return 0; // TODO use exception here?

	/*fwrite((char *)&n, sizeof(n), 1, fp_out2);
fwrite((char *)&js, sizeof(js), 1, fp_out2);
fwrite((char *)&kappa, sizeof(kappa), 1, fp_out2);*/
	/*id = 0;*/
	nsig = 0;
	x = 0;
	id2 = jsq - js;
	for (k = 0; k < js; k++) {
		tmp = id2;
		if (bnd[k] <= kappa * Math.abs(ritz[k]) && k > js-neig-1) {
			if (--x < 0) x = R.d - 1;
			w1 = R.Vt.value[x];
			for (i = 0; i < n; i++) w1[i] = 0.0;
			for (i = 0; i < js; i++) {
				store(n, RETRQ, i, w2);
				svd_daxpy(n, s[tmp], w2, 1, w1, 1);
				tmp -= js;
			}
			/*fwrite((char *)w1, size, 1, fp_out2);*/

			/* store the w1 vector row-wise in array xv1;   
			 * size of xv1 is (steps+1) * (nrow+ncol) elements 
			 * and each vector, even though only ncol long,
			 * will have (nrow+ncol) elements in xv1.      
			 * It is as if xv1 is a 2-d array (steps+1) by     
			 * (nrow+ncol) and each vector occupies a row  */

			/* j is the index in the R arrays, which are sorted by high to low 
singular values. */

			/*for (i = 0; i < n; i++) R->Vt->value[x]xv1[id++] = w1[i];*/
			/*id += nrow;*/
			nsig++;
		}
		id2++;
	}
	s = null;

	/* Rotate the singular vectors and values. */
	/* x is now the location of the highest singular value. */
	rotateArray(R.Vt.value[0], R.Vt.rows * R.Vt.cols, 
			x * R.Vt.cols);
	R.d = svd_imin(R.d, nsig);
	for (x = 0; x < R.d; x++) {
		/* multiply by matrix B first */
		svd_opb(A, R.Vt.value[x], xv2, OPBTemp);
		tmp0 = svd_ddot(n, R.Vt.value[x], 1, xv2, 1);
		svd_daxpy(n, -tmp0, R.Vt.value[x], 1, xv2, 1);
		tmp0 = sqrt(tmp0);
		xnorm = sqrt(svd_ddot(n, xv2, 1, xv2, 1));

		/* multiply by matrix A to get (scaled) left s-vector */
		svd_opa(A, R.Vt.value[x], R.Ut.value[x]);
		tmp1 = 1.0 / tmp0;
		svd_dscal(A.rows, tmp1, R.Ut.value[x], 1);
		xnorm *= tmp1;
		bnd[i] = xnorm;
		R.S[x] = tmp0;
	}

	xv2 = null;
	return nsig;
}

/***********************************************************************
*                                                                     *
*                          lanso()                                    *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

Function determines when the restart of the Lanczos algorithm should 
occur and when it should terminate.

Arguments 
---------

(input)
n         dimension of the eigenproblem for matrix B
iterations    upper limit of desired number of lanczos steps           
dimensions    upper limit of desired number of eigenpairs             
endl      left end of interval containing unwanted eigenvalues
endr      right end of interval containing unwanted eigenvalues
ritz      array to hold the ritz values                       
bnd       array to hold the error bounds                          
wptr      array of pointers that point to work space:            
wptr[0]-wptr[5]  six vectors of length n		
wptr[6] array to hold diagonal of the tridiagonal matrix T
wptr[9] array to hold off-diagonal of T	
wptr[7] orthogonality estimate of Lanczos vectors at 
step j
wptr[8] orthogonality estimate of Lanczos vectors at 
step j-1

(output)
j         number of Lanczos steps actually taken
neig      number of ritz values stabilized
ritz      array to hold the ritz values
bnd       array to hold the error bounds
ierr      (globally declared) error flag
ierr = 8192 if stpone() fails to find a starting vector
ierr = k if convergence did not occur for k-th eigenvalue
in imtqlb()
ierr = 0 otherwise


Functions used
--------------

LAS		stpone, error_bound, lanczos_step
MISC		svd_dsort2
UTILITY	svd_imin, svd_imax

***********************************************************************/

int lanso(SMat A, long iterations, long dimensions, double endl,
double endr, double *ritz, double *bnd, double *wptr[], 
long *neigp, long n) {
double *alf, *eta, *oldeta, *bet, *wrk, rnm, tol;
long ll, first, last, ENOUGH, id2, id3, i, l, neig, j = 0, intro = 0;

alf = wptr[6];
eta = wptr[7];
oldeta = wptr[8];
bet = wptr[9];
wrk = wptr[5];

/* take the first step */
stpone(A, wptr, &rnm, &tol, n);
if (!rnm || ierr) return 0;
eta[0] = eps1;
oldeta[0] = eps1;
ll = 0;
first = 1;
last = svd_imin(dimensions + svd_imax(8, dimensions), iterations);
ENOUGH = FALSE;
/*id1 = 0;*/
while (/*id1 < dimensions && */!ENOUGH) {
if (rnm <= tol) rnm = 0.0;

/* the actual lanczos loop */
j = lanczos_step(A, first, last, wptr, alf, eta, oldeta, bet, &ll,
&ENOUGH, &rnm, &tol, n);
if (ENOUGH) j = j - 1;
else j = last - 1;
first = j + 1;
bet[j+1] = rnm;

/* analyze T */
l = 0;
for (id2 = 0; id2 < j; id2++) {
if (l > j) break;
for (i = l; i <= j; i++) if (!bet[i+1]) break;
if (i > j) i = j;

/* now i is at the end of an unreduced submatrix */
svd_dcopy(i-l+1, &alf[l],   1, &ritz[l],  -1);
svd_dcopy(i-l,   &bet[l+1], 1, &wrk[l+1], -1);

imtqlb(i-l+1, &ritz[l], &wrk[l], &bnd[l]);

if (ierr) {
svd_error("svdLAS2: imtqlb failed to converge (ierr = %ld)\n", ierr);
svd_error("  l = %ld  i = %ld\n", l, i);
for (id3 = l; id3 <= i; id3++) 
svd_error("  %ld  %lg  %lg  %lg\n", 
id3, ritz[id3], wrk[id3], bnd[id3]);
}
for (id3 = l; id3 <= i; id3++) 
bnd[id3] = rnm * fabs(bnd[id3]);
l = i + 1;
}

/* sort eigenvalues into increasing order */
svd_dsort2((j+1) / 2, j + 1, ritz, bnd);

/*    for (i = 0; i < iterations; i++)
printf("%f ", ritz[i]);
printf("\n"); */

/* massage error bounds for very close ritz values */
neig = error_bound(&ENOUGH, endl, endr, ritz, bnd, j, tol);
*neigp = neig;

/* should we stop? */
if (neig < dimensions) {
if (!neig) {
last = first + 9;
intro = first;
} else last = first + svd_imax(3, 1 + ((j - intro) * (dimensions-neig)) /
              neig);
last = svd_imin(last, iterations);
} else ENOUGH = TRUE;
ENOUGH = ENOUGH || first >= iterations;
/* id1++; */
/* printf("id1=%d dimen=%d first=%d\n", id1, dimensions, first); */
}
store(n, STORQ, j, wptr[1]);
return j;
}


/***********************************************************************
*                                                                     *
*			lanczos_step()                                 *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

Function embodies a single Lanczos step

Arguments 
---------

(input)
n        dimension of the eigenproblem for matrix B
first    start of index through loop				      
last     end of index through loop				     
wptr	    array of pointers pointing to work space		    
alf	    array to hold diagonal of the tridiagonal matrix T
eta      orthogonality estimate of Lanczos vectors at step j   
oldeta   orthogonality estimate of Lanczos vectors at step j-1
bet      array to hold off-diagonal of T                     
ll       number of intitial Lanczos vectors in local orthog. 
(has value of 0, 1 or 2)			
enough   stop flag			

Functions used
--------------

BLAS		svd_ddot, svd_dscal, svd_daxpy, svd_datx, svd_dcopy
USER		store
LAS		purge, ortbnd, startv
UTILITY	svd_imin, svd_imax

***********************************************************************/

long lanczos_step(SMat A, long first, long last, double *wptr[],
double *alf, double *eta, double *oldeta,
double *bet, long *ll, long *enough, double *rnmp, 
double *tolp, long n) {
double t, *mid, rnm = *rnmp, tol = *tolp, anorm;
long i, j;

for (j=first; j<last; j++) {
mid     = wptr[2];
wptr[2] = wptr[1];
wptr[1] = mid;
mid     = wptr[3];
wptr[3] = wptr[4];
wptr[4] = mid;

store(n, STORQ, j-1, wptr[2]);
if (j-1 < MAXLL) store(n, STORP, j-1, wptr[4]);
bet[j] = rnm;

/* restart if invariant subspace is found */
if (!bet[j]) {
rnm = startv(A, wptr, j, n);
if (ierr) return j;
if (!rnm) *enough = TRUE;
}
if (*enough) {
/* added by Doug... */
/* These lines fix a bug that occurs with low-rank matrices */
mid     = wptr[2];
wptr[2] = wptr[1];
wptr[1] = mid;
/* ...added by Doug */
break;
}

/* take a lanczos step */
t = 1.0 / rnm;
svd_datx(n, t, wptr[0], 1, wptr[1], 1);
svd_dscal(n, t, wptr[3], 1);
svd_opb(A, wptr[3], wptr[0], OPBTemp);
svd_daxpy(n, -rnm, wptr[2], 1, wptr[0], 1);
alf[j] = svd_ddot(n, wptr[0], 1, wptr[3], 1);
svd_daxpy(n, -alf[j], wptr[1], 1, wptr[0], 1);

/* orthogonalize against initial lanczos vectors */
if (j <= MAXLL && (fabs(alf[j-1]) > 4.0 * fabs(alf[j])))
*ll = j;  
for (i=0; i < svd_imin(*ll, j-1); i++) {
store(n, RETRP, i, wptr[5]);
t = svd_ddot(n, wptr[5], 1, wptr[0], 1);
store(n, RETRQ, i, wptr[5]);
svd_daxpy(n, -t, wptr[5], 1, wptr[0], 1);
eta[i] = eps1;
oldeta[i] = eps1;
}

/* extended local reorthogonalization */
t = svd_ddot(n, wptr[0], 1, wptr[4], 1);
svd_daxpy(n, -t, wptr[2], 1, wptr[0], 1);
if (bet[j] > 0.0) bet[j] = bet[j] + t;
t = svd_ddot(n, wptr[0], 1, wptr[3], 1);
svd_daxpy(n, -t, wptr[1], 1, wptr[0], 1);
alf[j] = alf[j] + t;
svd_dcopy(n, wptr[0], 1, wptr[4], 1);
rnm = sqrt(svd_ddot(n, wptr[0], 1, wptr[4], 1));
anorm = bet[j] + fabs(alf[j]) + rnm;
tol = reps * anorm;

/* update the orthogonality bounds */
ortbnd(alf, eta, oldeta, bet, j, rnm);

/* restore the orthogonality state when needed */
purge(n, *ll, wptr[0], wptr[1], wptr[4], wptr[3], wptr[5], eta, oldeta,
j, &rnm, tol);
if (rnm <= tol) rnm = 0.0;
}
*rnmp = rnm;
*tolp = tol;
return j;
}

/***********************************************************************
*                                                                     *
*                          ortbnd()                                   *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

Funtion updates the eta recurrence

Arguments 
---------

(input)
alf      array to hold diagonal of the tridiagonal matrix T         
eta      orthogonality estimate of Lanczos vectors at step j        
oldeta   orthogonality estimate of Lanczos vectors at step j-1     
bet      array to hold off-diagonal of T                          
n        dimension of the eigenproblem for matrix B		    
j        dimension of T					  
rnm	    norm of the next residual vector			 
eps1	    roundoff estimate for dot product of two unit vectors

(output)
eta      orthogonality estimate of Lanczos vectors at step j+1     
oldeta   orthogonality estimate of Lanczos vectors at step j        


Functions used
--------------

BLAS		svd_dswap

***********************************************************************/

void ortbnd(double *alf, double *eta, double *oldeta, double *bet, long step,
double rnm) {
long i;
if (step < 1) return;
if (rnm) {
if (step > 1) {
oldeta[0] = (bet[1] * eta[1] + (alf[0]-alf[step]) * eta[0] -
bet[step] * oldeta[0]) / rnm + eps1;
}
for (i=1; i<=step-2; i++) 
oldeta[i] = (bet[i+1] * eta[i+1] + (alf[i]-alf[step]) * eta[i] +
bet[i] * eta[i-1] - bet[step] * oldeta[i])/rnm + eps1;
}
oldeta[step-1] = eps1;
svd_dswap(step, oldeta, 1, eta, 1);  
eta[step] = eps1;
return;
}

/***********************************************************************
*                                                                     *
*				purge()                                *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

Function examines the state of orthogonality between the new Lanczos
vector and the previous ones to decide whether re-orthogonalization 
should be performed


Arguments 
---------

(input)
n        dimension of the eigenproblem for matrix B		       
ll       number of intitial Lanczos vectors in local orthog.       
r        residual vector to become next Lanczos vector            
q        current Lanczos vector			           
ra       previous Lanczos vector
qa       previous Lanczos vector
wrk      temporary vector to hold the previous Lanczos vector
eta      state of orthogonality between r and prev. Lanczos vectors 
oldeta   state of orthogonality between q and prev. Lanczos vectors
j        current Lanczos step				     

(output)
r	    residual vector orthogonalized against previous Lanczos 
vectors
q        current Lanczos vector orthogonalized against previous ones


Functions used
--------------

BLAS		svd_daxpy,  svd_dcopy,  svd_idamax,  svd_ddot
USER		store

***********************************************************************/

void purge(long n, long ll, double *r, double *q, double *ra,  
double *qa, double *wrk, double *eta, double *oldeta, long step, 
double *rnmp, double tol) {
double t, tq, tr, reps1, rnm = *rnmp;
long k, iteration, flag, i;

if (step < ll+2) return; 

k = svd_idamax(step - (ll+1), &eta[ll], 1) + ll;
if (fabs(eta[k]) > reps) {
reps1 = eps1 / reps;
iteration = 0;
flag = TRUE;
while (iteration < 2 && flag) {
if (rnm > tol) {

/* bring in a lanczos vector t and orthogonalize both 
* r and q against it */
tq = 0.0;
tr = 0.0;
for (i = ll; i < step; i++) {
store(n,  RETRQ,  i,  wrk);
t   = -svd_ddot(n, qa, 1, wrk, 1);
tq += fabs(t);
svd_daxpy(n,  t,  wrk,  1,  q,  1);
t   = -svd_ddot(n, ra, 1, wrk, 1);
tr += fabs(t);
svd_daxpy(n, t, wrk, 1, r, 1);
}
svd_dcopy(n, q, 1, qa, 1);
t   = -svd_ddot(n, r, 1, qa, 1);
tr += fabs(t);
svd_daxpy(n, t, q, 1, r, 1);
svd_dcopy(n, r, 1, ra, 1);
rnm = sqrt(svd_ddot(n, ra, 1, r, 1));
if (tq <= reps1 && tr <= reps1 * rnm) flag = FALSE;
}
iteration++;
}
for (i = ll; i <= step; i++) { 
eta[i] = eps1;
oldeta[i] = eps1;
}
}
*rnmp = rnm;
return;
}


/***********************************************************************
*                                                                     *
*                         stpone()                                    *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

Function performs the first step of the Lanczos algorithm.  It also
does a step of extended local re-orthogonalization.

Arguments 
---------

(input)
n      dimension of the eigenproblem for matrix B

(output)
ierr   error flag
wptr   array of pointers that point to work space that contains
wptr[0]             r[j]
wptr[1]             q[j]
wptr[2]             q[j-1]
wptr[3]             p
wptr[4]             p[j-1]
wptr[6]             diagonal elements of matrix T 


Functions used
--------------

BLAS		svd_daxpy, svd_datx, svd_dcopy, svd_ddot, svd_dscal
USER		store, opb
LAS		startv

***********************************************************************/

void stpone(SMat A, double *wrkptr[], double *rnmp, double *tolp, long n) {
double t, *alf, rnm, anorm;
alf = wrkptr[6];

/* get initial vector; default is random */
rnm = startv(A, wrkptr, 0, n);
if (rnm == 0.0 || ierr != 0) return;

/* normalize starting vector */
t = 1.0 / rnm;
svd_datx(n, t, wrkptr[0], 1, wrkptr[1], 1);
svd_dscal(n, t, wrkptr[3], 1);

/* take the first step */
svd_opb(A, wrkptr[3], wrkptr[0], OPBTemp);
alf[0] = svd_ddot(n, wrkptr[0], 1, wrkptr[3], 1);
svd_daxpy(n, -alf[0], wrkptr[1], 1, wrkptr[0], 1);
t = svd_ddot(n, wrkptr[0], 1, wrkptr[3], 1);
svd_daxpy(n, -t, wrkptr[1], 1, wrkptr[0], 1);
alf[0] += t;
svd_dcopy(n, wrkptr[0], 1, wrkptr[4], 1);
rnm = sqrt(svd_ddot(n, wrkptr[0], 1, wrkptr[4], 1));
anorm = rnm + fabs(alf[0]);
*rnmp = rnm;
*tolp = reps * anorm;

return;
}

/***********************************************************************
*                                                                     *
*                         startv()                                    *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

Function delivers a starting vector in r and returns |r|; it returns 
zero if the range is spanned, and ierr is non-zero if no starting 
vector within range of operator can be found.

Parameters 
---------

(input)
n      dimension of the eigenproblem matrix B
wptr   array of pointers that point to work space
j      starting index for a Lanczos run
eps    machine epsilon (relative precision)

(output)
wptr   array of pointers that point to work space that contains
r[j], q[j], q[j-1], p[j], p[j-1]
ierr   error flag (nonzero if no starting vector can be found)

Functions used
--------------

BLAS		svd_ddot, svd_dcopy, svd_daxpy
USER		svd_opb, store
MISC		random

***********************************************************************/

double startv(SMat A, double *wptr[], long step, long n) {
double rnm2, *r, t;
long irand;
long id, i;

/* get initial vector; default is random */
rnm2 = svd_ddot(n, wptr[0], 1, wptr[0], 1);
irand = 918273 + step;
r = wptr[0];
for (id = 0; id < 3; id++) {
if (id > 0 || step > 0 || rnm2 == 0) 
for (i = 0; i < n; i++) r[i] = svd_random2(&irand);
svd_dcopy(n, wptr[0], 1, wptr[3], 1);

/* apply operator to put r in range (essential if m singular) */
svd_opb(A, wptr[3], wptr[0], OPBTemp);
svd_dcopy(n, wptr[0], 1, wptr[3], 1);
rnm2 = svd_ddot(n, wptr[0], 1, wptr[3], 1);
if (rnm2 > 0.0) break;
}

/* fatal error */
if (rnm2 <= 0.0) {
ierr = 8192;
return(-1);
}
if (step > 0) {
for (i = 0; i < step; i++) {
store(n, RETRQ, i, wptr[5]);
t = -svd_ddot(n, wptr[3], 1, wptr[5], 1);
svd_daxpy(n, t, wptr[5], 1, wptr[0], 1);
}

/* make sure q[step] is orthogonal to q[step-1] */
t = svd_ddot(n, wptr[4], 1, wptr[0], 1);
svd_daxpy(n, -t, wptr[2], 1, wptr[0], 1);
svd_dcopy(n, wptr[0], 1, wptr[3], 1);
t = svd_ddot(n, wptr[3], 1, wptr[0], 1);
if (t <= eps * rnm2) t = 0.0;
rnm2 = t;
}
return(sqrt(rnm2));
}

/***********************************************************************
*                                                                     *
*			error_bound()                                  *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

Function massages error bounds for very close ritz values by placing 
a gap between them.  The error bounds are then refined to reflect 
this.


Arguments 
---------

(input)
endl     left end of interval containing unwanted eigenvalues
endr     right end of interval containing unwanted eigenvalues
ritz     array to store the ritz values
bnd      array to store the error bounds
enough   stop flag


Functions used
--------------

BLAS		svd_idamax
UTILITY	svd_dmin

***********************************************************************/

long error_bound(long *enough, double endl, double endr, 
double *ritz, double *bnd, long step, double tol) {
long mid, i, neig;
double gapl, gap;

/* massage error bounds for very close ritz values */
mid = svd_idamax(step + 1, bnd, 1);

for (i=((step+1) + (step-1)) / 2; i >= mid + 1; i -= 1)
if (fabs(ritz[i-1] - ritz[i]) < eps34 * fabs(ritz[i])) 
if (bnd[i] > tol && bnd[i-1] > tol) {
bnd[i-1] = sqrt(bnd[i] * bnd[i] + bnd[i-1] * bnd[i-1]);
bnd[i] = 0.0;
}


for (i=((step+1) - (step-1)) / 2; i <= mid - 1; i +=1 ) 
if (fabs(ritz[i+1] - ritz[i]) < eps34 * fabs(ritz[i])) 
if (bnd[i] > tol && bnd[i+1] > tol) {
bnd[i+1] = sqrt(bnd[i] * bnd[i] + bnd[i+1] * bnd[i+1]);
bnd[i] = 0.0;
}

/* refine the error bounds */
neig = 0;
gapl = ritz[step] - ritz[0];
for (i = 0; i <= step; i++) {
gap = gapl;
if (i < step) gapl = ritz[i+1] - ritz[i];
gap = svd_dmin(gap, gapl);
if (gap > bnd[i]) bnd[i] = bnd[i] * (bnd[i] / gap);
if (bnd[i] <= 16.0 * eps * fabs(ritz[i])) {
neig++;
if (!*enough) *enough = endl < ritz[i] && ritz[i] < endr;
}
}   
return neig;
}

/***********************************************************************
*                                                                     *
*				imtqlb()			       *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

imtqlb() is a translation of a Fortran version of the Algol
procedure IMTQL1, Num. Math. 12, 377-383(1968) by Martin and 
Wilkinson, as modified in Num. Math. 15, 450(1970) by Dubrulle.  
Handbook for Auto. Comp., vol.II-Linear Algebra, 241-248(1971).  
See also B. T. Smith et al, Eispack Guide, Lecture Notes in 
Computer Science, Springer-Verlag, (1976).

The function finds the eigenvalues of a symmetric tridiagonal
matrix by the implicit QL method.


Arguments 
---------

(input)
n      order of the symmetric tridiagonal matrix                   
d      contains the diagonal elements of the input matrix           
e      contains the subdiagonal elements of the input matrix in its
last n-1 positions.  e[0] is arbitrary	             

(output)
d      contains the eigenvalues in ascending order.  if an error
exit is made, the eigenvalues are correct and ordered for
indices 0,1,...ierr, but may not be the smallest eigenvalues.
e      has been destroyed.					    
ierr   set to zero for normal return, j if the j-th eigenvalue has
not been determined after 30 iterations.		    

Functions used
--------------

UTILITY	svd_fsign
MISC		svd_pythag

***********************************************************************/

void imtqlb(long n, double d[], double e[], double bnd[])

{
long last, l, m, i, iteration;

/* various flags */
long exchange, convergence, underflow;	

double b, test, g, r, s, c, p, f;

if (n == 1) return;
ierr = 0;
bnd[0] = 1.0;
last = n - 1;
for (i = 1; i < n; i++) {
bnd[i] = 0.0;
e[i-1] = e[i];
}
e[last] = 0.0;
for (l = 0; l < n; l++) {
iteration = 0;
while (iteration <= 30) {
for (m = l; m < n; m++) {
convergence = FALSE;
if (m == last) break;
else {
test = fabs(d[m]) + fabs(d[m+1]);
if (test + fabs(e[m]) == test) convergence = TRUE;
}
if (convergence) break;
}
p = d[l]; 
f = bnd[l]; 
if (m != l) {
if (iteration == 30) {
ierr = l;
return;
}
iteration += 1;
/*........ form shift ........*/
g = (d[l+1] - p) / (2.0 * e[l]);
r = svd_pythag(g, 1.0);
g = d[m] - p + e[l] / (g + svd_fsign(r, g));
s = 1.0;
c = 1.0;
p = 0.0;
underflow = FALSE;
i = m - 1;
while (underflow == FALSE && i >= l) {
f = s * e[i];
b = c * e[i];
r = svd_pythag(f, g);
e[i+1] = r;
if (r == 0.0) underflow = TRUE;
else {
s = f / r;
c = g / r;
g = d[i+1] - p;
r = (d[i] - g) * s + 2.0 * c * b;
p = s * r;
d[i+1] = g + p;
g = c * r - b;
f = bnd[i+1];
bnd[i+1] = s * bnd[i] + c * f;
bnd[i] = c * bnd[i] - s * f;
i--;
}
}       /* end while (underflow != FALSE && i >= l) */
/*........ recover from underflow .........*/
if (underflow) {
d[i+1] -= p;
e[m] = 0.0;
}
else {
d[l] -= p;
e[l] = g;
e[m] = 0.0;
}
} 		       		   /* end if (m != l) */
else {

/* order the eigenvalues */
exchange = TRUE;
if (l != 0) {
i = l;
while (i >= 1 && exchange == TRUE) {
if (p < d[i-1]) {
d[i] = d[i-1];
bnd[i] = bnd[i-1];
i--;
}
else exchange = FALSE;
}
}
if (exchange) i = 0;
d[i] = p;
bnd[i] = f; 
iteration = 31;
}
}			       /* end while (iteration <= 30) */
}				   /* end for (l=0; l<n; l++) */
return;
}						  /* end main */

/***********************************************************************
*                                                                     *
*				imtql2()			       *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

imtql2() is a translation of a Fortran version of the Algol
procedure IMTQL2, Num. Math. 12, 377-383(1968) by Martin and 
Wilkinson, as modified in Num. Math. 15, 450(1970) by Dubrulle.  
Handbook for Auto. Comp., vol.II-Linear Algebra, 241-248(1971).  
See also B. T. Smith et al, Eispack Guide, Lecture Notes in 
Computer Science, Springer-Verlag, (1976).

This function finds the eigenvalues and eigenvectors of a symmetric
tridiagonal matrix by the implicit QL method.


Arguments
---------

(input)                                                             
nm     row dimension of the symmetric tridiagonal matrix           
n      order of the matrix                                        
d      contains the diagonal elements of the input matrix        
e      contains the subdiagonal elements of the input matrix in its
last n-1 positions.  e[0] is arbitrary	             
z      contains the identity matrix				    
                                            
(output)                                                       
d      contains the eigenvalues in ascending order.  if an error
exit is made, the eigenvalues are correct but unordered for
for indices 0,1,...,ierr.				   
e      has been destroyed.					  
z      contains orthonormal eigenvectors of the symmetric   
tridiagonal (or full) matrix.  if an error exit is made,
z contains the eigenvectors associated with the stored 
eigenvalues.					
ierr   set to zero for normal return, j if the j-th eigenvalue has
not been determined after 30 iterations.		    


Functions used
--------------
UTILITY	svd_fsign
MISC		svd_pythag

***********************************************************************/

void imtql2(long nm, long n, double d[], double e[], double z[])

{
long index, nnm, j, last, l, m, i, k, iteration, convergence, underflow;
double b, test, g, r, s, c, p, f;
if (n == 1) return;
ierr = 0;
last = n - 1;
for (i = 1; i < n; i++) e[i-1] = e[i];
e[last] = 0.0;
nnm = n * nm;
for (l = 0; l < n; l++) {
iteration = 0;

/* look for small sub-diagonal element */
while (iteration <= 30) {
for (m = l; m < n; m++) {
convergence = FALSE;
if (m == last) break;
else {
test = fabs(d[m]) + fabs(d[m+1]);
if (test + fabs(e[m]) == test) convergence = TRUE;
}
if (convergence) break;
}
if (m != l) {

/* set error -- no convergence to an eigenvalue after
* 30 iterations. */     
if (iteration == 30) {
ierr = l;
return;
}
p = d[l]; 
iteration += 1;

/* form shift */
g = (d[l+1] - p) / (2.0 * e[l]);
r = svd_pythag(g, 1.0);
g = d[m] - p + e[l] / (g + svd_fsign(r, g));
s = 1.0;
c = 1.0;
p = 0.0;
underflow = FALSE;
i = m - 1;
while (underflow == FALSE && i >= l) {
f = s * e[i];
b = c * e[i];
r = svd_pythag(f, g);
e[i+1] = r;
if (r == 0.0) underflow = TRUE;
else {
s = f / r;
c = g / r;
g = d[i+1] - p;
r = (d[i] - g) * s + 2.0 * c * b;
p = s * r;
d[i+1] = g + p;
g = c * r - b;

/* form vector */
for (k = 0; k < nnm; k += n) {
index = k + i;
f = z[index+1];
z[index+1] = s * z[index] + c * f;
z[index] = c * z[index] - s * f;
} 
i--;
}
}   /* end while (underflow != FALSE && i >= l) */
/*........ recover from underflow .........*/
if (underflow) {
d[i+1] -= p;
e[m] = 0.0;
}
else {
d[l] -= p;
e[l] = g;
e[m] = 0.0;
}
}
else break;
}		/*...... end while (iteration <= 30) .........*/
}		/*...... end for (l=0; l<n; l++) .............*/

/* order the eigenvalues */
for (l = 1; l < n; l++) {
i = l - 1;
k = i;
p = d[i];
for (j = l; j < n; j++) {
if (d[j] < p) {
k = j;
p = d[j];
}
}
/* ...and corresponding eigenvectors */
if (k != i) {
d[k] = d[i];
d[i] = p;
for (j = 0; j < nnm; j += n) {
p = z[j+i];
z[j+i] = z[j+k];
z[j+k] = p;
}
}   
}
return;
}		/*...... end main ............................*/

/***********************************************************************
*                                                                     *
*				machar()			       *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

This function is a partial translation of a Fortran-77 subroutine 
written by W. J. Cody of Argonne National Laboratory.
It dynamically determines the listed machine parameters of the
floating-point arithmetic.  According to the documentation of
the Fortran code, "the determination of the first three uses an
extension of an algorithm due to M. Malcolm, ACM 15 (1972), 
pp. 949-951, incorporating some, but not all, of the improvements
suggested by M. Gentleman and S. Marovich, CACM 17 (1974), 
pp. 276-277."  The complete Fortran version of this translation is
documented in W. J. Cody, "Machar: a Subroutine to Dynamically 
Determine Determine Machine Parameters," TOMS 14, December, 1988.


Parameters reported 
-------------------

ibeta     the radix for the floating-point representation       
it        the number of base ibeta digits in the floating-point
significand					 
irnd      0 if floating-point addition chops		      
1 if floating-point addition rounds, but not in the 
ieee style					
2 if floating-point addition rounds in the ieee style
3 if floating-point addition chops, and there is    
partial underflow				
4 if floating-point addition rounds, but not in the
ieee style, and there is partial underflow    
5 if floating-point addition rounds in the ieee style,
and there is partial underflow                   
machep    the largest negative integer such that              
1.0+float(ibeta)**machep .ne. 1.0, except that 
machep is bounded below by  -(it+3)          
negeps    the largest negative integer such that          
1.0-float(ibeta)**negeps .ne. 1.0, except that 
negeps is bounded below by  -(it+3)	       

***********************************************************************/

void machar(long *ibeta, long *it, long *irnd, long *machep, long *negep) {

volatile double beta, betain, betah, a, b, ZERO, ONE, TWO, temp, tempa,
temp1;
long i, itemp;

ONE = (double) 1;
TWO = ONE + ONE;
ZERO = ONE - ONE;

a = ONE;
temp1 = ONE;
while (temp1 - ONE == ZERO) {
a = a + a;
temp = a + ONE;
temp1 = temp - a;
b += a; /* to prevent icc compiler error */
}
b = ONE;
itemp = 0;
while (itemp == 0) {
b = b + b;
temp = a + b;
itemp = (long)(temp - a);
}
*ibeta = itemp;
beta = (double) *ibeta;

*it = 0;
b = ONE;
temp1 = ONE;
while (temp1 - ONE == ZERO) {
*it = *it + 1;
b = b * beta;
temp = b + ONE;
temp1 = temp - b;
}
*irnd = 0; 
betah = beta / TWO; 
temp = a + betah;
if (temp - a != ZERO) *irnd = 1;
tempa = a + beta;
temp = tempa + betah;
if ((*irnd == 0) && (temp - tempa != ZERO)) *irnd = 2;

*negep = *it + 3;
betain = ONE / beta;
a = ONE;
for (i = 0; i < *negep; i++) a = a * betain;
b = a;
temp = ONE - a;
while (temp-ONE == ZERO) {
a = a * beta;
*negep = *negep - 1;
temp = ONE - a;
}
*negep = -(*negep);

*machep = -(*it) - 3;
a = b;
temp = ONE + a;
while (temp - ONE == ZERO) {
a = a * beta;
*machep = *machep + 1;
temp = ONE + a;
}
eps = a;
return;
}

/***********************************************************************
*                                                                     *
*                     store()                                         *
*                                                                     *
***********************************************************************/
/***********************************************************************

Description
-----------

store() is a user-supplied function which, based on the input
operation flag, stores to or retrieves from memory a vector.


Arguments 
---------

(input)
n       length of vector to be stored or retrieved
isw     operation flag:
isw = 1 request to store j-th Lanczos vector q(j)
isw = 2 request to retrieve j-th Lanczos vector q(j)
isw = 3 request to store q(j) for j = 0 or 1
isw = 4 request to retrieve q(j) for j = 0 or 1
s	   contains the vector to be stored for a "store" request 

(output)
s	   contains the vector retrieved for a "retrieve" request 

Functions used
--------------

BLAS		svd_dcopy

***********************************************************************/

void store(long n, long isw, long j, double *s) {
/* printf("called store %ld %ld\n", isw, j); */
switch(isw) {
case STORQ:
if (!LanStore[j + MAXLL]) {
if (!(LanStore[j + MAXLL] = svd_doubleArray(n, FALSE, "LanStore[j]")))
svd_fatalError("svdLAS2: failed to allocate LanStore[%d]", j + MAXLL);
}
svd_dcopy(n, s, 1, LanStore[j + MAXLL], 1);
break;
case RETRQ:	
if (!LanStore[j + MAXLL])
svd_fatalError("svdLAS2: store (RETRQ) called on index %d (not allocated)", 
j + MAXLL);
svd_dcopy(n, LanStore[j + MAXLL], 1, s, 1);
break;
case STORP:	
if (j >= MAXLL) {
svd_error("svdLAS2: store (STORP) called with j >= MAXLL");
break;
}
if (!LanStore[j]) {
if (!(LanStore[j] = svd_doubleArray(n, FALSE, "LanStore[j]")))
svd_fatalError("svdLAS2: failed to allocate LanStore[%d]", j);
}
svd_dcopy(n, s, 1, LanStore[j], 1);
break;
case RETRP:	
if (j >= MAXLL) {
svd_error("svdLAS2: store (RETRP) called with j >= MAXLL");
break;
}
if (!LanStore[j])
svd_fatalError("svdLAS2: store (RETRP) called on index %d (not allocated)", 
j);
svd_dcopy(n, LanStore[j], 1, s, 1);
break;
}
return;
}
	

}
