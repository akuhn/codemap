package ch.akuhn.util;

public class Stopwatch {

    private int n = 0;
    private long time = -1;
    
    public void print(String message) {
        long diff = -(time - (time = System.nanoTime()));
        System.out.print("\t("+n+")");
        if (n++ > 0) System.out.print(" "+(1e-9)*diff);
        if (message != null) System.out.print(" " + message);
        System.out.println();
    }
    
    private static Stopwatch SINGELTON = new Stopwatch();
    
    public static void p(String message) {
        SINGELTON.print(message);
    }
    
    public static void p() {
        SINGELTON.print(null);
    }

}
