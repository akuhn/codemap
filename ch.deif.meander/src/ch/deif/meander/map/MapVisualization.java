package ch.deif.meander.map;

import ch.deif.meander.swt.Background;
import ch.deif.meander.swt.CodemapVisualization;
import ch.deif.meander.swt.CompositeLayer;
import ch.deif.meander.swt.CurrSelectionOverlay;
import ch.deif.meander.swt.OpenFilesOverlay;
import ch.deif.meander.swt.YouAreHereOverlay;

public class MapVisualization {

    private MapValues values;
    private CodemapVisualization viz;
    private CompositeLayer shared;
    
    public MapVisualization(MapValues values) {
        this.values = values;
        this.initializeVisualization();
    }
    
    private void initializeVisualization() {

        CompositeLayer foreground = new CompositeLayer();
//        foreground.add(new LabelOverlay());
        foreground.add(new CurrSelectionOverlay());
//        foreground.add(new ProviderDrivenImageOverlay());
        foreground.add(new OpenFilesOverlay());
        foreground.add(new YouAreHereOverlay());
        foreground.add(shared = new CompositeLayer());

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
    
}
