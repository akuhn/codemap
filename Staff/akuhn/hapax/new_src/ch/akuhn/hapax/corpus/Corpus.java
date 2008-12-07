package ch.akuhn.hapax.corpus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ch.akuhn.util.Files;

public class Corpus {

	private Map<Document,Terms> documents;
	private Terms terms;
	
	public Corpus() {
		this.documents = new HashMap<Document,Terms>();
		this.terms = new Terms();
	}

	public Terms get(Document document) {
		return documents.get(document);
	}

	public void put(Document document) {
		this.put(document, new Terms());
	}
	
	public void put(Document document, String content) {
		this.put(document, new Terms(content));
	}
	
	public void put(Document document, Terms terms) {
		Terms value = documents.get(document);
		if (value == null) {
			value = new Terms();
			documents.put(document,value);
		}
		this.terms.addAll(terms);
		value.addAll(terms);
	}
	
	@Override
    public String toString() {
        return String.format("Corpus (%d documents, %d terms)", documents.size(), termSize());
    }

    public Iterable<Document> documents() {
	    return documents.keySet();
	}
	
	public Terms terms() {
	    return terms;
	}
	
	public int termSize() {
	    return terms.uniqueSize();
	}
	
	public int documentSize() {
	    return documents.size();
	}
	
	public void scanFolder(File folder, String... extensions) {
	    for (File each: Files.find(folder, extensions)) scanFile(each);
	}

    private void scanFile(File each) {
        System.out.println(each);
        put(new Document(each), new Terms(each)); 
    }

	
	
}
