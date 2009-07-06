package ch.deif.aNewMeander.builder;

import java.util.ArrayList;
import java.util.Collection;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.deif.aNewMeander.Location;
import ch.deif.aNewMeander.MapColor;
import ch.deif.aNewMeander.MapConfiguration;
import ch.deif.aNewMeander.MapScheme;
import ch.deif.aNewMeander.Point;
import ch.deif.meander.MapSelection;
import ch.deif.meander.internal.MDS;
import ch.deif.meander.viz.Layers;
import ch.deif.meander.viz.MapSelectionOverlay;
import ch.deif.meander.viz.MapVisualization;

/** Not thread-safe.
 * 
 * @author akuhn
 *
 */
public class Meander implements MapBuilder, VisualizationBuilder {

	private Hapax hapax = null;
	
	public static MapBuilder builder() {
		return new Meander();
	}

	@Override
	public MapBuilder addCorpus(Hapax hapax) {
		if (this.hapax != null) throw new IllegalStateException();
		this.hapax = hapax;
		return this;
	}

	@Override
	public MapConfiguration makeMap() {
		if (this.hapax == null) throw new IllegalStateException();
		LatentSemanticIndex lsi = hapax.getIndex();
		MDS mds = MDS.fromCorrelationMatrix(lsi);
		mds.normalize();
		Collection<Location> locations = new ArrayList<Location>();
		int index = 0;
		for (Document each: lsi.documents) {
			Point p = new Point(mds.x[index], mds.y[index]);
			locations.add(new Location(p, each, each.termSize()));
		}
		return new MapConfiguration(locations);
	}

	private Layers layers = new Layers();
	
	public static VisualizationBuilder visualization() {
		return new Meander();
	}

	@Override
	public MapVisualization makeVisualization() {
		return null;
	}

	@Override
	public VisualizationBuilder withColors(MapScheme<MapColor> colorScheme) {
		
		return this;
	}

	@Override
	public VisualizationBuilder withLabels(MapScheme<String> labelScheme) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisualizationBuilder withSelection(
			MapSelectionOverlay currentSelectionOverlay,
			MapSelection currentSelection) {
		// TODO Auto-generated method stub
		return null;
	}

}