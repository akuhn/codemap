package sandbox;

import java.util.Arrays;

public class Vector {

    public final double[] val;
    
    public Vector(int length) {
        this.val = new double[length];
    }
    
    public Vector(double[] values) {
        this.val = values;
    }

    public Vector fill(double value) {
        Arrays.fill(val, value);
        return this;
    }
    
    public Vector p() {
        System.out.print("[");
        for (int i = 0; i < val.length; i++) {
            if (i > 0) System.out.print(", ");
            double v = val[i];
            System.out.print(v == Math.round(v) ? Integer.toString((int) v) : Double.toString(v));
        }
        System.out.println("]");
        return this;
    }
    
    public static Vector upto(int n) {
        Vector v = new Vector(n);
        for (int i = 0; i < v.val.length; i++) v.val[i] = i;
        return v;
    }

    public static Vector size(int n) {
        return new Vector(n);
    }

    public static Vector from(double[] values) {
        return new Vector(values.clone());
    }

    @Override
    public Vector clone() {
        return new Vector(val.clone());
    }
    
    public Vector sorted() {
        Vector clone = this.clone();
        Arrays.sort(clone.val);
        return clone;
    }

    public Vector reversed() {
        double[] clone = new double[val.length];
        for (int i = 0; i < val.length; i++) clone[val.length - 1 - i] = val[i];
        return new Vector(clone);
    }
    
}
