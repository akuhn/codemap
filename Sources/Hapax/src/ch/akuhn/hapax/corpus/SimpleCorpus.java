package ch.akuhn.hapax.corpus;

import java.util.ArrayList;
import java.util.Collection;


public class SimpleCorpus extends Corpus {

    private Collection<Document> documents;
    
    public SimpleCorpus() {
        this.documents = new ArrayList<Document>();
    }
    
    private static class Doc extends Document {

        public Doc(String name, String version) {
            super(name, version);
        }

        private Terms terms = new Terms();
        
        @Override
        public int termSize() {
            return terms.uniqueSize();
        }

        @Override
        public Terms terms() {
            return terms;
        }

        @Override
        public Document addTerms(Terms terms) {
            terms.addAll(terms);
            return this;
        }

        @Override
        public Corpus owner() {
            return null;
        }
        
    }

    @Override
    public boolean contains(Document doc) {
        return documents.contains(doc);
    }

    @Override
    public int documentSize() {
        return documents.size();
    }

    @Override
    public Iterable<Document> documents() {
        return documents;
    }

    @Override
    public Document makeDocument(String name, String version) {
        Document doc = new Doc(name, version);
        documents.add(doc);
        return doc;
    }

    @Override
    public Terms terms(Document doc) {
        return doc.terms();
    }


}
