package ch.akuhn.org.ggobi.plugins.ggvis;

import java.io.IOException;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.mds.MultidimensionalScaling;

public class Main {

	private transient static boolean VIZ = true;

	public static void main(String[] args) throws IOException {
		
		Hapax hapax = Hapax.newCorpus()
			.useTFIDF()
			.useCamelCaseScanner()
			.addFiles("..", ".java")
			.build();
		
		System.out.println("done");

		Viz viz = VIZ ? new Viz().open() : null;
		new MultidimensionalScaling()
				.similarities(hapax.getIndex().documentCorrelation().asArray())
				.verbose()
				.listener(viz)
				.maxIterations(10+0*Integer.MAX_VALUE)
				.run();
				
	
	}

	
}
