package hapax.test;

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.akuhn.hapax.linalg.Matrix;
import ch.akuhn.hapax.linalg.SymetricMatrix;

public class SymetricMatrixTest {

    @Test
    public void testPutGetIsSymetric() {
        Matrix m = new SymetricMatrix(5);
        m.put(4, 2, PI);
        assertEquals(PI, m.get(4, 2), Double.MIN_VALUE);
        assertEquals(PI, m.get(2, 4), Double.MIN_VALUE);
        m.put(3, 3, PI);
        assertEquals(PI, m.get(3, 3), Double.MIN_VALUE);
    }

}
