package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;

/** Elevation on the map, (value object); 
 * 
 * @author Adrian Kuhn
 *
 */
public interface Location {

    public double elevation();
    
    public double x();
    
    public double y();

    public void setDocument(Document document);
    
    public Document getDocument();
    
}
