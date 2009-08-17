package ch.deif.meander.map;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.values.IntegerValue;
import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;
import ch.deif.meander.Labeling;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;


public class MapValues {

    public final IntegerValue mapSize;
    public final Value<Collection<String>> elements;
    public final Value<MapScheme<Boolean>> hills;
    public final Value<MapScheme<MColor>> colorScheme;
    public final Value<MapScheme<String>> labelScheme;

    public final MapSelection currentSelection;
    public final MapSelection openFilesSelection;
    public final MapSelection youAreHereSelection;

    public final Value<LatentSemanticIndex> index;
    public final Value<Configuration> configuration;
    public final Value<MapInstance> mapInstance;
    public final Value<DigitalElevationModel> elevationModel;
    public final Value<HillShading> shading;
    public final Value<Image> background;
    public final Value<Labeling> labeling;

    public MapValues(MapValueBuilder make) {

        mapSize = make.mapSizeValue();
        elements  = make.elementsValue();
        hills  = make.hillsValue();
        colorScheme  = make.colorsValue();
        labelScheme  = make.labelsValue();
        
        currentSelection = make.currentSelectionValue();
        openFilesSelection = make.openFilesSelectionValue();
        youAreHereSelection = make.youAreHereSelection();

        index = make.indexValue(elements);
        configuration = make.configurationValue(index);
        mapInstance = make.mapInstanceValue(mapSize, index, configuration);
        elevationModel = make.elevationModelValue(mapInstance, hills);
        shading = make.hillShadingValue(mapInstance, elevationModel);
        background = make.backgroundValue(mapInstance, elevationModel, shading, colorScheme);
        labeling = make.labelingValue(mapInstance, labelScheme);

    }
    
    /** Returns current visualization size in pixels.
     * 
     */
    public int getSize() {
        return mapSize.getValue();
    }

}
