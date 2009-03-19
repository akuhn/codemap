package ch.deif.meander.mds;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * @author spupyrev 20.11.2008
 * 
 *         This is the original algorithm found at
 *         http://code.google.com/p/graphvis/. Stored here for testing purposes,
 *         ie to see if our modified version yields the same output as the
 *         original algorithm.
 * 
 *         Multidimensional Scaling according to best distance matrix
 *         reconstruction. Based on signs of directions to maximize correlation
 */
public class HitMDSOriginal {
    private static final boolean DEBUG_OUTPUT = true;

    private static final double M_EXPONENT = 0.0;

    /* linear learning rate annealing at . times cycles */
    private final double START_ANNEALING_RATIO = 0.5;

    private final double EPS = 1e-16;

    private double D(double[] mat, int i, int j) {
        return mat[((i < j) ? (j - 1 + ((((pattern_length << 1) - i - 3) * i) >> 1))
                : ((i == j) ? matsize
                        : (i - 1 + ((((pattern_length << 1) - j - 3) * j) >> 1))))];
    }

    private void DSet(double[] mat, int i, int j, double newVal) {
        mat[((i < j) ? (j - 1 + ((((pattern_length << 1) - i - 3) * i) >> 1))
                : ((i == j) ? matsize
                        : (i - 1 + ((((pattern_length << 1) - j - 3) * j) >> 1))))] = newVal;
    }

    private int pattern_length; /* Length of data series */
    private int pattern_dimension; /* pattern dimension */
    private int matsize; /* number of distance matrix elements */
    private int target_dim; /* dimension of target space */
    private int matrix_input; /* != 0 if data given as distance matrix */

    private double[][] pattern; /* contains float data from STDIN */
    private double[][] points;

    private double points_distmat_mean;
    private double points_distmat_mixed;
    private double points_distmat_mono;
    private double pattern_distmat_var_sum;
    private double pattern_distmat_mean;

    /* save memory space */
    private double[] pattern_distmat;
    private double[] points_distmat;

    private int[] shuffle_index; /* helper for data shuffling */

    private IRandom random = new ModRandom();

    private IDistance distance;

    /* just a mean */
    private double mean(int dimension, double[] p) {
        double sum = 0.;

        for (int i = 0; i < dimension; i++)
            sum += p[i];

        return sum / dimension;
    }

    /* for distance matrix discrepancy evaluation. Min val: 0 */
    private double corr_2() {
        double p2 = points_distmat_mixed * points_distmat_mixed;
        double e2 = EPS * EPS;

        return pattern_distmat_var_sum * points_distmat_mono
                / ((p2 < e2) ? e2 : p2) - 1.0;
    }

    private double preres;
    private int _j = -1;
    private int _k = -1;

    /* derivative of HiT-MDS stress function */
    private double deriv(int j, int k, int idx) {
        double d = D(points_distmat, j, k);

        if (_j != j || _k != k) {

            double DD = D(pattern_distmat, j, k), dif = d - points_distmat_mean;

            _j = j;
            _k = k;

            preres = dif * points_distmat_mixed - DD * points_distmat_mono;
        }

        return preres * (points[j][idx] - points[k][idx])
                / ((d < EPS) ? EPS : d);
    }

    /******************************************************************************/
    /* Some data handling routines */
    /******************************************************************************/

    private void data_alloc() {
        /* also valid for neg values */
        matsize = ((pattern_length - 1) * pattern_length) >> 1;

        /* upper triangle matrix */
        pattern_distmat = new double[(1 + matsize)];
        points_distmat = new double[(1 + matsize)];

        /* init access to diagonal elements: 0 by default */
        DSet(points_distmat, 0, 0, 0);
        DSet(pattern_distmat, 0, 0, 0);

        if (pattern_dimension > 0) {
            /* patterns not given as distance matrix */
            pattern = new double[pattern_length][];
            for (int i = 0; i < pattern_length; i++) {
                pattern[i] = new double[pattern_dimension];
            }
        }
    }

    private void data_stdin(double[][] a, int destDim) {
        double[] random_matrix = null;
        int tdim;

        pattern_length = a.length;
        pattern_dimension = a[0].length;

        data_alloc();

        if (pattern_length < 0) /* read full distance matrix */
        matrix_input = pattern_length = pattern_dimension = -pattern_length;
        else matrix_input = 0;

        for (int line = 0; line < pattern_length; line++) {
            for (int column = 0; column < pattern_dimension; column++) {
                if (matrix_input != 0) {
                    double dtmp = a[line][column];
                    DSet(pattern_distmat, line, column, dtmp);
                } else {
                    pattern[line][column] = a[line][column];
                }
            }
        }

        target_dim = destDim;

        if ((tdim = target_dim) < 0) {
            target_dim = -target_dim;

            if (matrix_input == 0) { /* init by random projection */
                int j = target_dim * pattern_dimension;

                random_matrix = new double[j];

                while (--j >= 0)
                    random_matrix[j] = random.nextDouble();
            }
        }

        /* ABC: should be moved to data_alloc */
        points = new double[pattern_length][];

        for (int line = 0; line < pattern_length; line++) {
            points[line] = new double[target_dim];

            for (int column = 0; column < target_dim; column++) {
                if (tdim > 0) { /* read from input */
                    // TODO!!!
                    // if(1 != scanf(SCANFMT, points[line]+column))
                    // {
                    // char panistr[100];
                    // sprintf(panistr,
                    // "data_stdin(): Trouble reading pre-inited points in line #%d/%d, column #%d/%d !",
                    // line+off, pattern_length, column+1, target_dim);
                    // error(panistr);
                    // }
                } else {
                    if (matrix_input != 0) { /* random init */
                        points[line][column] = 2. * random.nextDouble() - 1.;
                    } else { /* random projection */
                        double dtmp = 0.0;

                        for (int j = 0; j < pattern_dimension; j++)
                            dtmp += random_matrix[column * pattern_dimension
                                    + j]
                                    * pattern[line][j];

                        points[line][column] = dtmp;
                    }
                }
            }
        }
    }

    private void data_init() {
        /* fill distance matrices */
        for (int i = 0; i < pattern_length - 1; i++) {
            for (int j = i + 1; j < pattern_length; j++) {
                if (matrix_input == 0)
                    DSet(pattern_distmat, i, j, distance.distance(
                            pattern_dimension, pattern[i], pattern[j]));
                DSet(points_distmat, i, j, distance.distance(target_dim,
                        points[i], points[j]));
            }
        }

        /* initial values of point distance matrix, mean, mixed, mono */
        points_distmat_mean = mean(matsize, points_distmat);

        /* mean value of pattern distance matrix will be subtracted */
        pattern_distmat_mean = mean(matsize, pattern_distmat);

        points_distmat_mixed = points_distmat_mono = pattern_distmat_var_sum = 0.;

        for (int i = 0; i < matsize; i++) {
            double tmp = points_distmat[i] - points_distmat_mean;
            points_distmat_mixed += tmp
                    * (pattern_distmat[i] -= pattern_distmat_mean);
            points_distmat_mono += tmp * tmp;
            pattern_distmat_var_sum += pattern_distmat[i] * pattern_distmat[i];
        }

    }

    /*** end of data routines *****************************************************/

    /******************************************************************************/
    /* Some data shuffling routines */
    /******************************************************************************/
    private void shuffle_index_alloc() {
        int len = pattern_length;

        shuffle_index = new int[len];

        while (--len >= 0)
            shuffle_index[len] = len;
    }

    private void shuffle_do() {
        if (shuffle_index == null) shuffle_index_alloc();

        for (int cnt = 0; cnt < pattern_length; cnt++) {
            int ind = random.nextInt(pattern_length);
            int tmp = shuffle_index[cnt];
            shuffle_index[cnt] = shuffle_index[ind];
            shuffle_index[ind] = tmp;
        }
    }

    private int ind = Integer.MAX_VALUE - 1;

    private int shuffle_next() {
        if (++ind >= pattern_length) {
            shuffle_do();
            ind = 0;
        }

        return shuffle_index[ind];
    }

    /*** end of data shuffling routines *******************************************/

    /* the training loop */
    private void mds_train(int cycles, double learning_rate) {
        double[] delta_point;
        // double[] point;
        double dtmp;
        double diff, diff_mixed, diff_mono, diffs_mean;
        double[] diffs;

        double ptmp;

        int c, k, i, j, m, t;

        delta_point = new double[target_dim];
        diffs = new double[pattern_length];

        t = 0;
        m = cycles / 10;

        for (c = 0; c < cycles; c++) {
            if (++t == m) {
                t = 0;
                if (DEBUG_OUTPUT)
                    System.err.println(100.0 * c / cycles + "%: " + corr_2()
                            + " \t(r = " + 1.0 / sqrt(corr_2() + 1.0) + ")");
            }

            i = shuffle_next();
            // System.out.println("shuffle=" + i);

            for (k = 0; k < target_dim; k++)
                delta_point[k] = 0.;

            for (j = 0; j < pattern_length; j++) {
                if (j != i) {
                    for (k = 0; k < target_dim; k++) {
                        delta_point[k] += deriv(i, j, k);
                    }
                }
            }

            if ((diff_mixed = START_ANNEALING_RATIO * cycles - c) < 0.) {
                diff_mixed = learning_rate
                        * (1.0 + 1.0 / (1.0 - START_ANNEALING_RATIO)
                                * diff_mixed / cycles);
            } else {
                diff_mixed = learning_rate;
            }

            for (k = 0; k < target_dim; k++) {
                diff = diff_mixed * delta_point[k] / abs(delta_point[k]);
                delta_point[k] = points[i][k] - diff;
            }

            /* track change of mean, mixed, mono { */
            diff = 0.0;

            for (k = 0; k < pattern_length; k++)
                if (k != i) {
                    dtmp = distance
                            .distance(target_dim, delta_point, points[k]);
                    diff += (diffs[k] = dtmp - D(points_distmat, i, k));
                }

            diffs_mean = diff / matsize;

            dtmp = -diffs_mean - points_distmat_mean;

            diff_mixed = diff_mono = 0.;

            for (k = 0; k < pattern_length; k++)
                if (k != i) {
                    ptmp = D(points_distmat, i, k);

                    diff_mixed += D(pattern_distmat, i, k) * diffs[k];

                    diff_mono += diffs[k] * (diffs[k] + 2. * (ptmp + dtmp));

                    DSet(points_distmat, i, k, ptmp + diffs[k]);
                }

            points_distmat_mean += diffs_mean;

            points_distmat_mixed += diff_mixed;

            points_distmat_mono += diff_mono + matsize * diffs_mean
                    * diffs_mean;

            /* } track changes of mean, mixed, mono */

            for (k = 0; k < target_dim; k++)
                points[i][k] = delta_point[k];

        }
    }

    /*
     * move center to origin, scale data in such a way that standard deviation
     * of dimension of largest variance is equal to 1
     */
    private void mds_postprocess() {
        double var = 1.0, maxvar = -1.0;

        for (int j = 0; j < target_dim; j++) {
            double mean = 0.;

            for (int i = 0; i < pattern_length; i++)
                mean += points[i][j];

            mean /= pattern_length;

            for (int i = 0; i < pattern_length; i++)
                points[i][j] -= mean;
        }

        for (int j = 0; j < target_dim; j++) {
            var = 0.0;

            for (int i = 0; i < pattern_length; i++)
                var += points[i][j] * points[i][j];

            var /= pattern_length - 1;

            if (var > maxvar) maxvar = var;
        }

        /* in fact inverse standard deviation */
        var = 1.0 / Math.sqrt(maxvar);

        for (int j = 0; j < target_dim; j++)
            for (int i = 0; i < pattern_length; i++)
                points[i][j] *= var;
    }

    /* model output */
    private double[][] mds_stdout() {
        double[][] res = new double[pattern_length][target_dim];
        for (int i = 0; i < pattern_length; i++) {
            for (int j = 0; j < target_dim; j++) {
                res[i][j] = points[i][j];
                if (DEBUG_OUTPUT) System.out.print(points[i][j] + " ");
            }

            if (DEBUG_OUTPUT) {
                System.out.println("");
            }
        }

        return res;
    }

    // ////////////////PUBLIC PART////////////////////////////
    public double[][] evaluate(double[][] a, int destDim) {
        int cycles = 10;
        assert (cycles != 0);

        double rate = 1;

        // distance = DistanceRegistry.createMinkowskiDistance();
        // distance = DistanceRegistry.createQuadraticEuclideanDistance();
        // distance = DistanceRegistry.createSpearmanDistance();
        // distance =
        // DistanceRegistry.createPearsonCorrelationDistance(M_EXPONENT == 0 ?
        // 1.0 : M_EXPONENT);
        // distance = DistanceRegistry.createCorr_Deriv_Vec();
        distance = DistanceRegistry.createEuclideanDistance();

        /* calls data_alloc */
        data_stdin(a, destDim);

        /* calc distmats */
        data_init();

        if (DEBUG_OUTPUT)
            System.out.println("# corr(D,d): " + 1. / sqrt(corr_2() + 1));

        mds_train(cycles * pattern_length, rate);

        mds_postprocess();

        return mds_stdout();
    }
}
