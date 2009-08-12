package ch.deif.meander.map;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;
import ch.deif.meander.Labeling;
import ch.deif.meander.MapInstance;
import ch.deif.meander.swt.CodemapVisualization;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;


public final class MapValues {

	public final Value<Integer> mapSize;
	public final Value<Collection<String>> elements;
	public final Value<MapScheme<Boolean>> hills;
	public final Value<MapScheme<MColor>> colors;
	public final Value<MapScheme<String>> labels;
	
	public final Value<LatentSemanticIndex> index;
	public final Value<Configuration> configuration;
	public final Value<MapInstance> mapInstance;
	public final Value<CodemapVisualization> vizualization;
	public final Value<DigitalElevationModel> elevationModel;
	public final Value<HillShading> shading;
	public final Value<Image> background;
	public final Value<Labeling> labeling;

	public MapValues() {
	    this(new MapValueBuilder());
	}
	
	public MapValues(MapValueBuilder make) {
		
		mapSize = make.mapSize();
		elements  = make.elements();
		hills  = make.hills();
		colors  = make.colors();
		labels  = make.labels();
		
		index = make.indexValue(elements);
		configuration = make.configurationValue(index);
		mapInstance = make.mapInstanceValue(mapSize, index, configuration);
		vizualization = make.visualizationValue(mapInstance);
		elevationModel = make.elevationModelValue(mapInstance, hills);
		shading = make.hillShadingValue(elevationModel);
		background = make.backgroundValue(elevationModel, shading, colors);
		labeling = make.labelingValue(mapInstance, labels);
		
	}

}
