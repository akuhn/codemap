package ch.deif.meander;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;


public class MColorTest {
	
	@Test
	public void compareColorValues() {
		assertEquals(Color.BLUE.getRGB(), MColor.BLUE().rgb());
	}

}
