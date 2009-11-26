package sandbox;

public class Matrix {

    public double[] val;
    public int n, m;
    
    public Matrix(double[][] data) {
        val = new double[data.length * data[0].length];
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, val, i * data[0].length, data[i].length);
        }
        n = data.length;
        m = data[0].length;
    }
    
}
