package ch.deif.meander;

import org.junit.Test;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.builder.Meander;

public class SmallDocumentCorporaTest {

	@Test
	public void corpusWithoutDocuments() {
		Meander.builder()
			.addCorpus(Hapax.legomenon().makeCorpus(".", ".foobar"))
			.makeMap()
			.withSize(512);
	}
	
}
