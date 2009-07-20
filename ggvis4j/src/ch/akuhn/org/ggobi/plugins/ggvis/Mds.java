package ch.akuhn.org.ggobi.plugins.ggvis;

/*
 * mds.c: multidimensional scaling 
 *  code originally written for xgvis by Michael Littman, greatly extended
 *  and tuned by Andreas Buja.  Now being ported to ggvis.
*/

///* Includes. */
//#include <sys/types.h>
//#include <math.h>
//#include <stdio.h>
//#include <stdlib.h>
//#include <string.h>
//
//#include <gtk/gtk.h>
//#include "ggobi.h"
//#include "externs.h"
//#include "GGobiAPI.h"
//
//#include "plugin.h"
//#include "ggvis.h"
//
//extern void add_stress_value (gdouble, ggvisd *);  /* in stressplot.c */
//extern void draw_stress (ggvisd *ggv, ggobid *gg);
///*extern void update_shepard_labels (int);*/
//extern void Myqsort(void* bot, int nmemb, int size, CompareFunc compar);
//   /* in ggv_qsort.c */
//
//
//#define SAMEGLYPH(d,i,j) \
//( d->color_now.els[(i)]      == d->color_now.els[(j)] &&      \
//  d->glyph_now.els[(i)].type == d->glyph_now.els[(j)].type && \
//  d->glyph_now.els[(i)].size == d->glyph_now.els[(j)].size )
//#define SIGNUM(x) (((x) < 0.0) ? (-1.0) : (((x) > 0.0) ? (1.0) : (0.0)))
//
//#define IS_DRAGGED(i) (ggv.point_status.els[(i)] == DRAGGED)
//#define IS_INCLUDED(i) (ggv.point_status.els[(i)] == INCLUDED)
//#define IS_EXCLUDED(i) (ggv.point_status.els[(i)] == EXCLUDED)
//#define IS_ANCHOR(i) (ggv.point_status.els[(i)] == ANCHOR)
//
//#define ANCHOR_SCALE (ggv.anchor_ind == scaled)
//#define ANCHOR_FIXED (ggv.anchor_ind == fixed)
//
///* */

import static java.lang.Math.sqrt;

import static java.lang.Math.pow;
import static java.lang.Math.abs;
public class Mds {

	/* these belong in ggv */
	double stress, stress_dx, stress_dd, stress_xx;

	static final double delta = 1E-10;
	static final int EXCLUDED = 0;
	static final int INCLUDED = 1;
	static final int ANCHOR   = 2;
	static final int DRAGGED  = 4; 
	
	private static final boolean ANCHOR_FIXED = false;
	private static final boolean ANCHOR_SCALE = false;
	double sig_pow (double x, double p) {
		return((x >= 0.0 ? pow(x, p) : -pow(-x, p)));
	}

	double Lp_distance_pow (int i, int j, Ggvisd ggv) {
		double dsum = 0.0;
		int k;
		double pos[][] = ggv.pos.vals;

		if (ggv.lnorm == 2. && ggv.dist_power == 1.) {
			for (k = 0; k < ggv.dim; k++) {
				dsum += (pos[i][k] - pos[j][k]) * (pos[i][k] - pos[j][k]);
			}
			return (sqrt(dsum));
		} else { /* non-Euclidean or Dtarget power != 1. */
			for (k = 0; k < ggv.dim; k++) {
				dsum += pow (abs (pos[i][k] - pos[j][k]), ggv.lnorm);
			}
			return (pow(dsum, ggv.dist_power_over_lnorm));
		}
	}

/* begin centering and sizing routines */

	void get_center (Ggvisd ggv) {
		int i, k, n;

		ggv.pos_mean = new double[ggv.dim];

		n = 0;

		for (i=0; i<ggv.pos.nrows; i++) {
			// FIXME if (IS_EXCLUDED(i) || IS_DRAGGED(i)) continue; 
			for(k=0; k<ggv.dim; k++) {
				ggv.pos_mean[k] += ggv.pos.vals[i][k];
			}
			n++;
		}
		for(k=0; k<ggv.dim; k++) {
			ggv.pos_mean[k] /= n;
		}
	}

	private boolean IS_DRAGGED(int i) {
		return false;
	}

	private boolean IS_EXCLUDED(int i) {
		return false;
	}

	void get_center_scale (Ggvisd ggv) {
		int n, i, k;

		get_center (ggv);
		n = 0;
		ggv.pos_scl = 0.;

		for(i=0; i<ggv.pos.nrows; i++) {
			// FIXME if (IS_EXCLUDED(i) || IS_DRAGGED(i)) continue;
			for (k=0; k<ggv.dim; k++) {
				ggv.pos_scl += ((ggv.pos.vals[i][k] - ggv.pos_mean[k]) *
						(ggv.pos.vals[i][k] - ggv.pos_mean[k]));
			}
			n++;
		}
		ggv.pos_scl = sqrt(ggv.pos_scl/(double)n/(double)ggv.dim);
	}


	void ggv_center_scale_pos (Ggvisd ggv) {
		int i, k;
		double pos[][] = ggv.pos.vals;

		get_center_scale (ggv);

		for (i=0; i<ggv.pos.nrows; i++) {
			// FIXME if (IS_EXCLUDED(i) || IS_DRAGGED(i)) continue;
			for (k=0; k<ggv.dim; k++)
				pos[i][k] = (pos[i][k] - ggv.pos_mean[k])/ggv.pos_scl;
		}
	}

/* end centering and sizing routines */

	double dot_prod (int i, int j, Ggvisd ggv) {
		double dsum = 0.0;
		int k;
		double pos[][] = ggv.pos.vals;

		for (k=0; k<ggv.dim; k++) {
			dsum += (pos[i][k] - ggv.pos_mean[k]) *
			(pos[j][k] - ggv.pos_mean[k]);
		}
		return(dsum);
	}

	double	L2_norm (double p1[], Ggvisd ggv) {
		double dsum = 0.0;
		int k;

		for (k = ggv.freeze_var; k < ggv.dim; k++) { 
			dsum += (p1[k] - ggv.pos_mean[k])*(p1[k] - ggv.pos_mean[k]);
		}
		return(dsum);
	}


/*
 * weights are only set if weightpow != 0; for 0 there's simpler
 *code throughout, and we save space
*/
void set_weights (Ggvisd ggv) {

  int i, j;
  double this_weight;
  double local_weight_power = 0.;
  double local_within_between = 1.;

  /* the weights will be used in metric and nonmetric scaling 
   * as soon as weightpow != 0. or within_between != 1.
   * weights vector only if needed */
  if ((ggv.weight_power != local_weight_power &&
       ggv.weight_power != 0.) || 
     (ggv.within_between != local_within_between &&
      ggv.within_between != 1.)) 
  {
    if (ggv.weights.nels < ggv.ndistances)  /* power weights */ {
    	ggv.weights.arrayd_alloc (ggv.Dtarget.nrows,ggv.Dtarget.ncols);
    }
    
    for (i=0; i<ggv.Dtarget.nrows; i++) {
      for (j=0; j<ggv.Dtarget.ncols; j++) {
        if (ggv.Dtarget.vals[i][j] == Double.MAX_VALUE) {
          ggv.weights.els[i][j] = Double.MAX_VALUE;
          continue;
        }
        if (ggv.weight_power != 0.) {
          if(ggv.Dtarget.vals[i][j] == 0.) { /* cap them */
            if (ggv.weight_power < 0.) {
              ggv.weights.els[i][j] = 1E5;
              continue;
            }
            else {
              ggv.weights.els[i][j] = 1E-5;
            }
          }
          this_weight = pow(ggv.Dtarget.vals[i][j], ggv.weight_power); 
          /* cap them */
          if (this_weight > 1E5)  this_weight = 1E5;
          else if (this_weight < 1E-5) this_weight = 1E-5;
          /* within-between weighting */
          if (SAMEGLYPH(ggv.dpos,i,j)) 
            this_weight *= (2. - ggv.within_between);
          else
            this_weight *= ggv.within_between;
          ggv.weights.els[i][j] = this_weight;
        } else { /* weightpow == 0. */
          if (SAMEGLYPH(ggv.dpos,i,j)) 
            this_weight = (2. - ggv.within_between);
          else 
            this_weight = ggv.within_between;
          ggv.weights.els[i][j] = this_weight;
        }
      }
    }
  }
} /* end set_weights() */


void set_random_selection (Ggvisd ggv) {
	if (ggv.rand_select_val != 1.0) { 
		throw null;
//		if (ggv.rand_sel.length < ggv.ndistances) {
//			ggv.rand_sel.vectord_realloc(ggv.ndistances);
//			for (i=0; i<ggv.ndistances; i++) { 
//				ggv.rand_sel.els[i] = (double) Math.random();  /* uniform on [0,1] */
//			}
//		}
//		if (!Double.isNaN(ggv.rand_select_new)) {
//			for (i=0; i<ggv.ndistances; i++)
//				ggv.rand_sel.els[i] = (double) Math.random();
//			ggv.rand_select_new = Double.NaN;
//		}
	}
} /* end set_random_selection() */


void update_stress (Ggvisd ggv, Ggobid gg) {
  int i, j;
  double this_weight, dist_config, dist_trans;

  stress_dx = stress_xx = stress_dd = 0.0;

  for (i=0; i < ggv.Dtarget.nrows; i++) 
    for (j=0; j < ggv.Dtarget.ncols; j++) {
      dist_trans  = ggv.trans_dist.els[i][j];
      if (dist_trans == Double.MAX_VALUE) continue;
      dist_config = ggv.config_dist.els[i][j];
      if (ggv.weight_power == 0. && ggv.within_between == 1.) { 
        stress_dx += dist_trans  * dist_config;
        stress_xx += dist_config * dist_config;
        stress_dd += dist_trans  * dist_trans;
      } else {
        this_weight = ggv.weights.els[i][j];
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
void power_transform (Ggvisd ggv) { 

  double tmp, fac;
  int i;

  if (ggv.Dtarget_power == 1.) { 
    return; 
  } else if (ggv.Dtarget_power == 2.) {
	  throw null;
//    if (ggv.KruskalShepard_classic == MDSKSInd.KruskalShepard) { 
//      for (i=0; i<ggv.ndistances; i++) {
//        tmp = ggv.trans_dist.els[i];
//        if (tmp != Double.MAX_VALUE)
//          ggv.trans_dist.els[i] = tmp*tmp/ggv.Dtarget_max;
//      }
//    } else { 
//      for (i=0; i<ggv.ndistances; i++) {
//        tmp = ggv.trans_dist.els[i];
//        if (tmp != Double.MAX_VALUE)
//          ggv.trans_dist.els[i] = -tmp*tmp/ggv.Dtarget_max;
//      }
//    }
  } else {
	  throw null;
//    fac = pow (ggv.Dtarget_max, ggv.Dtarget_power-1);
//    if (ggv.KruskalShepard_classic == MDSKSInd.KruskalShepard) { 
//      for(i=0; i<ggv.ndistances; i++) {
//        tmp = ggv.trans_dist.els[i];
//        if (tmp != Double.MAX_VALUE)
//          ggv.trans_dist.els[i] = pow(tmp, ggv.Dtarget_power)/fac;
//      }
//    } else { 
//      for(i=0; i<ggv.ndistances; i++) {
//        tmp = ggv.trans_dist.els[i];
//        if(tmp != Double.MAX_VALUE)
//          ggv.trans_dist.els[i] = -pow(-tmp, ggv.Dtarget_power)/fac;
//      }
//    }
  }

} /* end power_transform() */


/* for sorting in isotonic regression */
static double tmpVector[];
/* */
int realCompare(final Object aPtr, final Object bPtr) {
//
//  double aReal, bReal;
//  int aIndex, bIndex;
//
//  aIndex = *(int*)aPtr;
//  bIndex = *(int*)bPtr;
//  aReal = tmpVector[aIndex];
//  bReal = tmpVector[bIndex];
//  if (aReal < bReal) return -1;
//  else if (aReal == bReal) return 0;
//  else return 1; 
	throw null;
}
/* nonmetric transform with isotonic regression of config_dist on dist */
void isotonic_transform (Ggvisd ggv, Ggobid gg) {
	throw null;
//
//  int i, j, ii, i][j, k;
//  double tmp_dist, tmp_distsum, tmp_weightsum, this_weight,
//    t_d_i, t_d_ii;
//  gboolean finished;
//
//  /* the sort index for dist.data */
//  if (ggv.trans_dist_index.length < ggv.ndistances) {
//    vectori_realloc (&ggv.trans_dist_index, ggv.ndistances);
//    g_printerr ("allocated trans_dist_index \n");
//  }
//  /* block lengths */
//  if (ggv.bl.length < ggv.ndistances) {
//    vectori_realloc (&ggv.bl, ggv.ndistances);
//    g_printerr ("allocated block lengths \n");
//  }
//  /* block weights */
//  if (ggv.bl_w.length < ggv.ndistances &&
//       (ggv.weight_power != 0. || ggv.within_between != 1.))
//  {
//    vectord_realloc (&ggv.bl_w, ggv.ndistances);
//    g_printerr ("allocated block weights \n");
//  }
//
//  /* sort if necessary 
//   *  (This is not the proper criterion because the active distances
//   *  could change while their number remains the same...; needs
//   *  thought.)
//   */
//  if (ggv.num_active_dist != ggv.prev_nonmetric_active_dist) {
//    tmpVector = ggv.trans_dist.els; 
//     /* "tmpVector" is the vector by which to sort; see "realCompare" above */
//    for (i = 0 ; i < ggv.Dtarget.nrows; i++) {
//      for (j = 0; j < ggv.Dtarget.ncols; j++) {
//        ggv.trans_dist_index.els[i][j] = i][j;
//    }}
//
//    Myqsort (ggv.trans_dist_index.els, ggv.ndistances,
//      sizeof(int), realCompare);
//    ggv.prev_nonmetric_active_dist = ggv.num_active_dist;
//    g_printerr ("sorted the dissimilarity data \n");
//  }
//
//  /* initialize blocks wrt ties; this should also preserve symmetry if present */
//  for (i = 0 ; i < ggv.ndistances; i += ggv.bl.els[i]) {  
//    ii = i+1;
//    tmp_dist = ggv.trans_dist.els[ggv.trans_dist_index.els[i]];
//    while ((ii < ggv.ndistances) &&
//           (ggv.trans_dist.els[ggv.trans_dist_index.els[ii]] == tmp_dist))
//    {
//      ii++;
//    }
//    /* ii points to start of the next block */
//    ggv.bl.els[i] = ii-i;
//  }
//
//  /* trans_dist is computed by isotonic regression of config_dist on
//     trans_dist_index, therefore: */
//  for (i = 0; i < ggv.ndistances; i++)
//    ggv.trans_dist.els[i] = ggv.config_dist.els[i];
//
//  /* form initial block means (and weights if necessary); need to
//     fill only first element of a block */
//  for (i = 0; i < ggv.ndistances; i += ggv.bl.els[i]) {        
//    if (ggv.trans_dist.els[ggv.trans_dist_index.els[i]] != G_MAXDOUBLE) {
//      ii = i + ggv.bl.els[i];
//      if (ggv.weight_power == 0. && ggv.within_between == 1.) {
//        tmp_distsum = 0.;  
//        for (j = i; j < ii; j++)
//          tmp_distsum += ggv.trans_dist.els[ggv.trans_dist_index.els[j]];
//        k = ggv.trans_dist_index.els[i];
//        ggv.trans_dist.els[k] = tmp_distsum / ggv.bl.els[i];
//      } else {
//        tmp_distsum = tmp_weightsum = 0.;  
//        for(j = i; j < ii; j++) {
//          k = ggv.trans_dist_index.els[j];
//          this_weight = ggv.weights.els[k];
//          tmp_distsum += ggv.trans_dist.els[k] * this_weight;
//          tmp_weightsum += this_weight;
//        }
//        ggv.bl_w.els[i] = tmp_weightsum;
//        ggv.trans_dist.els[ggv.trans_dist_index.els[i]] = tmp_distsum / tmp_weightsum;
//      }
//    }
//  }
//
//  /* pool-adjacent-violator algorithm for isotonic regression */
//  finished = false;
//  while (!finished) {
//    finished = true;
//    i = 0;  ii = i + ggv.bl.els[i];
//    while (i < ggv.ndistances && ii < ggv.ndistances) {
//      t_d_i  = ggv.trans_dist.els[ggv.trans_dist_index.els[i]];
//      t_d_ii = ggv.trans_dist.els[ggv.trans_dist_index.els[ii]];
//      if (t_d_i > t_d_ii) { /* pool blocks starting at i and ii */
//        if (ggv.weight_power == 0. && ggv.within_between == 1.) {
//          ggv.trans_dist.els[ggv.trans_dist_index.els[i]] = 
//            (t_d_i * ggv.bl.els[i] + t_d_ii * ggv.bl.els[ii]) /
//            (ggv.bl.els[i] + ggv.bl.els[ii]);
//        } else {
//          ggv.trans_dist.els[ggv.trans_dist_index.els[i]] = 
//            (t_d_i * ggv.bl_w.els[i] + t_d_ii * ggv.bl_w.els[ii]) /
//            (ggv.bl_w.els[i] + ggv.bl_w.els[ii]); 
//          ggv.bl_w.els[i] += ggv.bl_w.els[ii];
//        }
//        ggv.bl.els[i] += ggv.bl.els[ii];
//        finished = false;
//      }
//      i += ggv.bl.els[i];  
//      if(i < ggv.ndistances) ii = i + ggv.bl.els[i];
//    }
//  }
//
//  /* p-a-v sets only the first element of each block, so now we need
//     to fill the blocks: */
//  for (i = 0; i < ggv.ndistances; i = i + ggv.bl.els[i]) {
//    for (j = i + 1; j < i + ggv.bl.els[i]; j++) {
//      ggv.trans_dist.els[ggv.trans_dist_index.els[j]] =
//        ggv.trans_dist.els[ggv.trans_dist_index.els[i]];
//      ggv.bl.els[j] = 0; /* for debugging: blocks are easier to read w/o historic junk */
//    }
//  }
//
//  /* mix isotonic with raw according to isotonic_mix entered interactively */
//  if (ggv.isotonic_mix != 1.0) {
//    for (i = 0 ; i < ggv.Dtarget.nrows; i++) 
//      for (j = 0; j < ggv.Dtarget.ncols; j++) {
//        i][j = i][j;
//        if (ggv.trans_dist.els[i][j] != G_MAXDOUBLE) {
//          if (ggv.Dtarget_power == 1.0) {
//            if (ggv.KruskalShepard_classic == KruskalShepard) {
//              ggv.trans_dist.els[i][j] =
//                ggv.isotonic_mix * ggv.trans_dist.els[i][j] + 
//                 (1 - ggv.isotonic_mix) * ggv.Dtarget.vals[i][j];
//            } else {
//              ggv.trans_dist.els[i][j] =
//                ggv.isotonic_mix * ggv.trans_dist.els[i][j] - 
//                (1 - ggv.isotonic_mix) *
//                ggv.Dtarget.vals[i][j]*ggv.Dtarget.vals[i][j];
//            }
//          } else { /* Dtarget_power != 1.0 */
//            if (ggv.KruskalShepard_classic == KruskalShepard) {
//              ggv.trans_dist.els[i][j] =
//                ggv.isotonic_mix * ggv.trans_dist.els[i][j] + 
//                (1 - ggv.isotonic_mix) *
//                pow(ggv.Dtarget.vals[i][j], ggv.Dtarget_power);
//            } else {
//              ggv.trans_dist.els[i][j] =
//                ggv.isotonic_mix * ggv.trans_dist.els[i][j] - 
//                (1 - ggv.isotonic_mix) *
//                pow(ggv.Dtarget.vals[i][j], 2*ggv.Dtarget_power);
//            }
//          }
//        } /* end if(trans_dist[i][j] != G_MAXDOUBLE) */
//      } /* end for (j = 0; j < dist.ncols; j++) */
//  } /* end if(isotonic_mix != 1.0) */
//
//  /*-- update histogram of transformed D --*/
//  ggv_Dtarget_histogram_update (ggv, gg);

} /* end isotonic_transform() */

void update_ggobi (Ggvisd ggv, Ggobid gg) {
//
//  int i, j;
//
//  for (i=0; i<ggv.pos.nrows; i++) {
//    for (j=0; j<ggv.pos.ncols; j++) {
//      ggv.dpos->tform.vals[i][j] =
//        ggv.dpos->raw.vals[i][j] = ggv.pos.vals[i][j];
//    }
//  }
//
//  tform_to_world (ggv.dpos, gg);
//  displays_tailpipe (FULL, gg);
}

int mds_idle_func (/*PluginInstance*/ Object inst) {
//
//  ggvisd *ggv = ggvisFromInst (inst);
//  ggobid *gg = inst->gg;
//  gboolean doit = ggv.running_p;
//
//  if (doit) {
//    mds_once (true, ggv, gg);
//    update_ggobi (ggv, gg);
//  }
//
//  return (doit);
	throw null;
}

/* ---------------------------------------------------------------- */
/*
 * Perform one loop of the iterative mds function.
 *
 * If doit is False, then we really want to determine the
 * stress function without doing anything to the gradient
*/
void mds_once (boolean doit, Ggvisd ggv, Ggobid gg) {


  int num_active_dist_prev = ggv.num_active_dist;
  
  GGobiData dpos = ggv.dpos;

  mds_once_part1(ggv);

  mds_once_part2(ggv);

  mds_once_part3(doit, ggv, gg, dpos); /* close:  if (doit && num_active_dist > 0)  */

  /* update Shepard labels */
  if (ggv.num_active_dist != num_active_dist_prev) {
    /*update_shepard_labels (ggv.num_active_dist);*/
  }
  
} /* end mds_once() */

private void mds_once_part3(boolean doit, Ggvisd ggv, Ggobid gg,
		GGobiData dpos) {
	double dist_trans;
	double resid;
	double dist_config;
	double weight;
	int k;
	int n;
	double step_mag;
	double gsum;
	double psum;
	double gfactor;
	double tmp;
	/* ---------- for active dissimilarities, do some work ------------------ */ 
	  if (ggv.num_active_dist > 0) {
	    /*-- power transform for metric MDS; isotonic transform for nonmetric --*/
	    if (ggv.metric_nonmetric == MDSMetricInd.metric)
	      power_transform (ggv);
	    else
	      isotonic_transform (ggv, gg);
	    /*-- stress (always lags behind gradient by one step) --*/
	    update_stress (ggv, gg);
	  }
	
	  /* --- for active dissimilarities, do the gradient push if asked for ----*/
	  if (doit && ggv.num_active_dist > 0) {
	
	    /* all of the following need to be run thru rows_in_plot and erase ! */
	
	    /* Zero out the gradient matrix. */
	    if (ggv.gradient.nrows != ggv.pos.nrows ||
	        ggv.gradient.ncols != ggv.pos.ncols)
	    {
	    	ggv.gradient.arrayd_free(ggv.gradient.nrows, ggv.gradient.ncols);
	    	ggv.gradient.arrayd_alloc (ggv.pos.nrows, ggv.pos.ncols);
	    }
	    ggv.gradient.arrayd_zero();
	
	    /* ------------- gradient accumulation: j's push i's ----------- */
	    for (int i = 0; i < ggv.Dtarget.nrows; i++) {
	      for (int j = 0; j < ggv.Dtarget.ncols; j++) {
	    	  
	        dist_trans  = ggv.trans_dist.els[i][j];
	        if (dist_trans  == Double.MAX_VALUE)
	          continue;
	        dist_config = ggv.config_dist.els[i][j];
	        if (ggv.weight_power == 0. && ggv.within_between == 1.) {
	          weight = 1.0;
	        } else {
	          weight = ggv.weights.els[i][j];
	        }
	
	        /* gradient */
	        if (ggv.KruskalShepard_classic == MDSKSInd.KruskalShepard) {
	          if (abs(dist_config) < delta) dist_config = delta;
	          /* scale independent version: */
	          resid = (dist_trans - stress_dx / stress_xx * dist_config);
	          /* scale dependent version: 
	          resid = (dist_trans - dist_config);
	          */
	          if (ggv.lnorm != 2) {
	            /* non-Euclidean Minkowski/Lebesgue metric */
	            step_mag = weight * resid *
	              pow (dist_config, 1 - ggv.lnorm_over_dist_power);
	            for (k = 0; k < ggv.dim; k++) {
	              ggv.gradient.vals[i][k] += step_mag * 
	                sig_pow(ggv.pos.vals[i][k]-ggv.pos.vals[j][k],
	                  ggv.lnorm-1.0);
	            }
	          } else { /* Euclidean Minkowski/Lebesgue metric */
	            /* Note the simplification of the code for the special
	             * cases when dist_power takes on an integer value.  */
	            if (ggv.dist_power == 1)
	              step_mag = weight * resid / dist_config;
	            else if(ggv.dist_power == 2)
	              step_mag = weight * resid;
	            else if (ggv.dist_power == 3)
	              step_mag = weight * resid * dist_config;
	            else if (ggv.dist_power == 4)
	              step_mag = weight * resid * dist_config * dist_config;
	            else
	              step_mag = weight * resid *
	                pow(dist_config, ggv.dist_power-2.);
	            for (k = 0; k < ggv.dim; k++) {
	              ggv.gradient.vals[i][k] += step_mag *
	                (ggv.pos.vals[i][k]-ggv.pos.vals[j][k]); /* Euclidean! */
	            }
	          }
	        } else { /* CLASSIC */
	          /* scale independent version: */
	           resid = (dist_trans - stress_dx / stress_xx * dist_config);
	          /**/
	          /* scale dependent version:
	          resid = (dist_trans - dist_config);
	          */
	          step_mag = weight * resid; 
	          for (k = 0; k < ggv.dim; k++) {
	            ggv.gradient.vals[i][k] += step_mag *
	              (ggv.pos.vals[j][k] - ggv.pos_mean[k]);
	            /* exact formula would be:
	             * ((1-1/pos.nrows)*pos.vals[j][k] -
	             *  (1-2/pos.nrows)*pos_mean[k] - pos.vals[i][k]/pos.nrows); 
	            */
	          }
	        }
	
	      } /* for (j = 0; j < dist.nrows; j++) */
	    } /* for (i = 0; i < dist.nrows; i++) */
	    /* ------------- end gradient accumulation ----------- */   
	
	    /* center the classical gradient */
	    if (ggv.KruskalShepard_classic == MDSKSInd.classic) {
	      for (k=0; k<ggv.dim; k++) {
	        tmp = 0.;  n = 0;
	        for (int i=0; i<ggv.pos.nrows; i++) {
	          if (IS_INCLUDED(i) || (ANCHOR_SCALE && IS_ANCHOR(i))) {
	            tmp += ggv.gradient.vals[i][k]; 
	            n++;
	          }
	        }
	        tmp /= n;
	        for (int i=0; i<ggv.pos.nrows; i++) {
	          if (IS_INCLUDED(i) || (ANCHOR_SCALE && IS_ANCHOR(i))) {
	            ggv.gradient.vals[i][k] -= tmp;
	          }
	        }
	      }
	    }
	
	    /* gradient normalizing factor to scale gradient to a fraction of
	       the size of the configuration */
	    gsum = psum = 0.0 ;
	    for (int i=0; i<ggv.pos.nrows; i++) {
	      if (IS_INCLUDED(i) || (ANCHOR_SCALE && IS_ANCHOR(i))) {
	        gsum += L2_norm (ggv.gradient.vals[i], ggv);
	        psum += L2_norm (ggv.pos.vals[i], ggv);
	      }
	    }
	    if (gsum < delta) gfactor = 0.0;
	    else gfactor = ggv.stepsize * sqrt(psum/gsum);
	
	    /* add the gradient matrix to the position matrix and drag points */
	    for (int i=0; i<ggv.pos.nrows; i++) {
	      if (!IS_DRAGGED(i)) {
	        for (k = ggv.freeze_var; k<ggv.dim; k++)
	          ggv.pos.vals[i][k] += (gfactor * ggv.gradient.vals[i][k]);
	      } else {
	        for (k=0; k < ggv.dim; k++) 
	          ggv.pos.vals[i][k] = dpos.tform.vals[i][k] ;
	      }
	    }
	
	    /* experiment: normalize point cloud after using simplified gradient */
	    // FIXME can we avoid this? ggv_center_scale_pos (ggv);
	
	  }
}

private void mds_once_part2(Ggvisd ggv) {
	/* allocate position and compute means */
	  get_center (ggv);
	
	  /*-- collect and count active dissimilarities (j's move i's) ------------*/
	  ggv.num_active_dist = 0;
	
	  /* i's are moved by j's */
	  for (int i = 0; i < ggv.Dtarget.nrows; i++) {
	    /* do not exclude moving i's: in nonmetric MDS it matters what
	       the set of distances is!  */
	
	    /* these points are not moved by the gradient */
	    // FIXME if (IS_EXCLUDED(i) || IS_DRAGGED(i) || (ANCHOR_FIXED && IS_ANCHOR(i))) {
	    //  continue;
	    //}
	
	    /* j's are moving i's */    
	    for (int j = 0; j < ggv.Dtarget.ncols; j++) {
	
	      /* skip diagonal elements for distance scaling */
	      if (i == j && ggv.KruskalShepard_classic == MDSKSInd.KruskalShepard) continue; 
	
	      /* these points do not contribute to the gradient */
	      if (IS_EXCLUDED(j)) continue;
	      if ((ANCHOR_SCALE || ANCHOR_FIXED) && !IS_ANCHOR(j) && !IS_DRAGGED(j))
	        continue;
	
	      /* if the target distance is missing, skip */
	      if (ggv.Dtarget.vals[i][j] == Double.MAX_VALUE) continue;
	
	      /* if weight is zero, skip */
	      if (ggv.weights.nels != 0 && ggv.weights.els[i][j] == 0.) continue;
	
	      /* using groups */
	//      if (ggv.group_ind == MDSGroupInd.within && !SAMEGLYPH(dpos,i,j))
	//        continue;
	//      if (ggv.group_ind == MDSGroupInd.between && SAMEGLYPH(dpos,i,j))
	//        continue;
	
	      /*
	       * if the target distance is within the thresholds
	       * set using the barplot of distances, keep going.
	       */
	      if (ggv.Dtarget.vals[i][j] < ggv.threshold_low || 
	          ggv.Dtarget.vals[i][j] > ggv.threshold_high) continue;
	
	      /*
	       * random selection: needs to be done symmetrically
	       */
	      if (ggv.rand_select_val < 1.0) {
	        if (i < j && ggv.rand_sel.els[i][j] > ggv.rand_select_val) continue;
	        if (i > j && ggv.rand_sel.els[j][i] > ggv.rand_select_val) continue;
	      }
	
	      /* 
	       * zero weights:
	       * assume weights exist if test is positive, and
	       * can now assume that weights are >0 for non-NA
	       */
	      if (ggv.weight_power != 0. || ggv.within_between != 1.) {
	        if (ggv.weights.els[i][j] == 0.) continue;
	      }        
	
	      /* another active dissimilarity */
	      ggv.num_active_dist++;  
	
	      /* configuration distance */
	      if (ggv.KruskalShepard_classic == MDSKSInd.KruskalShepard) {
	        ggv.config_dist.els[i][j] = Lp_distance_pow(i, j, ggv);
	        ggv.trans_dist.els[i][j]  = ggv.Dtarget.vals[i][j];
	      } else { /* CLASSIC */
	        ggv.config_dist.els[i][j] = dot_prod(i, j, ggv);
	        ggv.trans_dist.els[i][j]  = -ggv.Dtarget.vals[i][j]*
	                                    ggv.Dtarget.vals[i][j];
	      }
	      /* store untransformed dissimilarity in transform vector for now:
	       * METRIC will transform it; NONMETRIC will used it for sorting first.
	       */
	
	    } /* j */
	  } /* i */
	  /* ------------ end collecting active dissimilarities ------------------ */
}

private void mds_once_part1(Ggvisd ggv) {
	/* preparation for transformation */
	  if (ggv.trans_dist.nels < ggv.ndistances) {
	    /* transformation of raw_dist */
		  ggv.trans_dist.arrayd_alloc (ggv.Dtarget.nrows,ggv.Dtarget.ncols);
	  }
	  /* distances of configuration points */
	  if (ggv.config_dist.nels < ggv.ndistances) {
		  ggv.config_dist.arrayd_alloc (ggv.Dtarget.nrows,ggv.Dtarget.ncols);
	  }
	  /* initialize everytime we come thru because missings may change
	      due to user interaction */
	  for (int i = 0 ; i < ggv.Dtarget.nrows; i++) {
	    for (int j = 0; j < ggv.Dtarget.ncols; j++) {
	      ggv.config_dist.els[i][j] = Double.MAX_VALUE;
	      ggv.trans_dist.els[i][j]  = Double.MAX_VALUE;
	    } 
	  }
	
	  /* weight vector */
	  set_weights (ggv);
	
	  /* random selection vector */
	  set_random_selection (ggv);
	
	  /*-- set the status for each point: excluded, included, anchor, dragged --*/
	//  if (ggv.point_status.length < ggv.pos.nrows) {
	//	  ggv.point_status.vectori_realloc ( ggv.pos.nrows);
	//  }
	//  for (i=0; i<ggv.pos.nrows; i++) 
	//    ggv.point_status.els[i] = EXCLUDED;
	//  for (i=0; i<dpos.nrows_in_plot; i++) { 
	//    n = dpos.rows_in_plot.els[i]; 
	//    if(!dpos.hidden_now.els[n])
	//      ggv.point_status.els[n] = INCLUDED;
	//  }
	
	/*  I think this has been accounted for.
	    for (i=0; i<ggv.pos.nrows; i++) {
	      if (d->clusterid.length > 0 &&
	          d->clusv[(int)GROUPID(i)].excluded == 1) 
	      {
	        ggv.point_status.els[i] = EXCLUDED;
	      }
	    }
	*/
	  /* anchors of either kind */  
	// FIXME if (ggv.anchor_group.length > 0 && ggv.n_anchors > 0 &&
	//      (ggv.anchor_ind == MDSAnchorInd.fixed || ggv.anchor_ind == MDSAnchorInd.scaled))
	//  {
	//    for (i=0; i<ggv.pos.nrows; i++) {
	//      if (!IS_EXCLUDED(i) &&
	//          ggv.anchor_group.els[ggv.dsrc.clusterid.els[i]])  /* which d? */
	//      {
	//        ggv.point_status.els[i] = ANCHOR;
	//      }
	//    }
	//  }
	
	  /* dragged by mouse */
	  if (/* TODO imode_get (gg) == MOVEPTS && gg->buttondown && dpos->nearest_point != -1 */
			  dragged_by_mouse()) {
	// TODO    if (gg.movepts.cluster_p) {
	// TODO		  for (i=0; i<ggv.pos.nrows; i++) {
	// TODO			  if (!IS_EXCLUDED(i) && SAMEGLYPH(dpos,i,dpos->nearest_point)) {
	// TODO				  ggv.point_status.els[i] = DRAGGED;
	// TODO			  }
	// TODO		  }
	// TODO	  } else {
	// TODO		  ggv.point_status.els[dpos->nearest_point] = DRAGGED;
	// TODO	  }
	  }
}

  private boolean IS_INCLUDED(int i) {
	  return true;
  }

  private boolean SAMEGLYPH(GGobiData dpos, int i, int j) {
	  return false;
  }

  private boolean IS_ANCHOR(int i) {
	  return false;
  }

  private boolean dragged_by_mouse() {
	  return false;
  }
  
  public void init(Ggvisd ggv, double[][] dissimilarities) {

	  ggv.Dtarget = new array_d();
	  ggv.Dtarget.arrayd_alloc(dissimilarities.length, dissimilarities.length);
	  ggv.ggv_init_Dtarget(dissimilarities);  /* populate with INF */
	  ggv.ggv_compute_Dtarget(dissimilarities);

	  ggv.pos = new array_d();
	  ggv.pos.arrayd_alloc(dissimilarities.length, dissimilarities.length);
	  for (int a = 0; a < dissimilarities.length; a++) {
		  for (int b = 0; b < dissimilarities.length; b++) {
			  ggv.pos.vals[a][b] = (Math.random() - 0.5) * 2;
		  }
	  }

	  mds_func();

  }

private void mds_func() {
	// TODO Auto-generated method stub
	
}

}