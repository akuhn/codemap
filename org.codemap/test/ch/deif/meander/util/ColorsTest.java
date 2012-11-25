package ch.deif.meander.util;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.codemap.util.MColor;
import org.junit.Test;




public class ColorsTest {
	
	@Test
	public void compareColorValues() {
		assertEquals(Color.BLUE.getRGB(), new MColor(0,0,255).asRGB());
		assertEquals(new Color(123, 123, 123).getRGB(), new MColor(123, 123, 123).asRGB());
	}
	
}
