package ch.deif.meander.mds;

public class ModRandom implements IRandom {

    private static final int MULT = 16807;
    private static final int MOD = 2147483647;
    private int rand_num = 30;

    @Override
    public double nextDouble() {
        int next = nextInt();
        return ((next % 50) + 50.0) / 100;
    }

    private int nextInt() {
        return nextInt(Integer.MAX_VALUE);
    }

    @Override
    public int nextInt(int upper) {
        rand_num = (rand_num * MULT) % MOD;
        return Math.abs(rand_num % upper);
    }

}
