package ch.deif.meander;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.deif.meander.viz.Label;

public class LabelTest {

	@Test
	public void testSortOrder() {
		Label a = new Label("aaaa");
		Label at = new Label("aaaaTest");
		a.size = at.size = 100;
		Label b = new Label("bbbb");
		Label bt = new Label("bbbbTest");
		b.size = bt.size = 200;
		assertTrue(0 < a.compareTo(b));
		assertTrue(0 < at.compareTo(bt));
		assertTrue(0 < at.compareTo(b));
		assertTrue(0 < at.compareTo(a));
		assertTrue(0 < bt.compareTo(b));
		assertTrue(0 < bt.compareTo(a));
	}
	
}
