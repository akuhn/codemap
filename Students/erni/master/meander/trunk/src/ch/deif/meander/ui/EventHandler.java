package ch.deif.meander.ui;

import java.util.ArrayList;

import ch.akuhn.hapax.corpus.Document;
import ch.deif.meander.Location;
import ch.deif.meander.ui.Meander.MeanderWindow;

public class EventHandler implements IEventHandler {

	private MeanderWindow window;
	private MapViz applet;

	public EventHandler(MeanderWindow m, MapViz a) {
		window = m;
		applet = a;
		a.registerHandler(this);
		m.registerHandler(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.deif.meander.ui.IEventHandler#onAppletSelectionCleared()
	 */
	public void onAppletSelectionCleared() {
		assert window.getDisplay() != null;
		window.getDisplay().syncExec(new Runnable() {
			public void run() {
				window.files().deselectAll();
				assert window.cloud() != null;
				window.cloud().clear();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.deif.meander.ui.IEventHandler#onAppletSelection(ch.deif.meander.Location
	 * )
	 */
	public void onAppletSelection(Location location) {
		ArrayList<Location> l = new ArrayList<Location>();
		l.add(location);
		this.onAppletSelection(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.deif.meander.ui.IEventHandler#onMeanderSelection(int[])
	 */
	public void onMeanderSelection(int[] indices) {
		applet.indicesSelected(indices);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.deif.meander.ui.IEventHandler#onAppletSelection(java.util.List)
	 */
	public void onAppletSelection(final java.util.List<Location> locations) {
		window.getDisplay().syncExec(new Runnable() {
			public void run() {
				Document document;
				window.files().deselectAll();
				for (Location each : locations) {
					document = each.document;
					int index = window.files().indexOf(document.name());
					window.files().select(index);
					window.cloud().append(document.terms());
				}
				window.cloud().renderText();
			}
		});
	}

}
