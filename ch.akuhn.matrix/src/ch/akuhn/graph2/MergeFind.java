package ch.akuhn.graph2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class MergeFind {

	int[] parent;

	public MergeFind(int size) {
		parent = new int[size];
		for (int n = 0; n < parent.length; n++)
			parent[n] = n;
	}

	public int find(int n) {
		if (parent[n] == n) return n;
		// Use path compression for speed
		return parent[n] = find(parent[n]);
	}

	public void merge(int a, int b) {
		if (parent[a] != a) throw new IllegalArgumentException();
		if (parent[b] != b) throw new IllegalArgumentException();
		parent[a] = b;
	}

	public int setCount() {
		int count = 0;
		for (int n = 0; n < parent.length; n++)
			if (parent[n] == n) count++;
		return count;
	}

	public int size() {
		return parent.length;
	}

	public String toString() {
		Map<Integer, List<Integer>> map = new TreeMap();
		for (int n = 0; n < parent.length; n++) {
			if (!map.containsKey(find(n))) map.put(find(n), new ArrayList());
			map.get(find(n)).add(n);
		}
		StringBuilder s = new StringBuilder();
		for (int each: map.keySet()) {
			s.append(map.get(each));
		}
		return s.toString();
	}

	public static class Examples {

		@Test
		public void shouldCreateDisjointSets() {
			MergeFind mf = new MergeFind(7);
			assertEquals(7, mf.size());
			assertEquals(7, mf.setCount());
		}

		@Test
		public void shouldMergeSets() {
			MergeFind mf = new MergeFind(7);
			mf.merge(mf.find(4), mf.find(6));
			mf.merge(mf.find(5), mf.find(0));
			mf.merge(mf.find(3), mf.find(6));
			mf.merge(mf.find(1), mf.find(2));
			String s = mf.toString();
			assertTrue(s.contains("[0, 5]"));
			assertTrue(s.contains("[1, 2]"));
			assertTrue(s.contains("[3, 4, 6]"));
			assertEquals(3, mf.setCount());
		}

	}
}
