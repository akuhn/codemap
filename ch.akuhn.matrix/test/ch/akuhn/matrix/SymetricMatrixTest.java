package ch.akuhn.matrix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SymetricMatrixTest {

    @Test
    public void shouldMultiplyMatrixWithVector() {
        Matrix m = SymmetricMatrix.fromJagged(new double[][] {{0},{1,0},{2,3,0},{4,5,6,0}});
        Vector v = Vector.from(10, 20, 30, 40);
        Vector w = m.mult(v);
        assertEquals(0*10+1*20+2*30+4*40, w.get(0), Double.MIN_VALUE);
        assertEquals(1*10+0*20+3*30+5*40, w.get(1), Double.MIN_VALUE);
        assertEquals(2*10+3*20+0*30+6*40, w.get(2), Double.MIN_VALUE);
        assertEquals(4*10+5*20+6*30+0*40, w.get(3), Double.MIN_VALUE);
    }
    
}
