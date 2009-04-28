package ch.deif.meander;

import org.junit.Test;


public class AssertionsEnabledTest {
	
	@Test(expected = AssertionError.class)
	public void assertion() {
		assert false;
	}
	
}
