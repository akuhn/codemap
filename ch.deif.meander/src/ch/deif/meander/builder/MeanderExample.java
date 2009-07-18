package ch.deif.meander.builder;


import ch.akuhn.hapax.Hapax;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;
import ch.deif.meander.Point;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;
import ch.deif.meander.visual.CurrentSelectionOverlay;
import ch.deif.meander.visual.Layer;
import ch.deif.meander.visual.MapVisualization;

public class MeanderExample {

	public static void main(String[] args) {

		Hapax hapax = Hapax.newCorpus()
			.addFiles(".", ".java")
			.build();

		Configuration map = Meander.builder()
				.addCorpus(hapax)
				.makeMap();

		MapScheme<MColor> colorScheme = MapScheme.with(MColor.HILLGREEN);
		MapSelection currentSelection = new MapSelection();
		MapSelection openEditorSelection = new MapSelection();
		MapSelection currentEditorSelection = new MapSelection();

		Layer layer = Meander.visualization()
				.withColors(colorScheme)
				.withLabels(filenames_only)
				.withColors(colorScheme)
				.withSelection(new CurrentSelectionOverlay(), currentSelection)
				//.withSelection(new OpenFilesOverlay(), openEditorSelection)
				//.withSelection(new YouAreHereOverlay(), currentEditorSelection)
				.makeLayer();
		
		MapInstance mapWithSize = map.withSize(512, MapBuilder.FILE_LENGTH_SQRT);
		new MapVisualization(mapWithSize, layer).openApplet();

	}

	private static final MapScheme<String> filenames_only = new MapScheme<String>() {
		@Override
		public String forLocation(Point location) {
			String name = location.getDocument();
			int lastPathSeparator = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
			int lastDot = name.lastIndexOf('.');
			if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
			return name;
		}
	};
	
}
