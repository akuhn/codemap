package ch.akuhn.org.ggobi.plugins.ggvis;

import java.util.Arrays;

public class Ggvisd {

	// WAS GGobiData dsrc;  /*-- original data values --*/
	GGobiData dpos;  /*-- the new datad which contains the values in pos --*/
	// WAS GGobiData e;     /*-- edge set, corresponds both to dsrc and dpos --*/

	boolean running_p;
	int idle_id;

	array_d Dtarget;  /*-- D in the documentation; dist in the xgvis code --*/
	array_d pos;

	// FIXME GtkWidget *stressplot_da;
	// FIXME GdkPixmap *stressplot_pix;
	vector_d stressvalues;  /*-- allocated to hold NSTRESSVALUES values --*/
	int nstressvalues;     /*-- the number of stress values */

	// FIXME dissimd *dissim;

	int dim;
	double stepsize;

	double Dtarget_power;  /* was mds_power */
	double weight_power;   /* was mds_weightpow */
	double dist_power;     /* was mds_distpow */

	double lnorm;
	double dist_power_over_lnorm;
	double lnorm_over_dist_power;

	double isotonic_mix;
	double within_between;
	double rand_select_val;
	double rand_select_new;
	double perturb_val;
	double threshold_high;
	double threshold_low;

	vector_d pos_mean;
	vector_d weights;
	vector_d trans_dist;
	vector_d config_dist;
	vector_i point_status;
	vector_i trans_dist_index, bl;
	array_d gradient;
	vector_d bl_w;
	double pos_scl;
	double Dtarget_max, Dtarget_min;
	vector_d rand_sel;
	int freeze_var;
	int ndistances;
	int num_active_dist;
	int prev_nonmetric_active_dist;

	MDSMetricInd metric_nonmetric;
	MDSKSInd KruskalShepard_classic;
	// FIXME  MDSTask mds_task;  /* DissimAnalysis or GraphLayout */
	// FIXME  MDSDtargetSource Dtarget_source;
	int weight_var;  /* index of the variable which is the source of
			       the distance matrix or vector of weights */
	// FIXME GtkWidget *tree_view_dist;
	boolean complete_Dtarget;

	MDSGroupInd group_ind;
	/* anchors */
	MDSAnchorInd anchor_ind;
	vector_b anchor_group;
	// FIXME  GtkWidget *anchor_frame, *anchor_table;
	int n_anchors;

	// FIXME  GtkTooltips *tips;

	// FIXME  GtkWidget *varnotebook;  /* this might make it easier to destroy */

	/*-- for Shepard plot --*/
	int shepard_iter;

	void ggvis_init (Ggobid gg) {
	  
	  // FIXME GGobiData *d;
	  // GSList *l;
	  Object d;
		
	  this.running_p = false;
	  this.idle_id = 0;

	  this.Dtarget = new array_d();
	  this.pos = new array_d();

	  // FIXME this.stressplot_pix = null;
	  // this.nstressvalues = 0;
	  // this.stressvalues.vectord_init_null();
	  // vectord_alloc (&this.stressvalues, NSTRESSVALUES);

//	  FIXME this.dissim = (dissimd *) g_malloc (sizeof (dissimd));
//	  this.dissim->pix = NULL;
//	  this.dissim->low = 0.;
//	  this.dissim->high = 1.;
//	  this.dissim->lgrip_pos = -1;
//	  this.dissim->rgrip_pos = -1;
//	  this.dissim->bars = NULL;
//	  vectorb_init_null (&this.dissim->bars_included);
//	  vectori_init_null (&this.dissim->bins);

	  this.dim = 3;

	  this.stepsize = 0.02;
	  this.dist_power = 1.0;
	  this.Dtarget_power = 1.0;
	  this.lnorm = 2.0;
	  this.weight_power = 0.0;

	  this.isotonic_mix = 1.0;
	  this.dist_power_over_lnorm = 0.5;
	  this.lnorm_over_dist_power = 2.0;
	  this.within_between = 1.0;
	  this.rand_select_val = 1.0;  /* selection probability */
	  // FIXME this.rand_select_new = false;
	  this.perturb_val = 1.0;
	  this.threshold_high = 0.0;
	  this.threshold_low = 0.0;

	  this.metric_nonmetric = MDSMetricInd.metric;
	  this.KruskalShepard_classic = MDSKSInd.KruskalShepard;

	  this.num_active_dist = 0;

	  /* Assume that we're doing graph layout */
	  //this.mds_task = GraphLayout;
	  //this.Dtarget_source = LinkDist;
	  this.complete_Dtarget = false;
	  this.weight_var = -1;
	  /* 
	     Then loop over datads, looking for one devoted to specifying
	     dissimilarities for MDS
	  */
//	  FIXME for (l = gg->d; l; l = l->next) {
//	    d = l->data;
//	    if (d->edge.n > 0) {
//	      if (g_strcasecmp (d->name, "dist") == 0 ||
//	          g_strcasecmp (d->name, "distance") == 0 ||
//	          g_strcasecmp (d->name, "dissim") == 0)
//	      { /* then we're in the MDS case */
//	        this.mds_task = DissimAnalysis;
//	        break;
//	      }
//	    }
//	  }

	  this.group_ind = MDSGroupInd.all_distances;

//	  FIXME this.anchor_ind = no_anchor;
//	  this.anchor_table = (GtkWidget *) NULL;
//	  vectorb_init_null (&this.anchor_group);
//	  this.n_anchors = 0;

	  /*-- used in mds.c --*/
	  this.pos_mean = new vector_d();
	  this.weights = new vector_d();
	  this.rand_sel = new vector_d();
	  this.trans_dist = new vector_d();
	  this.config_dist = new vector_d();
	  this.point_status = new vector_i();
	  this.trans_dist_index = new vector_i();
	  this.bl = new vector_i();
	  this.bl_w = new vector_d();
	  this.gradient = new array_d();

	  this.pos_scl = 0.0;
	  this.freeze_var = 0;
	  this.Dtarget_max = Mds.G_MAXDOUBLE;
	  this.Dtarget_min = -Mds.G_MAXDOUBLE;
	  this.prev_nonmetric_active_dist = 0;
	  /* */

	  this.shepard_iter = 0;
	}

	
	void ggv_init_Dtarget(double[][] array) {
		double infinity, largest = Double.NaN;

		/*-- initalize Dtarget --*/
		infinity = (double) (2 * this.Dtarget.nrows);
		largest = array[0][0];
		for (int a = 0; a < array.length; a++) {
			for (int b = 0; b < array.length; b++) {
				if (array[a][b] > infinity) {
					infinity = array[a][b];
				}
				if (array[a][b] > largest) {
					largest = array[a][b];
				}
				
			}
		}

	  /* Report the value of largest */
	  if (largest > 100000) throw new Error("Warning: your largest weight, %.2f (index %d), is extremely large. ");

	/* Continue to initialize using the value of infinity.  Is that ok? */
		for (int a = 0; a < array.length; a++) {
			for (int b = 0; b < array.length; b++) {
				Dtarget.vals[a][b] = infinity;
			}
			Dtarget.vals[a][a] = 0.0;
		}
	}

	void ggv_compute_Dtarget (double[][] array) {
	  double[][] Dvals = Dtarget.vals;

	  /*-- populate --*/
	  if (!complete_Dtarget) {
			for (int a = 0; a < array.length; a++) {
				for (int b = 0; b < array.length; b++) {
					Dtarget.vals[a][b] = array[a][b];
				}
			}
	  } else {  /*-- complete Dtarget using a shortest path algorithm --*/
//
//	    nsteps = 0;
//	    changing = true;
//	    while (changing) {
//	      changing = false;
//	      for (i = 0; i < e->edge.n; i++) {
//	        end1 = endpoints[i].a;
//	        end2 = endpoints[i].b;
//	        d12 = 
//	          (ggv->mds_task == DissimAnalysis || ggv->Dtarget_source == VarValues) ?
//	          e->tform.vals[i][selected_var] : 1.0;
//	        if (d12 < 0) {
//	          g_printerr ("Re-setting negative dissimilarity to zero: index %d, value %f\n",
//	            i, d12);
//	          d12 = 0;
//	        }
//
//	        for (end3 = 0; end3 < dsrc->nrows; end3++) {
//	          /* So we have a direct link from end1 to end2.  Can this be */
//	          /* used to shortcut a path from end1 to end3 or end2 to end3? */
//	          if (end3 != end1 && Dvals[end1][end3] > d12 + Dvals[end2][end3]) {
//	            Dvals[end3][end1] = Dvals[end1][end3] = d12 + Dvals[end2][end3];
//	            changing = true;
//	          }
//	          if (end3 != end2 && Dvals[end2][end3] > d12 + Dvals[end1][end3]) {
//	            Dvals[end3][end2] = Dvals[end2][end3] = d12 + Dvals[end1][end3];
//	            changing = true;
//	          }
//	        }    /* end3 */
//	      }    /* end1 and end2 */
//	      nsteps++;
//	      if (nsteps > 10) {
//	        g_printerr ("looping too many times; something's wrong ...\n");
//	        break;
//	      }
//	    }    /* while changing. */
		  throw null;
	  }

	/*
	{
	gint n = (ggv->Dtarget.nrows < 10)?ggv->Dtarget.nrows:10;
	g_printerr ("n: %d\n", n);
	for (i=0; i<n; i++) {
	  for (j=0; j<n; j++) {
	    g_printerr ("%.2f ", Dvals[i][j]);
	  }
	  g_printerr ("\n");
	}
	}
	*/

	  this.ndistances = this.Dtarget.nrows * this.Dtarget.ncols;

	  this.Dtarget_max = -Mds.G_MAXDOUBLE;  this.Dtarget_min = Mds.G_MAXDOUBLE;
	  for (int i=0; i<Dtarget.nrows; i++) {
	    for (int j=0; j<Dtarget.ncols; j++) {
	      double dtmp = Dtarget.vals[i][j]; 
	      if (dtmp < 0) {
	        throw new Error("negative dissimilarity: D[%d][%d] = %3.6f -> NA\n");
	        // FIXME dtmp = Dtarget.vals[i][j] = Mds.G_MAXDOUBLE;
	      }
	      if(dtmp != Mds.G_MAXDOUBLE) {
	        if (dtmp > Dtarget_max) Dtarget_max = dtmp;
	        if (dtmp < Dtarget_min) Dtarget_min = dtmp;
	      }
	    }
	  }
	  threshold_low =  Dtarget_min;
	  threshold_high = Dtarget_max;
	}


	
}

enum MDSAnchorInd {no_anchor, scaled, fixed};

enum MDSKSInd {KruskalShepard, classic};

enum MDSGroupInd {all_distances, within, between};

enum MDSMetricInd {metric, nonmetric} ;

class Ggobid {
	
}

class GGobiData {

	int nrows_in_plot;
	vector_i rows_in_plot;
	vector_b hidden_now;
	vector_i clusterid;
	array_d tform;
	
}

class array_d {
	double vals[][];
	int nrows, ncols;
	public void arrayd_free(int nrows2, int ncols2) { 
		nrows = ncols = 0;
		vals = null;
	}
	public void arrayd_init_null() { throw null; }
	public void arrayd_alloc(int nrows2, int ncols2) { 
		nrows = nrows2; 
		ncols = ncols2;
		vals = new double[nrows][ncols];
	}
	public void arrayd_zero() { 
		vals = new double[nrows][ncols]; // LULZ
	}
}

class vector_d {
	double els[];
	int nels;
	void vectord_realloc(int dim) { 
		nels = dim;
		els = new double[dim];
	}
	public void vectord_init_null() { throw null; }
	void vectord_zero() {
		Arrays.fill(els, 0.0);
	}
};

class vector_b {
	boolean els[];
	int nels;
}

class vector_i {
	int els[];
	int nels;
	void vectori_realloc(int nrows) { throw null; }
	public void vectori_init_null() { throw null; }
}