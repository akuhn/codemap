package ch.akuhn.matrix;

import ch.akuhn.util.Stopwatch;

public class PerformanceTest {

	private M a;

	public static void main(String[] args) {
		new PerformanceTest().run();
	}
	
	public void run() {
		a = new OneDimensional(1000);
		double[] v = new double[1000];
			
		Stopwatch.p();
		for (int i = 0; i < 10; i++) {
			a.put((int)(Math.random() * 1000),(int)(Math.random() * 1000),Math.random());
			v[(int)(Math.random() * 1000)] = Math.random();
		}
		
		Stopwatch.p();
		for (int i = 0; i < 100; i++) {
			v = a.mult(v);
		}
		Stopwatch.p();
		System.out.println(v);
	}

	private double opto() {
		double prod = 0;
		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 1000; j++) {
				prod *= a.get(i,j);
			}
		}
		return prod;
	}
	
}

class OneDimensional extends M {
	
	protected int lda;
	protected double[] a;

	public OneDimensional(int n) {
		lda = n;
		a = new double[n*n];
	}
	
	@Override
	public double get(int i, int j) {
		return a[i + lda * j];
	}
	
	@Override
	public void put(int i, int j, double value) {
		a[i + lda * j] = value;
	}

	@Override
	public double[] mult(double[] y) {
		double[] x = new double[a.length];
		for (int n = 0, i = 0; i < lda; i++) {
			double y_i = y[i];
			for (int j = 0; j < lda; j++, n++) {
				x[j] += a[n] * y_i;
			}
		}
		return x;
	}
}

class TwoDimensional extends M {
	
	protected double[][] a;

	public TwoDimensional(int n) {
		a = new double[n][n];
	}
	
	@Override
	public double get(int i, int j) {
		return a[i][j];
	}
	
	@Override
	public void put(int i, int j, double value) {
		a[i][j] = value;
	}

	@Override
	public double[] mult(double[] y) {
		double[] x = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				x[i] += a[i][j] * y[j];
			}
		}
		return x;
	}
	
}


abstract class M {

	public abstract double get(int i, int j);

	public abstract void put(int i, int j, double value);
	
	public abstract double[] mult(double[] v);

}
