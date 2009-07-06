package ch.deif.aNewMeander.builder;

import java.util.ArrayList;
import java.util.Collection;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.deif.aNewMeander.Point;
import ch.deif.aNewMeander.MapColor;
import ch.deif.aNewMeander.MapConfiguration;
import ch.deif.aNewMeander.MapScheme;
import ch.deif.aNewMeander.visual.Composite;
import ch.deif.aNewMeander.visual.HillshadeVisualization;
import ch.deif.aNewMeander.visual.Layer;
import ch.deif.aNewMeander.visual.ShoreVizualization;
import ch.deif.aNewMeander.visual.WaterVisualization;
import ch.deif.meander.MapSelection;
import ch.deif.meander.internal.MDS;
import ch.deif.meander.viz.MapSelectionOverlay;

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
		Collection<Point> locations = new ArrayList<Point>();
		int index = 0;
		for (Document each: lsi.documents) {
			locations.add(new Point(mds.x[index], mds.y[index], each));
			index++;
		}
		return new MapConfiguration(locations).normalize();
	}

	private Composite<Layer> layers = new Composite<Layer>();
	
	public static VisualizationBuilder visualization() {
		return new Meander().useHillshade();
	}

	private VisualizationBuilder useHillshade() {
		layers.add(new WaterVisualization());
		layers.add(new ShoreVizualization());
		layers.add(new HillshadeVisualization());
		
		//layers.add(new SketchVisualization());
		
		return this;
	}

	@Override
	public Layer makeLayer() {
		return layers;
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
