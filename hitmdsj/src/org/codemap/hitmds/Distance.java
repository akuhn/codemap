package org.codemap.hitmds;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public interface Distance {

	public double dist(double[] d1, double[] d2);
	
    /** Euclidean distance */
	public static class DIST implements Distance {

		@Override
		public double dist(double[] d1, double[] d2) {
			assert d1.length== d2.length;
			int dimension = d1.length;
			double sum = 0.0;
			for(int u = 0; u < dimension; u++) {
				double tmp = d1[u] - d2[u];
				sum += tmp * tmp;
			}
			return sqrt(sum);
		}

	};
	
	/** "quadratic" Euclidean yields nice separation, sometimes */
	public static class DIST4S implements Distance {
		
		public double dist(double[] d1, double[] d2) {
			assert d1.length== d2.length;
			int dimension = d1.length;
	        double sum = 0.0;
	        
	        for(int u = 0; u < dimension; u++) {
	            double tmp = d1[u] - d2[u];
	            tmp *= tmp;
	            sum += tmp * tmp;
	        }
	        
	        return sum;
	    }
		
	};
	  
	public static class DIST_MINKOWSKI implements Distance {

		/* general Minkowski metric, mexponent = 2 -> Euclidean distance */
		double mexponent;

		public DIST_MINKOWSKI(int mexponent) {
			this.mexponent = mexponent;
		}

		/* general Minkowski metric, mexponent = 2 -> Euclidean distance */
		public double dist(double[] d1, double[] d2) {
			assert d1.length== d2.length;
			int dimension = d1.length;
			double sum = 0.0;
			for(int u = 0; u < dimension; u++) {
				double tmp = d1[u] - d2[u];
				tmp = abs(tmp);
				sum += pow(tmp, mexponent);
			}
			return pow(sum,1./mexponent);
		}

	}

	
	/* (1 - Pearson)^expo correlation */
	public static class CORR implements Distance {

	    /* general Minkowski metric, mexponent = 2 -> Euclidean distance */
		double mexponent;

		public CORR(int mexponent) {
			this.mexponent = mexponent;
		}

		@Override
		public double dist(double[] d1, double[] d2) {
			assert d1.length== d2.length;
			int dimension = d1.length;
			
			double mono1 = 0.0, mono2 = 0.0, mixed = 0.0, tmp,
			md1 = Hitmds2.mean(dimension, d1),
			md2 = Hitmds2.mean(dimension, d2);

			for(int u = 0; u < dimension; u++) {
				tmp = d1[u] - md1; mono1 += tmp * tmp;
				tmp = d2[u] - md2; mono2 += tmp * tmp;
				mixed += tmp * (d1[u] - md1);
			}

			tmp = sqrt(mono1 * mono2);

			mixed /= (tmp < Hitmds2.EPS) ? Hitmds2.EPS : tmp;

			boolean u;
			/* allow correlated and anti-correlated patterns to be similar */
			if((u = mexponent < 0.) && mixed < 0.0) mixed = -mixed; 

			mono1 = u ? -mexponent : mexponent;

			return (mono1 == 1.0) ? 1.0 - mixed : pow(1.0 - mixed, mexponent);
		}

	}
	
    /* derivative of Pearson correlation w.r.t d2 or absolute to both directions (symmetric) */
	public static class CORR_DERIV_ABS implements Distance {

		private double corr_deriv_vec(int dimension, double[] d1, double[] d2, boolean symmetric) {

			int u;

			double mono1 = 0., mono2 = 0., mixed = 0., sum = 0., tmp, tmx, f,
			md1 = Hitmds2.mean(dimension, d1),
			md2 = Hitmds2.mean(dimension, d2);


			for(u = 0; u < dimension; u++) {

				tmx = d1[u] - md1; mono1 += tmx * tmx;
				tmp = d2[u] - md2; mono2 += tmp * tmp;
				mixed += tmp * tmx;
			}

			tmp = sqrt(mono1 * mono2);

			if(tmp < Hitmds2.EPS) tmp = Hitmds2.EPS;
			tmp = 1. / tmp;

			if(mono1 < Hitmds2.EPS) mono1 = Hitmds2.EPS;
			mono1 = mixed / mono1;

			if(symmetric) {
				if(mono2 < Hitmds2.EPS) mono2 = Hitmds2.EPS;
				mono2 = mixed / mono2;
			}

			for(u = 0; u < dimension; u++) {

				f = ((d1[u] - md1) - mono2 * (d2[u] - md2)) * tmp;

				if(symmetric) {
					tmx = ((d2[u] - md2) - mono1 * (d1[u] - md1)) * tmp;
					f *= tmx;
					f = abs(f);
				}
				sum += f;

			}

			return sum / dimension;  /* average (absolute) contribution */
		}


		/* realize symmetric measure d(x,y) = d(y,x) */
		public double dist(double[] d1, double[] d2) {
			assert d1.length== d2.length;
			int dimension = d1.length;
			return corr_deriv_vec(dimension, d1, d2, true);
		}

	}
	
}
