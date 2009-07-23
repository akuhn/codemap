package ch.akuhn.org.ggobi.plugins.ggvis;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/** Multidimensional scaling.
 * Initially ported from ggvis and greatly rewritten in Java by Adrian Kuhn.
 * Original copyright notice:
 *<PRE>/<span/>*
 * * mds.c: multidimensional scaling 
 * *  code originally written for xgvis by Michael Littman, greatly extended
 * *  and tuned by Andreas Buja.  Now being ported to ggvis.
 * *<span/>/</PRE>
 * @author Adrian Kuhn (this in Java)
 * @author Andreas Buja (ggvis in C++)
 * @author Michael Littman (xgvis in C++)
 * 
 */
public class Mds {

	static final int ANCHOR = 2;

	private static boolean ANCHOR_FIXED = false;
	private static boolean ANCHOR_SCALE = false;
	static final double delta = 1E-10;

	static final int DRAGGED = 4;
	private static final boolean TODO_SYMMETRY = false;
	private SMat config_dist;
	private static final int DIM = 2;
	private double dist_power; /* was mds_distpow */
	private double dist_power_over_lnorm;
	private SMat Dtarget; /*-- D in the documentation; dist in the xgvis code --*/

	/* begin centering and sizing routines */

	private double Dtarget_max, Dtarget_min;
	private double Dtarget_power; /* was mds_power */
	private Points gradient;
	private MDSGroupInd group_ind;
	/* end centering and sizing routines */

	private double lnorm;
	private double lnorm_over_dist_power;
	private Points pos;
	private double[] pos_mean;
	private double pos_scl;
	private double rand_select_val;
	private double stepsize;
	/* these belong in ggv */
	@SuppressWarnings("unused")
	private double stress, stress_dx, stress_dd, stress_xx;
	private double threshold_high;
	private double threshold_low;
	private SMat trans_dist;
	private double weight_power; /* was mds_weightpow */
	private SMat weights;
	private double within_between;

	private final double sig_pow(double x, double p) {
		return (x >= 0.0 ? pow(x, p) : -pow(-x, p));
	}

	{
		this.stepsize = 0.02;

		this.dist_power = 1.0;
		this.Dtarget_power = 1.0;
		this.lnorm = 2.0;
		this.weight_power = 0.0;

		this.dist_power_over_lnorm = 0.5;
		this.lnorm_over_dist_power = 2.0;
		this.within_between = 1.0;
		this.rand_select_val = 1.0;  /* selection probability */
		this.threshold_high = 0.0;
		this.threshold_low = 0.0;

		this.group_ind = MDSGroupInd.all_distances;

		/*-- used in mds.c --*/
		this.pos_mean = new double[] {};
		this.weights = new SMat(0);
		this.gradient = new Points(0,0);

		this.pos_scl = 0.0;
		this.Dtarget_max = Double.MAX_VALUE;
		this.Dtarget_min = Double.MIN_VALUE;
		/* */

		/* weight vector */
		set_weights();

	}
	private double dot_prod (int i, int j ) {
		double dsum = 0.0;
			dsum += (this.pos.x[i] - this.pos_mean[0]) *
			(this.pos.x[j] - this.pos_mean[0]);
			dsum += (this.pos.y[i] - this.pos_mean[1]) *
			(this.pos.y[j] - this.pos_mean[1]);
		return(dsum);
	}
	private void get_center () {
		this.pos_mean = new double[Mds.DIM];

		int n = 0;

		for (int i=0; i<this.pos.nrows; i++) {
			if (IS_DRAGGED(i)) continue; 
			this.pos_mean[0] += this.pos.x[i];
			this.pos_mean[1] += this.pos.y[i];
			n++;
		}
		this.pos_mean[0] /= n;
		this.pos_mean[1] /= n;
	}
	private void get_center_scale () {
		int n, i, k;

		get_center ();
		n = 0;
		this.pos_scl = 0.;

		for(i=0; i<this.pos.nrows; i++) {
			if (IS_DRAGGED(i)) continue;
			for (k=0; k<Mds.DIM; k++) {
				this.pos_scl += ((this.pos.x[i] - this.pos_mean[0]) *
						(this.pos.x[i] - this.pos_mean[0]));
				this.pos_scl += ((this.pos.y[i] - this.pos_mean[1]) *
						(this.pos.y[i] - this.pos_mean[1]));
			}
			n++;
		}
		this.pos_scl = sqrt(this.pos_scl/(double)n/(double)Mds.DIM);
	}

	private void ggv_center_scale_pos() {
		int i, k;

		get_center_scale();

		for (i=0; i<this.pos.nrows; i++) {
			if (IS_DRAGGED(i)) continue;
			this.pos.x[i] = (this.pos.x[i] - this.pos_mean[0])/this.pos_scl;
			this.pos.y[i] = (this.pos.y[i] - this.pos_mean[1])/this.pos_scl;
		}
	}
	public void init(double[][] dissimilarities) {		

		this.Dtarget = new SMat(dissimilarities.length);
		this.trans_dist = new SMat(dissimilarities.length);
		this.config_dist = new SMat(dissimilarities.length);
		double infinity, largest = Double.NaN;

		/*-- initalize Dtarget --*/
		infinity = (double) (2 * this.Dtarget.vals.length);
		largest = dissimilarities[0][0];
		for (int a3 = 0; a3 < dissimilarities.length; a3++) {
			for (int b3 = 0; b3 < dissimilarities.length; b3++) {
				if (dissimilarities[a3][b3] > infinity) {
					infinity = dissimilarities[a3][b3];
				}
				if (dissimilarities[a3][b3] > largest) {
					largest = dissimilarities[a3][b3];
				}

			}
		}

		/* Report the value of largest */
		if (largest > 100000) throw new Error("Warning: your largest weight, %.2f (index %d), is extremely large. ");

		/* Continue to initialize using the value of infinity.  Is that ok? */
		for (int a2 = 0; a2 < dissimilarities.length; a2++) {
			for (int b2 = 0; b2 < a2; b2++) {
				this.Dtarget.vals[a2][b2] = infinity;
			}
		}  /* populate with INF */
		for (int a1 = 0; a1 < dissimilarities.length; a1++) {
			for (int b1 = 0; b1 < a1; b1++) {
				this.Dtarget.vals[a1][b1] = dissimilarities[a1][b1];
			}
		}

		this.Dtarget_max = Double.MIN_VALUE;  this.Dtarget_min = Double.MAX_VALUE;
		for (int i=0; i<this.Dtarget.vals.length; i++) {
			for (int j=0; j<i; j++) {
				double dtmp = this.Dtarget.vals[i][j]; 
				if (dtmp < 0) {
					throw new Error("negative dissimilarity: D[%d][%d] = %3.6f -> NA\n");
					// FIXME dtmp = Dtarget.vals[i][j] = Mds.G_MAXDOUBLE;
				}
				if(dtmp != Double.NaN) {
					if (dtmp > this.Dtarget_max) this.Dtarget_max = dtmp;
					if (dtmp < this.Dtarget_min) this.Dtarget_min = dtmp;
				}
			}
		}
		this.threshold_low =  this.Dtarget_min;
		this.threshold_high = this.Dtarget_max;

		this.pos = new Points(dissimilarities.length, DIM);
		this.gradient = new Points(dissimilarities.length, DIM);
		for (int a = 0; a < pos.x.length; a++) {
			this.pos.x[a] = (Math.random() - 0.5) * 2;
			this.pos.y[a] = (Math.random() - 0.5) * 2;
		}

		this.trans_dist.fill(Double.NaN);
		this.config_dist.fill(Double.NaN);

	}
	private boolean IS_ANCHOR(int i) {
		return i % 100 == 0;
	}
	private boolean IS_DRAGGED(int i) {
		return false;
	}
	private double	L2_norm (double x, double y) {
		double dsum = 0.0;
		dsum += (x - this.pos_mean[0])*(x - this.pos_mean[0]);
		dsum += (y - this.pos_mean[1])*(y - this.pos_mean[1]);
		return(dsum);
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
		// ggv_center_scale_pos ();

	}
	private void mds_once_part2() {
		// allocate position and compute means 
		get_center();
		// i's are moved by j's 
		for (int i = 0; i < this.Dtarget.vals.length; i++) {
			// these points are not moved by the gradient 
			if (IS_DRAGGED(i) || (ANCHOR_FIXED && IS_ANCHOR(i))) continue;
			/* j's are moving i's */    
			for (int j = 0; j < i; j++) {
				if (mds_once_part2_continue(i, j)) continue;
				this.config_dist.vals[i][j] = Lp_distance_pow(i, j);
				this.trans_dist.vals[i][j] = this.Dtarget.vals[i][j];
			}
		} 
	}
	
	private boolean mds_once_part2_continue(int i, int j) {

		/* these points do not contribute to the gradient */
		if ((ANCHOR_SCALE || ANCHOR_FIXED) && !IS_ANCHOR(j) && !IS_DRAGGED(j)) return true;
		if ((ANCHOR_SCALE || ANCHOR_FIXED) && !IS_ANCHOR(i) && !IS_DRAGGED(i)) return true;

		/* if the target distance is missing, skip */
		if (Double.isNaN(this.Dtarget.vals[i][j])) return true;

		/* if weight is zero, skip */
		if (this.weights != null && this.weights.vals.length != 0 && this.weights.vals[i][j] == 0.) return true;

		/* using groups */
		if (this.group_ind == MDSGroupInd.within && !SAMEGLYPH(i,j)) return true;
		if (this.group_ind == MDSGroupInd.between && SAMEGLYPH(i,j)) return true;

		/*
		 * if the target distance is within the thresholds
		 * set using the barplot of distances, keep going.
		 */
		if (this.Dtarget.vals[i][j] < this.threshold_low || 
				this.Dtarget.vals[i][j] > this.threshold_high) return true;

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
		if (this.weight_power != 0. || this.within_between != 1.) {
			if (this.weights.vals[i][j] == 0.) return true;
		}        

		return false;

	}
	private void mds_once_part3(boolean doit) {
		power_transform();
		update_stress();

		/* --- for active dissimilarities, do the gradient push if asked for ----*/
		if (doit) {
			/* Zero out the gradient matrix. */
			this.gradient.arrayd_zero();
			/* ------------- gradient accumulation: j's push i's ----------- */
			for (int i = 0; i < this.trans_dist.vals.length; i++) {
				for (int j = 0; j < i; j++) {
					double weight;
					double dist_trans  = this.trans_dist.vals[i][j];
					if (Double.isNaN(dist_trans)) continue;
					double dist_config = this.config_dist.vals[i][j];
					if (abs(dist_config) < delta) dist_config = delta;
					if (this.weight_power == 0. && this.within_between == 1.) {
						weight = 1.0;
					} else {
						weight = this.weights.vals[i][j];
					}
					mds_once_part3_gradient(dist_trans, dist_config, weight, i, j);
				} 
			} 

			/* center the classical gradient */
			double gfactor = mds_once_part3_normalizeGradient();

			/* add the gradient matrix to the position matrix and drag points */
			for (int i=0; i<this.pos.nrows; i++) {
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
			for (int k = 0; k < Mds.DIM; k++) { 
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
		for (int i=0; i<this.pos.nrows; i++) {
			// if (true || (ANCHOR_SCALE && IS_ANCHOR(i)))
			gsum += L2_norm (this.gradient.x[i],this.gradient.y[i]);
			psum += L2_norm (this.pos.x[i],this.pos.y[i]);
		}
		return (gsum < delta) ? 0.0 : this.stepsize * sqrt(psum/gsum);
	}
	public double[][] points() {
		double[][] result = new double[pos.x.length][2];
		for (int i = 0; i < pos.x.length; i++) {
			result[i][0] = pos.x[i];
			result[i][1] = pos.y[i];
		}
		return result;
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

		int i, j;
		double this_weight;
		double local_weight_power = 0.;
		double local_within_between = 1.;

		/* the weights will be used in metric and nonmetric scaling 
		 * as soon as weightpow != 0. or within_between != 1.
		 * weights vector only if needed */
		if ((this.weight_power != local_weight_power &&
				this.weight_power != 0.) || 
				(this.within_between != local_within_between &&
						this.within_between != 1.)) 
		{
			assert false; // TODO

			for (i=0; i<this.Dtarget.vals.length; i++) {
				for (j=0; j<this.Dtarget.vals.length; j++) {
					if (Double.isNaN(this.Dtarget.vals[i][j])) {
						this.weights.vals[i][j] = Double.NaN;
						continue;
					}
					if (this.weight_power != 0.) {
						if(this.Dtarget.vals[i][j] == 0.) { /* cap them */
							if (this.weight_power < 0.) {
								this.weights.vals[i][j] = 1E5;
								continue;
							}
							else {
								this.weights.vals[i][j] = 1E-5;
							}
						}
						this_weight = pow(this.Dtarget.vals[i][j], this.weight_power); 
						/* cap them */
						if (this_weight > 1E5)  this_weight = 1E5;
						else if (this_weight < 1E-5) this_weight = 1E-5;
						/* within-between weighting */
						if (SAMEGLYPH(i,j)) 
							this_weight *= (2. - this.within_between);
						else
							this_weight *= this.within_between;
						this.weights.vals[i][j] = this_weight;
					} else { /* weightpow == 0. */
						if (SAMEGLYPH(i,j)) 
							this_weight = (2. - this.within_between);
						else 
							this_weight = this.within_between;
						this.weights.vals[i][j] = this_weight;
					}
				}
			}
		}
	} /* end set_weights() */
	private void update_stress () {
		stress_dx = stress_xx = stress_dd = 0;
		for (int i=0; i < this.trans_dist.vals.length; i++) 
			for (int j=0; j < i; j++) {
				double dist_trans  = this.trans_dist.vals[i][j] * 2; // symmetry!
				if (Double.isNaN(dist_trans)) continue;
				double dist_config = this.config_dist.vals[i][j] * 2; // symmetry!
				if (this.weight_power == 0. && this.within_between == 1.) { 
					stress_dx += dist_trans  * dist_config;
					stress_xx += dist_config * dist_config;
					stress_dd += dist_trans  * dist_trans;
				} else {
					double this_weight = this.weights.vals[i][j] * 2; // symmetry!
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
			throw new Error("didn't draw stress: stress_dx = %5.5g   stress_dd = %5.5g   stress_xx = %5.5g\n");
		}
	} /* end update_stress() */

	enum MDSAnchorInd {fixed, no_anchor, scaled};

	enum MDSGroupInd {all_distances, between, within};

}