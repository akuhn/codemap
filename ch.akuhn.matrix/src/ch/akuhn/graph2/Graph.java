package ch.akuhn.graph2;

import static org.junit.Assert.assertEquals;

import java.util.Scanner;

import org.junit.Test;

public class Graph {

	public static Edge[] parse(String s) {
		Scanner in = new Scanner(s);
		int N = in.nextInt();
		int M = in.nextInt();
		Edge[] edges = new Edge[M];
		for (int m = 0; m < M; m++) {
			int a = in.nextInt();
			int b = in.nextInt();
			int c = in.nextInt();
			edges[m] = new Edge(a, b, c);
		}
		return edges;
	}

	public static class Examples {

		@Test
		public void shouldParseFromString() {
			String s = "6 7\n1 2 20\n1 3 5\n1 4 10\n2 3 8\n2 4 15\n3 4 2\n5 6 9";
			Edge[] edges = Graph.parse(s);
			assertEquals(7, edges.length);
		}

	}

}
