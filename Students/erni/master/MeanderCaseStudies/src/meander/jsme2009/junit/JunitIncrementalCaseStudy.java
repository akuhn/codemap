package meander.jsme2009.junit;

import java.util.List;

import ch.akuhn.fame.Tower;
import ch.akuhn.fame.parser.InputSource;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.As;
import ch.akuhn.util.Get;
import ch.akuhn.util.query.Collect2;
import ch.akuhn.util.query.Detect;
import ch.deif.meander.HausdorffDistance;
import ch.deif.meander.Location;
import ch.deif.meander.MDS;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;
import ch.unibe.jsme2009.HapaxDoc;

public class JunitIncrementalCaseStudy implements Runnable {

    public final String FILENAME = "mse/junit_case.h.mse";
    private Corpus corpus;

    public Tower tower() {
        Tower t = new Tower();
        t.metamodel.with(HapaxDoc.class);
        t.model.importMSE(InputSource.fromFilename(FILENAME));
        return t;
    }

    public Corpus corpus(String versionName) {
        Corpus c = new Corpus();
        for (HapaxDoc each: Get.sorted(tower().model.all(HapaxDoc.class))) {
            // System.out.println(each.version.string);
            if (each.version.string.equals(versionName)) {
                c.add(new Document(each, each.terms));
            }
        }
        return c;
    }

    public static void main(String[] args) {
        new JunitIncrementalCaseStudy().run();
    }
    
    public void run() {
        Serializer ser = new Serializer();
        ser.model().importMSEFile("mse/junit_meander.mse");
        MSEProject proj = ser.model().all(MSEProject.class).iterator().next();
        MSERelease previous = null;
        Map previousMap = null;

        for (MSERelease rel: proj.releases) {
            if (previous != null) {
                Map newMap = nextMap(rel.name, previousMap);
                System.out.println(rel.name);
                System.out.println(new HausdorffDistance().distance(newMap, previousMap));
                previousMap = newMap;
            } else {
                previousMap = firstMap(rel.name);
            }
            previous = rel;
        }
    }

    private Map firstMap(String versionName) {
        corpus = corpus(versionName);
        List<Document> tempDocuments = As.list(corpus.documents());
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus);
        tdm = tdm.rejectAndWeight();
        LatentSemanticIndex lsi = tdm.createIndex();
        MDS mds = MDS.fromCorrelationMatrix(lsi.documentCorrelation());
        MapBuilder builder = new MapBuilder();
        for (Document each: tempDocuments) {
            int index = lsi.documents.get(each);
            builder.location(mds.x[index], mds.y[index], Math.sqrt(each.terms.size()), each);
        }
        return builder.build();
    }

    private Map nextMap(String versionName, Map previous) {
        Corpus prevCorpus = corpus;
        corpus = corpus(versionName);
        List<Document> tempDocuments = As.list(corpus.documents());
        corpus.addAll(prevCorpus);
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus);
        tdm = tdm.rejectAndWeight();
        LatentSemanticIndex lsi = tdm.createIndex();
        MDS mds = MDS.fromCorrelationMatrix(lsi.documentCorrelation(), matchingLocations(lsi, previous));
        
        System.out.println("dim(lsi) = " + lsi.documents.size());
        
        MapBuilder builder = new MapBuilder();
        for (Document each: tempDocuments) {
            int index = lsi.documents.get(each);
            builder.location(mds.x[index], mds.y[index], Math.sqrt(each.terms.size()), each);
        }
        Map map =  builder.build();
        System.out.println("dim(map) = " + map.locationSize());
        return map;
    }

    private Iterable<Location> matchingLocations(LatentSemanticIndex lsi, Map previous) {
        int tally = 0;
        Collect2<Document,Location> collect = Collect2.from(lsi.documents, Location.class);
        for (Collect2<Document,Location> each: collect) {
            Detect<Location> match = Detect.from(previous.locations);
            for (Detect<Location> other: match) {
                other.yield = norm(((HapaxDoc) other.element.document.handle).name).equals(
                        norm(((HapaxDoc) each.element.handle).name));
            }
            if (match.resultIfNone(null) == null) tally++; 
            collect.yield = match.resultIfNone(makeRandomLocation());
        }
        System.out.println(lsi.documents.size() + " " + tally);
        return collect.result();
    }

    private Location makeRandomLocation() {
        return new Location(Math.random() * 6 - 3, Math.random() * 6 - 3, 0.0);
        // return new Location(1.0, 1.0, 0.0);
    }

    private Object norm(String name) {
        //System.out.println(name);
        name = name.trim();
        if (name.startsWith("junit2/")) return name.substring("junit2/".length());
        if (name.startsWith("junit2.1/")) return name.substring("junit2.1/".length());
        if (name.startsWith("junit3/")) return name.substring("junit3/".length());
        return name;
    }

}
