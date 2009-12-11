package org.codemap.resources;

import java.util.Collection;

import org.codemap.Configuration;
import org.codemap.DigitalElevationModel;
import org.codemap.HillShading;
import org.codemap.Labeling;
import org.codemap.MapInstance;
import org.codemap.MapSelection;
import org.codemap.commands.Commands;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;
import org.eclipse.swt.graphics.Image;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.values.CollectionValue;
import ch.akuhn.values.IntegerValue;
import ch.akuhn.values.Value;


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
    
    public final Value<Collection<String>> projects;
    public final Value<Collection<String>> extensions;
    public final CollectionValue<MapSelection> selections;

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
        
        projects = make.projectsValue();
        extensions = make.extensionsValue();
        selections = make.selectionsValue(currentSelection, openFilesSelection, youAreHereSelection);  
    }
    
    public void applyCommands(Commands commands) {
        commands.getColoringCommand().apply(colorScheme);
        commands.getLabelingCommand().apply(labelScheme);        
    }

    /** Returns current visualization size in pixels.
     * 
     */
    public int getSize() {
        return mapSize.getValue();
    }

}
