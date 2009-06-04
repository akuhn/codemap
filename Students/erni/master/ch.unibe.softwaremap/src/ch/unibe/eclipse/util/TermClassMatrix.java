package ch.unibe.eclipse.util;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.TermDocumentMatrix;

/**
 * @author deif
 *  
 * Provides Documents that have identifiers. Used for ICompilationUnits that are
 * linked to a Location as Documents. Here the name displayed on the map and 
 * the identifier used for event processing differ.  
 *
 */
public class TermClassMatrix extends TermDocumentMatrix {
	
    public static class Doc extends TermDocumentMatrix.Doc {

		private String identifier;

		protected Doc(String name, String version, String identifier, TermDocumentMatrix owner) {
			super(name, version, owner);
			this.identifier = identifier;
		}
		
		public String identifier() {
			return identifier;
		}

    }
    
    public static Doc adaptDoc(Document candidate) {
    	if (candidate instanceof Doc) {
    		return (Doc)candidate;
    	}
    	return null;
    }

	public Document makeDocument(String name, String version, String identifier) {
		Document doc = new Doc(name, version, identifier, this);
		indexDocument(doc);
		return doc;
	}
	
	@Override
	public Document makeDocument(String name, String version) {
		throw new RuntimeException("only calls to makeDocument(name, identifier) make sense in this context!");
	}

	@Override
	public Document makeDocument(String name) {
		throw new RuntimeException("only calls to makeDocument(name, identifier) make sense in this context!");
	}

}
