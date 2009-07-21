package ch.akuhn.org.ggobi.plugins.ggvis;

import static java.lang.Math.sqrt;

import static java.lang.Math.pow;
import static java.lang.Math.abs;

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
public class Mds extends Ggvisd {

	/* these belong in ggv */
	double stress, stress_dx, stress_dd, stress_xx;

	static final double delta = 1E-10;
	static final int ANCHOR   = 2;
	static final int DRAGGED  = 4; 

	private static final boolean ANCHOR_FIXED = false;
	private static final boolean ANCHOR_SCALE = false;

	private static final boolean TODO_SYMMETRY = false;

	private boolean SAMEGLYPH(int i, int j) {
		return (i / 100) == (j / 100);
	}

	private boolean IS_ANCHOR(int i) {
		return i % 100 == 0;
	}

	private boolean dragged_by_mouse() {
		return false;
	}

	public void init(double[][] dissimilarities) {

		this.Dtarget = new array_d();
		this.Dtarget.arrayd_alloc(dissimilarities.length, dissimilarities.length);
		this.trans_dist.arrayd_alloc(dissimilarities.length, dissimilarities.length);
		this.config_dist.arrayd_alloc(dissimilarities.length, dissimilarities.length);
		this.ggv_init_Dtarget(dissimilarities);  /* populate with INF */
		this.ggv_compute_Dtarget(dissimilarities);

		this.pos = new array_d();
		this.pos.arrayd_alloc(dissimilarities.length, dim);
		this.gradient.arrayd_alloc(dissimilarities.length, dim);
		for (int a = 0; a < pos.vals.length; a++) {
			for (int b = 0; b < pos.vals[a].length; b++) {
				this.pos.vals[a][b] = (Math.random() - 0.5) * 2;
			}
		}

		this.trans_dist.fill(Double.NaN);
		this.config_dist.fill(Double.NaN);

	}


	static final double sig_pow(double x, double p) {
		return (x >= 0.0 ? pow(x, p) : -pow(-x, p));
	}

	double Lp_distance_pow (int i, int j) {
		double dsum = 0.0;
		int k;
		if (this.lnorm == 2. && this.dist_power == 1.) {
			for (k = 0; k < this.dim; k++) {
				dsum += (this.pos.vals[i][k] - this.pos.vals[j][k]) * (this.pos.vals[i][k] - this.pos.vals[j][k]);
			}
			return (sqrt(dsum));
		} else { /* non-Euclidean or Dtarget power != 1. */
			for (k = 0; k < this.dim; k++) {
				dsum += pow (abs (this.pos.vals[i][k] - this.pos.vals[j][k]), this.lnorm);
			}
			return (pow(dsum, this.dist_power_over_lnorm));
		}
	}

	/* begin centering and sizing routines */

	void get_center () {
		int i, k, n;

		this.pos_mean = new double[this.dim];

		n = 0;

		for (i=0; i<this.pos.nrows; i++) {
			if (IS_DRAGGED(i)) continue; 
			for(k=0; k<this.dim; k++) {
				this.pos_mean[k] += this.pos.vals[i][k];
			}
			n++;
		}
		for(k=0; k<this.dim; k++) {
			this.pos_mean[k] /= n;
		}
	}

	private boolean IS_DRAGGED(int i) {
		return false;
	}

	void get_center_scale () {
		int n, i, k;

		get_center ();
		n = 0;
		this.pos_scl = 0.;

		for(i=0; i<this.pos.nrows; i++) {
			if (IS_DRAGGED(i)) continue;
			for (k=0; k<this.dim; k++) {
				this.pos_scl += ((this.pos.vals[i][k] - this.pos_mean[k]) *
						(this.pos.vals[i][k] - this.pos_mean[k]));
			}
			n++;
		}
		this.pos_scl = sqrt(this.pos_scl/(double)n/(double)this.dim);
	}


	void ggv_center_scale_pos() {
		int i, k;

		get_center_scale();

		for (i=0; i<this.pos.nrows; i++) {
			if (IS_DRAGGED(i)) continue;
			for (k=0; k<this.dim; k++)
				this.pos.vals[i][k] = (this.pos.vals[i][k] - this.pos_mean[k])/this.pos_scl;
		}
	}

	/* end centering and sizing routines */

	double dot_prod (int i, int j ) {
		double dsum = 0.0;
		int k;
		for (k=0; k<this.dim; k++) {
			dsum += (this.pos.vals[i][k] - this.pos_mean[k]) *
			(this.pos.vals[j][k] - this.pos_mean[k]);
		}
		return(dsum);
	}

	double	L2_norm (double p1[] ) {
		double dsum = 0.0;
		int k;

		for (k = 0; k < this.dim; k++) { 
			dsum += (p1[k] - this.pos_mean[k])*(p1[k] - this.pos_mean[k]);
		}
		return(dsum);
	}


	/*
	 * weights are only set if weightpow != 0; for 0 there's simpler
	 *code throughout, and we save space
	 */
	void set_weights () {

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
			if (this.weights.nels < this.ndistances)  /* power weights */ {
				this.weights.arrayd_alloc (this.Dtarget.nrows,this.Dtarget.ncols);
			}

			for (i=0; i<this.Dtarget.nrows; i++) {
				for (j=0; j<this.Dtarget.ncols; j++) {
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


	void set_random_selection () {
		if (this.rand_select_val != 1.0) { 
			throw null;
			//		if (this.rand_sel.length < this.ndistances) {
			//			this.rand_sel.vectord_realloc(this.ndistances);
			//			for (i=0; i<this.ndistances; i++) { 
			//				this.rand_sel.els[i] = (double) Math.random();  /* uniform on [0,1] */
			//			}
			//		}
			//		if (!Double.isNaN(this.rand_select_new)) {
			//			for (i=0; i<this.ndistances; i++)
			//				this.rand_sel.els[i] = (double) Math.random();
			//			this.rand_select_new = Double.NaN;
			//		}
		}
	} /* end set_random_selection() */


	void update_stress () {
		int i, j;
		double this_weight, dist_config, dist_trans;

		stress_dx = stress_xx = stress_dd = 0.0;

		for (i=0; i < this.Dtarget.nrows; i++) 
			for (j=0; j < this.Dtarget.nrows; j++) {
				dist_trans  = this.trans_dist.vals[i][j];
				if (Double.isNaN(dist_trans)) continue;
				dist_config = this.config_dist.vals[i][j]; 
				if (this.weight_power == 0. && this.within_between == 1.) { 
					stress_dx += dist_trans  * dist_config;
					stress_xx += dist_config * dist_config;
					stress_dd += dist_trans  * dist_trans;
				} else {
					this_weight = this.weights.vals[i][j]; 
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


	/* we assume in this routine that trans_dist contains 
   dist.data for KruskalShepard and 
   -dist.data*dist.data for CLASSIC MDS */
	void power_transform () { 

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


	/* ---------------------------------------------------------------- */
	/*
	 * Perform one loop of the iterative mds function.
	 *
	 * If doit is False, then we really want to determine the
	 * stress function without doing anything to the gradient
	 */
	public void mds_once (boolean doit) {


		int num_active_dist_prev = this.num_active_dist;

		mds_once_part1();
		this.printSymtriacMatrices();

		mds_once_part2();
		this.printSymtriacMatrices();

		mds_once_part3(doit); /* close:  if (doit && num_active_dist > 0)  */
		this.printSymtriacMatrices();

		/* experiment: normalize point cloud after using simplified gradient */
		// FIXME can we avoid this? 
		// ggv_center_scale_pos ();

		/* update Shepard labels */
		if (this.num_active_dist != num_active_dist_prev) {
			/*update_shepard_labels (this.num_active_dist);*/
		}

	} /* end mds_once() */

	private void mds_once_part1() {
		//	/* preparation for transformation */
		//	if (this.trans_dist.nels < this.ndistances) {
		//	 	 /* transformation of raw_dist */
		//		this.trans_dist.arrayd_alloc (this.Dtarget.nrows,this.Dtarget.ncols);
		//	}
		//	/* distances of configuration points */
		//	if (this.config_dist.nels < this.ndistances) {
		//		this.config_dist.arrayd_alloc (this.Dtarget.nrows,this.Dtarget.ncols);
		//	}
		//	/* initialize everytime we come thru because missings may change
		//	      due to user interaction */
		//	for (int i = 0 ; i < this.Dtarget.nrows; i++) {
		//		for (int j = 0; j < this.Dtarget.ncols; j++) {
		//			this.config_dist.els[i][j] = Double.MAX_VALUE;
		//			this.trans_dist.els[i][j]  = Double.MAX_VALUE;
		//		} 
		//	}
		// this.trans_dist.fill(Double.NaN);
		// this.config_dist.fill(Double.NaN);

		/* weight vector */
		set_weights ();

		/* random selection vector */
		set_random_selection ();

		/* anchors of either kind */  
		// FIXME if (this.anchor_group.length > 0 && this.n_anchors > 0 &&
		//      (this.anchor_ind == MDSAnchorInd.fixed || this.anchor_ind == MDSAnchorInd.scaled))
		//  {
		//    for (i=0; i<this.pos.nrows; i++) {
		//      if (!IS_EXCLUDED(i) &&
		//          this.anchor_group.els[this.dsrc.clusterid.els[i]])  /* which d? */
		//      {
		//        this.point_status.els[i] = ANCHOR;
		//      }
		//    }
		//  }

		/* dragged by mouse */
		if (/* TODO imode_get (gg) == MOVEPTS && gg->buttondown && dpos->nearest_point != -1 */
				dragged_by_mouse()) {
			// TODO    if (gg.movepts.cluster_p) {
			// TODO		  for (i=0; i<this.pos.nrows; i++) {
			// TODO			  if (!IS_EXCLUDED(i) && SAMEGLYPH(dpos,i,dpos->nearest_point)) {
			// TODO				  this.point_status.els[i] = DRAGGED;
			// TODO			  }
			// TODO		  }
			// TODO	  } else {
			// TODO		  this.point_status.els[dpos->nearest_point] = DRAGGED;
			// TODO	  }
		}
	}

	private boolean mds_once_part2_continue(int i, int j) {

		/* these points do not contribute to the gradient */
		if ((ANCHOR_SCALE || ANCHOR_FIXED) && !IS_ANCHOR(j) && !IS_DRAGGED(j)) return true;
		if ((ANCHOR_SCALE || ANCHOR_FIXED) && !IS_ANCHOR(i) && !IS_DRAGGED(i)) return true;

		/* if the target distance is missing, skip */
		if (Double.isNaN(this.Dtarget.vals[i][j])) return true;

		/* if weight is zero, skip */
		if (this.weights.nels != 0 && this.weights.vals[i][j] == 0.) return true;

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

	private void mds_once_part2() {
		// allocate position and compute means 
		get_center ();

		// collect and count active dissimilarities (j's move i's)
		this.num_active_dist = 0;

		// i's are moved by j's 
		for (int i = 0; i < this.Dtarget.nrows; i++) {
			// do not exclude moving i's: in non-metric MDS it matters what the set of distances is!
			// these points are not moved by the gradient 
			// if (IS_DRAGGED(i) || (ANCHOR_FIXED && IS_ANCHOR(i))) continue;

			/* j's are moving i's */    
			for (int j = 0; j < this.Dtarget.nrows; j++) {
				if (i == j) continue; 
				if (mds_once_part2_continue(i, j)) continue;
				this.num_active_dist++;

				/* configuration distance */
				this.config_dist.vals[i][j] = Lp_distance_pow(i, j);
				this.trans_dist.vals[i][j] = this.Dtarget.vals[i][j];
				/* store untransformed dissimilarity in transform vector for now:
				 * METRIC will transform it; NONMETRIC will used it for sorting first.
				 */

			} /* j */
		} /* i */
		/* ------------ end collecting active dissimilarities ------------------ */
	}


	private void mds_once_part3(boolean doit) {
		/* ---------- for active dissimilarities, do some work ------------------ */ 
		if (this.num_active_dist > 0) {
			/*-- power transform for metric MDS --*/
			power_transform ();
			/*-- stress (always lags behind gradient by one step) --*/
			update_stress ();
		}

		/* --- for active dissimilarities, do the gradient push if asked for ----*/
		if (doit && this.num_active_dist > 0) {
			/* Zero out the gradient matrix. */
			this.gradient.arrayd_zero();
			/* ------------- gradient accumulation: j's push i's ----------- */
			for (int i = 0; i < this.Dtarget.nrows; i++) {
				for (int j = 0; j < this.Dtarget.ncols; j++) {
					double weight;
					double dist_trans  = this.trans_dist.vals[i][j];
					if (Double.isNaN(dist_trans))
						continue;
					double dist_config = this.config_dist.vals[i][j];
					if (this.weight_power == 0. && this.within_between == 1.) {
						weight = 1.0;
					} else {
						weight = this.weights.vals[i][j];
					}

					/* gradient */
					mds_once_part3_gradient(dist_trans,
							dist_config, weight, i, j);

				} /* for (j = 0; j < dist.nrows; j++) */
			} /* for (i = 0; i < dist.nrows; i++) */
			/* ------------- end gradient accumulation ----------- */   

			/* center the classical gradient */
			double gfactor = mds_once_part3_normalizeGradient();

			/* add the gradient matrix to the position matrix and drag points */
			for (int i=0; i<this.pos.nrows; i++) {
				if (!IS_DRAGGED(i)) {
					for (int k = 0; k<this.dim; k++) {
						this.pos.vals[i][k] += (gfactor * this.gradient.vals[i][k]);
					}
				} else {
					throw null;
					//		        for (int k=0; k < this.dim; k++) 
					//		          this.pos.vals[i][k] = dpos.tform.vals[i][k] ;
				}
			}


		}
	}

	private double mds_once_part3_normalizeGradient() {
		double gsum;
		double psum;
		double gfactor;
		/* gradient normalizing factor to scale gradient to a fraction of
		   the size of the configuration */
		gsum = psum = 0.0 ;
		for (int i=0; i<this.pos.nrows; i++) {
			// if (true || (ANCHOR_SCALE && IS_ANCHOR(i)))
			gsum += L2_norm (this.gradient.vals[i]);
			psum += L2_norm (this.pos.vals[i]);
		}
		if (gsum < delta) gfactor = 0.0;
		else gfactor = this.stepsize * sqrt(psum/gsum);
		return gfactor;
	}


	private void mds_once_part3_gradient(double dist_trans,
			double dist_config, double weight, int i, int j) {
		double resid;
		double step_mag;
		if (abs(dist_config) < delta) dist_config = delta;
		/* scale independent version: */
		resid = (dist_trans - stress_dx / stress_xx * dist_config);
		/* scale dependent version: resid = (dist_trans - dist_config); */
		if (this.lnorm != 2) {
			assert TODO_SYMMETRY;
			/* non-Euclidean Minkowski/Lebesgue metric */
			step_mag = weight * resid *
			pow (dist_config, 1 - this.lnorm_over_dist_power);
			for (int k = 0; k < this.dim; k++) {
				this.gradient.vals[i][k] += step_mag * 
				sig_pow(this.pos.vals[i][k]-this.pos.vals[j][k],
						this.lnorm-1.0);
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
			for (int k = 0; k < this.dim; k++) {
				this.gradient.vals[i][k] += step_mag *
				(this.pos.vals[i][k]-this.pos.vals[j][k]); /* Euclidean! */
			}
		}

	}

}