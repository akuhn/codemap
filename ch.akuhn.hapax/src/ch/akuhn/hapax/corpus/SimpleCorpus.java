package ch.akuhn.hapax.corpus;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


public class SimpleCorpus extends Corpus {

    private Map<String,Terms> documents;
    
    public SimpleCorpus() {
        this.documents = new TreeMap<String,Terms>();
    }
    
    @Override
    public boolean containsDocument(String doc) {
        return documents.containsKey(doc);
    }

    @Override
    public int documentCount() {
        return documents.size();
    }

    @Override
    public Iterable<String> documents() {
        return Collections.unmodifiableCollection(documents.keySet());
    }

    @Override
    public Terms getDocument(String doc) {
        return documents.get(doc);
    }

	@Override
	public void putDocument(String name, Terms contents) {
		documents.put(name, contents);
	}


}
