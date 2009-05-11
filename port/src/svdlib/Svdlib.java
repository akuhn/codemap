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
			  this.pointr = new int[cols+1];
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
//	  int i, c;
//	  DMat D = svdNewDMat(S->rows, S->cols);
//	  if (!D) {
//	    svd_error("svdConvertStoD: failed to allocate D");
//	    return NULL;
//	  }
//	  for (i = 0, c = 0; i < S->vals; i++) {
//	    while (S->pointr[c + 1] <= i) c++;
//	    D->value[S->rowind[i]][c] = S->value[i];
//	  }
//	  return D;
	}

	/* Converts a dense matrix to a sparse one (without affecting the dense one) */
	SMat svdConvertDtoS(DMat D) {
		throw null;
//	  SMat S;
//	  int i, j, n;
//	  for (i = 0, n = 0; i < D->rows; i++)
//	    for (j = 0; j < D->cols; j++)
//	      if (D->value[i][j] != 0) n++;
//	  
//	  S = svdNewSMat(D->rows, D->cols, n);
//	  if (!S) {
//	    svd_error("svdConvertDtoS: failed to allocate S");
//	    return NULL;
//	  }
//	  for (j = 0, n = 0; j < D->cols; j++) {
//	    S->pointr[j] = n;
//	    for (i = 0; i < D->rows; i++)
//	      if (D->value[i][j] != 0) {
//	        S->rowind[n] = i;
//	        S->value[n] = D->value[i][j];
//	        n++;
//	      }
//	  }
//	  S->pointr[S->cols] = S->vals;
//	  return S;
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
	    N.pointr[r] = N.pointr[r+1] - N.pointr[r-1];
	  N.pointr[0] = 0;
	  /* Assign the new columns and values. */
	  for (c = 0, i = 0; c < S.cols; c++) {
	    for (; i < S.pointr[c+1]; i++) {
	      r = S.rowind[i];
	      j = N.pointr[r+1]++;
	      N.rowind[j] = c;
	      N.value[j] = S.value[i];
	    }
	  }
	  return N;
	}

}
