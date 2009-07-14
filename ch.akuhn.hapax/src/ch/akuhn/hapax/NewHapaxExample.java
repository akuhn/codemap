package ch.akuhn.hapax;

import ch.akuhn.hapax.index.TermDocumentMatrix;

public class NewHapaxExample {

	public static void main(String[] args) {
		
		TermDocumentMatrix tdm;
		
		tdm = Hapax.newCorpus()
				.useCamelCaseScanner()
				.rejectRareTerms()
				.rejectCommonTerms()
				.useTFIDF()
				.addFiles(".", ".java")
				.makeTDM();
		
		System.out.println(tdm);
		
		tdm = Hapax.newCorpus()
				.useLetterScanner()
				.ignoreCase()
				.rejectStopwords()
				.rejectRareTerms()
				.dontUseWeighting()
				.addDocument("c1", "Human machine interface for Lab ABC computer applications")
				.addDocument("c2", "A survey of user opinion of computer system response time")
				.addDocument("c3", "The EPS user interface management system")
				.addDocument("c4", "System and human system engineering testing of EPS")
				.addDocument("c5", "Relation of user-perceived response time to error measurement")
				.addDocument("m1", "The generation of random, binary, unordered trees")
				.addDocument("m2", "The intersection graph of paths in trees")
				.addDocument("m3", "Graph minors IV: Widths of trees and well-quasi-ordering")
				.addDocument("m4", "Graph minors: A survey")
				.makeTDM();
				
		System.out.println(tdm);
		
	}
	
}
