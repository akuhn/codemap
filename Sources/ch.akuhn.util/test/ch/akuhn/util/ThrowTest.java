package ch.akuhn.util;


import org.junit.Test;

import ch.akuhn.util.Throw;
import static org.junit.Assert.*;

public class ThrowTest {

	@Test(expected = Exception.class )
	public void throwException() {
		// Throw checked exception without throws declaration
		Throw.exception(new Exception());
	}
	
	@Test(expected = RuntimeException.class)
	public void throwRuntimeException() {
		// Wraps checked exception into RuntimeException
		Throw.runtimeException(new Exception());
	}

	public void throwRuntimeException_1() {
		Exception cause = new Exception();
		try {
			Throw.runtimeException(cause);
			fail();
		}
		catch (RuntimeException ex) {
			assertNotSame(cause, ex);
			assertEquals(cause, ex.getCause());
		}
	}
	
	public void throwRuntimeException_2() {
		RuntimeException cause = new RuntimeException();
		try {
			Throw.runtimeException(cause);
			fail();
		}
		catch (RuntimeException ex) {
			assertEquals(cause, ex);
		}
	}
	
	public void throwRuntimeException_3() {
		Error cause = new Error();
		try {
			Throw.runtimeException(cause);
			fail();
		}
		catch (RuntimeException ex) {
			assertEquals(cause, ex);
		}
	}
	
}
