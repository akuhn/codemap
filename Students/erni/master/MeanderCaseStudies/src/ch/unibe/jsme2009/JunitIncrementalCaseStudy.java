package ch.unibe.jsme2009;

import ch.akuhn.fame.Tower;
import ch.akuhn.fame.parser.InputSource;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.akuhn.util.query.AnySatisfy;
import ch.akuhn.util.query.Collect;
import ch.akuhn.util.query.Collect2;
import ch.akuhn.util.query.Count;
import ch.akuhn.util.query.Detect;
import ch.deif.meander.Location;
import ch.deif.meander.MDS;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;

public class JunitIncrementalCaseStudy {

	public static final String FILENAME = "mse/junit_case.h.mse";

	public static Tower tower() {
		Tower t = new Tower();
		t.metamodel.with(HapaxDoc.class);
		t.model.importMSE(InputSource.fromFilename(FILENAME));
		return t;
	}

	public static Corpus corpus(String versionName) {
		Corpus c = new Corpus();
		for (HapaxDoc each : Get.sorted(tower().model.all(HapaxDoc.class))) {
			// System.out.println(each.version.string);
			if (each.version.string.equals(versionName)) {
				c.add(new Document(each, each.terms));
			}
		}
		return c;
	}

	public static void main(String... args) {
		Serializer ser = new Serializer();
		ser.model().importMSEFile("mse/junit_meander.mse");
		MSEProject proj = ser.model().all(MSEProject.class).iterator().next();
		MSERelease previous = null;
		Map previousMap = null;
		
		for (MSERelease rel : proj.releases) {
			if (previous != null) {
				Count<MSEDocument> count = Count.from(rel.documents);
				// System.out.println(norm(Get.head(previous.documents).name));

//				for (Count<MSEDocument> each : count) {
//					AnySatisfy<MSEDocument> any = AnySatisfy
//							.from(previous.documents);
//					for (AnySatisfy<MSEDocument> other : any) {
//						other.yield = norm(other.element.name).equals(
//								norm(each.element.name));
//					}
//					count.yield = any.result();
//				}
				previousMap = nextMap(rel.name, previousMap);
				// System.out.printf("\t\t\t\t %.2f\n",
				// (1.0*count.result()/rel.documents.size()));
			} else {
				previousMap = firstMap(rel.name);
			}
			previous = rel;
		}
	}

	private static Map firstMap(String versionName) {
		Corpus corpus = corpus(versionName);
		TermDocumentMatrix tdm = new TermDocumentMatrix();
		tdm.addCorpus(corpus);
		tdm = tdm.rejectAndWeight();
		LatentSemanticIndex lsi = tdm.createIndex();
		MDS mds = MDS.fromCorrelationMatrix(lsi.documentCorrelation());
		
		MapBuilder builder = new MapBuilder();
		int index = 0;
		for (Document each: lsi.documents) {
			builder.location(mds.x[index], mds.y[index], Math
					.sqrt(each.terms.size()), each);
			index++;
		}
		return builder.build();
	}

	private static Map nextMap(String versionName, Map previous) {
		Corpus corpus = corpus(versionName);
		TermDocumentMatrix tdm = new TermDocumentMatrix();
		tdm.addCorpus(corpus);
		tdm = tdm.rejectAndWeight();
		LatentSemanticIndex lsi = tdm.createIndex();
		MDS mds = MDS.fromCorrelationMatrix(lsi.documentCorrelation(),
				matchingLocations(lsi, previous));
		MapBuilder builder = new MapBuilder();
		int index = 0;
		for (Document each: lsi.documents) {
			builder.location(mds.x[index], mds.y[index], Math
					.sqrt(each.terms.size()));
			index++;
		}
		return builder.build();
	}
	
	
	private static Iterable<Location> matchingLocations(LatentSemanticIndex lsi,
			Map previous) {
		Collect2<Document,Location> collect = Collect2.from(lsi.documents, Location.class);
		for (Collect2<Document,Location> each: collect) {
			Detect<Location> match = Detect.from(previous.locations);
			for (Detect<Location> other: match) {
				other.yield = norm(other.element.document.toString()).equals(
						norm(each.element.toString()));
			}
			collect.yield = match.resultIfNone(makeRandomLocation());
		}
		return collect.result();
	}

	private static Location makeRandomLocation() {
		return new Location(Math.random() * 6 - 3, Math.random() * 6 - 3, 0.0);
	}

	private static Object norm(String name) {
		name = name.trim();
		if (name.startsWith("junit2/"))
			return name.substring("junit2/".length());
		if (name.startsWith("junit2.1/"))
			return name.substring("junit2.1/".length());
		if (name.startsWith("junit3/"))
			return name.substring("junit3/".length());
		return name;
	}

}
