package ch.deif.meander.builder;

import ch.deif.meander.MapSelection;
import ch.deif.meander.swt.SWTLayer;
import ch.deif.meander.swt.SelectionOverlay;
import ch.deif.meander.util.MapScheme;

public interface LayersBuilder {

	LayersBuilder withLabels(MapScheme<String> labelScheme);

	LayersBuilder withSelection(SelectionOverlay overlay, MapSelection selection);

	SWTLayer makeLayer();

	LayersBuilder withLayer(SWTLayer sharedLayer);

}
