package ch.deif.meander.ui;

import java.util.EventListener;

public interface CodemapListener extends EventListener {

	public void handleEvent(CodemapEvent event);
	
}
