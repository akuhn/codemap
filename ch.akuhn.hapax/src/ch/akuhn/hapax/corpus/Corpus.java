package ch.akuhn.hapax.corpus;

/** Text corpus, with term frequencies for each document.
 * Both terms and documents are identified by strings.
 * 
 * @author Adrian Kuhn
 *
 */
public abstract class Corpus {

    public abstract void putDocument(String name, Terms content);
    
    public Terms terms() {
        Terms terms = new Terms();
        for (String doc: documents()) terms.addAll(getDocument(doc));
        return terms;
    }
   
    public abstract Iterable<String> documents();

    public abstract int documentCount();

    public abstract boolean containsDocument(String doc);
    
    public boolean containsTerm(String term) {
    	for (String doc: documents()) if (getDocument(doc).contains(term)) return true;
    	return false;
    }
    
    public abstract Terms getDocument(String doc);
    
    public int termCount() {
        return terms().uniqueSize();
    }

    @Override
    public String toString() {
        return String.format("%s (%d documents, %d terms)",
                this.getClass().getName(),
                documentCount(),
                termCount());
    }
    
}