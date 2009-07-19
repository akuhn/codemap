package ch.akuhn.org.ggobi.plugins.ggvis;

public class Ggvisd {

	array_d pos;
	double lnorm;
	double dist_power;
	int dim;
	double dist_power_over_lnorm;
	vector_d pos_mean;
}

class array_d {
	double vals[][];
	int nrows, ncols;
}

class vector_d {
	double els[];
	int nels;
	void vectord_realloc(int dim) {
		// TODO Auto-generated method stub
		
	}
	void vectord_zero() {
		// TODO Auto-generated method stub
		
	}
};
