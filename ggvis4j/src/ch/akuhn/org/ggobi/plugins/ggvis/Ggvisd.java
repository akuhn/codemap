package ch.akuhn.org.ggobi.plugins.ggvis;

import java.util.Arrays;


public class Ggvisd {

	array_d Dtarget;  /*-- D in the documentation; dist in the xgvis code --*/
	array_d pos;

	public double[][] points() {
		return pos.vals;
	}
	
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

	double[] pos_mean;
	array_d weights;
	array_d trans_dist;
	array_d config_dist;
	int[] point_status;
	int[] trans_dist_index, bl;
	array_d gradient;
	double[] bl_w;
	double pos_scl;
	double Dtarget_max, Dtarget_min;
	int ndistances;
	int num_active_dist;
	int prev_nonmetric_active_dist;

	MDSGroupInd group_ind;
	MDSAnchorInd anchor_ind;

	boolean[] anchor_group;
	int n_anchors;

	void printSymtriacMatrices() {
//				System.out.print(weights.isSymetric() ? "S" : ".");
//				System.out.print(trans_dist.isSymetric() ? "S" : ".");
//				System.out.print(config_dist.isSymetric() ? "S" : ".");
//				System.out.print(gradient.isSymetric() ? "S" : ".");
//				//System.out.print(rand_sel.isSymetric() ? "S" : ".");
//				System.out.println();
	}

	public void ggvis_init () {

		this.Dtarget = new array_d();
		this.pos = new array_d();

		this.dim = 2;
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
		this.perturb_val = 1.0;
		this.threshold_high = 0.0;
		this.threshold_low = 0.0;

		this.num_active_dist = 0;
		this.group_ind = MDSGroupInd.all_distances;

		/*-- used in mds.c --*/
		this.pos_mean = new double[] {};
		this.weights = new array_d();
		this.trans_dist = new array_d();
		this.config_dist = new array_d();
		this.point_status = new int[] {};
		this.trans_dist_index = new int[] {};
		this.bl = new int[] {};
		this.bl_w = new double[] {};
		this.gradient = new array_d();

		this.pos_scl = 0.0;
		this.Dtarget_max = Double.MAX_VALUE;
		this.Dtarget_min = Double.MIN_VALUE;
		this.prev_nonmetric_active_dist = 0;
		/* */
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

		for (int a = 0; a < array.length; a++) {
			for (int b = 0; b < array.length; b++) {
				Dtarget.vals[a][b] = array[a][b];
			}
		}

		this.ndistances = this.Dtarget.nrows * this.Dtarget.ncols;

		this.Dtarget_max = Double.MIN_VALUE;  this.Dtarget_min = Double.MAX_VALUE;
		for (int i=0; i<Dtarget.nrows; i++) {
			for (int j=0; j<Dtarget.ncols; j++) {
				double dtmp = Dtarget.vals[i][j]; 
				if (dtmp < 0) {
					throw new Error("negative dissimilarity: D[%d][%d] = %3.6f -> NA\n");
					// FIXME dtmp = Dtarget.vals[i][j] = Mds.G_MAXDOUBLE;
				}
				if(dtmp != Double.NaN) {
					if (dtmp > Dtarget_max) Dtarget_max = dtmp;
					if (dtmp < Dtarget_min) Dtarget_min = dtmp;
				}
			}
		}
		threshold_low =  Dtarget_min;
		threshold_high = Dtarget_max;
	}

	enum MDSAnchorInd {no_anchor, scaled, fixed};

	enum MDSGroupInd {all_distances, within, between};

}


class GGobiData {

	int nrows_in_plot;
	int[] rows_in_plot;
	boolean[] hidden_now;
	int[] clusterid;
	array_d tform;

}

class array_d {
	double[][] vals;
	int nrows, ncols, nels;
	public void arrayd_free(int nrows2, int ncols2) { 
		nels = nrows = ncols = 0;
		vals = null;
	}
	public void arrayd_init_null() { throw null; }
	public void arrayd_alloc(int nrows2, int ncols2) { 
		nrows = nrows2; 
		ncols = ncols2;
		nels = ncols * nrows;
		vals = new double[nrows][ncols];
	}
	public void arrayd_zero() { 
		vals = new double[nrows][ncols]; // LULZ
	}
	public boolean isSymetric() {
		if (nrows != ncols) return false;
		if (nrows == 0) return true;
		for (int i = 0; i < vals.length; i++) {
			for (int j = 0; j < i; j++) {
				if (Double.isNaN(vals[i][j]) != Double.isNaN(vals[j][i])) return false;
				if ((vals[i][j] - vals[j][i]) > 1e-9) return false;
			}
		}
		return true;
	}
	public void fill(double value) {
		for (double[] each: vals) Arrays.fill(each, value);
	}
}