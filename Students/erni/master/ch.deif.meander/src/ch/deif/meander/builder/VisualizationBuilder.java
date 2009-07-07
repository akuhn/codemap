package ch.deif.meander.builder;

import ch.deif.meander.MapSelection;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;
import ch.deif.meander.visual.Layer;
import ch.deif.meander.visual.MapSelectionOverlay;

public interface VisualizationBuilder {

	VisualizationBuilder withColors(MapScheme<MColor> colorScheme);

	VisualizationBuilder withLabels(MapScheme<String> labelScheme);

	VisualizationBuilder withSelection(MapSelectionOverlay currentSelectionOverlay, MapSelection currentSelection);

	Layer makeLayer();

}
