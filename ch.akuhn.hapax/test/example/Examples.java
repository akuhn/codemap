package example;

import org.junit.Test;

import ch.akuhn.hapax.Hapax;

public class Examples {

	private Hapax hapax;
	
	@Test
	public void hapaxExample() {
		hapax = Hapax.legomenon()
				.makeCorpus(".", "java");
		System.out.println(hapax
				.createIndex()
				.find("split string by lower- and upper-case"));
	}

	
	
}
