package ch.deif.meander.ui;

import ch.deif.meander.Location;

public interface IEventHandler {

	public abstract void onAppletSelectionCleared();

	public abstract void onAppletSelection(Location location);

	public abstract void onMeanderSelection(int[] indices);

	public abstract void onAppletSelection(
			final java.util.List<Location> locations);

}