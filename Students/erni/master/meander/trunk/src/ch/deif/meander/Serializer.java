package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;
import ch.akuhn.fame.Repository;
import ch.akuhn.fame.Tower;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.util.Bag;

@FamePackage("Meander")
public class Serializer {

    @FameDescription("Document")
    public static class MSEDocument {
        @FameProperty
        public double x;
        @FameProperty
        public double y;
        @FameProperty
        public double height;
        @FameProperty
        public String name;
        
        public Bag<String> terms;
        @FameProperty
        public Collection<?> getTerms() {
            if (terms == null)
                return Collections.EMPTY_LIST;
            Collection<Object> coll = new ArrayList<Object>();
            int count = -1;
            for (Bag.Count<String> each : terms.sortedCounts()) {
                if (each.count != count)
                    coll.add(count = each.count);
                coll.add(each.element);
            }
            return coll;
        }

        public void setTerms(Collection<Object> encoded) {
            terms = new Bag<String>();
            int count = -1;
            for (Object each : encoded) {
                if (each instanceof Number)
                    count = ((Number) each).intValue();
                else
                    terms.add((String) each, count);
            }
        }

        public void setTerms(Terms terms) {
            this.terms = new Bag<String>();
            for (String each : terms) {
                this.terms.add(each);
            }
        }
    }

    @FameDescription("Release")
    public static class MSERelease {
        @FameProperty
        public String name;
        @FameProperty
        public Collection<MSEDocument> documents;
    }

    @FameDescription("Project")
    public static class MSEProject {
        @FameProperty
        public String name;
        @FameProperty
        public Collection<MSERelease> releases;
    }

    private Tower t = new Tower();
    private MSEProject project;
    private MSERelease release;

    public Serializer() {
        t.metamodel.withAll(MSEDocument.class,
                MSEProject.class, MSERelease.class);
    }

    public Serializer project(String name) {
        project = new MSEProject();
        project.name = name;
        project.releases = new ArrayList<MSERelease>();
        model().add(project);
        return this;
    }

    public Serializer release(String name) {
        assert project != null;
        release = new MSERelease();
        release.name = name;
        release.documents = new ArrayList<MSEDocument>();
        project.releases.add(release);
        model().add(release);
        return this;
    }

    public Serializer location(double x, double y, double height, Document doc) {
        assert release != null;
        MSEDocument document = new MSEDocument();
        document.name = doc.name();
        document.setTerms(doc.terms());
        document.x = x;
        document.y = y;
        document.height = height;
        release.documents.add(document);
        model().add(document);
        return this;
    }

    public Repository model() {
        return t.model;
    }

}
