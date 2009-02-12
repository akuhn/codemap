package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;
import ch.akuhn.fame.Repository;
import ch.akuhn.fame.Tower;
import ch.akuhn.util.Bag;

@FamePackage("Meander")
public class Serializer {

    @FameDescription("Location")
    public static class MSELocation {
        @FameProperty public double x;
        @FameProperty public double y;
        @FameProperty public double height;
    }
    
    @FameDescription("Document")
    public static class MSEDocument {
        @FameProperty public String name;
        private Bag<String> terms;
        @FameProperty
        public Collection<?> getTerms() {
            Collection<Object> coll = new ArrayList<Object>();
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

    }
    
    @FameDescription("Release")
    public static class MSERelease {
        @FameProperty public String name;
        @FameProperty public Collection<MSEDocument> documents;
        @FameProperty public Collection<MSELocation> locations;
    }
    
    @FameDescription("Project")
    public static class MSEProject {
        @FameProperty public String name;
        @FameProperty public Collection<MSERelease> releases;
    }

    private Tower t = new Tower();
    private MSEProject project;
    private MSERelease release;
    
    public Serializer() {
        t.metamodel.withAll(MSELocation.class, MSEDocument.class, MSEProject.class, MSERelease.class);
    }
    
    public Serializer project(String name) {
        project = new MSEProject();
        project.name = name;
        project.releases = new ArrayList<MSERelease>();
        t.model.add(project);
        return this;
    }
    
    public Serializer release(String name) {
        assert project != null;
        release = new MSERelease();
        release.name = name;
        release.documents = new ArrayList<MSEDocument>();
        release.locations = new ArrayList<MSELocation>();
        project.releases.add(release);
        t.model.add(release);
        return this;
    }
    
    public Serializer location(double x, double y, double height, Object doc) {
        assert release != null;
        MSELocation location = new MSELocation();
        MSEDocument document = new MSEDocument();
        location.x = x;
        location.y = y;
        location.height = height;
        document.name = doc.toString();
        release.documents.add(document);
        release.locations.add(location);
        t.model.add(location, document);
        return this;
    }
    
    public Serializer document(String name, Bag<String> terms) {
        assert release != null;
        MSEDocument document = new MSEDocument();
        document.name = name;
        document.terms = terms;
        release.documents.add(document);
        t.model.add(document);
        return this;
    }
    
    public Repository model() {
        return t.model;
    }
    
}
