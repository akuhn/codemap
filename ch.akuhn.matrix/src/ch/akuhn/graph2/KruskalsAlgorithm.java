package ch.akuhn.graph2;

import static org.junit.Assert.assertEquals;

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
			Edge[] edges = Graph.parse(s);
			Edge[] mst = new KruskalsAlgorithm().apply(edges);
			String out = Arrays.asList(mst).toString();
			assertEquals(4, mst.length);
			assertEquals(true, out.contains("(3,4)"));
			assertEquals(true, out.contains("(1,3)"));
			assertEquals(true, out.contains("(2,3)"));
			assertEquals(true, out.contains("(5,6)"));
		}

	}

}
