package ch.deif.meander;

import ch.deif.meander.viz.LabelsOverlay;


public class FromSourceFilesToMap {
	
	public void run() {
		
		Meander.script().addDocuments("../JExample", ".java").makeMap().useHillshading().add(LabelsOverlay.class)
		        .openApplet();
		
	}
	
	public static void main(String... args) {
		
		Meander.script().addDocuments("../JExample", ".java").makeMap().useHillshading().add(LabelsOverlay.class)
		        .openApplet();
		
		// TermDocumentMatrix tdm = new TermDocumentMatrix();
		// Importer importer = new Importer(tdm);
		// importer.importAllFiles(new File("../JExample"), ".java");
		// tdm = tdm.rejectAndWeight();
		// LatentSemanticIndex lsi = tdm.createIndex();
		// MDS mds = MDS.fromCorrelationMatrix(lsi);
		// MapBuilder builder = Map.builder().size(512, 512);
		// Iterator<Document> iterator = lsi.documents.iterator();
		// for (int n = 0; n < mds.x.length; n++) {
		// Document each = iterator.next();
		// builder.location(mds.x[n], mds.y[n], Math.sqrt(each.termSize()), each);
		// }
		// builder.normalizeXY();
		// Map map = builder.done();
		// new DEMAlgorithm(map).run();
		// new NormalizeElevationAlgorithm(map).run();
		// new HillshadeAlgorithm(map).run();
		// new ContourLineAlgorithm(map).run();
		// Layers layers = new Layers(map);
		// layers.useHillshading();
		// layers.add(new LabelsOverlay(map));
		// layers.openApplet();
	}
	
}
