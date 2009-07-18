package ch.deif.meander.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.Pair;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapSelection;
import ch.deif.meander.Point;
import ch.deif.meander.internal.MDS;
import ch.deif.meander.legacy.builder.VisualizationBuilder;
import ch.deif.meander.swt.CompositeLayer;
import ch.deif.meander.swt.HillshadeLayer;
import ch.deif.meander.swt.LabelOverlay;
import ch.deif.meander.swt.SWTLayer;
import ch.deif.meander.swt.SelectionOverlay;
import ch.deif.meander.swt.ShoreLayer;
import ch.deif.meander.swt.WaterBackground;
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

	private static class MeanderVizBuilder implements VisualizationBuilder {
		
		private Composite<Layer> layers;
		private HillshadeVisualization hillShade;

		public MeanderVizBuilder() {
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
		public Composite<Layer> makeLayer() {
			return layers;
		}

		@Override
		public VisualizationBuilder appendLayer(Layer anotherLayer) {
			layers.append(anotherLayer);
			return this;
		}
	}

	private static final class MeanderMapBuilder implements MapBuilder {
		
		private Hapax hapax = null;

		@Override
		public Configuration makeMap() {
			if (this.hapax == null) throw new IllegalStateException();
			LatentSemanticIndex lsi = hapax.getIndex();
			MDS mds = MDS.fromCorrelationMatrix(lsi);
			mds.normalize();
			Collection<Point> locations = new ArrayList<Point>();
			int index = 0;
			for (String each: lsi.documents()) {
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

		@Override
		public Configuration makeMap(Map<String, Pair<Double, Double>> cachedPoints) {
			// we need a hapax for later recalculations.
			if (this.hapax == null) throw new IllegalStateException();
			// fallback when there is no cache or cache-reload failed.
			if (cachedPoints == null) return makeMap();
			
			Collection<Point> locations = new ArrayList<Point>();
			for (String each: hapax.getIndex().documents()) {
				Pair<Double, Double> coordinates = cachedPoints.get(each);
				if (coordinates == null) {
					// cached data does not match current data, so we start from scratch.
					return makeMap();
				}
				locations.add(new Point(coordinates.fst, coordinates.snd, each));
			}
			// no normalize here, the cached data is already normalized.
			return new Configuration(locations);
		}
	}
	
	private static class NewVizBuilder implements VizBuilder {
		
		private CompositeLayer layers;

		public NewVizBuilder() {
			layers = new CompositeLayer();
		}

		@Override
		public VizBuilder withLabels(MapScheme<String> labelScheme) {
			layers.add(new LabelOverlay(labelScheme));
			return this;
		}

		@Override
		public VizBuilder withSelection(SelectionOverlay overlay, MapSelection selection) {
			overlay.setSelection(selection);
			layers.add(overlay);
			return this;
		}

		@Override
		public VizBuilder withBackground() {
			ch.deif.meander.swt.Background background = new ch.deif.meander.swt.Background();
			background.children.add(new WaterBackground());
			background.children.add(new ShoreLayer());
			background.children.add(new HillshadeLayer());
			layers.prepend(background);
			return this;
		}

		@Override
		public SWTLayer makeLayer() {
			return layers;
		}
		
	}

	public static MapBuilder builder() {
		return new MeanderMapBuilder();
	}
	
	public static VizBuilder vizBuilder() {
		return new NewVizBuilder();
	}
	
	/**
	 * @deprecated please switch to the SWT implementation and use <code>vizBuilder()</code>> 
	 */
	@Deprecated
	public static VisualizationBuilder visualization() {
		return new MeanderVizBuilder();
	}
}
