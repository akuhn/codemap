package ch.deif.meander.builder;

import ch.akuhn.hapax.Hapax;
import ch.deif.aNewMeander.visual.Layer;
import ch.deif.aNewMeander.visual.MapVisualization;
import ch.deif.meander.MapColor;
import ch.deif.meander.MapConfiguration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapScheme;
import ch.deif.meander.MapSelection;
import ch.deif.meander.Point;

public class MeanderExample {

	public static void main(String[] args) {

		Hapax hapax = Hapax.legomenon().makeCorpus(".", ".java");

		MapConfiguration map = Meander.builder()
				.addCorpus(hapax)
				.makeMap();

		MapScheme<MapColor> colorScheme = MapScheme.with(MapColor.HILLGREEN);
		MapScheme<String> labelScheme = MapScheme.with("Name");
		// MapSelection currentSelection = new MapSelection();
		MapSelection openEditorSelection = new MapSelection();
		MapSelection currentEditorSelection = new MapSelection();

		Layer layer = Meander.visualization()
				.withColors(colorScheme)
				//.withLabels(labelScheme)
				// .withSelection(new CurrentSelectionOverlay(), currentSelection)
				//.withSelection(new OpenFilesOverlay(), openEditorSelection)
				//.withSelection(new YouAreHereOverlay(), currentEditorSelection)
				.makeLayer();
		
		MapInstance mapWithSize = map.withSize(512);
		new MapVisualization(mapWithSize, layer).openApplet();

	}

	// TODO move this to codemap plug-in
	MapScheme<String> eclipseMapper = new MapScheme<String>() {
		@Override
		public String forLocation(Point location) {
			String name = location.getDocument().getIdentifier();
			int lastPathSeparator = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
			int lastDot = name.lastIndexOf('.');
			if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
			return name;
		}
	};
	
}
