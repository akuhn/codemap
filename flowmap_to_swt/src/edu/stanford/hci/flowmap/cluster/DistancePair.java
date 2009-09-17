package edu.stanford.hci.flowmap.cluster;

import edu.stanford.hci.flowmap.utils.Pair;

/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class DistancePair extends Pair implements Comparable<DistancePair> {
	
	public final double dist;
	
	public DistancePair() {
		super();
		dist = -1;
	}
	
	public DistancePair(Object a, Object b, double dist) {
		super(a, b);
		this.dist = dist;
	}
	
    @Override
    public int compareTo(DistancePair dp) {
        if (dist != dp.dist) {
            if (dist < dp.dist)
                return -1;
            else
                return 1;
        } else if (one != dp.one) {
            if (one.hashCode() < dp.one.hashCode())
                return -1;
            else
                return 1;
        } else if (two != dp.two) {
            if (two.hashCode() < dp.two.hashCode())
                return -1;
            else
                return 1;
        } else {
            return 0;
        }
    }
    
	public boolean equals(Object o) {
	    return (o instanceof DistancePair) && ((DistancePair)o).dist == dist;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(dist);
		sb.append(": ");
		sb.append(super.toString());
		return sb.toString();
	}
}
