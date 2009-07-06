package ch.deif.aNewMeander.builder;

import ch.deif.aNewMeander.MapColor;
import ch.deif.aNewMeander.MapScheme;
import ch.deif.meander.MapSelection;
import ch.deif.meander.viz.MapSelectionOverlay;
import ch.deif.meander.viz.MapVisualization;

public interface VisualizationBuilder {

	VisualizationBuilder withColors(MapScheme<MapColor> colorScheme);

	VisualizationBuilder withLabels(MapScheme<String> labelScheme);

	VisualizationBuilder withSelection(MapSelectionOverlay currentSelectionOverlay, MapSelection currentSelection);

	MapVisualization makeVisualization();

}
