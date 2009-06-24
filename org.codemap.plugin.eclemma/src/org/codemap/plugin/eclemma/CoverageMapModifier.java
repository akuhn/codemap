package org.codemap.plugin.eclemma;

import java.util.List;

import ch.akuhn.util.Pair;
import ch.deif.meander.Colors;
import ch.deif.meander.Location;
import ch.deif.meander.Map;
import ch.deif.meander.MapModifier;

public class CoverageMapModifier implements MapModifier {
	
	/**
	 * CoverageMapModifiers equal each other regardless of the coverageInfo they
	 * contain.
 	 * Thus two instances of this class are allways equal to each other.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) return true;
		return super.equals(obj);
	}

	private List<Pair<String, Double>> coverageInfo;

	public CoverageMapModifier(List<Pair<String, Double>> coverageInfo) {
		this.coverageInfo = coverageInfo;
	}

	@Override
	public void applyOn(Map map) {
		for (Location loc: map.locations) {
			String identifier = loc.document().getIdentifier();
			for (Pair<String, Double> pair: coverageInfo) {
				if (pair.fst.equals(identifier)) {
					Double ratio = pair.snd;
					int redVal = (int)(ratio*255);
					Colors col = new Colors(redVal, 255, 255);
					loc.setColor(col);
					break;
				}
			}
		}	
	}

}
