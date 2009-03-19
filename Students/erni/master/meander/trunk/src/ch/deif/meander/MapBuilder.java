package ch.deif.meander;

import java.util.ArrayList;
import java.util.List;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.util.Bag;
import ch.deif.meander.Serializer.MSEDocument;

public class MapBuilder {

    private static class Doc extends Document {

        private Terms terms;

        public Doc(String name, String version) {
            super(name, version);
            terms = new Terms();
        }

        @Override
        public Document addTerms(Terms terms) {
            this.terms.addAll(terms);
            return this;
        }

        @Override
        public Corpus owner() {
            return null;
        }

        @Override
        public Terms terms() {
            return this.terms;
        }

        public Document addTerms(Bag<String> terms) {
            this.terms.addAll(terms);
            return this;
        }

    }

    private Parameters params;
    private List<Location> locations;
    private String name;

    public MapBuilder() {
        params = new Parameters();
        locations = new ArrayList<Location>();
    }

    public MapBuilder size(int width, int height) {
        params.width = width;
        params.height = height;
        return this;
    }

    public MapBuilder location(double xNormed, double yNormed, double h) {
        locations.add(new Location(xNormed, yNormed, h));
        return this;
    }

    public MapBuilder location(double xNormed, double yNormed, double h,
            Document document) {
        Location loc = new Location(xNormed, yNormed, h);
        loc.document = document;
        locations.add(loc);
        return this;
    }

    public MapBuilder location(MSEDocument each, String version) {
        Doc document = new Doc(each.name, version);
        document.addTerms(each.terms);
        Location loc = new Location(each.x, each.y, each.height);
        loc.document = document;
        locations.add(loc);
        return this;
    }

    public Map build() {
        Map map = new Map(params, locations);
        map.name = name;
        return map;
    }

    public MapBuilder name(String versionName) {
        name = versionName;
        return this;
    }

}
