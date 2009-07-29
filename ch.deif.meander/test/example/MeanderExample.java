package example;


import ch.akuhn.hapax.Hapax;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;
import ch.deif.meander.Point;
import ch.deif.meander.builder.MapBuilder;
import ch.deif.meander.builder.Meander;
import ch.deif.meander.swt.Background;
import ch.deif.meander.swt.CodemapVisualization;
import ch.deif.meander.swt.CurrSelectionOverlay;
import ch.deif.meander.swt.LabelOverlay;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

public class MeanderExample {

	public static void main(String[] args) {

		Hapax hapax = Hapax.newCorpus()
			.addFiles(".", ".java")
			.build();

		Configuration map = Meander.builder()
				.addCorpus(hapax)
				.makeMap();

		MapScheme<MColor> colorScheme = MapScheme.with(MColor.HILLGREEN);

		Background bg = Meander.background()
			.withColors(colorScheme)
			.makeBackground();
		
		MapInstance mapWithSize = map.withSize(512, MapBuilder.FILE_LENGTH_SQRT);

		CodemapVisualization visual = new CodemapVisualization(mapWithSize);
		visual.add(bg);
		visual.add(new LabelOverlay(FILENAMES));
		visual.add(new CurrSelectionOverlay().setSelection(new MapSelection()));
		visual.openAndBlock();		

	}

	private static final MapScheme<String> FILENAMES = new MapScheme<String>() {
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
