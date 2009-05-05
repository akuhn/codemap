package ch.deif.meander;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;


public class ColorsTest {
	
	@Test
	public void compareColorValues() {
		assertEquals(Color.BLUE.getRGB(), new Colors(0,0,255).asRGB());
		assertEquals(new Color(123, 123, 123).getRGB(), new Colors(123, 123, 123).asRGB());
	}
	
}
