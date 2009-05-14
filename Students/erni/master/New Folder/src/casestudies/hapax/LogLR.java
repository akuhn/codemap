package casestudies.hapax;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.index.LogLikelihood;
import ch.akuhn.hapax.index.LogLikelihood.Comparison;

public class LogLR {
	
	public static void main(String... args) {
		
		Hapax h = Hapax.legomenon()
		.ignoreCase()
		.useCamelCaseScanner()
		.dontRejectRareWords()
		.dontRejectStopwords()
		.setBaseFolder("../MeanderCaseStudies/data/junit/");
	for (String each: JunitMSR.VERSIONS) {
		h.makeCorpus(each, "java");
	}
	Corpus prev = null;
	for (Corpus each: h.closeCorpus().corpora()) {
		System.out.println("to " + each.documents().iterator().next().version());
		if (prev != null) logLR(prev, each);
		prev = each;
	}
		
	}

	private static void logLR(Corpus prev, Corpus each) {
		Comparison logLR = LogLikelihood.compare(each.terms(), prev.terms());
		System.out.println(logLR.withThreshold(20));
		System.out.println(logLR.withThreshold(-20));
	}

}
