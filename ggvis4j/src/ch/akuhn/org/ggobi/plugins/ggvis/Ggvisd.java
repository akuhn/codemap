package ch.akuhn.org.ggobi.plugins.ggvis;

public class Ggvisd {

	GGobiData dsrc;  /*-- original data values --*/
	GGobiData dpos;  /*-- the new datad which contains the values in pos --*/
	GGobiData e;     /*-- edge set, corresponds both to dsrc and dpos --*/

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

	// FIXME MDSMetricInd metric_nonmetric;
	// FIXME  MDSKSInd KruskalShepard_classic;
	// FIXME  MDSTask mds_task;  /* DissimAnalysis or GraphLayout */
	// FIXME  MDSDtargetSource Dtarget_source;
	int weight_var;  /* index of the variable which is the source of
			       the distance matrix or vector of weights */
	// FIXME GtkWidget *tree_view_dist;
	boolean complete_Dtarget;

	// FIXME  MDSGroupInd group_ind;
	/* anchors */
	MDSAnchorInd anchor_ind;
	vector_b anchor_group;
	// FIXME  GtkWidget *anchor_frame, *anchor_table;
	int n_anchors;

	// FIXME  GtkTooltips *tips;

	// FIXME  GtkWidget *varnotebook;  /* this might make it easier to destroy */

	/*-- for Shepard plot --*/
	int shepard_iter;

}

enum MDSAnchorInd {no_anchor, scaled, fixed};

class Ggobid {
	
}

class GGobiData {

	int nrows_in_plot;
	vector_i rows_in_plot;
	vector_b hidden_now;
	vector_i clusterid;
	
}

class array_d {
	double vals[][];
	int nrows, ncols;
}

class vector_d {
	double els[];
	int nels;
	void vectord_realloc(int dim) {
		throw null;
	}
	void vectord_zero() {
		throw null;
	}
};

class vector_b {
	boolean els[];
	int nels;
}

class vector_i {
	int els[];
	int nels;
	void vectori_realloc(int nrows) {
		throw null;
	}
}