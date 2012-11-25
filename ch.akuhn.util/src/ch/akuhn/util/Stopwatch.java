package ch.akuhn.util;

import java.io.IOException;

public class Stopwatch {

    private int n = 0;
    private long time = Long.MAX_VALUE;
    private long collect = 0; 
    
    public void print(String message) {
    	privatePrint((time - (time = System.nanoTime())) * -1, message);
    }
    
    private void privatePrint(long nanos, String message) {
        System.out.print("\t("+n+")");
        if (nanos > 0) System.out.print(" "+(1e-9)*nanos);
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

	public void on() {
		time = System.nanoTime();
	}
	
	public <T> T off(T t) {
		collect += (System.nanoTime() - time);
		time = 0;
		return t;
	}
	
	public void total(String message) {
		privatePrint(collect, message);
		collect = 0;
	}

	public static void enter() {
		try {
			System.out.print("Press enter: ");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
