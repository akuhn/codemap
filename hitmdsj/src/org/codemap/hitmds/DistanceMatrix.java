package org.codemap.hitmds;

public class DistanceMatrix {

	private float[] data;
	private int size;
	double points_distmat_mixed;
	double points_distmat_mono;
	
	public DistanceMatrix(int size) {
		assert size > 0;
        data = new float[((size * (size - 1)) / 2) + 1];
        this.size = size;
        this.set(0,0, 0.0f);
	}

	public void set(int i, int j, float f) {
		assert i < size : i;
		assert j < size : j;
        data[(i < j) ? (j - 1 + ((((size * 2) - i - 3) * i) / 2))
                : ((i==j) ? (data.length - 1)
                        : (i - 1 + ((((size * 2) - j - 3) * j) / 2)))] = f;
	}
	
	public float get(int i, int j) {
		return data[(i < j) ? (j - 1 + ((((size * 2) - i - 3) * i) / 2))
            : ((i==j) ? (data.length - 1) 
                    : (i - 1 + ((((size * 2) - j - 3) * j) / 2)))];
	}

	public int size() {
		return size;
	}

	public float meanf() {
		float sum = 0;
		for (float f: data) sum += f;
		sum -= data[data.length - 1];
		return sum / length();
	}

	public int length() {
		return data.length - 1;
	}
	
	public void extracted(DistanceMatrix distmat, double points_distmat_mean, double pattern_distmat_mean) {
		points_distmat_mixed = points_distmat_mono = 0.0;
	    for(int i = 0; i < length(); i++) {
	        double tmp = data[i] - points_distmat_mean;
	        points_distmat_mixed += tmp * (distmat.data[i] -= pattern_distmat_mean);
	        points_distmat_mono +=  tmp * tmp;
	    }
	}

	public double var_sum(DistanceMatrix distmat) {
		float pattern_distmat_var_sum = 0;
		for(int i = 0; i < length(); i++) {
			pattern_distmat_var_sum  += data[i] * distmat.data[i];
		}	
		return pattern_distmat_var_sum;
	}
	
}
