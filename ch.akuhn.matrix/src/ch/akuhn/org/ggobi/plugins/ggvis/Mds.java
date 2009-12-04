package ch.akuhn.org.ggobi.plugins.ggvis;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import ch.akuhn.matrix.Function;
import ch.akuhn.matrix.Matrix;
import ch.akuhn.matrix.SymetricMatrix;

/** Multidimensional scaling.
 * Initially ported from ggvis and greatly rewritten in Java by Adrian Kuhn.
 * Original copyright notice:
 *<PRE>/<span/>*
 * * mds.c: multidimensional scaling
 * *  code originally written for xgvis by Michael Littman, greatly extended
 * *  and tuned by Andreas Buja.  Now being ported to ggvis.
 * *<span/>/</PRE>
 * @author Adrian Kuhn (this in Java)
 * @author Andreas Buja (ggvis in C/C++)
 * @author Michael Littman (xgvis in C/C++)
 * 
 */
public class Mds {

    static final int ANCHOR = 2;

    private static boolean ANCHOR_FIXED = false;
    private static boolean ANCHOR_SCALE = false;
    static final double delta = 1E-10;

    static final int DRAGGED = 4;
    private static final boolean TODO_SYMMETRY = false;
    final SymetricMatrix config_dist;
    final Matrix Dtarget; /*-- D in the documentation; dist in the xgvis code --*/

    private Points pos;
    /* these belong in ggv */
    public double stress;

    private double stress_dx, stress_dd, stress_xx;

    private final double sig_pow(double x, double p) {
        return (x >= 0.0 ? pow(x, p) : -pow(-x, p));
    }

    private double stepsize = 0.02;

    private double dist_power = 1.0;
    private double Dtarget_power = 1.0;
    private double lnorm = 2.0;
    private double weight_power = 0.0;

    private double dist_power_over_lnorm = 0.5;
    private double lnorm_over_dist_power = 2.0;
    private double within_between = 1.0;
    private double rand_select_val = 1.0;  /* selection probability */
    private double threshold_high = 0.0;
    private double threshold_low = 0.0;

    private MDSGroupInd group_ind = MDSGroupInd.all_distances;

    /*-- used in mds.c --*/
    private Matrix weights = null;
    final private Points gradient;

    private double Dtarget_max = Double.MAX_VALUE;
    private double Dtarget_min = Double.MIN_VALUE;

    private final int len;
    /* */
    
    public void init(boolean randomPoints) {

        /* populate with INF */
        this.Dtarget.apply(f_dtarget);

        this.Dtarget_max = Dtarget.max();  
        this.Dtarget_min = Dtarget.min();

        if (Dtarget_min < 0) throw new Error("negative dissimilarity: D[?][?] = ? -> NA\n");

        this.threshold_low =  this.Dtarget_min;
        this.threshold_high = this.Dtarget_max;
        
        if (randomPoints) {
            for (int a = 0; a < pos.x.length; a++) {
                this.pos.x[a] = (Math.random() - 0.5) * 2;
                this.pos.y[a] = (Math.random() - 0.5) * 2;
            }
        }

        this.config_dist.fill(Double.NaN);
        set_weights();

    }
    public Mds(Matrix dissimilarities, Points initial,  
            Function fConfigDist, Function fWeights, Function fDtarget) {
        len = dissimilarities.rowCount();
        Dtarget = dissimilarities;
        config_dist = new SymetricMatrix(len);
        this.pos = initial == null ? new Points(len) : initial;
        this.gradient = new Points(len);
        f_config_dist = fConfigDist;
        f_weights = fWeights;
        f_dtarget = fDtarget;
        this.init(initial == null);
    }

    private final Function f_config_dist;
    private final Function f_weights;
    private final Function f_dtarget;
    private boolean IS_ANCHOR(int i) {
        return i % 100 == 0;
    }
    boolean IS_DRAGGED(int i) {
        return false;
    }
    private double Lp_distance_pow (int i, int j) {
        double dsum = 0.0;
        if (this.lnorm == 2. && this.dist_power == 1.) {
            dsum += (this.pos.x[i] - this.pos.x[j]) * (this.pos.x[i] - this.pos.x[j]);
            dsum += (this.pos.y[i] - this.pos.y[j]) * (this.pos.y[i] - this.pos.y[j]);
            return (sqrt(dsum));
        } else { /* non-Euclidean or Dtarget power != 1. */
            dsum += pow (abs (this.pos.x[i] - this.pos.x[j]), this.lnorm);
            dsum += pow (abs (this.pos.y[i] - this.pos.y[j]), this.lnorm);
            return (pow(dsum, this.dist_power_over_lnorm));
        }
    }
    /**
     * Perform one loop of the iterative mds function.
     *<P>
     * If doit is False, then we really want to determine the
     * stress function without doing anything to the gradient
     */
    public void mds_once (boolean doit) {

        mds_once_part2();
        mds_once_part3(doit); /* close:  if (doit && num_active_dist > 0)  */

        /* experiment: normalize point cloud after using simplified gradient */
        // FIXME can we avoid this?
        pos.ggv_center_scale_pos();

    }
    private void mds_once_part2() {
        // allocate position and compute means
        pos.get_center();
        // i's are moved by j's
        double[][] values = this.Dtarget.unwrap();
        for (int i = 0; i < values.length; i++) {
            // these points are not moved by the gradient
            if (IS_DRAGGED(i) || (ANCHOR_FIXED && IS_ANCHOR(i))) continue;
            /* j's are moving i's */
            for (int j = 0; j < i; j++) {
                if (mds_once_part2_continue(i, j)) continue;
                values[i][j] = f_config_dist.apply(Lp_distance_pow(i, j));
            }
        }
    }

    private boolean mds_once_part2_continue(int i, int j) {

        /* these points do not contribute to the gradient */
        if ((ANCHOR_SCALE || ANCHOR_FIXED) && !IS_ANCHOR(j) && !IS_DRAGGED(j)) return true;
        if ((ANCHOR_SCALE || ANCHOR_FIXED) && !IS_ANCHOR(i) && !IS_DRAGGED(i)) return true;

        /* if the target distance is missing, skip */
        if (Double.isNaN(this.Dtarget.get(i,j))) return true;

        /* if weight is zero, skip */
        if (this.weights != null && this.weights.get(i,j) == 0.) return true;

        /* using groups */
        if (this.group_ind == MDSGroupInd.within && !SAMEGLYPH(i,j)) return true;
        if (this.group_ind == MDSGroupInd.between && SAMEGLYPH(i,j)) return true;

        /*
         * if the target distance is within the thresholds
         * set using the barplot of distances, keep going.
         */
        if (this.Dtarget.get(i,j) < this.threshold_low ||
                this.Dtarget.get(i,j) > this.threshold_high) return true;

        /*
         * random selection: needs to be done symmetrically
         */
        if (this.rand_select_val < 1.0) {
            //if (i < j && this.rand_sel.vals[i][j] > this.rand_select_val) continue;
            //if (i > j && this.rand_sel.vals[j][i] > this.rand_select_val) continue;
        }

        /*
         * zero weights:
         * assume weights exist if test is positive, and
         * can now assume that weights are >0 for non-NA
         */
        if (!doesNotWeight()) {
            if (this.weights.get(i,j) == 0.) return true;
        }

        return false;

    }
    private void mds_once_part3(boolean doit) {
        power_transform();
        update_stress();

        /* --- for active dissimilarities, do the gradient push if asked for ----*/
        if (doit) {
            /* Zero out the gradient matrix. */
            this.gradient.clear();
            /* ------------- gradient accumulation: j's push i's ----------- */
            for (int i = 0; i < this.Dtarget.rowCount(); i++) {
                for (int j = 0; j < i; j++) {
                    double weight;
                    double dist_trans  = this.Dtarget.get(i,j);
                    if (Double.isNaN(dist_trans)) continue;
                    double dist_config = this.config_dist.get(i,j);
                    if (abs(dist_config) < delta) dist_config = delta;
                    if (doesNotWeight()) {
                        weight = 1.0;
                    } else {
                        weight = this.weights.get(i,j);
                    }
                    mds_once_part3_gradient(dist_trans, dist_config, weight, i, j);
                }
            }

            /* center the classical gradient */
            double gfactor = mds_once_part3_normalizeGradient();

            /* add the gradient matrix to the position matrix and drag points */
            for (int i=0; i<this.pos.x.length; i++) {
                if (!IS_DRAGGED(i)) {
                    this.pos.x[i] += (gfactor * this.gradient.x[i]);
                    this.pos.y[i] += (gfactor * this.gradient.y[i]);
                } else {
                    throw null;
                    //		        for (int k=0; k < this.dim; k++)
                    //		          this.pos.vals[i][k] = dpos.tform.vals[i][k] ;
                }
            }


        }
    }
    private void mds_once_part3_gradient(double dist_trans,
            double dist_config, double weight, int i, int j) {
        double resid;
        double step_mag;
        // scale independent version: */
        resid = (dist_trans - stress_dx / stress_xx * dist_config);
        // scale dependent version:
        // resid = (dist_trans - dist_config);
        if (this.lnorm != 2) {
            assert TODO_SYMMETRY;
            /* non-Euclidean Minkowski/Lebesgue metric */
            step_mag = weight * resid *
            pow (dist_config, 1 - this.lnorm_over_dist_power);
            for (int k = 0; k < 2; k++) {
                this.gradient.x[i] += step_mag * sig_pow(this.pos.x[i]-this.pos.x[j], this.lnorm-1.0);
                this.gradient.y[i] += step_mag * sig_pow(this.pos.y[i]-this.pos.y[j], this.lnorm-1.0);
                this.gradient.x[j] += step_mag * sig_pow(this.pos.x[j]-this.pos.x[i], this.lnorm-1.0);
                this.gradient.y[j] += step_mag * sig_pow(this.pos.y[j]-this.pos.y[i], this.lnorm-1.0);
            }
        } else { /* Euclidean Minkowski/Lebesgue metric */
            /* Note the simplification of the code for the special
             * cases when dist_power takes on an integer value.  */
            if (this.dist_power == 1)
                step_mag = weight * resid / dist_config;
            else if(this.dist_power == 2)
                step_mag = weight * resid;
            else if (this.dist_power == 3)
                step_mag = weight * resid * dist_config;
            else if (this.dist_power == 4)
                step_mag = weight * resid * dist_config * dist_config;
            else
                step_mag = weight * resid *
                pow(dist_config, this.dist_power-2.);
            this.gradient.x[i] += step_mag * (this.pos.x[i]-this.pos.x[j]); /* Euclidean! */
            this.gradient.y[i] += step_mag * (this.pos.y[i]-this.pos.y[j]); /* Euclidean! */
            this.gradient.x[j] += step_mag * (this.pos.x[j]-this.pos.x[i]); /* Euclidean! */
            this.gradient.y[j] += step_mag * (this.pos.y[j]-this.pos.y[i]); /* Euclidean! */
        }

    }
    /** gradient normalizing factor to scale gradient to a fraction of
		   the size of the configuration */
    private double mds_once_part3_normalizeGradient() {
        double gsum = 0, psum = 0;
        for (int i=0; i<this.pos.x.length; i++) {
            // if (true || (ANCHOR_SCALE && IS_ANCHOR(i)))
            gsum += pos.L2_norm (this.gradient.x[i], this.gradient.y[i]);
            psum += pos.L2_norm (this.pos.x[i], this.pos.y[i]);
        }
        return (gsum < delta) ? 0.0 : this.stepsize * sqrt(psum/gsum);
    }

    public double[][] points() {
        return pos.points();
    }

    /* we assume in this routine that trans_dist contains
   dist.data for KruskalShepard and
   -dist.data*dist.data for CLASSIC MDS */
    private void power_transform () {

        if (this.Dtarget_power == 1.) {
            return;
        } else if (this.Dtarget_power == 2.) {
            throw null;
            //    if (this.KruskalShepard_classic == MDSKSInd.KruskalShepard) {
            //      for (i=0; i<this.ndistances; i++) {
            //        tmp = this.trans_dist.els[i];
            //        if (tmp != Double.MAX_VALUE)
            //          this.trans_dist.els[i] = tmp*tmp/this.Dtarget_max;
            //      }
            //    } else {
            //      for (i=0; i<this.ndistances; i++) {
            //        tmp = this.trans_dist.els[i];
            //        if (tmp != Double.MAX_VALUE)
            //          this.trans_dist.els[i] = -tmp*tmp/this.Dtarget_max;
            //      }
            //    }
        } else {
            throw null;
            //    fac = pow (this.Dtarget_max, this.Dtarget_power-1);
            //    if (this.KruskalShepard_classic == MDSKSInd.KruskalShepard) {
            //      for(i=0; i<this.ndistances; i++) {
            //        tmp = this.trans_dist.els[i];
            //        if (tmp != Double.MAX_VALUE)
            //          this.trans_dist.els[i] = pow(tmp, this.Dtarget_power)/fac;
            //      }
            //    } else {
            //      for(i=0; i<this.ndistances; i++) {
            //        tmp = this.trans_dist.els[i];
            //        if(tmp != Double.MAX_VALUE)
            //          this.trans_dist.els[i] = -pow(-tmp, this.Dtarget_power)/fac;
            //      }
            //    }
        }

    } /* end power_transform() */
    private boolean SAMEGLYPH(int i, int j) {
        return (i / 100) == (j / 100);
    }

    /*
     * weights are only set if weightpow != 0; for 0 there's simpler
     *code throughout, and we save space
     */
    private void set_weights () {

        double this_weight;
        double local_weight_power = 0.;
        double local_within_between = 1.;

        this.weights = new SymetricMatrix(Dtarget.rowCount());
        for (int i = 0; i < Dtarget.rowCount(); i++) {
            for (int j = 0; j < i; j++) {
                this.weights.put(i,j, f_weights.apply(this.Dtarget.get(i,j)));
            }
        }
        
        this.weights = null;
        
        /* the weights will be used in metric and nonmetric scaling
         * as soon as weightpow != 0. or within_between != 1.
         * weights vector only if needed */
//        if ((this.weight_power != local_weight_power &&
//                this.weight_power != 0.) ||
//                (this.within_between != local_within_between &&
//                        this.within_between != 1.))
//        {
//            assert false; // TODO
//
//            for (i=0; i<this.Dtarget.vals.length; i++) {
//                for (j=0; j<this.Dtarget.vals.length; j++) {
//                    if (Double.isNaN(this.Dtarget.vals[i][j])) {
//                        this.weights.vals[i][j] = Double.NaN;
//                        continue;
//                    }
//                    if (this.weight_power != 0.) {
//                        if(this.Dtarget.vals[i][j] == 0.) { /* cap them */
//                            if (this.weight_power < 0.) {
//                                this.weights.vals[i][j] = 1E5;
//                                continue;
//                            }
//                            else {
//                                this.weights.vals[i][j] = 1E-5;
//                            }
//                        }
//                        this_weight = pow(this.Dtarget.vals[i][j], this.weight_power);
//                        /* cap them */
//                        if (this_weight > 1E5)  this_weight = 1E5;
//                        else if (this_weight < 1E-5) this_weight = 1E-5;
//                        /* within-between weighting */
//                        if (SAMEGLYPH(i,j))
//                            this_weight *= (2. - this.within_between);
//                        else
//                            this_weight *= this.within_between;
//                        this.weights.vals[i][j] = this_weight;
//                    } else { /* weightpow == 0. */
//                        if (SAMEGLYPH(i,j))
//                            this_weight = (2. - this.within_between);
//                        else
//                            this_weight = this.within_between;
//                        this.weights.vals[i][j] = this_weight;
//                    }
//                }
//            }
//        }
    } /* end set_weights() */


    private void update_stress () {
        stress_dx = stress_xx = stress_dd = 0;
        for (int i=0; i < this.Dtarget.rowCount(); i++)
            for (int j=0; j < i; j++) {
                double dist_trans  = this.Dtarget.get(i,j) * 2; // symmetry!
                if (Double.isNaN(dist_trans)) continue;
                double dist_config = this.config_dist.get(i,j) * 2; // symmetry!
                if (doesNotWeight()) {
                    stress_dx += dist_trans  * dist_config;
                    stress_xx += dist_config * dist_config;
                    stress_dd += dist_trans  * dist_trans;
                } else {
                    double this_weight = this.weights.get(i,j) * 2; // symmetry!
                    stress_dx += dist_trans  * dist_config * this_weight;
                    stress_xx += dist_config * dist_config * this_weight;
                    stress_dd += dist_trans  * dist_trans  * this_weight;
                }
            }

        /* calculate stress and draw it */
        if (stress_dd * stress_xx > delta*delta) {
            stress = pow( 1.0 - stress_dx * stress_dx / stress_xx / stress_dd, 0.5);
            // FIXME add_stress_value (stress, ggv);
            // draw_stress (ggv, gg);
        } else {
//            FIXME do we need to throw an error? this results in an error for very small projects
//            throw new Error("didn't draw stress: stress_dx = " + stress_dx + " stress_dd = " + stress_dd + " stress_xx = " + stress_xx);
        }
    } /* end update_stress() */

    private boolean doesNotWeight() {
        return this.weight_power == 0. && this.within_between == 1.;
    }

    enum MDSAnchorInd {fixed, no_anchor, scaled};

    enum MDSGroupInd {all_distances, between, within};

}