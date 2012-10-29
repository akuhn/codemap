package ch.akuhn.graph2;

import static org.junit.Assert.assertEquals;

import java.util.Scanner;

import org.junit.Test;

public class Edge implements Comparable<Edge> {

	public static Edge[] parse(String s) {
		Scanner in = new Scanner(s);
		in.nextInt(); // unused
		int M = in.nextInt();
		Edge[] edges = new Edge[M];
		for (int m = 0; m < M; m++) {
			int a = in.nextInt() - 1;
			int b = in.nextInt() - 1;
			int c = in.nextInt();
			edges[m] = new Edge(a, b, c);
		}
		return edges;
	}
	int a, b;

	int cost;

	public Edge(int a, int b, int cost) {
		super();
		this.a = a;
		this.b = b;
		this.cost = cost;
	}

	@Override
	public int compareTo(Edge that) {
		return this.cost - that.cost;
	}

	@Override
	public String toString() {
		return String.format("cost(%d,%d) = %d", a, b, cost);
	}

	public static class Examples {

		@Test
		public void shouldParseFromString() {
			String s = "6 7\n1 2 20\n1 3 5\n1 4 10\n2 3 8\n2 4 15\n3 4 2\n5 6 9";
			Edge[] edges = Edge.parse(s);
			assertEquals(7, edges.length);
		}
	}

}
