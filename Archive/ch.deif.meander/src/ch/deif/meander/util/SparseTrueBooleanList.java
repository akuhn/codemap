package ch.deif.meander.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.akuhn.util.As;

public class SparseTrueBooleanList {

	private int size;
	private int[] keys;
	
	public SparseTrueBooleanList(boolean... elements) {
		this.size = elements.length;
		List<Integer> keys = new ArrayList<Integer>();
		for (int n = 0; n < elements.length; n++) {
			if (elements[n]) keys.add(n);
		}
		this.keys = As.intArray(keys);
	}	

	public boolean get(int index) {
		if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException();
		return Arrays.binarySearch(keys, index) >= 0;
	}
	
	public int size() {
		return size;
	}
	
}
