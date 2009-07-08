package org.codemap.plugin.eclemma;

import java.util.List;

import org.codemap.CodemapCore;
import org.codemap.mapview.ICodemapPluginAction;
import org.codemap.util.CodemapColors;
import org.eclipse.jface.action.Action;

import ch.akuhn.util.Pair;
import ch.deif.meander.util.MColor;

public class ShowCoverageAction implements ICodemapPluginAction {
	
	private Action action;
	private List<Pair<String, Double>> lastCoverageInfo;
	
	public ShowCoverageAction() {
		EclemmaOverlay.getPlugin().registerCoverageAction(this);
	}

	@Override
	public void run(Action act) {
		action = act;
//		CodemapCore r = CodemapCore.getPlugin();
//		r.getMapView().updateMap(r);
		if (isChecked()) showCoverage();
		else hideCoverage();
	}

	private void hideCoverage() {
		CodemapCore.getPlugin().getColorScheme().clearColors();
		CodemapCore.getPlugin().redrawCodemapBackground();
	}

	public boolean isChecked() {
		if (action == null) return false;
		return action.isChecked();
	}

	public void newCoverageAvailable(List<Pair<String, Double>> coverageInfo) {
		lastCoverageInfo = coverageInfo;		
		if (!isChecked()) return;
		
		showCoverage();
	}

	private void showCoverage() {
		if (lastCoverageInfo == null) return;
		
		CodemapColors colorScheme = CodemapCore.getPlugin().getColorScheme();
		for (Pair<String, Double> pair : lastCoverageInfo) {
			String identifier = pair.fst;
			Double ratio = pair.snd;
			int redVal = (int) ((1 - ratio) * 255);
			int greenVal = (int) (ratio * 255);
			MColor col = new MColor(redVal, greenVal, 0);
			colorScheme.setColor(identifier, col);
		}
		CodemapCore.getPlugin().redrawCodemapBackground();		
	}

}
