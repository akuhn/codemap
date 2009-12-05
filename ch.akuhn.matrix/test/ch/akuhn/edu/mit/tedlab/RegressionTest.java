package ch.akuhn.edu.mit.tedlab;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

public class RegressionTest {

    private static final double KAPPA = 1e-6;
	private static final double[] END = new double[] { 1e-30, 1e30 } ;

	@Test
    public void dat1() throws FileNotFoundException {
    	test("lib/dat1.txt");
    }
    
    @Test
    public void dat2() throws FileNotFoundException {
    	test("lib/dat2.txt");
    }
    
    @Test
    public void dat3() throws FileNotFoundException {
    	test("lib/dat3.txt");
    }
    
    private void test(String name) throws FileNotFoundException {
    	Revision39.SMat s39 = new Revision39().svdLoadSparseTextHBFile(new File(name));
		Revision39.SVDRec r39 = new Revision39().svdLAS2(s39, 20, 0, END, KAPPA);
		SMat s = Svdlib.svdLoadSparseTextHBFile(new File(name));
		SVDRec r = new Svdlib().svdLAS2(s, 20, 0, END, KAPPA);
		assertEquals(20, r39.S.length);
		assertEquals(20, r.S.length);
		for (int n = 0; n < 20; n++) {
			assertEquals(r39.S[n], r.S[n], KAPPA);
		}
    }
    
}
