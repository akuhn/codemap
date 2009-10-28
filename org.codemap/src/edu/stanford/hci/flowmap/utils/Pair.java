package edu.stanford.hci.flowmap.utils;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class Pair {

	public Object one;
	public Object two;
	
	public Pair() {
		one = two = null;
	}
	
	public Pair(Object a, Object b) {
		one = a;
		two = b;
	}
	
	public boolean equals(Object o) {
		Pair p = (Pair) o;
		if ((one != null) && (p.one != null) && (two != null) && (p.two != null))
			return (one.equals(p.one) && two.equals(p.two));
		else
			return false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(one.toString());
		sb.append(",");
		sb.append(two.toString());
		sb.append(")");
		return sb.toString();
	}
	
	
}
