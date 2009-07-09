package org.codemap.hitmds;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import java.util.Random;

/*
 * Multidimensional Scaling according to best distance matrix reconstruction.
 * Based on signs of directions to maximize correlation 
 * (previously based on: Fisher's z)
 *
 *   Copyright (C) 2006  Marc Strickert (stricker@ipk-gatersleben.de)
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   A copy of the GNU General Public License (gp_license.txt) comes
 *   along with this program. (Free Software Foundation, Inc.)
 *
 *  Wed Apr  4 11:05:01     2007: ABS encountered, making some equations unnecessary
 */ 
public class Hitmds2 {

    private Random random = new Random(0L);

    /* linear learning rate annealing at . times cycles */
    public static final double START_ANNEALING_RATIO = 0.5;
    public static final boolean VERBOSE = false;
    public static final double EPS = 1e-16;

    private int pattern_length;
    private int target_dim;

    /* flag to rate embedding qualities (last column) */
    // boolean printqual;
    /* true if data given as distance matrix */
    private boolean matrix_input;

    // double pattern[][];
    private double points[][];

    private double points_distmat_mean;
    private double points_distmat_mixed;
    private double points_distmat_mono;
    private double pattern_distmat_var_sum;
    private double pattern_distmat_mean;

    private DistanceMatrix pattern_distmat;
    private DistanceMatrix points_distmat;
    
    private final Distance dist = new Distance.DIST();
	private Distance distance = new Distance.CORR(8);

	private Shuffle shuffle;

    public Hitmds2 seed(long seed) {
        random.setSeed(seed);
        return this;
    }

    public Hitmds2 useRandom(Random rand) {
        random = rand;
        return this;
    }

    public Hitmds2 useDistance(Distance distance) {
    	this.distance = distance;
    	return this;
    }
    
    private double rand() {
        return random.nextDouble();
    }

//    private static void printf(String format, Object... args) {
//        if (VERBOSE) System.out.printf(format, args);
//    }


    private int irand(int x) {
        return (int) (x * rand());
    }




    /* Spearman rank correlation measure */
    /* compatible with numerical recipes in c */
    /* assumes 1<=rank<=dimension in rank or .5+rank from tornk.awk */
    /* essentially compatible with corr applied to rank vectors */
    @SuppressWarnings("unused")
	private double dist_spearman(int dimension, double[] d1, double[] d2) {
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
    public static double mean(int dimension, double[] p) {
        assert dimension == p.length;
        double sum = 0.;
        for(double each: p) sum += each;
        return sum / dimension;
    }

    /* for distance matrix discrepancy evaluation. Min val: 0 */
    @SuppressWarnings("unused")
	private double corr_2()
    {

        double p2 = points_distmat_mixed * points_distmat_mixed,
        e2 = EPS * EPS;

        return pattern_distmat_var_sum*points_distmat_mono/((p2<e2)? e2 : p2) - 1.;
    }


    /* derivative of HiT-MDS stress function */
    private double deriv(int j, int k, int idx) {

        //#if 0
        //  /* Wed Apr  4 11:05:01 */
        //  static double sq, scal;
        //#endif
        double preres = 0.0;
        // int _j = -1, _k = -1;

        double d = points_distmat.get(j,k);

        //if(_j != j || _k != k) {

            double D = pattern_distmat.get(j,k);
            double dif = d - points_distmat_mean;

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
            // _j = j; _k = k;

            preres = dif * points_distmat_mixed - D * points_distmat_mono;

        // }

        return preres * (points[j][idx] - points[k][idx]) / ((d < EPS) ? EPS : d);
    }

    private void data_stdin(int pattern_length, int pattern_dimension,
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

		double[][] pattern = null;
		
		if(pattern_dimension > 0) { /* patterns not given as distance matrix */ 
		    pattern = new double[pattern_length][pattern_dimension];  
		}

        if(pattern_length < 0) {
            matrix_input = true; // read full distance matrix
            pattern_length = pattern_dimension = -pattern_length;
        }
        else {
            matrix_input = false;
        }

        this.pattern_length = pattern_length;
        this.target_dim = abs(target_dim);

        pattern_distmat = new DistanceMatrix(pattern_length);
        points_distmat = new DistanceMatrix(pattern_length);

        // TODO rewrite this loop!
        for(int line = 0; line < pattern_length; line++) {
            for(int column = 0; column < pattern_dimension; column++) {
                if (matrix_input) {
                    pattern_distmat.set(line,column, ((float) data[line][column]));
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

        double random_matrix[] = null;
        for(int line = 0; line < pattern_length; line++) {
            for(int column = 0; column < target_dim; column++) {
                if(!randomInitiailization) { /* read from input */
                    points[line][column] = data2[line][column];
                }
                else { 
                    if(matrix_input) {  /* random init */
                        points[line][column] = 2. * rand() - 1.;
                    }
                    else {              /* random projection */
                        if (random_matrix == null) {
                            int j = target_dim * pattern_dimension;
                            random_matrix = new double[j];
                            while(--j >= 0) random_matrix[j] = 2.0 * rand() - 1.; // 1.0 .. -1.0
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

        /* calc distmats */
        
        int i,j;

        /* fill distance matrices */
        for(i = 0; i < pattern_length-1; i++) {
            for(j = i+1; j < pattern_length; j++) { 
                if(!matrix_input)
					pattern_distmat.set(i,j, ((float) distance.dist(pattern_dimension, pattern[i], pattern[j])));
                points_distmat.set(i,j, ((float) dist.dist(target_dim, points[i], points[j])));
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
        points_distmat_mean = points_distmat.meanf();

        /* mean value of pattern distance matrix will be subtracted */
        pattern_distmat_mean = pattern_distmat.meanf();

        points_distmat.extracted(pattern_distmat, points_distmat_mean, pattern_distmat_mean);
        points_distmat_mixed = points_distmat.points_distmat_mixed;
        points_distmat_mono = points_distmat.points_distmat_mono;
        
		pattern_distmat_var_sum = pattern_distmat.var_sum(points_distmat);

     }

	/*** end of data routines *****************************************************/

    private class Shuffle {
    	
    	private int ind;
    	private int[] shuffle_index;
    	
    	public Shuffle(int length) {
    		this.ind = length;
    		this.shuffle_index = new int[length];
    		while(--length >= 0) shuffle_index[length] = length;
    	}
    	
    	private void shuffle() {
            int cnt, ind, tmp;
            for(cnt = 0; cnt < shuffle_index.length; cnt++) {
                ind = irand(shuffle_index.length); // FIXME should choose from remaining rather than all positions
                tmp = shuffle_index[cnt];
                shuffle_index[cnt] = shuffle_index[ind];
                shuffle_index[ind] = tmp;
            }
        }

        int next() {
            if(++ind >= pattern_length) {
                this.shuffle();
                ind = 0;
            }
            return shuffle_index[ind];
        }
    	
    }
    

    /*** end of data shuffling routines 
     * @param progress *******************************************/


    /* the training loop */
    private void mds_train(int cycles, double learning_rate, ProgressMonitor progress)
    {

    	ProgressMonitor spawn = progress.spawn("mds_train", 50);
    	spawn.begin(cycles);

        double[] delta_point = new double[target_dim];
        double[] diffs = new double[pattern_length];

        int m = cycles / 10;

        for (int c = 0, t = 0; c < cycles; c++, t++) {

            if (t == m) {
                t = 0;
//                printf("%3.2f%%: %g  \t(r = %g)\n", 
//                        100.0 * c/cycles, corr_2(), 
//                        1.0/sqrt(corr_2()+1.));
            }

            int i = shuffle .next();
            double[] point = points[i];

            for (int k = 0; k < target_dim; k++) { 
            	delta_point[k] = 0.;
            }
            for (int j = 0; j < pattern_length; j++) {
                if (j == i) continue;
                for(int k = 0; k < target_dim; k++) {
                    delta_point[k] += deriv(i, j, k);
                } 
            }

            double diff_mixed = START_ANNEALING_RATIO * cycles - c;

            if (diff_mixed < 0.)  {
                diff_mixed = learning_rate * (1. + 1. / (1.-START_ANNEALING_RATIO) * diff_mixed / cycles);
            } else {
                diff_mixed = learning_rate;
            }

            for (int k = 0; k < target_dim; k++) {
                double diff = diff_mixed * delta_point[k] / abs(delta_point[k]);
                delta_point[k] = point[k] - diff /* * (2. * frand() - .5) */;
            }

            mds_trackChangeOfMeanMixedMono(delta_point, diffs, i);

            for (int k = 0; k < target_dim; k++) {
                point[k] = delta_point[k];
            }

        	spawn.worked(1);
        } /* for cycles */
        spawn.done();
    }

    /** track change of mean, mixed, mono { */
	private void mds_trackChangeOfMeanMixedMono(double[] delta_point,	double[] diffs, int i) {
		double diff = 0.;
		double diff_mono;

		for (int k = 0; k < pattern_length; k++) {
		    if(k == i) continue;
		    double dtmp = dist.dist(target_dim, delta_point, points[k]);
		    diff += (diffs[k] = dtmp - points_distmat.get(i,k));
		}

		double diffs_mean = diff / points_distmat.length();
		double dtmp = -diffs_mean - points_distmat_mean;
		double diff_mixed = diff_mono = 0.;

		for (int k = 0; k < pattern_length; k++) {
		    if(k == i) continue;
		    double temp = points_distmat.get(i,k);
		    diff_mixed += pattern_distmat.get(i,k) * diffs[k];
		    diff_mono += diffs[k] * (diffs[k] + 2. * (temp + dtmp));
		    points_distmat.set(i,k, ((float) (temp + diffs[k])));
		}

		points_distmat_mean += diffs_mean;
		points_distmat_mixed += diff_mixed; 
		points_distmat_mono += diff_mono + points_distmat.length() * diffs_mean * diffs_mean;

	}


    private void mds_postprocessScaleDataSuchThatStandardDeviationIsOne(ProgressMonitor progress) {
		/*  scale data in such a way that 
   		standard deviation of dimension of largest variance is equal to 1 */
        double maxvar = -1.0;

    	ProgressMonitor loop = progress.spawn("mds_postprocessScaleDataSuchThatStandardDeviationIsOne", 25);
    	loop.begin(target_dim*2);
        for(int j = 0; j < target_dim; j++) {
            double var = 0.;
            for(int i = 0; i < pattern_length; i++)
                var += points[i][j] * points[i][j];
            var /= pattern_length - 1;
            if(var > maxvar) 
                maxvar = var;
            loop.worked(1);
        }

        double var = 1. / sqrt(maxvar);  /* in fact inverse standard deviation */

        for(int j = 0; j < target_dim; j++) {
            for(int i = 0; i < pattern_length; i++) {
                points[i][j] *= var;
            }
            loop.worked(1);
        }
        loop.done();
	}

	private void mds_postprocessMoveCenterToOrigin(ProgressMonitor progress) {
		/* move center to origin */        
    	ProgressMonitor loop = progress.spawn("mds_postprocessMoveCenterToOrigin", 25);
    	loop.begin(target_dim);
        for(int j = 0; j < target_dim; j++) {
            double mean = 0.;
            for(int i = 0; i < pattern_length; i++) {
                mean += points[i][j];
            }
            mean /= pattern_length;
            for(int i = 0; i < pattern_length; i++) {
                points[i][j] -= mean;
            }
            loop.worked(1);
        }
        loop.done();
	}


    @SuppressWarnings("unused")
	private void analyze_corr_deriv() {
//
//        int i, j;
//
//        double derivs[];
//
//        derivs = new double[pattern_dimension];
//
//        for(i = 0; i < pattern_dimension; i++) derivs[i] = 0.;
//
//        for(i = 0; i < pattern_length-1; i++) {
//            for(j = i+1; j < pattern_length; j++) {
//                /* fprintf(stderr, "%d %d %g\n", i, j, corr(pattern_dimension, pattern[i], pattern[j])); */
//                corr_deriv_vec(pattern_dimension, pattern[i], pattern[j], derivs, 1, 1);
//            }
//        }
//
//        for(i = 0; i < pattern_dimension; i++) 
//            printf("%g\n", derivs[i] / (pattern_length * (pattern_length - 1)) );
//
//        derivs = null;
    }


    /* model output */
    @SuppressWarnings("unused")
	private void mds_stdout() {
//
//        int i, j;
//        double sum = 0., tmp;
//
//        if(printqual) {  /* correlation influence w.r.t. points_distmat */ 
//            corr_deriv_vec_flt(points_distmat.length(), pattern_distmat, points_distmat, points_distmat, 0, 0);
//        }
//
//        for(i = 0; i < pattern_length; i++) {
//
//            if(printqual) {
//                sum = 0.;
//                for(j = 0; j < pattern_length; j++) {
//                    tmp = points_distmat.get(i,j);
//                    sum += abs(tmp);
//                }
//            }
//
//            for(j = 0; j < target_dim; j++) printf("%g ", points[i][j]);
//
//            if(printqual)
//                printf("%g\n", sum / (pattern_length-1));
//            else
//                printf("\n");
//        }
    }

    public double[][] run(double[][] data) {
        return run(data, ProgressMonitor.NULL);
    }
    
    public double[][] run(double[][] data, ProgressMonitor progress) {
        if (data.length == 0) return new double[0][];
        // run(100, 1.0, 1.0487507802749606, null, data.length, data[0].length, data, -2, null, progress);
        run(100, 0.01, 1.0487507802749606, null, data.length, data[0].length, data, -2, null, progress);
        return points;
    }

    public double[][] run2(double[][] data) {
        if (data.length == 0) return new double[0][];
        run(10, 1.0, 1.0487507802749606, null, -data.length, 0, data, -2, null, null);
        return points;
    }
    
	public void run(int cycles, double rate, double zero, String forth, 
    		int pattern_length, int pattern_dimension,
            double[][] data, int target_dim, double[][] data2, ProgressMonitor progress) {

    	assert cycles > 0;
    	
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

        // initializeRandomSeed();

        data_stdin(pattern_length, pattern_dimension, data, target_dim, data2);	/* calls data_alloc */ 

        shuffle = new Shuffle(pattern_length = this.pattern_length);
        
//        if(cycles == 0) {  /* for correlation-based analog of variance */
//
//            analyze_corr_deriv();
//
//        }
//        else {

			/* clear data vectors, after distmat creation */
//          printf("# corr(D,d): %g\n", 1./sqrt(corr_2()+1));

        	progress.begin(100);
        
            mds_train(cycles * pattern_length, rate, progress);
//          printf("# corr(D,d): %g\n", 1./sqrt(corr_2()+1));
			
			mds_postprocessMoveCenterToOrigin(progress);
			mds_postprocessScaleDataSuchThatStandardDeviationIsOne(progress);

//			mds_stdout();
        }

//    }

}