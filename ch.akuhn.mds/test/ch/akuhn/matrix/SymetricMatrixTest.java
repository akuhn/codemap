package ch.akuhn.matrix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SymetricMatrixTest {

    @Test
    public void shouldMultiplyMatrixWithVector() {
        SymetricMatrix m = new SymetricMatrix(new double[][] {{},{1},{2,3},{4,5,6}});
        double[] v = new double[] { 10, 20, 30, 40 };
        double[] w = m.mult(v);
        assertEquals(w[0], 0*10+1*20+2*30+4*40,Double.MIN_VALUE);
        assertEquals(w[1], 1*10+0*20+3*30+5*40,Double.MIN_VALUE);
        assertEquals(w[2], 2*10+3*20+0*30+6*40,Double.MIN_VALUE);
        assertEquals(w[3], 4*10+5*20+6*30+0*40,Double.MIN_VALUE);
    }
    
}
