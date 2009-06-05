package org.codemap.hitmds;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

import java.util.Random;

/** Revision 37 of {@link org.codemap.hitmds.Hitmds2} used for regression testing.
 * 
 */
public class Revision37 {

	private Random random = new Random();
	
	public Revision37 seed(long seed) {
		random = new Random(seed);
		return this;
	}
	
	//	#define SCANFMT "%lf"
	//
	//	/* linear learning rate annealing at . times cycles */
	public static final double START_ANNEALING_RATIO = 0.5;

	public double rand() {
		return random.nextDouble();
	}

	public static final boolean VERBOSE = false;
	
	public void printf(String format, Object... args) {
		if (VERBOSE) System.out.printf(format, args);
	}


	public float frand() {
		return (float) rand(); // TODO assume that ((double) rand() / (RAND_MAX+1.0)) does this 
	}

	public int	irand(int x) {
		return ((int)((double)(x) * frand()));
	}

	//	/* address matrix components */
	//	#define D(mat,i,j) (*(mat + ( \
	//	  (i<j) ? (j - 1 + ((((pattern_length<<1) - i - 3) * i) >> 1)) \
	//	        : ((i==j) ? matsize \
	//	                  : (i - 1 + ((((pattern_length<<1) - j - 3) * j) >> 1))) \
	//	    )))

	public float DGet(float[] mat, int i, int j) {
		return mat[(i < j) ? (j - 1 + ((((pattern_length<<1) - i - 3) * i) >> 1))
				: ((i==j) ? matsize 
						: (i - 1 + ((((pattern_length<<1) - j - 3) * j) >> 1)))];
	}

	public void DSet(float[] mat, int i, int j, float value) {
		mat[(i < j) ? (j - 1 + ((((pattern_length<<1) - i - 3) * i) >> 1))
				: ((i==j) ? matsize 
						: (i - 1 + ((((pattern_length<<1) - j - 3) * j) >> 1)))] = value;
	}

	private boolean stop_calculation = false;
	//	static double (*distance)(int dim, double *d1, double *d2);

	int correlation_exponent,/* (1/r^2)^k */
	pattern_length,      /* Length of data series */
	pattern_dimension,   /* pattern dimension     */
	matsize,             /* number of distance matrix elements */
	target_dim;          /* dimension of target space */
	boolean printqual;           /* flag to rate embedding qualities (last column) */
	boolean matrix_input;       /* != 0 if data given as distance matrix */

	double pattern[][],        /* contains float data from STDIN */
	points[][],
	points_distmat_mean,
	points_distmat_mixed,
	points_distmat_mono,
	pattern_distmat_var_sum,
	pattern_distmat_mean,
	correps;          /* correlation epsilon avoiding 1/0 */

	/* save memory space */
	float  pattern_distmat[],
	points_distmat[];


	int shuffle_index[];      /* helper for data shuffling */


	void initializeRandomSeed() {
		//		TODO use same random fun and seed in Java and C to compare results!
		//	  int srd, tmp;  /* auto-variable: not initialized -> random */
		//	                 /* Compiler Warnings are welcome here ! */
		//	  time_t t;
		//	  struct tm *ts;
		//
		//	  time(&t);
		//	  ts = localtime(&t);
		//
		//	  tmp = ts->tm_mday | (ts->tm_min << 6) | (ts->tm_sec << 12);
		//
		//	  if(srd == 0)
		//	    srd |= tmp;
		//	  else
		//	    srd *= tmp;
		//
		//	  srand((unsigned)srd);
		//
		//	  return (unsigned)srd;
	}

	public static double EPS = 1e-16;

	/* Euclidean distance */
	public double dist(int dimension, double[] d1, double[] d2) {
		assert dimension == d1.length && dimension == d2.length;
		double sum = 0.0;
		for(int u = 0; u < dimension; u++) {
			double tmp = d1[u] - d2[u];
			sum += tmp * tmp;
		}
		return sqrt(sum);
	}


	/* "quartic" Euclidean yields nice separation, sometimes */
	public double dist4s(int dimension, double[] d1, double[] d2) {
		//
		//  int u;
		//  double sum = 0., tmp;
		//
		//  for(u = 0; u < dimension; u++) {
		//    tmp = *d1++ - *d2++;
		//    tmp *= tmp;
		//    sum += tmp * tmp;
		//  }
		//
		//  return sum;
		throw null;
	}


	/* general Minkowski metric, mexponent = 2 -> Euclidean distance */

	private double mexponent = 1.0;                                    

	public double dist_minkowski(int dimension, double[] d1, double[] d2) {
		double sum = 0.0;
		for(int u = 0; u < dimension; u++) {
			double tmp = d1[u] - d2[u];
			tmp = abs(tmp);
			sum += pow(tmp, mexponent);
		}
		return pow(sum,1./mexponent);
	}



	/* Spearman rank correlation measure */
	/* compatible with numerical recipes in c */
	/* assumes 1<=rank<=dimension in rank or .5+rank from tornk.awk */
	/* essentially compatible with corr applied to rank vectors */
	public double dist_spearman(int dimension, double[] d1, double[] d2) {
		//{
		//
		//  static double f = 0.;
		//  static int *idx1, *idx2, dim2, dim4;
		//
		//  int u, i;
		//  double sum = 0., tmp, s1 = 0., s2 = 0.;
		//
		//
		//  if(f == 0.) {
		//    f = 1. / (dimension * (dimension * dimension - 1.));
		//
		//    dim2 = dimension << 1; dim4 = dim2 << 1;
		//
		//    idx1 = (int *)malloc(dim4 * sizeof(int));  /* contin. block */
		//    idx2 = idx1 + dim2;
		//
		//    for(u = 0; u < dim4; u++) idx1[u] = 0;
		//  }
		//
		//
		//  /* multiplicity */
		//
		//  i = 0;
		//  for(u = 0; u < dimension; u++) {
		//    if(++idx1[(int)(2. * (d1[u]-1.))] > 1) ++i;
		//    if(++idx2[(int)(2. * (d2[u]-1.))] > 1) ++i;
		//  }
		//
		//  if(i)  /* only if necessary */
		//    for(u = 0; u < dim2; u++) {
		//      if((i = idx1[u]) > 1) s1 += i * (i * i - 1);
		//      if((i = idx2[u]) > 1) s2 += i * (i * i - 1);
		//    }
		//
		//  for(u = 0; u < dimension; u++) {
		//    tmp = *d1++ - *d2++;
		//    sum += tmp * tmp;
		//    idx1[u] = idx2[u] = idx1[u+dimension] = idx2[u+dimension] = 0;
		//  }
		//  
		//  if(s1 == 0. && s2 == 0.) 
		//
		//    return 6.*f * sum;
		//
		//  else {
		//    s2 *= f;
		//    if((s1 *= f) > 1-EPS) 
		//      return (s2 > 1-EPS) ? 0 : 1.;
		//    else if(s2 > 1-EPS)
		//      return 1.;
		//  }
		//
		//  return 1. - (1. - 6.* (f*sum + (s1+s2)/12.)) / sqrt((1.-s1) * (1.-s2));
		throw null;
	}


	/* just a mean */
	public double mean(int dimension, double[] p) {
		assert dimension == p.length;
		double sum = 0.;
		for(double each: p) sum += each;
		return sum / dimension;
	}


	/* just a (redunant) mean of floats */
	public double meanf(int dimension, float[] p) {
		float sum = 0.0f;
		for (int u = 0; u < dimension; u++) sum += p[u];
		return sum / dimension;
	}


	/* derivative of Pearson correlation w.r.t d2 or absolute to both directions (symmetric) */
	public double corr_deriv_vec_flt(int dimension, float[] d1, float[] d2, float[] res, int symmetric, int addup) { 
		//	{
		//
		//  int u, isres = (res != NULL);
		//
		//  double mono1 = 0., mono2 = 0., mixed = 0., sum = 0., tmp, tmx, f;
		//
		//  FLT md1 = meanf(dimension, d1),
		//      md2 = meanf(dimension, d2);
		// 
		//
		//  for(u = 0; u < dimension; u++) {
		//    
		//    tmx = d1[u] - md1; mono1 += tmx * tmx;
		//    tmp = d2[u] - md2; mono2 += tmp * tmp;
		//    mixed += tmp * tmx;
		//  }
		//  
		//  tmp = sqrt(mono1 * mono2);
		//
		//  if(tmp < EPS) tmp = EPS;
		//  tmp = 1. / tmp;
		//
		//  if(mono1 < EPS) mono1 = EPS;
		//  mono1 = mixed / mono1;
		//
		//  if(symmetric) {
		//    if(mono2 < EPS) mono2 = EPS;
		//    mono2 = mixed / mono2;
		//  }
		//
		//  for(u = 0; u < dimension; u++) {
		//
		//    f = ((*d1 - md1) - mono2 * (*d2 - md2)) * tmp;
		//
		//    if(symmetric) {
		//      tmx = ((*d2 - md2) - mono1 * (*d1 - md1)) * tmp;
		//      f *= tmx;
		//      f = ABS(f);
		//    }
		//    sum += f;
		//
		//    if(isres) {
		//      if(addup) *res++ += f; else *res++ = f;
		//    }
		//
		//    ++d1; ++d2;
		//  }
		//
		//  return sum / dimension;  /* average (absolute) contribution */
		throw null;
	}


	/* derivative of Pearson correlation w.r.t d2 or absolute to both directions (symmetric) */
	public double corr_deriv_vec(int dimension, double[] d1, double[] d2, double[] res, int symmetric, int addup) {
		//{
		//
		//  int u, isres = (res != NULL);
		//
		//  double mono1 = 0., mono2 = 0., mixed = 0., sum = 0., tmp, tmx, f,
		//         md1 = mean(dimension, d1),
		//         md2 = mean(dimension, d2);
		// 
		//
		//  for(u = 0; u < dimension; u++) {
		//    
		//    tmx = d1[u] - md1; mono1 += tmx * tmx;
		//    tmp = d2[u] - md2; mono2 += tmp * tmp;
		//    mixed += tmp * tmx;
		//  }
		//  
		//  tmp = sqrt(mono1 * mono2);
		//
		//  if(tmp < EPS) tmp = EPS;
		//  tmp = 1. / tmp;
		//
		//  if(mono1 < EPS) mono1 = EPS;
		//  mono1 = mixed / mono1;
		//
		//  if(symmetric) {
		//    if(mono2 < EPS) mono2 = EPS;
		//    mono2 = mixed / mono2;
		//  }
		//
		//  for(u = 0; u < dimension; u++) {
		//
		//    f = ((*d1 - md1) - mono2 * (*d2 - md2)) * tmp;
		//
		//    if(symmetric) {
		//      tmx = ((*d2 - md2) - mono1 * (*d1 - md1)) * tmp;
		//      f *= tmx;
		//      f = ABS(f);
		//    }
		//    sum += f;
		//
		//    if(isres) {
		//      if(addup) *res++ += f; else *res++ = f;
		//    }
		//
		//    ++d1; ++d2;
		//  }
		//
		//  return sum / dimension;  /* average (absolute) contribution */
		throw null;
	}


	/* realize symmetric measure d(x,y) = d(y,x) */
	public double corr_deriv_abs(int dimension, double[] d1, double[] d2) {
		return corr_deriv_vec(dimension, d1, d2, null, 1, 1);
	}



	/* (1 - Pearson)^expo correlation */
	double corr(int dimension, double[] d1, double[] d2) {

		double mono1 = 0.0, mono2 = 0.0, mixed = 0.0, tmp,
		md1 = mean(dimension, d1),
		md2 = mean(dimension, d2);


		for(int u = 0; u < dimension; u++) {
			tmp = d1[u] - md1; mono1 += tmp * tmp;
			tmp = d2[u] - md2; mono2 += tmp * tmp;
			mixed += tmp * (d1[u] - md1);
		}

		tmp = sqrt(mono1 * mono2);

		mixed /= (tmp < EPS) ? EPS : tmp;

		boolean u;
		/* allow correlated and anti-correlated patterns to be similar */
		if((u = mexponent < 0.) && mixed < 0.0) mixed = -mixed; 

		mono1 = u ? -mexponent : mexponent;

		return (mono1 == 1.0) ? 1.0 - mixed : pow(1.0 - mixed, mexponent);
	}



	/* for distance matrix discrepancy evaluation. Min val: 0 */
	double corr_2()
	{

		double p2 = points_distmat_mixed * points_distmat_mixed,
		e2 = EPS * EPS;

		return pattern_distmat_var_sum*points_distmat_mono/((p2<e2)? e2 : p2) - 1.;
	}


	/* derivative of HiT-MDS stress function */
	public double deriv(int j, int k, int idx) {

		//#if 0
		//  /* Wed Apr  4 11:05:01 */
		//  static double sq, scal;
		//#endif
		double preres = 0.0;
		int _j = -1, _k = -1;

		double d = DGet(points_distmat,j, k);

		if(_j != j || _k != k) {

			double  D = DGet(pattern_distmat,j,k),
			dif = d - points_distmat_mean;

			//#if 0
			///*    Wed Apr  4 11:05:01     2007: ABS encountered, making following unnecessary */
			//    if(_j != j) {    /* only if new reference signal */
			//      _j = j;
			//      scal = sqrt(pattern_distmat_var_sum * points_distmat_mono);
			//      sq = points_distmat_mixed / scal;
			//      scal = correps / (points_distmat_mono * scal * (correps - sq) * (correps + sq));
			//
			//      /*
			//      sq = scal * correps - points_distmat_mixed;
			//      scal = correps * pattern_distmat_var_sum / (scal * sq * sq);
			//      */
			//    }
			//    
			//     _k = k;
			//    
			//    preres = scal * (dif * points_distmat_mixed - D * points_distmat_mono);
			//#endif
			_j = j; _k = k;

			preres = dif * points_distmat_mixed - D * points_distmat_mono;

		}

		return preres * (points[j][idx] - points[k][idx]) / ((d < EPS) ? EPS : d);
	}

	/******************************************************************************/
	/*              Some data handling routines                                   */
	/******************************************************************************/
	void data_free(int i, boolean what)
	{

		pattern = null;

		if(what == false) {
			points = null;
			points_distmat = null;
			pattern_distmat = null;
		}
	}

	public int data_alloc() {

		/* also valid for neg values */
		matsize = ((pattern_length-1) * pattern_length) >> 1;

		pattern_distmat = new float[1 + matsize];
		points_distmat = new float[1 + matsize];

		/* init access to diagonal elements: 0 by default */
		DSet(points_distmat,0,0, 0.0f);
		DSet(pattern_distmat,0,0, 0.0f);


		if(pattern_dimension > 0) { /* patterns not given as distance matrix */ 
			pattern = new double[pattern_length][pattern_dimension];  
		}

		return 1;
	}


	public void data_stdin(int pattern_length, int pattern_dimension,
			double[][] data, int target_dim, double[][] data2) {

		assert target_dim < 0 || data2 != null;
		
		//	The first line of the text input matrix contains two ascii integer numbers
		//	the number samples (i.e. following number of lines = data vectors) and
		//	the input dimension
		//
		//	After the data matrix, there is another number indicating the target
		//	dimension, e.g. 2. A negative sign here denotes random initialization,
		//	the standard case.
		//
		//
		//	If Your matrix is already a dissimilarity or distance matrix, then the
		//	number of lines given in the first row must possess a negative sign.


		this.pattern_length = pattern_length;
		this.pattern_dimension = pattern_dimension;
		this.target_dim = abs(target_dim);

		double random_matrix[] = null;

		data_alloc();

		if(pattern_length < 0) {
			matrix_input = true; // read full distance matrix
			pattern_length = pattern_dimension = -pattern_length;
		}
		else {
			matrix_input = false;
		}

		// TODO rewrite this loop!
		for(int line = 0; line < pattern_length; line++) {
			for(int column = 0; column < pattern_dimension; column++) {
				if (matrix_input) {
					DSet(pattern_distmat, line, column, (float) data[line][column]);
				}
				else {
					pattern[line][column] = data[line][column];
				}
			}
		}

		// off += line + 1;

		boolean randomInitiailization = target_dim < 0;
		
		if(randomInitiailization) {  
			target_dim = -target_dim;
		}

		/* ABC: should be moved to data_alloc */
		points = new double[pattern_length][target_dim];

		for(int line = 0; line < pattern_length; line++) {
			for(int column = 0; column < target_dim; column++) {
				if(!randomInitiailization) { /* read from input */
					points[line][column] = data2[line][column];
				}
				else { 
					if(matrix_input) {  /* random init */
						points[line][column] = 2. * frand() - 1.;
					}
					else {              /* random projection */
						if (random_matrix == null) {
							int j = target_dim * pattern_dimension;
							random_matrix = new double[j];
							while(--j >= 0) random_matrix[j] = 2.0 * frand() - 1.; // 1.0 .. -1.0
						}
						double dtmp = 0.;
						for(int j = 0; j < pattern_dimension; j++) {
							dtmp += random_matrix[column * pattern_dimension + j] * pattern[line][j];
						}
						points[line][column] = dtmp;
					}
				}
			}
		}
		random_matrix = null;
	}


	public double distance(int dim, double[] a, double[] b) {
		return corr(dim, a, b); // XXX hardwired parameter "... 1:8"
	}

	public void data_init() {

		double tmp;
		int i,j;


		/* fill distance matrices */
		for(i = 0; i < pattern_length-1; i++) {
			for(j = i+1; j < pattern_length; j++) { 
				if(!matrix_input) DSet(pattern_distmat,i,j, 
						(float) distance(pattern_dimension, pattern[i], pattern[j]));
				DSet(points_distmat,i,j, (float) dist(target_dim, points[i], points[j]));
			}
		}

		//#if 0
		//  /* output distance */
		//  printf("(\n");
		//  for(i = 0; i < matsize; i++) 
		//    printf("%g\n",pattern_distmat[i]);
		//  printf(")\n");
		//#endif

		/* initial values of point distance matrix, mean, mixed, mono */
		points_distmat_mean = meanf(matsize, points_distmat);

		/* mean value of pattern distance matrix will be subtracted */
		pattern_distmat_mean = meanf(matsize, pattern_distmat);

		points_distmat_mixed = points_distmat_mono = pattern_distmat_var_sum = 0.;

		for(i = 0; i < matsize; i++) {
			tmp = points_distmat[i] - points_distmat_mean;
			points_distmat_mixed += tmp * (pattern_distmat[i] -= pattern_distmat_mean);
			points_distmat_mono +=  tmp * tmp;
			pattern_distmat_var_sum += pattern_distmat[i] * pattern_distmat[i];
		}



	}

	/*** end of data routines *****************************************************/


	/******************************************************************************/
	/*              Some data shuffling routines                                  */
	/******************************************************************************/

	public int SHUFFLE_NUMBER() {
		return pattern_length; // TODO inline this
	}

	void shuffle_index_alloc()
	{
		int len = SHUFFLE_NUMBER();
		shuffle_index = new int[len];
		while(--len >= 0) shuffle_index[len] = len;
	}

	void shuffle_do()
	{
		int cnt, ind, tmp;
		if(shuffle_index == null)
			shuffle_index_alloc();

		for(cnt = 0; cnt < SHUFFLE_NUMBER(); cnt++) {
			ind = irand(SHUFFLE_NUMBER());
			tmp = shuffle_index[cnt];
			shuffle_index[cnt] = shuffle_index[ind];
			shuffle_index[ind] = tmp;
		}
	}

	private int ind = Integer.MAX_VALUE -1;


	public int shuffle_next() {
		if(++ind >= SHUFFLE_NUMBER()) {
			shuffle_do();
			ind = 0;
		}
		return shuffle_index[ind];
	}

	/*** end of data shuffling routines *******************************************/


	/* the training loop */
	void mds_train(int cycles, double learning_rate)
	{

		double delta_point[], point[], dtmp, 
		diff, diff_mixed, diff_mono, 
		diffs[], diffs_mean;

		int c, k, i, j, m, t;

		delta_point = new double[target_dim];
		diffs = new double[pattern_length];

		t = 0;
		m = cycles / 10;

		for(c = 0; c < cycles && !stop_calculation; c++) {

			if(++t == m) {
				t = 0;
				printf("%3.2f%%: %g  \t(r = %g)\n", 
						100.0 * c/cycles, corr_2(), 
						1.0/sqrt(corr_2()+1.));
			}

			i = shuffle_next();
			point = points[i];

			for(k = 0; k < target_dim; k++) delta_point[k] = 0.;
			for(j = 0; j < pattern_length; j++) {
				if(j != i) {
					for(k = 0; k < target_dim; k++) {
						delta_point[k] += deriv(i, j, k);
					} 
				}
			}


			if((diff_mixed = START_ANNEALING_RATIO * cycles - c) < 0.)  {
				diff_mixed = learning_rate * (1. + 1. / (1.-START_ANNEALING_RATIO) * diff_mixed / cycles);
			}else
				diff_mixed = learning_rate;

			for(k = 0; k < target_dim; k++) {
				diff = diff_mixed * delta_point[k] / abs(delta_point[k]);
				delta_point[k] = point[k] - diff /* * (2. * frand() - .5) */;
			}



			/* track change of mean, mixed, mono { */
			diff = 0.;  

			for(k = 0; k < pattern_length; k++) 
				if(k != i) {
					dtmp = dist(target_dim, delta_point, points[k]);
					diff += (diffs[k] = dtmp - DGet(points_distmat, i, k));
				}

			diffs_mean = diff / matsize;

			dtmp = -diffs_mean - points_distmat_mean;

			diff_mixed = diff_mono = 0.;

			for(k = 0; k < pattern_length; k++) 
				if(k != i) {
					//ptmp = &D(points_distmat, i, k);
					double temp = DGet(points_distmat, i, k);
					diff_mixed += DGet(pattern_distmat, i, k) * diffs[k];
					diff_mono += diffs[k] * (diffs[k] + 2. * (temp + dtmp));
					//*ptmp += diffs[k]; /* ugly */
					DSet(points_distmat, i, k, (float) (temp + diffs[k]));
				}

			points_distmat_mean += diffs_mean;

			points_distmat_mixed += diff_mixed; 

			points_distmat_mono += diff_mono + matsize * diffs_mean * diffs_mean;

			/* } track changes of mean, mixed, mono */

			for(k = 0; k < target_dim; k++) 
				point[k] = delta_point[k];



		} /* for cycles */

		diffs = null;
		delta_point = null;
	}


	/* move center to origin, scale data in such a way that 
   standard deviation of dimension of largest variance is equal to 1 */
	public void mds_postprocess() 
	{

		double mean, var = 1.0, maxvar = -1.0;

		int i, j;

		for(j = 0; j < target_dim; j++) {

			mean = 0.;

			for(i = 0; i < pattern_length; i++)
				mean += points[i][j];

			mean /= pattern_length;

			for(i = 0; i < pattern_length; i++)
				points[i][j] -= mean;
		}


		for(j = 0; j < target_dim; j++) {

			var = 0.;

			for(i = 0; i < pattern_length; i++)
				var += points[i][j] * points[i][j];

			var /= pattern_length - 1;

			if(var > maxvar) 
				maxvar = var;

		}

		var = 1. / sqrt(maxvar);  /* in fact inverse standard deviation */

		for(j = 0; j < target_dim; j++)
			for(i = 0; i < pattern_length; i++)
				points[i][j] *= var;
	}


	public void analyze_corr_deriv() {

		int i, j;

		double derivs[];

		derivs = new double[pattern_dimension];

		for(i = 0; i < pattern_dimension; i++) derivs[i] = 0.;

		for(i = 0; i < pattern_length-1; i++) {
			for(j = i+1; j < pattern_length; j++) {
				/* fprintf(stderr, "%d %d %g\n", i, j, corr(pattern_dimension, pattern[i], pattern[j])); */
				corr_deriv_vec(pattern_dimension, pattern[i], pattern[j], derivs, 1, 1);
			}
		}

		for(i = 0; i < pattern_dimension; i++) 
			printf("%g\n", derivs[i] / (pattern_length * (pattern_length - 1)) );

		derivs = null;
	}


	/* model output */
	public void mds_stdout() 
	{

		int i, j;
		double sum = 0., tmp;

		if(printqual) {  /* correlation influence w.r.t. points_distmat */ 
			corr_deriv_vec_flt(matsize, pattern_distmat, points_distmat, points_distmat, 0, 0);
		}

		for(i = 0; i < pattern_length; i++) {

			if(printqual) {
				sum = 0.;
				for(j = 0; j < pattern_length; j++) {
					tmp = DGet(points_distmat, i, j);
					sum += abs(tmp);
				}
			}

			for(j = 0; j < target_dim; j++) printf("%g ", points[i][j]);

			if(printqual)
				printf("%g\n", sum / (pattern_length-1));
			else
				printf("\n");
		}

	}


	/* On interrupt (CTRL-C) stop. Initiate write current results. */
	public void interrup(int signal) {
		stop_calculation = true;
	}


	void main_bye() {

		printf("# corr(D,d): %g\n", 1./sqrt(corr_2()+1));

		mds_postprocess();

		mds_stdout();

		shuffle_index = null;
		pattern_length = 0;
	}

	public double[][] run(double[][] data) {
		run(10, 1.0, 1.0487507802749606, null, data.length, data[0].length, data, -2, null);
		return points;
	}

	public void run(int cycles, double rate, double zero, String forth, int a, int b, double[][] c, int d, double[][] e) {

		//	Dear Adrian,
		//
		//	thanks for Your interest in hitmds2.
		//	Sorry, the documentation is indeed very poor.
		//
		//	First of all, an ascii (white-space-separated)
		//	data matrix or distance matrix is assumed to be read from
		//	standard input via 'cat myfile | hitmds2 x y z v' or 'hitmds x y z v < myfile'
		//
		//	x is number of cycles, i.e. sweeps through complete data set, usually 50 <= x < 1000
		//	y is the learning rate between 10 and .01, try .1 first,
		//	z is zero
		//	v is the metric selector, namely
		//	0: Standard Euclidean distance -> improved (nonlinear) PCA-like visualization
		//	1: 1-Pearson Correlation; '1:n' means (1-Pearson Correlation)^n
		//	2: Spearman rank correlation, only valid after transforming data to ranks (torank.awk)
		//	':n' General Minkowski metrics, e.g. 2:Eucl.Dist, 1:Manhattan.Dist, etc
		//
		//	Hope that these information helps to interpret the makefile examples
		//	and to run hitmds for Your purpose. The ZIP code map of Switzerland
		//	should be feasable.
		//
		//	If not, let me know. If You are successful You might want to share
		//	this by just letting me know.
		//
		//	Best
		//	Marc

		correps = 1.0 + zero;

		//  mexponent = (argc > 4) ? (*argv[4]==':' ? -1. : atof(argv[4])) : 0.;
		//  
		//  if(mexponent < 0.) { /* interpret neg vals as neg minkowsi expos */
		//    distance = dist_minkowski;
		//    mexponent = atof(argv[4]+1);
		//  }
		//  else if(mexponent >= 4.)
		//    distance = corr_deriv_abs;
		//  else if(mexponent >= 3.)
		//    distance = dist4s;
		//  else if(mexponent >= 2.)
		//    distance = dist_spearman;
		//  else if(mexponent >= 1.) { /* optional exponent for Pearson correlation */
		//    distance = corr;
		//    mexponent = (*(argv[4]+1) == ':') ? atof(argv[4]+2) : 0;
		//    if(mexponent == 0.) mexponent = 1.;        /* tricky use of mexponent */
		//  } else
		//    distance = dist;

		mexponent = 8; // XXX hardwired parameter "... 1:8"

		initializeRandomSeed();

		data_stdin(a, b, c, d, e);	/* calls data_alloc */


		if(cycles == 0) {  /* for correlation-based analog of variance */

			analyze_corr_deriv();

		}
		else {

			if(cycles < 0) { cycles = -cycles; printqual = true; } // TODO fix this!

			data_init();   /* calc distmats */

			data_free(pattern_length,true); /* clear data vectors, after distmat creation */

			printf("# corr(D,d): %g\n", 1./sqrt(corr_2()+1));

			mds_train(cycles * pattern_length, rate);

			main_bye();
		}

	}

}