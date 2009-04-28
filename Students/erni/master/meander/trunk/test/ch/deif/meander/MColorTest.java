package ch.deif.meander;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class MColorTest {

	@Test
	public void compareColorValues() {
		assertEquals(Color.BLUE.getRGB(), MColor.BLUE().rgb());
		assertEquals(new Color(123, 123, 123).getRGB(), new MColor(123, 123, 123).rgb());
	}

}
