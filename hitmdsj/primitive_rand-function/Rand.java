import java.lang.Math;

public class Rand {
    
    private static int rand_num = 30;
    
    public static void main(String... args) {
        for (int i = 0; i<100000; i++) {
            System.out.printf("%f\n", frand());
        }
    }
    
    public static double frand() {
        return (double) myrand() / 2147483648.0;
    }
    
    public static int myrand() {
        rand_num = Math.abs((rand_num * 16807) % 2147483647);
        return rand_num;
    }   
}