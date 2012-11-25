package ch.deif.meander.util;

import static org.junit.Assert.assertEquals;

import org.codemap.util.RunLengthEncodedList;
import org.junit.Test;

public class RunLengthEncodedListTest {

	private static final String A = "A";
	private static final String B = "B";
	private static final String C = "C";
	
	@Test
	public void testAAABB() {
		RunLengthEncodedList<String> rle = new RunLengthEncodedList<String>(A, A, A, B, B);
		assertEquals(5, rle.size());
		assertEquals(A, rle.get(0));
		assertEquals(A, rle.get(1));
		assertEquals(A, rle.get(2));
		assertEquals(B, rle.get(3));
		assertEquals(B, rle.get(4));
	}

	@Test
	public void testEmpty() {
		RunLengthEncodedList<String> rle = new RunLengthEncodedList<String>();
		assertEquals(0, rle.size());
	}

	@Test
	public void testA() {
		RunLengthEncodedList<String> rle = new RunLengthEncodedList<String>(A);
		assertEquals(1, rle.size());
		assertEquals(A, rle.get(0));
	}
	
	
	@Test
	public void testNullAAANullNull() {
		RunLengthEncodedList<String> rle = new RunLengthEncodedList<String>(null, A, A, null, null, B);
		assertEquals(6, rle.size());
		assertEquals(null, rle.get(0));
		assertEquals(A, rle.get(1));
		assertEquals(A, rle.get(2));
		assertEquals(null, rle.get(3));
		assertEquals(null, rle.get(4));
		assertEquals(B, rle.get(5));
	}

	@Test
	public void testABC() {
		RunLengthEncodedList<String> rle = new RunLengthEncodedList<String>(A, B, C);
		assertEquals(3, rle.size());
		assertEquals(A, rle.get(0));
		assertEquals(B, rle.get(1));
		assertEquals(C, rle.get(2));
	}
	
	@Test
	public void testAAABBBCCC() {
		RunLengthEncodedList<String> rle = new RunLengthEncodedList<String>(A, A, A, B, B, B, C, C, C);
		assertEquals(9, rle.size());
		assertEquals(A, rle.get(0));
		assertEquals(A, rle.get(1));
		assertEquals(A, rle.get(2));
		assertEquals(B, rle.get(3));
		assertEquals(B, rle.get(4));
		assertEquals(B, rle.get(5));
		assertEquals(C, rle.get(6));
		assertEquals(C, rle.get(7));
		assertEquals(C, rle.get(8));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testIndexBelowBounds() {
		RunLengthEncodedList<String> rle = new RunLengthEncodedList<String>(A, A, A, B, B);
		assertEquals(5, rle.size());
		rle.get(-1);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testIndexAboveBounds() {
		RunLengthEncodedList<String> rle = new RunLengthEncodedList<String>(A, A, A, B, B);
		assertEquals(5, rle.size());
		rle.get(5);
	}
	
}
