package ch.akuhn.hapax.corpus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;
import ch.akuhn.util.Bag;

@FamePackage("Hapax")
@FameDescription("Document")
public class Document {

    @FameProperty
    private String name;
    private Terms terms;
    @FameProperty
    private int termSize;
    @FameProperty
    private VersionNumber version;

    @SuppressWarnings("unused")
    private Document() {
        // for Fame
    }
    
    public Document(String name, Collection<String> terms) {
        this(name, null, terms);
    }

    public Document(String name, VersionNumber version, Collection<String> terms) {
        this.name = name;
        this.terms = new Terms(terms);
        this.termSize = this.terms.size();
        this.version = version;
    }

    @Override
    public String toString() {
        return name;
    }

    public String name() {
        return name;
    }

    public Terms terms() {
        return terms;
    }

    public int termSize() {
        return termSize;
    }

    public VersionNumber version() {
        return version;
    }
    
    @SuppressWarnings("unused")
    @FameProperty(name="terms")
    private Collection<Object> getFameTerms() {
        if (terms == null) return Collections.emptyList();
        Collection<Object> coll = new ArrayList<Object>();
        int count = -1;
        for (Bag.Count<String> each: terms.sortedCounts()) {
            if (each.count != count) coll.add(count = each.count);
            coll.add(each.element);
        }
        return coll;
    }
    @SuppressWarnings("unused")
    private void setFameTerms(Collection<Object> values) {
        terms = new Terms();
        int count = -1;
        for (Object each: values) {
            if (each instanceof Number) count = ((Number) each).intValue();
            else terms.add((String) each, count);
        }
    }

    public void addTerms(Terms terms) {
        this.terms().addAll(terms); 
    }        
    
}
