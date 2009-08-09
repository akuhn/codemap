package ch.deif.meander.swt;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;
import ch.deif.meander.builder.MapBuilder;
import ch.deif.meander.builder.Meander;

public class SWTExample {

	public static void main(String[] args) {
		MapInstance map = makeMap();
		CodemapVisualization visual = new CodemapVisualization(map);
		Background layer = new Background();
		visual.add(layer);
		layer.children.add(new WaterBackground());
		layer.children.add(new ShoreLayer());
		layer.children.add(new HillshadeLayer());
		visual.add(new LabelOverlay());
		visual.add(new CurrSelectionOverlay().setSelection(new MapSelection()));
		visual.openAndBlock();
	}

	static MapInstance makeMap() {
		return Meander.builder()
			.addCorpus(Hapax.newCorpus().addFiles("../ch.akuhn.hapax", ".java").build())
			.makeMap(new Configuration())
			.withSize(512, MapBuilder.FILE_LENGTH_SQRT);
	}

}
