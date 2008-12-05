package ch.akuhn.hapax;

import static ch.akuhn.util.query.Query.$result;
import static ch.akuhn.util.query.Query.sum;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ch.akuhn.util.Files;
import ch.akuhn.util.query.Inject;
import ch.akuhn.util.query.Query;
import ch.akuhn.util.query.Sum;

public class Corpus {

	private Map<Document,Terms> documents;
	
	public Corpus() {
		this.documents = new HashMap<Document,Terms>();
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
		value.addAll(terms);
	}
	
	@Override
    public String toString() {
        return String.format("Corpus (%d documents, %d terms)", documents.size(), termSize());
    }

    public Iterable<Document> documents() {
	    return documents.keySet();
	}
	
	public Terms allTerms() {
	    Terms all = new Terms();
	    for (Terms each: documents.values()) all.addAll(each);
	    return all;
	}
	
	public int termSize() {
	    for (Sum<Terms> each: sum(documents.values())) {
	        each.sum += each.element.elementSize();
	    }
	    return $result();
	}
	
	public void scanFolder(File folder, String... extensions) {
	    for (File each: Files.find(folder, extensions)) scanFile(each);
	}

    private void scanFile(File each) {
        put(new Document(each), new Terms(each)); 
    }

	
	
}
