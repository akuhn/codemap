package ch.akuhn.hapax;


public class NewHapaxExample {

	public static void main(String[] args) {
		
		Hapax hapax = Hapax.newCorpus()
				.useLetterScanner()
				.ignoreCase()
				.rejectStopwords()
				.rejectRareTerms()
				.useTFIDF()
				.addDocument("c1", "Human machine interface for Lab ABC computer applications")
				.addDocument("c2", "A survey of user opinion of computer system response time")
				.addDocument("c3", "The EPS user interface management system")
				.addDocument("c4", "System and human system engineering testing of EPS")
				.addDocument("c5", "Relation of user-perceived response time to error measurement")
				.addDocument("m1", "The generation of random, binary, unordered trees")
				.addDocument("m2", "The intersection graph of paths in trees")
				.addDocument("m3", "Graph minors IV: Widths of trees and well-quasi-ordering")
				.addDocument("m4", "Graph minors: A survey")
				.latentDimensions(2)
				.build();
				
		System.out.println(hapax);
		System.out.println(hapax.find("human computer interaction"));

		hapax.updateDocument("m5", "The EPS user interface management system");
		hapax.updateDocument("m2", "Human machine interface for Lab ABC computer applications");
		hapax.removeDocument("c2");
		
		System.out.println(hapax);
		System.out.println(hapax.find("human computer interaction"));

	}

}
