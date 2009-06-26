
package org.codemap.plugin.eclemma;

import java.util.List;

import ch.akuhn.util.Pair;
import ch.deif.meander.Colors;
import ch.deif.meander.Location;
import ch.deif.meander.Map;
import ch.deif.meander.MapModifier;
import ch.unibe.softwaremap.ProjectMap;

public class CoverageMapModifier implements MapModifier {
	
	/**
	 * CoverageMapModifiers equal each other regardless of the coverageInfo they
	 * contain.
 	 * Thus two instances of this class are always equal to each other.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) return true;
		return super.equals(obj);
	}

	private List<Pair<String, Double>> coverageInfo;
	private ShowCoverageAction showCoverageAction;

	public CoverageMapModifier(List<Pair<String, Double>> coverageInfo, ShowCoverageAction action) {
		this.coverageInfo = coverageInfo;
		this.showCoverageAction = action;
	}

	@Override
	public void applyOn(Map map) {
		for (Location loc: map.locations()) {
			if ( showCoverageAction.isChecked()) {
				String identifier = loc.document().getIdentifier();
				for (Pair<String, Double> pair: coverageInfo) {
					if (pair.fst.equals(identifier)) {
						Double ratio = pair.snd;
						int redVal = (int) ((1-ratio)*255);
						int greenVal = (int)(ratio*255);
						Colors col = new Colors(redVal, greenVal, 0);
						loc.setColor(col);
						break;
					}
				}				
			} else {
				loc.removeColor();
			}
		}	
	}

	public void addTo(ProjectMap map) {
		map.addModifier(this);
	}

}
