package ch.akuhn.hapax.linalg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.hapax.linalg.SVD;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
public class SVDTest {

	// given in [Deerwester 1990]
	static final double[][] U = { 
		{ 0.22, -0.11 }, { 0.20, -0.07 }, { 0.24, 0.04 }, { 0.40, 0.06 },
		{ 0.64, -0.17}, { 0.27, 0.11}, { 0.27, 0.11}, { 0.30, -0.14},
		{ 0.21, 0.27}, { 0.01, 0.49}, { 0.04, 0.62}, { 0.03, 0.45 }};

	// given in [Deerwester 1990]
	static final double[] S = { 3.34, 2.54 };
	
	// given in [Deerwester 1990]
	static final double[][] V = {
		{ 0.20, -0.06 }, { 0.61, 0.17 }, { 0.46, -0.13 },
		{ 0.54, -0.23 }, { 0.28, 0.11 }, { 0.00, 0.19 },
		{ 0.02, 0.44 }, { 0.02, 0.62 }, { 0.08, 0.53 }};	
	
	@Test
	public SVD example() {
		SVD svd = new SVD(S, U, V);
		assertEquals(2, svd.getRank());
		assertEquals(12, svd.rowCount()); // U is terms is rows
		assertEquals(9, svd.columnCount()); // V is documents is columns
		return svd;
	}
	
	@Test @Given("example")
	public void testTrivialSimilarities(SVD svd) {
		assertEquals(1.0, svd.similarityUU(1, 1), 1e-9);
		assertEquals(1.0, svd.similarityUU(2, 2), 1e-9);
		assertEquals(1.0, svd.similarityUU(3, 3), 1e-9);
		assertEquals(1.0, svd.similarityVV(1, 1), 1e-9);
		assertEquals(1.0, svd.similarityVV(2, 2), 1e-9);
		assertEquals(1.0, svd.similarityVV(3, 3), 1e-9);
	}

	@Test @Given("example")
	public void testPseudoU(SVD svd) {
		// execute "human computer interface" query
		double[] pseudo = svd.makePseudoU(new double[] {
				0, 1, 1, 2, 0, 0, 0, 0, 0
		});
		assertEquals(2, pseudo.length);
		for (int i = 0; i < 12; i++) {
			System.out.println(svd.similarityU(i, pseudo));
		}
	}

	@Test @Given("example")
	public void testPseudoV(SVD svd) {
		// "human computer interface" query [Deerwester 1990]
		double[] query = svd.makePseudoV(new double[] {
				1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 
		});
		assertEquals(2, query.length);
		// ( 0.1383, 0.0275 ) given in [O'Brien 1994]
		assertEquals(0.1383, query[0], 1e-3);
		assertEquals(-0.0275, query[1], 1e-3);
		// similarities given in [O'Brien 1994]
		assertEquals(0.99, svd.similarityV(0, query), 1e-2);
		assertEquals(0.93, svd.similarityV(1, query), 1e-2);
		assertEquals(0.99, svd.similarityV(2, query), 1e-2);
		assertEquals(0.98, svd.similarityV(3, query), 1e-2);
		assertEquals(0.90, svd.similarityV(4, query), 1e-2);
		// threshold as given in [Deerwester 1990]
		assertTrue(svd.similarityV(5, query) < 0.5);
		assertTrue(svd.similarityV(6, query) < 0.5);
		assertTrue(svd.similarityV(7, query) < 0.5);
		assertTrue(svd.similarityV(8, query) < 0.5);
	}
	
	@Test
	public void testRemoveFunction() {
		double[][] d0 = new double[][] {{70},{71},{72},{73},{74}};
		double[][] d = SVD.remove(d0, 2);
		assertEquals(4, d.length);
		assertEquals(70, d[0][0], 1e-6);
		assertEquals(71, d[1][0], 1e-6);
		assertEquals(73, d[2][0], 1e-6);
		assertEquals(74, d[3][0], 1e-6);
	}
	
	@Test
	public void testRemoveFunctionFirst() {
		double[][] d0 = new double[][] {{70},{71},{72},{73},{74}};
		double[][] d = SVD.remove(d0, 0);
		assertEquals(4, d.length);
		assertEquals(71, d[0][0], 1e-6);
		assertEquals(72, d[1][0], 1e-6);
		assertEquals(73, d[2][0], 1e-6);
		assertEquals(74, d[3][0], 1e-6);
	}

	@Test
	public void testRemoveFunctionLast() {
		double[][] d0 = new double[][] {{70},{71},{72},{73},{74}};
		double[][] d = SVD.remove(d0, 4);
		assertEquals(4, d.length);
		assertEquals(70, d[0][0], 1e-6);
		assertEquals(71, d[1][0], 1e-6);
		assertEquals(72, d[2][0], 1e-6);
		assertEquals(73, d[3][0], 1e-6);
	}
	
	@Test(expected=AssertionError.class)
	public void testRemoveFunctionBelow() {
		SVD.remove(new double[][] {{},{},{}}, -1);
	}
	
	@Test(expected=AssertionError.class)
	public void testRemoveFunctionBeyond() {
		SVD.remove(new double[][] {{},{},{}}, 3);
	}
	
}
