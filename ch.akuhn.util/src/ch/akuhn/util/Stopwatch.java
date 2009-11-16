package ch.akuhn.util;

public class Stopwatch {

    private int n = 0;
    private long time = -1;
    
    public void print() {
        long diff = -(time - (time = System.nanoTime()));
        System.out.print("\t("+n+")");
        if (n++ > 0) System.out.print(" "+(1e-12)*diff);
        System.out.println();
    }
    
    private static Stopwatch SINGELTON = new Stopwatch();
    
    public static void p() {
        SINGELTON.print();
    }
    
}
