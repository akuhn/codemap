package meander.jsme2009;

import java.util.ArrayList;
import java.util.Collection;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;
import ch.akuhn.util.Bag;

@FamePackage("Hapax")
@FameDescription("Doc")
@SuppressWarnings("unchecked")
public class HapaxDoc implements Comparable<HapaxDoc> {

    @FameProperty
    public String contents;

    @FameProperty
    public String name;

    public Bag<String> terms = new Bag<String>();

    public String version;
    
    @FameProperty
    public Collection getTerms() {
        Collection coll = new ArrayList();
        int count = -1;
        for (Bag.Count<String> each: terms.sortedCounts()) {
            if (each.count != count) coll.add(count = each.count);
            coll.add(each.element);
        }
        return coll;
    }

    public void setTerms(Collection<Object> encoded) {
        terms = new Bag<String>();
        int count = -1;
        for (Object each: encoded) {
            if (each instanceof Number) count = ((Number) each).intValue();
            else terms.add((String) each, count);
        }
    }

    @Override
    public String toString() {
        return name + " (" + getVersion() + ")";
    }

    @FameProperty
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    //@Override
    public int compareTo(HapaxDoc other) {
        int diff = this.version.compareTo(other.version);
        if (diff == 0) diff = this.name.compareTo(other.name);
        return diff;
    }


}
