package ch.deif.meander.builder;

import ch.deif.meander.MapColor;
import ch.deif.meander.MapScheme;
import ch.deif.meander.MapSelection;
import ch.deif.meander.visual.Layer;
import ch.deif.meander.viz.MapSelectionOverlay;

public interface VisualizationBuilder {

	VisualizationBuilder withColors(MapScheme<MapColor> colorScheme);

	VisualizationBuilder withLabels(MapScheme<String> labelScheme);

	VisualizationBuilder withSelection(MapSelectionOverlay currentSelectionOverlay, MapSelection currentSelection);

	Layer makeLayer();

}
