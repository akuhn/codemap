package ch.deif.meander.ui;

import java.util.List;

import ch.deif.meander.Location;

/**
 *  * @author deif
 *
 *  EventHandler that does nothing, used to initialize the EventHandler 
 *  in the MapViz Applet.
 *   
 */
public class NullEventHandler implements IEventHandler {

	@Override
	public void onAppletSelection(Location location) {
	}

	@Override
	public void onAppletSelection(List<Location> locations) {
	}

	@Override
	public void onAppletSelectionCleared() {
	}

	@Override
	public void onMeanderSelection(int[] indices) {
	}
}
