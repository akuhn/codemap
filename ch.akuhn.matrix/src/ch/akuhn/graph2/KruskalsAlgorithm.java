package ch.akuhn.graph2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class KruskalsAlgorithm {

	public Edge[] apply(Edge[] edges) {
		Arrays.sort(edges);
		List<Edge> mst = new ArrayList();
		MergeFind forest = new MergeFind(edges.length);
		for (Edge each: edges) {
			int a = forest.find(each.a);
			int b = forest.find(each.b);
			if (a != b) {
				forest.merge(a, b);
				mst.add(each);
			}
		}
		return mst.toArray(new Edge[mst.size()]);
	}

	public static class Examples {

		@Test
		public void shouldFindMinimalSpanningTree() {
			String s = "6 7\n1 2 20\n1 3 5\n1 4 10\n2 3 8\n2 4 15\n3 4 2\n5 6 9";
			Edge[] edges = Edge.parse(s);
			Edge[] mst = new KruskalsAlgorithm().apply(edges);
			String out = Arrays.asList(mst).toString();
			assertEquals(4, mst.length);
			assertTrue(out.contains("(2,3)"));
			assertTrue(out.contains("(0,2)"));
			assertTrue(out.contains("(1,2)"));
			assertTrue(out.contains("(4,5)"));
		}

	}

}
