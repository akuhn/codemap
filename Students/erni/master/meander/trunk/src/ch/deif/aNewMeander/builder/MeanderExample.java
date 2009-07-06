package ch.deif.aNewMeander.builder;

import ch.akuhn.hapax.Hapax;
import ch.deif.aNewMeander.MapColor;
import ch.deif.aNewMeander.MapConfiguration;
import ch.deif.aNewMeander.MapScheme;
import ch.deif.meander.MapSelection;
import ch.deif.meander.viz.MapVisualization;
import ch.deif.meander.viz.OpenFilesOverlay;
import ch.deif.meander.viz.YouAreHereOverlay;

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

		MapVisualization visual = Meander.visualization()
				.withColors(colorScheme)
				.withLabels(labelScheme)
				// .withSelection(new CurrentSelectionOverlay(), currentSelection)
				.withSelection(new OpenFilesOverlay(), openEditorSelection)
				.withSelection(new YouAreHereOverlay(), currentEditorSelection)
				.makeVisualization();

		MapVisualization.makeApplet(map.withSize(512), visual).start();

	}

}
