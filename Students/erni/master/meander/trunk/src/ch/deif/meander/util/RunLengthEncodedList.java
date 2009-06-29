package ch.deif.meander.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RunLengthEncodedList<E> {

	private int size;
	private int[] keys;
	private Object[] values;
	
	public RunLengthEncodedList(E... elements) {
		this(Arrays.asList(elements));
	}
	
	public RunLengthEncodedList(Iterable<E> elements) {
		this.size = 0;
		List<Integer> keys = new ArrayList<Integer>();
		List<Object> values = new ArrayList<Object>();
		Object current = new Object();
		for (E each: elements) {
			size++;
			if (each == null ? each == current : each.equals(current)) continue;
			keys.add(size - 1);
			values.add(current = each);
		}
		this.keys = new int[keys.size()];
		int n = 0;
		for (int each: keys) {
			this.keys[n++] = each;
		}
		this.values = values.toArray();
	}
	
	@SuppressWarnings("unchecked")
	public E get(int index) {
		if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException();
		int pos = Arrays.binarySearch(keys, index);
		return (E) (pos < 0 ? values[-pos-2] : values[pos]);
	}
	
	public int size() {
		return size;
	}
	
}
