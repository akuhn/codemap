package ch.akuhn.hapax.corpus;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Corpora implements Iterable<Corpus>{

	private List<Corpus> corpora = new LinkedList<Corpus>();
	
	public Corpora() {
		// do nothing
	}
	
	public void add(Corpus corpus) {
		corpora.add(corpus);
	}

	public Iterator<Corpus> iterator() {
		return corpora.iterator();
	}
	
}
