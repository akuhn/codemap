package ch.akuhn.hapax.corpus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;
import ch.akuhn.util.Bag;
import ch.akuhn.util.CacheMap;

@FamePackage("Hapax")
@FameDescription("Corpus")
public class TermBagCorpus implements Corpus {

    @FameProperty
    private Collection<Document> documents;
    @FameProperty
    private Collection<VersionNumber> versions;
    private Map<String, VersionNumber> versionMap;

    public TermBagCorpus() {
        this.documents = new ArrayList<Document>();
        this.versions = new ArrayList<VersionNumber>();
    }

    private VersionNumber getVersion(String name) {
        if (versionMap == null)
            versionMap = new VersionMap();
        return versionMap.get(name);
    }

    @SuppressWarnings("serial")
    private class VersionMap extends CacheMap<String, VersionNumber> {
        public VersionMap() {
            for (VersionNumber each : versions)
                this.put(each.name(), each);
        }

        @Override
        public VersionNumber initialize(String name) {
            VersionNumber version = new VersionNumber(name);
            versions.add(version);
            return version;
        }

    }

    /* (non-Javadoc)
     * @see ch.akuhn.hapax.corpus.Corpus#documents()
     */
    public Iterable<Document> documents() {
        return documents;
    }

    public int documentSize() {
        return documents.size();
    }

    public void add(Document document) {
        documents.add(document);
    }

    public void addAll(TermBagCorpus other) {
        for (Document each : other.documents)
            this.add(each);
    }

 
    public void addDocument(String name, String version,
            Bag<String> terms) {
        this.add(new Document(name, getVersion(version), terms));
    }

    public Terms terms() {
        Terms terms = new Terms();
        for (Document each : documents)
            terms.addAll(each.terms());
        return terms;
    }

    public int termSize() {
        return terms().uniqueSize();
    }

    @Override
    public String toString() {
        return String.format("%s (%d documents, %d terms)",
                this.getClass().getName(),
                documentSize(),
                termSize());
    }


}
