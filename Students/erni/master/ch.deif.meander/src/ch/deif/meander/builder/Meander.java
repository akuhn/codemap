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
public class Meander {

	private static class MyVisualizationBuilder implements VisualizationBuilder {
		
		private Composite<Layer> layers;
		private HillshadeVisualization hillShade;

		public MyVisualizationBuilder() {
			layers = new Composite<Layer>();
			hillShade = new HillshadeVisualization();
			Background bg = new Background();
			bg.append(new WaterVisualization());
			bg.append(new ShoreVizualization());
			bg.append(hillShade);
			layers.append(bg);							
		}

		@Override
		public VisualizationBuilder withSelection(
				MapSelectionOverlay overlay,
				MapSelection selection) {
			layers.append(overlay.setSelection(selection));
			return this;
		}

		@Override
		public VisualizationBuilder withLabels(MapScheme<String> labelScheme) {
			layers.append(new LabelsOverlay(labelScheme));
			return this;
		}

		@Override
		public VisualizationBuilder withColors(MapScheme<MColor> colorScheme) {
			hillShade.setColorScheme(colorScheme);
			return this;
		}

		@Override
		public Layer makeLayer() {
			return layers;
		}
	}


	private static final class MapBuilderImplementation implements MapBuilder {
		private Hapax hapax = null;

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

		@Override
		public MapBuilder addCorpus(Hapax hapax) {
			if (this.hapax != null) throw new IllegalStateException();
			this.hapax = hapax;
			return this;
		}
	}


	public static MapBuilder builder() {
		return new MapBuilderImplementation();
	}
	
	
	public static VisualizationBuilder visualization() {
		return new MyVisualizationBuilder();
	}
}
