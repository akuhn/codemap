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

	public void onAppletSelection(Location location) {
	}

	public void onAppletSelection(List<Location> locations) {
	}

	public void onAppletSelectionCleared() {
	}

	public void onMeanderSelection(int[] indices) {
	}
}
