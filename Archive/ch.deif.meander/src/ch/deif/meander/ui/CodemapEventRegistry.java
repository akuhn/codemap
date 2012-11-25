/**
 * 
 */
package ch.deif.meander.ui;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.swt.widgets.Display;

public class CodemapEventRegistry {

	private Collection<CodemapListener> listeners = new HashSet<CodemapListener>();

	public void fireEvent(String kind, Object source, Object value) {
		final CodemapEvent event = new CodemapEvent(kind, source, value);
		Display.getDefault().asyncExec(
//		new Thread(
			new Runnable() {
			@Override
			public void run() {
				for (CodemapListener each : listeners) {
					each.handleEvent(event);
				}
			}
		});//.run();
	}
	
	public void removeListener(CodemapListener listener) {
		listeners.remove(listener);
	}

	public void addListener(CodemapListener listener) {
		listeners.add(listener);
	}

}