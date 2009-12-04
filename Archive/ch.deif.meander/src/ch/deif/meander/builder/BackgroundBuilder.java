package ch.deif.meander.builder;

import ch.deif.meander.swt.Background;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;


public interface BackgroundBuilder {

	BackgroundBuilder withColors(MapScheme<MColor> colorScheme);

	Background makeBackground();

}
