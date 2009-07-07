package ch.deif.meander.builder;

import java.util.ArrayList;
import java.util.Collection;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapSelection;
import ch.deif.meander.Point;
import ch.deif.meander.internal.MDS;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;
import ch.deif.meander.visual.Background;
import ch.deif.meander.visual.Composite;
import ch.deif.meander.visual.HillshadeVisualization;
import ch.deif.meander.visual.LabelsOverlay;
import ch.deif.meander.visual.Layer;
import ch.deif.meander.visual.MapSelectionOverlay;
import ch.deif.meander.visual.ShoreVizualization;
import ch.deif.meander.visual.WaterVisualization;

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
	public Configuration makeMap() {
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
		return new Configuration(locations).normalize();
	}

	private Composite<Layer> layers = new Composite<Layer>();
	
	public static VisualizationBuilder visualization() {
		return new Meander().useHillshade();
	}

	private VisualizationBuilder useHillshade() {
		Background bg = new Background();
		bg.append(new WaterVisualization());
		bg.append(new ShoreVizualization());
		bg.append(new HillshadeVisualization());
		layers.append(bg);
		
		//layers.add(new SketchVisualization());
		
		return this;
	}

	@Override
	public Layer makeLayer() {
		return layers;
	}

	@Override
	public VisualizationBuilder withColors(MapScheme<MColor> colorScheme) {
		
		return this;
	}

	@Override
	public VisualizationBuilder withLabels(MapScheme<String> labelScheme) {
		layers.append(new LabelsOverlay(labelScheme));
		return this;
	}

	@Override
	public VisualizationBuilder withSelection(
			MapSelectionOverlay overlay,
			MapSelection selection) {
		layers.append(overlay.setSelection(selection));
		return this;
	}

}
