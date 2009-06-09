package org.codemap.hitmds.random;
import java.lang.Math;
import java.util.Random;

@SuppressWarnings("serial")
public class PrimitiveRandom extends Random {
    
    private int rand_num = 30;
    
    public double frand() {
        return ((double) myrand()) / 2147483648.0d;
    }
    
    public int myrand() {
        rand_num = Math.abs((rand_num * 16807) % 2147483647);
        return rand_num;
    }

	@Override
	public double nextDouble() {
		return frand();
	}

	@Override
	protected int next(int bits) {
		throw null;
	}

	@Override
	public boolean nextBoolean() {
		throw null;
	}

	@Override
	public void nextBytes(byte[] bytes) {
		throw null;
	}

	@Override
	public float nextFloat() {
		throw null;
	}

	@Override
	public synchronized double nextGaussian() {
		throw null;
	}

	@Override
	public int nextInt() {
		throw null;
	}

	@Override
	public int nextInt(int n) {
		throw null;
	}

	@Override
	public long nextLong() {
		throw null;
	}

}