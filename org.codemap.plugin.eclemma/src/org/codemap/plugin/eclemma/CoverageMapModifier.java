package org.codemap.plugin.eclemma;

import java.util.List;

import ch.akuhn.util.Pair;
import ch.deif.meander.util.MColor;
import ch.unibe.softwaremap.util.CodemapColors;

public class CoverageMapModifier {

	/**
	 * CoverageMapModifiers equal each other regardless of the coverageInfo they
	 * contain. Thus two instances of this class are always equal to each other.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass()))
			return true;
		return super.equals(obj);
	}

	private List<Pair<String, Double>> coverageInfo;
	private ShowCoverageAction showCoverageAction;

	public CoverageMapModifier(List<Pair<String, Double>> coverageInfo,
			ShowCoverageAction action) {
		this.coverageInfo = coverageInfo;
		this.showCoverageAction = action;
	}

	public void addTo(CodemapColors colorScheme) {
		if (showCoverageAction.isChecked()) {
			for (Pair<String, Double> pair : coverageInfo) {
				String identifier = pair.fst;
				Double ratio = pair.snd;
				int redVal = (int) ((1 - ratio) * 255);
				int greenVal = (int) (ratio * 255);
				MColor col = new MColor(redVal, greenVal, 0);
				colorScheme.setColor(identifier, col);
			}
		} else {
			colorScheme.clearColors();
		}
	}

}
