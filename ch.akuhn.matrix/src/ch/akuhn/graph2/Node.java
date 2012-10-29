package ch.akuhn.graph2;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Scanner;

import org.junit.Test;

public class Node {

	public static int NIL = Integer.MAX_VALUE;

	int n;
	int[] cost;

	public Node(int n, int size) {
		this.n = n;
		this.cost = new int[size];
		Arrays.fill(cost, NIL);
	}

	public static Node[] parse(String s) {
		Scanner in = new Scanner(s);
		int N = in.nextInt();
		int M = in.nextInt();
		Node[] nodes = new Node[N];
		for (int n = 0; n < N; n++) {
			nodes[n] = new Node(n, N);
		}
		for (int m = 0; m < M; m++) {
			int a = in.nextInt() - 1;
			int b = in.nextInt() - 1;
			int c = in.nextInt();
			nodes[a].cost[b] = c;
			nodes[b].cost[a] = c;
		}
		return nodes;
	}

	public static class Examples {

		@Test
		public void shouldParseFromString() {
			String s = "6 7\n1 2 20\n1 3 5\n1 4 10\n2 3 8\n2 4 15\n3 4 2\n5 6 9";
			Node[] nodes = Node.parse(s);
			assertEquals(6, nodes.length);
		}
	}

}
