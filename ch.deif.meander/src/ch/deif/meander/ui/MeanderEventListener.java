package ch.deif.meander.ui;

import ch.deif.meander.Location;

public interface MeanderEventListener {

	void selectionChanged(Location... locations);

	void doubleClicked(Location location);

}
