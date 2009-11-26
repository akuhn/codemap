package sandbox;

import org.netlib.blas.BLAS;

public class FirstSteps {

    public static void main(String[] args) {
        
        BLAS blas = BLAS.getInstance();
        System.out.println(blas.getClass());
        
        Vector dx, dy;
        
        dx = Vector.upto(10);
        dx.p();
        blas.dscal(10, 2.0, dx.val, 1);
        dx.p();

        dx = Vector.upto(10);
        dy = Vector.size(10).fill(1);
        dy.p();
        blas.daxpy(10, 10.0, dx.val, 1, dy.val, 1);
        dy.p();

        dx = Vector.upto(10);
        dy = Vector.size(10).fill(0);
        dx.p();
        puts(blas.ddot(10, dx.val, 1, dy.val, 1));
        
        
        Matrix a;
        
        a = new Matrix(new double[][] {{1,0,0},{0,0,0}});
        dy = Vector.size(3).fill(1);
        dx = Vector.size(2).fill(1);
        dy.p();
        blas.dgemv("N", a.m, a.n, 0, a.val, a.m, dx.val, 1, 2, dy.val, 1);
        dy.p();
        
    }

    private static void puts(double d) {
        System.out.println(d);
    }
    
}
