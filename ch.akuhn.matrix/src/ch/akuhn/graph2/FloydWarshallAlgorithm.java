package ch.akuhn.graph2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FloydWarshallAlgorithm {

	public int[][] apply(Graph g) {
		int N = g.size();
		int[][] dist = g.clone().cost;
		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
				if (dist[i][k] == Graph.NIL) continue;
				for (int j = 0; j < N; j++) {
					if (dist[k][j] == Graph.NIL) continue;
					dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
				}
			}
		}
		return dist;
	}

	public static class Examples {

		@Test
		public void shouldFindAllShortestPaths() {
			String s = "6 7\n1 2 20\n1 3 5\n1 4 10\n2 3 8\n2 4 15\n3 4 2\n5 6 9";
			Graph g = Graph.parse(s);
			int[][] dist = new FloydWarshallAlgorithm().apply(g);
			assertEquals(6, dist.length);
		}
	}

}
