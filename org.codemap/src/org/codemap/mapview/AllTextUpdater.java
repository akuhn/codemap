package org.codemap.mapview;

import org.codemap.layers.CodemapVisualization;

public class AllTextUpdater implements ITextUpdater {

    private MapView mapView;
    private CodemapVisualization currentViz;

    public AllTextUpdater(MapView view) {
        mapView = view;
    }
    
    public void setVisualization(CodemapVisualization viz) {
        if (currentViz != null) {
            currentViz.removeUpdater();
        }
        currentViz = viz;
        currentViz.setUpdater(this);
    }

    @Override
    public void updateNearestNeighbor(String name) {
        mapView.updateToolTip(name);
        mapView.updateContentDescription(name);
    }
}
