package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;
import ch.akuhn.fame.Repository;
import ch.akuhn.fame.Tower;

@FamePackage("Meander")
public class Serializer {

    @FameDescription("Location")
    private static class MSELocation {
        @FameProperty double x;
        @FameProperty double y;
        @FameProperty double height;
    }
    
    @FameDescription("Document")
    private static class MSEDocument {
        @FameProperty String name;
    }
    
    @FameDescription("Release")
    private static class MSERelease {
        @FameProperty String name;
        @FameProperty Collection<MSEDocument> documents;
        @FameProperty Collection<MSELocation> locations;
    }
    
    @FameDescription("Project")
    private static class MSEProject {
        @FameProperty String name;
        @FameProperty Collection<MSERelease> releases;
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
    
    public Repository model() {
        return t.model;
    }
    
}
