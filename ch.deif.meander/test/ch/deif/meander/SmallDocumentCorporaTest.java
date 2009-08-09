package ch.deif.meander;

import org.junit.Test;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.builder.MapBuilder;
import ch.deif.meander.builder.Meander;

public class SmallDocumentCorporaTest {

	@Test
	public void corpusWithoutDocuments() {
		Meander.builder()
			.addCorpus(Hapax.newCorpus().addFiles(".", ".foobar").build())
			.makeMap(new Configuration())
			.withSize(512, MapBuilder.FILE_LENGTH_SQRT);
	}
	
}
