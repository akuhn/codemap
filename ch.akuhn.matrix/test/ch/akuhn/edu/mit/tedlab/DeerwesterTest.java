package ch.akuhn.edu.mit.tedlab;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class DeerwesterTest {

    private static SMat input;
    private int dimensions;
    private int iterations;
    private double[] end;
    private double kappa;

    public static final double[][] A = new double[][] {
        { 1, 0, 0, 1, 0, 0, 0, 0, 0 },
        { 1, 0, 1, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 1, 1, 0, 1, 0, 0, 0, 0 },
        { 0, 1, 1, 2, 0, 0, 0, 0, 0 },
        { 0, 1, 0, 0, 1, 0, 0, 0, 0 },
        { 0, 1, 0, 0, 1, 0, 0, 0, 0 },
        { 0, 0, 1, 1, 0, 0, 0, 0, 0 },
        { 0, 1, 0, 0, 0, 0, 0, 0, 1 },
        { 0, 0, 0, 0, 0, 1, 1, 1, 0 },
        { 0, 0, 0, 0, 0, 0, 1, 1, 1 },
        { 0, 0, 0, 0, 0, 0, 0, 1, 1 },
    };
    
    public static final double[][] Vt = new double[][] {
        { 0.197393, 0.60599, 0.462918, 0.542114, 0.279469, 0.00381521, 0.0146315, 0.0241368, 0.0819574 },
        { 0.0559135, -0.165593, 0.127312, 0.231755, -0.106775, -0.192848, -0.437875, -0.615122, -0.529937 },
        { -0.11027, 0.497326, -0.207606, -0.569921, 0.50545, -0.0981842, -0.192956, -0.252904, -0.0792731 },
        { -0.949785, -0.0286489, 0.0416092, 0.267714, 0.150035, 0.0150815, 0.0155072, 0.010199, -0.0245549 },
        { -0.0456786, 0.206327, -0.378336, 0.205605, -0.327194, -0.394841, -0.349485, -0.149798, 0.601993 },
        { -0.0765936, -0.256475, 0.7244, -0.368861, 0.034813, -0.300161, -0.212201, 9.74342e-05, 0.362219 },
        { -0.177318, 0.432984, 0.23689, -0.2648, -0.672304, 0.34084, 0.152195, -0.249146, -0.0380342 },
        { -0.0143933, 0.0493053, 0.0088255, -0.0194669, -0.0583496, 0.454477, -0.761527, 0.449643, -0.0696375 },
        { -0.0636923, 0.242783, 0.0240769, -0.0842069, -0.262376, -0.619847, 0.0179752, 0.51989, -0.453507 },
    };

    public static final double[][] Ut = new double[][] {
        { 0.221351, 0.197645, 0.24047, 0.403599, 0.644481, 0.265037, 0.265037, 0.300828, 0.205918, 0.0127462, 0.0361358, 0.0317563 },
        { 0.11318, 0.0720878, -0.043152, -0.0570703, 0.167301, -0.10716, -0.10716, 0.14127, -0.273647, -0.490162, -0.622785, -0.450509 },
        { -0.288958, -0.13504, 0.164429, 0.337804, -0.361148, 0.425998, 0.425998, -0.330308, 0.177597, -0.23112, -0.223086, -0.141115 },
        { -0.414751, -0.55224, -0.594962, 0.0991137, 0.333462, 0.0738122, 0.0738122, 0.188092, -0.0323519, 0.024802, 0.000700072, -0.00872947 },
        { 0.106275, -0.281769, 0.106755, -0.331734, 0.158955, -0.0803194, -0.0803194, -0.114785, 0.53715, -0.59417, 0.0682529, 0.300495 },
        { -0.340983, 0.495878, -0.254955, 0.384832, -0.206523, -0.169676, -0.169676, 0.272155, 0.080944, -0.392125, 0.114909, 0.277343 },
        { -0.522658, 0.0704234, 0.30224, -0.00287218, 0.165829, -0.282916, -0.282916, -0.0329941, 0.466898, 0.288317, -0.159575, -0.339495 },
        { -0.0604501, -0.00994004, 0.062328, -0.000390504, 0.034272, -0.0161465, -0.0161465, -0.018998, -0.0362988, 0.254568, -0.681125, 0.6784180 },
        { -0.406678, -0.10893, 0.492444, 0.0123293, 0.270696, -0.0538747, -0.0538747, -0.165339, -0.579426, -0.225424, 0.231961, 0.182535 },       
    };
    
    
    public static final double delta = 1e-5;

    @Before
    public void makeSMat() {
        DMat D = new DMat(12, 9);
        D.value = A;
        input = new Svdlib().svdConvertDtoS(D);
    }    

    @Before
    public void setup() {
        dimensions = 9;
        iterations = 100;
        end = new double[] { -1e-30, 1e-30 };
        kappa = 1e-6;
    }

    @Test
    public void singularValues() {
        SVDRec result = new Svdlib().svdLAS2(input, dimensions, iterations, end, kappa);
        assertEquals(3.34088, result.S[0], delta);
        assertEquals(2.5417, result.S[1], delta);
        assertEquals(2.35394, result.S[2], delta);
        assertEquals(1.64453, result.S[3], delta);
        assertEquals(1.50483, result.S[4], delta);
        assertEquals(1.30638, result.S[5], delta);
        assertEquals(0.845903, result.S[6], delta);
        assertEquals(0.560134, result.S[7], delta);
        assertEquals(0.363677, result.S[8], delta);
    }

    @Test
    public void leftSingularValues() {
        SVDRec result = new Svdlib().svdLAS2(input, dimensions, iterations, end, kappa);
        assertEquals(12, result.Ut.cols);
        assertEquals(9, result.Ut.rows);
        for (int row = 0; row < 9; row++) {
            double rs = Math.signum(result.Ut.value[row][0]);
            double s = Math.signum(Ut[row][0]);
            for (int col = 0; col < 12; col++) {
                assertEquals(s * Ut[row][col], rs * result.Ut.value[row][col], delta);
            }
        }
    }

    @Test
    public void rightSingularValues() {
        SVDRec result = new Svdlib().svdLAS2(input, dimensions, iterations, end, kappa);
        assertEquals(9, result.Vt.cols);
        assertEquals(9, result.Vt.rows);
        for (int row = 0; row < 9; row++) {
            double rs = Math.signum(result.Vt.value[row][0]);
            double s = Math.signum(Vt[row][0]);
            for (int col = 0; col < 9; col++) {
                assertEquals(s * Vt[row][col], rs * result.Vt.value[row][col], delta);
            }
        }
    }
    
    @Test
    public void recomposition() {
        SVDRec result = new Svdlib().svdLAS2(input, dimensions, iterations, end, kappa);
        for (int n = 0; n < 9; n++) {
            for (int m = 0; m < 12; m++) {
                double mult = 0;
                for (int i = 0; i < 9; i++) mult += result.Ut.value[i][m] * result.S[i] * result.Vt.value[i][n];
                assertEquals(A[m][n], mult, delta);
            }
        }
    }
    
}
