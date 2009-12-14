package org.codemap;

import org.codemap.layers.Background;
import org.codemap.layers.CodemapVisualization;
import org.codemap.layers.CompositeLayer;
import org.codemap.layers.CurrentSelectionOverlay;
import org.codemap.layers.LabelOverlay;
import org.codemap.layers.OpenFileIconsLayer;
import org.codemap.layers.YouAreHereOverlay;
import org.codemap.resources.MapValues;
import org.eclipse.jface.action.Action;


public class MapVisualization {

    private MapValues values;
    private CodemapVisualization viz;
    private CompositeLayer shared;
    private YouAreHereOverlay youAreHere;
    private OpenFileIconsLayer openFiles;
    private CurrentSelectionOverlay selection;
    
    public MapVisualization(MapValues values) {
        this.values = values;
        this.initializeVisualization();
    }
    
    private void initializeVisualization() {

        CompositeLayer foreground = new CompositeLayer();
        foreground.add(new LabelOverlay());
        foreground.add(selection = new CurrentSelectionOverlay());
        // TODO add methods to CompositeLayer to replace layers by eg an ID
        foreground.add(shared = new CompositeLayer());
        foreground.add(youAreHere = new YouAreHereOverlay());
        foreground.add(openFiles = new OpenFileIconsLayer());

        viz = new CodemapVisualization(values);
        viz.add(new Background());
        viz.add(foreground);
        
    }

    public CodemapVisualization getVisualization() {
        return viz;
    }
    
    public CompositeLayer getSharedLayer() {
        return shared;
    }

    public YouAreHereOverlay getYouAreHere() {
        return youAreHere;
    }

    public OpenFileIconsLayer getOpenFiles() {
        return openFiles;
    }

    public CurrentSelectionOverlay getSelection() {
        return selection;
    }
    
}
