package ch.deif.meander;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

import ch.deif.aNewMeander.MapColor;



public class ColorsTest {
	
	@Test
	public void compareColorValues() {
		assertEquals(Color.BLUE.getRGB(), new MapColor(0,0,255).asRGB());
		assertEquals(new Color(123, 123, 123).getRGB(), new MapColor(123, 123, 123).asRGB());
	}
	
}
