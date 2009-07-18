package ch.deif.meander.builder;

import ch.deif.meander.MapSelection;
import ch.deif.meander.swt.SWTLayer;
import ch.deif.meander.swt.SelectionOverlay;
import ch.deif.meander.util.MapScheme;

public interface VizBuilder {

	VizBuilder withLabels(MapScheme<String> labelScheme);

	VizBuilder withSelection(SelectionOverlay overlay, MapSelection selection);

	VizBuilder withBackground();

	SWTLayer makeLayer();

}
