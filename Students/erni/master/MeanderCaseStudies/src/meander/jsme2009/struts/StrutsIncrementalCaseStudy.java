package meander.jsme2009.struts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import meander.jsme2009.junit.ComputeHausdorff;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Throw;
import ch.akuhn.util.query.Collect2;
import ch.akuhn.util.query.Detect;
import ch.deif.meander.Location;
import ch.deif.meander.MDS;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;

public class StrutsIncrementalCaseStudy implements Runnable {

    public static final String TDMFILE = 
        "mse/struts_10.TDM";

    public final static String[] VERSIONS = ComputeLSIAndMDS.VERSIONS.toArray(new String[0]);
    
    public static TermDocumentMatrix corpus_full()  {
        try {
            TermDocumentMatrix TDM = TermDocumentMatrix.readFrom(new Scanner(new File(TDMFILE)));
            return TDM;
        } catch (FileNotFoundException ex) {
            throw Throw.exception(ex);
        }
    }

    public static void main(String[] args) {
        new StrutsIncrementalCaseStudy().run();
    }
    
    public Collection<Map> createMap() {
        Collection<Map> maps = new ArrayList<Map>();
        String previous = null;
        Map previousMap = null;

        for (String rel: VERSIONS) {
            if (previous != null) {
                previousMap = nextMap(rel, previousMap);
            } else {
                previousMap = firstMap(rel);
            }
            System.out.println(previousMap.locationSize());
            maps.add(previousMap);
            previous = rel;
        }
        return maps;
    }

    public void run() {
        new ComputeHausdorff(createMap()).run();
    }
    
    private Map firstMap(String versionName) {
        TermDocumentMatrix tdm = corpus_full().copyUpto(versionName, VERSIONS);
        System.out.println(tdm);        
        tdm = tdm.rejectAndWeight();
        LatentSemanticIndex lsi = tdm.createIndex();
        //lsi = lsi.select(versionName);
        MDS mds = MDS.fromCorrelationMatrix(lsi);
        MapBuilder builder = new MapBuilder();
        for (Document each: lsi.documents) {
            int index = lsi.documents.get(each);
            builder.location(mds.x[index], mds.y[index], Math.sqrt(each.termSize()), each);
        }
        builder.name(versionName);
        return builder.build();
    }

    private Map nextMap(String versionName, Map previous) {
        TermDocumentMatrix tdm = corpus_full().copyUpto(versionName, VERSIONS);
        System.out.println(versionName + " " + tdm);
        tdm = tdm.rejectAndWeight();
        LatentSemanticIndex lsi = tdm.createIndex();
        lsi = lsi.select(versionName);
        MDS mds = MDS.fromCorrelationMatrix(lsi, matchingLocations(lsi, previous));
        System.out.println("dim(lsi) = " + lsi.documents.size());
        MapBuilder builder = new MapBuilder();
        for (Document each: lsi.documents) {
            int index = lsi.documents.get(each);
            builder.location(mds.x[index], mds.y[index], Math.sqrt(each.termSize()), each);
        }
        builder.name(versionName);
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
                other.yield = norm(other.element.document.name()).equals(
                        norm(each.element.name()));
            }
            if (match.resultIfNone(null) == null) tally++; 
            collect.yield = match.resultIfNone(makeRandomLocation());
        }
        System.out.println("# matches --- " + lsi.documents.size() + " " + tally);
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
