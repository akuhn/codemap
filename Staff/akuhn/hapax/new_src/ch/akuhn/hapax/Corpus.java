package ch.akuhn.hapax;

import java.util.HashMap;
import java.util.Map;

public class Corpus<D> {

	private Map<D,Terms> documents;
	
	public Corpus() {
		this.documents = new HashMap<D,Terms>();
	}

	public Terms get(D document) {
		return documents.get(document);
	}

	public void put(D document) {
		this.put(document, new Terms());
	}
	
	public void put(D document, String content) {
		this.put(document, new Terms(content));
	}
	
	public void put(D document, Terms terms) {
		Terms value = documents.get(document);
		if (value == null) {
			value = new Terms();
			documents.put(document,value);
		}
		value.addAll(terms);
	}
	
}
