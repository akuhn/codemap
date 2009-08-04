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
import ch.deif.meander.swt.CompositeLayer;
import ch.deif.meander.swt.HillshadeLayer;
import ch.deif.meander.swt.LabelOverlay;
import ch.deif.meander.swt.SWTLayer;
import ch.deif.meander.swt.SelectionOverlay;
import ch.deif.meander.swt.ShoreLayer;
import ch.deif.meander.swt.WaterBackground;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

/** Not thread-safe.
 * 
 * @author akuhn
 *
 */
public class Meander {

	private static final class MeanderMapBuilder implements MapBuilder {
		
		private LatentSemanticIndex lsi = null;

		@Override
		public Configuration makeMap() {
			if (this.lsi == null) throw new IllegalStateException();
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
			if (this.lsi != null) throw new IllegalStateException();
			this.lsi = hapax.getIndex();
			return this;
		}

		@Override
		public Configuration makeMap(Map<String, Pair<Double, Double>> cachedPoints) {
			// we need a hapax for later recalculations.
			if (this.lsi == null) throw new IllegalStateException();
			// fallback when there is no cache or cache-reload failed.
			if (cachedPoints == null) return makeMap();
			
			Collection<Point> locations = new ArrayList<Point>();
			for (String each: lsi.documents()) {
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

		@Override
		public MeanderMapBuilder addCorpus(LatentSemanticIndex index) {
			if (this.lsi != null) throw new IllegalStateException();
			this.lsi = index;
			return this;
		}
	}
	
	private static class MyBackgroundBuilder implements BackgroundBuilder {

		private MapScheme<MColor> colorScheme;

		@Override
		public BackgroundBuilder withColors(MapScheme<MColor> scheme) {
			colorScheme = scheme;
			return this;
		}

		@Override
		public ch.deif.meander.swt.Background makeBackground() {
			ch.deif.meander.swt.Background background = new ch.deif.meander.swt.Background();
			HillshadeLayer hillshade = new HillshadeLayer();
			if (colorScheme != null) {
				hillshade.setScheme(colorScheme);
			}
			background.children.add(new WaterBackground());
			background.children.add(new ShoreLayer());
			background.children.add(hillshade);
			return background;
		}
		
	}
	
	private static class ForegroundBuilder implements LayersBuilder {
		
		private CompositeLayer layers;
		private MapScheme<MColor> colorScheme;
		private HillshadeLayer hillshade;

		public ForegroundBuilder() {
			layers = new CompositeLayer();
		}

		@Override
		public LayersBuilder withLabels(MapScheme<String> labelScheme) {
			layers.add(new LabelOverlay(labelScheme));
			return this;
		}

		@Override
		public LayersBuilder withSelection(SelectionOverlay overlay, MapSelection selection) {
			overlay.setSelection(selection);
			layers.add(overlay);
			return this;
		}
		
		@Override
		public LayersBuilder withLayer(SWTLayer layer) {
			layers.add(layer);
			return this;
		}
		
		@Override
		public SWTLayer makeLayer() {
			if (hillshade != null && colorScheme != null) {
				hillshade.setScheme(colorScheme);
			}
			return layers;
		}

	}

	public static MapBuilder builder() {
		return new MeanderMapBuilder();
	}
	
	public static BackgroundBuilder background() {
		return new MyBackgroundBuilder();
	}

	public static LayersBuilder layers() {
		return new ForegroundBuilder();
	}
}
