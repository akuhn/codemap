package org.codemap.mapview;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.List;

import org.codemap.util.CodemapColors;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;

import ch.akuhn.values.Value;

/** Keeps track of recently visited files for heat-map coloring.
 * 
 * @author David Erni
 *
 */
public class VisitedFilesHistory {

	private static final int HISTORY_LENGTH = 24;

	private List<String> history;
	private MapController theController;
	private boolean enabled;

	public VisitedFilesHistory(MapController controller) {
		theController = controller;
		history = new ArrayList<String>();
	}

	public void append(List<String> selection) {
		history.removeAll(selection);
		history.addAll(0, selection);
		history = new ArrayList<String>(history.subList(0, min(history.size(), HISTORY_LENGTH)));
		updateColors();
	}

	private void updateColors() {
		if (!enabled) return;
		Value<MapScheme<MColor>> colorValue = theController.getActiveMap().getValues().colorScheme;

		MColor cold = new MColor(0,0,180);
		MColor hot = new MColor(255,180,0);
		CodemapColors colorScheme = new CodemapColors(cold);
		double factor = 1.0;
		for(String each: history) { // from hottest to cold
			MColor col = cold.blendWith(hot, factor);
			colorScheme.setColor(each, col);       
			factor -= 0.5 / HISTORY_LENGTH;
		}
		colorValue.setValue(colorScheme);
	}

	public void setEnabled(boolean b) {
		enabled = b;
		updateColors();
	}
}
