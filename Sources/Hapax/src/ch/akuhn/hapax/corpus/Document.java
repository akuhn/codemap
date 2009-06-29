package ch.akuhn.hapax.corpus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Adrian Kuhn
 * 
 */
public abstract class Document implements Comparable<Document> {

	public static final String UNVERSIONED = "-";

	private String name;
	private String version;
	private String identifier;

	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Document(String name, String version) {
		// ASSUME Use intern, since thousands of documents share the same
		// version.
		this.version = version == null ? UNVERSIONED : version.intern();
		this.name = name;
	}

	public abstract Document addTerms(Terms terms);

	@Override
	public int compareTo(Document other) {
		int signum = this.version.compareTo(other.version);
		return signum != 0 ? signum : this.name.compareTo(other.name);
	}

	public String fullName() {
		return name;
	}

	public String name() {
		return name;
	}

	public abstract Corpus owner();

	public abstract Terms terms();

	public int termSize() {
		return terms().uniqueSize();
	}

	@Override
	public String toString() {
		Matcher matcher = Pattern.compile("([^\\\\\\/]+)$").matcher(name);
		if (matcher.find())
			return matcher.group(1);
		return name;
	}

	public String version() {
		return version;
	}

}