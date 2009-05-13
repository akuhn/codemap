package ch.akuhn.hapax.corpus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Adrian Kuhn
 *
 */
public abstract class Document implements Comparable<Document> {

	@Override
    public int compareTo(Document other) {
        int signum = this.version.compareTo(other.version); 
        return signum != 0 ? signum : this.name.compareTo(other.name); 
    }

    public static final String UNVERSIONED = "-";
    
    private String name;
    private String version;

    public Document(String name, String version) {
        // ASSUME Use intern, since thousands of documents share the same version.
        this.version = version == null ? UNVERSIONED : version.intern(); 
        this.name = name;
    }
    
    public abstract Terms terms();
    
    public abstract Document addTerms(Terms terms);

    public abstract Corpus owner();
    
    public int termSize() {
        return terms().uniqueSize();
    }

    @Override
    public String toString() {
    	Matcher matcher = Pattern.compile("([^\\\\\\/]+)$").matcher(name);
    	if (matcher.find()) return matcher.group(1);
        return name;
    }

    public String name() {
        return name;
    }

    public String version() {
        return version;
    }

	public String fullName() {
		return name;
	}
    
}