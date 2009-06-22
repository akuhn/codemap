package ch.deif.meander;

import org.junit.Test;

import ch.deif.meander.viz.LabelsOverlay;

public class SmallDocumentCorporaTest {

	@Test
	public void corpusWithoutDocuments() {
		Meander.script()
			.addDocuments(".", ".foobar")
			.makeMap(512)
			.useHillshading()
			.add(LabelsOverlay.class);
	}
	
}
