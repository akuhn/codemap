package ch.akuhn.graph2;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Scanner;

import org.junit.Test;

public class Graph {

	public static final int NIL = Integer.MAX_VALUE;

	public static Graph parse(String s) {
		Scanner in = new Scanner(s);
		int N = in.nextInt();
		int M = in.nextInt();
		Graph g = new Graph(N);
		for (int m = 0; m < M; m++) {
			int a = in.nextInt() - 1;
			int b = in.nextInt() - 1;
			int c = in.nextInt();
			g.cost[a][b] = c;
			g.cost[b][a] = c;
		}
		return g;
	}

	final int[][] cost;

	public Graph(int size) {
		cost = new int[size][size];
		for (int[] each: cost) {
			Arrays.fill(each, NIL);
		}
	}

	public Graph clone() {
		Graph g = new Graph(this.size());
		for (int n = 0; n < cost.length; n++) {
			g.cost[n] = Arrays.copyOf(cost[n], cost[n].length);
		}
		return g;
	}

	public int size() {
		return cost.length;
	}

	public static class Examples {

		@Test
		public void shouldParseString() {
			String s = "6 7\n1 2 20\n1 3 5\n1 4 10\n2 3 8\n2 4 15\n3 4 2\n5 6 9";
			Graph g = Graph.parse(s);
			assertEquals(6, g.size());
		}
	}

}
